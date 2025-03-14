package com.youshi.zebra.passport.service.impl;


import static com.youshi.zebra.core.constants.jedis.PersistRedisKey.UserSecret;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Service;

import com.dorado.framework.datasource.DataSourceByZooKeeper;
import com.dorado.framework.jedis.JedisByZooKeeper;
import com.dorado.framework.tuple.TwoTuple;
import com.dorado.framework.utils.DoradoMapperUtils;
import com.dorado.framework.utils.crypt.AES;
import com.dorado.framework.utils.crypt.Skip32;
import com.google.common.base.Objects;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.core.utils.DAOUtils;
import com.youshi.zebra.mobile.exception.MobileNotRegisteredException;
import com.youshi.zebra.mobile.service.MobileCodeService;
import com.youshi.zebra.passport.OldPasswordWrongException;
import com.youshi.zebra.passport.constant.PassportConstants;
import com.youshi.zebra.passport.dao.TicketDAO;
import com.youshi.zebra.passport.dao.UserPassportDAO;
import com.youshi.zebra.passport.model.TicketModel;
import com.youshi.zebra.passport.model.UserPassportModel;
import com.youshi.zebra.passport.service.UserPassportService;
/**
 *
 * 直接把sql写在service好吗。。。by wangsch
 *
 * @date 2016-09-12
 * 
 */
