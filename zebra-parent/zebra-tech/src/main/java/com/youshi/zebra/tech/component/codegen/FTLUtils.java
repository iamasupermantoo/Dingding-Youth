package com.youshi.zebra.tech.component.codegen;

/**
 * 
 * @author wangsch
 * @date 2017年4月11日
 */
public class FTLUtils {
	
	public static String toCamelStyle(String field){
		return toCamelStyle(field, false);
	}
	
	/**
	 * 数据库字段名／数据库表名，多个单词用"_"分割，转换为“驼峰”风格
	 * 
	 * @param field		数据库字段名
	 * @param capFirst	是否首字母大写
	 * @return			“驼峰”风格
	 */
	public static String toCamelStyle(String field, boolean capFirst) {
		char[] chars = field.toLowerCase().toCharArray();
		StringBuilder sb = new StringBuilder();
		sb.append(capFirst ? Character.toUpperCase(chars[0]) : chars[0]);
		
		boolean nextCap = false;
		for (int i = 1; i < chars.length; i++) {
			char c = chars[i];
			if(c == '_') {
				nextCap = true;
				continue;
			}
			if(nextCap) {
				sb.append(Character.toUpperCase(c));
				nextCap = false;
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
//	public static void main(String[] args) {
//		String is = toCamelStyle("user_name_aa", true);
//		System.out.println(is);
//		
//		is = toCamelStyle("user_name", true);
//		System.out.println(is);
//		
//		is = toCamelStyle("user", true);
//		System.out.println(is);
//		
//		// 
//		is = toCamelStyle("user_name_aa", false);
//		System.out.println(is);
//		
//		is = toCamelStyle("user_name", false);
//		System.out.println(is);
//		
//		is = toCamelStyle("user", false);
//		System.out.println(is);
//	}
}
