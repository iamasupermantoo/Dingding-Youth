package com.youshi.zebra.homework.constants;

import com.dorado.framework.constants.InProduction;
import com.dorado.framework.utils.crypt.DES;
import com.youshi.zebra.core.constants.ZebraConstants;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public interface HomeworkConstants {
	public static final DES HOMEWORK_DES = new DES(InProduction.get() 
			? new byte[] { 1, 19, -42, 38, 16, 45, 9, -62 } : ZebraConstants.DES_KEY_TEST);
	
	public static final DES HOMEWORK_ANSWER_DES = new DES(InProduction.get() 
			? new byte[] { 12, 60, 16, 45, 82, 38, 9, -62 } : ZebraConstants.DES_KEY_TEST);


}
