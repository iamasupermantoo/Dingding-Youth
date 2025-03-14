package com.youshi.zebra.lesson.constants;

import com.dorado.framework.constants.InProduction;
import com.dorado.framework.utils.crypt.DES;
import com.youshi.zebra.core.constants.ZebraConstants;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public interface LessonConstants {
	public static final DES LESSON_DES = new DES(InProduction.get() 
			? new byte[] { -49, 10, 121, 65, -97, -18, -82, -57 } : ZebraConstants.DES_KEY_TEST);
	
}