@Service
public class UserPassportServiceImpl extends NamedParameterJdbcDaoSupport 
	implements UserPassportService {

    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());
    /**
     * 盐长度
     */
    private static final int SALT_LENGTH = 8;

    private JedisByZooKeeper jedis;
    
    @Autowired
    private UserPassportDAO userPassportDAO;
    
    @Autowired
    private TicketDAO ticketDAO;
    
    @Autowired
    private MobileCodeService mobileCodeService;

    @PostConstruct
    protected void init() {
        setDataSource(DataSourceByZooKeeper.of(ZebraDB.passport.getZKName()));
        jedis = JedisByZooKeeper.of("d_passport");
    }
	
	@Override
	public TwoTuple<Integer, Integer> verifyTicketWithRand(String ticket) {
        AES aesOne = new AES(PassportConstants.TICKET_PUBLIC_KEY);
        byte[] afterDecode = aesOne.decodeBase64(ticket);
        String decodeOnce = new String(afterDecode);
        String[] split = StringUtils.split(decodeOnce, PassportConstants.NEW_TICKET_SEPARTOR);
        if (split.length != 3) {
        	logger.error("Ticket length NOT VALID. ticket: {}", ticket);
            return null;
        }
        int userId = Skip32.decrypt(NumberUtils.toInt(split[0]), PassportConstants.SKIP32KEY);
        int random = Skip32.decrypt(NumberUtils.toInt(split[1]), PassportConstants.SKIP32KEY);

        byte[] bytes = jedis.getBinary().hget(UserSecret.ofBytes(userId), 
        		String.valueOf(random).getBytes());
        if (bytes != null) {
            AES aesTwo = new AES(bytes);
            String third = new String(aesTwo.decodeBase64(split[2]));
            if (Objects.equal(third, userId + "" + random)) { 				//验票
                return new TwoTuple<Integer, Integer>(userId, random);
            }
            logger.warn("Redis TICKET no pass, use DB. ticket: {}", ticket);
        }
        
        TicketModel ticketModel = ticketDAO.getTicket(userId);
        if(ticketModel != null) {
        	byte[] secretKey = ticketModel.getSecret();
        	String randomsJson = ticketModel.getRandoms();
        	boolean contains = containsRandom(randomsJson, random);
        	if(!contains) {
        		logger.warn("DB TICKET RANDOM NOT FOUND. ticket: {}, required random: {}", ticket, random);
        		return null;
        	}
        	
        	AES aesTwo = new AES(secretKey);
        	String third = new String(aesTwo.decodeBase64(split[2]));
        	if (Objects.equal(third, userId + "" + random)) { 					//验票
        		return new TwoTuple<Integer, Integer>(userId, random);
        	} else {
        		return null;
        	}
        }
        return null;
	}
	@Override
	public void removeSecretRandom(int userId, int random) {
		jedis.getBinary().hdel(UserSecret.ofBytes(userId), String.valueOf(random).getBytes());
		
		TicketModel ticket = ticketDAO.getTicket(userId);
		String randomsJson = ticket.getRandoms();
		
		randomsJson = removeOneRandom(randomsJson, random);
		int c = ticketDAO.updateRandoms(userId, randomsJson);
        DAOUtils.checkAffectRows(c);
	}
	@Override
	public void removeTicket(int userId) {
		jedis.getBinary().del(UserSecret.ofBytes(userId));
		ticketDAO.deleteTicket(userId);
	}
    // ---------------------------------------------------- 手机密码账号 -------------------------------------------------------
    /**
     * 验证账号密码，成功返回用户id
     * 
     * @param mobile			手机号
     * @param password		明文密码
     * @return 成功返回用户id，否则返回null
     */
    @Override
    public Integer verifyAccount(String mobile, String password) {
        UserPassportModel passport = userPassportDAO.getUserPassport(mobile);
        if(passport == null) {
        	return null;
        }
        String md5Crypt = encodeMd5Password(password, passport.getSalt());
        if (StringUtils.equals(md5Crypt, passport.getPassword())) {
        	return passport.getId();
        }
        return null;
    }
    @Override
    public void createAccount(int userId, String mobile, String password) {
        String salt = RandomStringUtils.randomNumeric(SALT_LENGTH);
        String md5Crypt = encodeMd5Password(password, salt);
        
        userPassportDAO.insert(userId, mobile, md5Crypt, salt);
        logger.info("Create mobile account OK. userId: {}, mobile: {}", userId, mobile);
    }
    @Override
    public boolean isRegisterd(String mobile) {
        return userPassportDAO.isRegisterd(mobile);
    }
    @Override
    public Map<String, Integer> getRegisteredUserIds(Collection<String> mobiles) {
    	return userPassportDAO.getRegisteredUserIds(mobiles);
    }
    
    @Override
    public void resetPassword(String mobile, String password, String code) {
    	Integer userId = getRegisteredUserId(mobile);
    	if (userId == null) {
    		logger.warn("Ignore reset password, mobile NOT REGISTERED: {}", mobile);
    		throw new MobileNotRegisteredException();
    	}
    	// 验证码验证
    	mobileCodeService.verifyResetCode(mobile, code);
    	
    	// 更新密码
    	String salt = RandomStringUtils.randomNumeric(SALT_LENGTH);
        String md5Crypt = encodeMd5Password(password, salt);
        int c = userPassportDAO.updatePassword(userId, password, salt, md5Crypt);
        DAOUtils.checkAffectRows(c);
    	
    	logger.info("Create RESET password OK. userId: {}, password: {}", userId, password);
    }
	@Override
	public void changePassword(int userId, String oldPassword, String newPassword) {
		UserPassportModel passport = userPassportDAO.getUserPassport(userId);
        
        String md5Crypt = encodeMd5Password(oldPassword, passport.getSalt());
        if (!StringUtils.equals(md5Crypt, passport.getPassword())) {
        	throw new OldPasswordWrongException();
        }
        
        String salt = RandomStringUtils.randomNumeric(SALT_LENGTH);
        String newMd5Crypt = encodeMd5Password(newPassword, salt);
        int c = userPassportDAO.updatePassword(userId, newPassword, salt, newMd5Crypt);
		DAOUtils.checkAffectRows(c);
		
		logger.info("User change password succ. userId: {}", userId);
		
	}
    /**
     * 
     * 带盐的MD5加密
     * 
     * @param password		密码
     * @param salt					盐
     * @return							加密后的MD5密码
     */
    private final String encodeMd5Password(String password, String salt) {
    	// 这方法谁写的？规则复杂一些更好吧。否则salt不就是形同虚设了吗。
    	// by wangsch at 2016-12-24
        String result = DigestUtils.md5Hex(password + salt);
        return result;
    }
    // 同一个账号的最大设备数：10个
	@Override
	public String createTicketWithRand(int userId) {
		
    	// 1. secretKey
        byte[] secretKey;
        String randomsJson;
        TicketModel ticket = ticketDAO.getTicket(userId);
        if(ticket != null) {
        	secretKey = ticket.getSecret();
        	randomsJson = ticket.getRandoms();
        } else {
        	secretKey = AES.generatorSafeKey();
        	int effectLine = ticketDAO.insertTicket(userId, secretKey);
            DAOUtils.checkAffectRows(effectLine);
            randomsJson = "";
        }
        
        // 2. randoms
        TwoTuple<Integer, String> result = addOneRandom(randomsJson);
        int random = result.first;
        String randoms = result.second;
        
        int c = ticketDAO.updateRandoms(userId, randoms);
        DAOUtils.checkAffectRows(c);
        jedis.getBinary().hset(UserSecret.ofBytes(userId), 
        		String.valueOf(random).getBytes(), secretKey);
        // 3. create
        AES aesOne = new AES(secretKey);
        AES aesTwo = new AES(PassportConstants.TICKET_PUBLIC_KEY);
        String afterOnce = Skip32.encrypt(userId, PassportConstants.SKIP32KEY)
                + PassportConstants.NEW_TICKET_SEPARTOR
                + Skip32.encrypt(random, PassportConstants.SKIP32KEY)
                + PassportConstants.NEW_TICKET_SEPARTOR
                + aesOne.encodeBase64URLSafeString((userId + "" + random).getBytes());
        return aesTwo.encodeBase64URLSafeString(afterOnce.getBytes());
	}
	/**
	 * @param ticket
	 * @return
	 */
	private TwoTuple<Integer, String> addOneRandom(String randomsJson) {
        Set<Integer> randoms = DoradoMapperUtils.fromJSON(randomsJson, HashSet.class, Integer.class);
        int random;
        do {
        	random = RandomUtils.nextInt(100000, Integer.MAX_VALUE);
        	if(randoms.add(random)) {
        		break;
        	}
        } while(true);
        
		return new TwoTuple<>(random, DoradoMapperUtils.toJSON(randoms));
	}
	
	private String removeOneRandom(String randomsJson, int random) {
		Set<Integer> randoms = DoradoMapperUtils.fromJSON(randomsJson, HashSet.class, Integer.class);
		randoms.remove(random);
		return DoradoMapperUtils.toJSON(randoms);
	}
	
	private boolean containsRandom(String randomsJson, int random) {
		Set<Integer> randoms = DoradoMapperUtils.fromJSON(randomsJson, HashSet.class, Integer.class);
		return randoms.contains(random);
	}
}