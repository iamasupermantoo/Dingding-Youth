package com.youshi.zebra.mobile.model;

import com.dorado.framework.crud.model.HasId;
import com.dorado.framework.crud.model.HasStatus;

/**
 * 
 * 手机信息（实际上是验证码）。存储了手机验证码，用于注册、找回密码等场景。<br />
 * 
 * 注意：<br />
 * 当前“注册”和“找回密码”使用不同的表，但表结构完全相同，所以公用这个类。
 * 
 * 
 * @author wangsch
 *
 * @date 2016-09-12
 */
public class MobileInfo implements HasId<Long>, HasStatus {
	/**
	 * 主键id
	 */
    private final long id;

    /**
     * 手机号
     */
    private final String phone;
    
    /**
     * 验证码
     */
    private final String code;

    /**
     * 验证码有效期，秒。超过有效期，验证码将不可用。
     */
    private final int ttl;
    
    /**
     * 尝试次数，初始为0。每输错一次+1，超过某个阀值，验证码将不可用。
     */
    private final int tryCount;
    
    /**
     * 状态。参考{@link DoradoEntityStatus}，验证码相关的一些状态
     */
    private final int status;
    
    /**
     * 更新时间
     */
    private final long updateTime;

    /**
     * 创建时间
     */
    private final long createTime;

    public MobileInfo(long id, String phone, String code, long updateTime, long createTime, 
    		int status, int ttl, int tryCount) {
        this.id = id;
        this.phone = phone;
        this.code = code;
        this.updateTime = updateTime;
        this.createTime = createTime;
        this.status = status;
        this.ttl = ttl;
        this.tryCount = tryCount;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getCode() {
        return code;
    }

    public long getUpdateTime() {
		return updateTime;
	}
    
    public long getCreateTime() {
        return createTime;
    }

    @Override
    public int getStatus() {
        return status;
    }

	public int getTtl() {
		return ttl;
	}

	public int getTryCount() {
		return tryCount;
	}
}
