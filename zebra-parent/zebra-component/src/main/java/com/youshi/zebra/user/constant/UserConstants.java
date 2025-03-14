package com.youshi.zebra.user.constant;

import java.util.concurrent.TimeUnit;

import com.dorado.framework.constants.InProduction;
import com.dorado.framework.utils.crypt.DES;
import com.youshi.zebra.core.constants.ZebraConstants;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public interface UserConstants {
	public static final DES USER_DES = new DES(InProduction.get() 
			? new byte[] { 1, 19, -42, 38, 123, -45, -99, -62 } : ZebraConstants.DES_KEY_TEST);
    		
    byte[] CRYPT_DES_KEY = InProduction.get() 
    		? new byte[] {16, 37, 72, 38, -123, -45, -99, -72 } : new byte[] { -47, 37, 42, 38, -123, -45, -99, -70 };

	long DEFAULT_LONG_BIRTH = 315504000000L;

	public static final int TICKET_EXPIRE_SECOND = (int) TimeUnit.HOURS.toSeconds(3);

}
