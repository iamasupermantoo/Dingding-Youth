package com.youshi.zebra.book.constants;

import com.dorado.framework.constants.InProduction;
import com.dorado.framework.utils.crypt.DES;
import com.youshi.zebra.core.constants.ZebraConstants;

/**
 * 
 * @author wangsch
 * @date 2017年3月24日
 */
public class BookConstants {
	public static final DES BOOK_DES = new DES(InProduction.get() 
			? new byte[] { 9, 39, 43, 16, 33, 55, 8, 72 } : ZebraConstants.DES_KEY_TEST);
}
