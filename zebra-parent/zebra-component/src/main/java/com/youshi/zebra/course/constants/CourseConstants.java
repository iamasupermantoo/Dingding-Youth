package com.youshi.zebra.course.constants;

import com.dorado.framework.constants.InProduction;
import com.dorado.framework.utils.crypt.DES;
import com.youshi.zebra.core.constants.ZebraConstants;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public interface CourseConstants {
	public static final DES COURSE_DES = new DES(InProduction.get() 
			? new byte[] { 1, 19, 2, 13, 16, 45, 9, -62 } : ZebraConstants.DES_KEY_TEST);
	
	public static final DES COURSE_META_DES = new DES(InProduction.get() 
			? new byte[] { 12, 8, 16, 11, 82, 38, 9, -62 } : ZebraConstants.DES_KEY_TEST);


}
