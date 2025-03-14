package com.youshi.zebra.core.utils;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Joiner;
import com.youshi.zebra.exception.common.DAOException;

/**
 * DAO工具
 * 
 * OPTI: contains操作转换为位操作，并做“micro benchmark”
 * 
 * @author wangsch
 * @date 2016-09-22
 */
public class DAOUtils {
	public static final List<Integer> INSERT_DUPLICATE_ROWS = Arrays.asList(1, 2);
	
	public static final List<Integer> INSERT_IGNORE_ROWS = Arrays.asList(0, 1);
	
	public static void checkInsert(int id) {
		if(id <= 0) {
			throw new DAOException("id should > 0");
		}
	}
	
	/**
	 * @see #checkAffectRows(int, int)
	 */
	public static void checkAffectRows(int rowAffect) {
		checkAffectRows(rowAffect, 1);
	}
	
	/**
	 * 检查影响行数
	 * 
	 * @param rowAffect	影响行数
	 * @param expect		预期影响行数
	 * 
	 * @throws DAOException 如果影响行数和预期行数不相同
	 * 
	 */
	public static void checkAffectRows(int rowAffect, int expect) {
		if(rowAffect != expect) {
			throw new DAOException("Update fail, expect: "+expect+", real affect: " + rowAffect);
		}
	}
	
	/**
	 * 检查影响行数
	 * 
	 * @param rowAffect	影响行数
	 * @param expect		预期影响行数，允许多个值
	 * 
	 * @throws DAOException 如果预期行数不包含，影响行数
	 * 
	 */
	public static void checkAffectRows(int rowAffect, List<Integer> expect) {
		if(!expect.contains(rowAffect)) {
			throw new DAOException("Update fail, expect: "+Joiner.on("/").join(expect)+", real affect: " + rowAffect);
		}
	}
	
	public static void checkInsertIgnore(int rowAffect) {
		checkAffectRows(rowAffect, INSERT_IGNORE_ROWS);
	}
	
	public static void checkInsertDuplicate(int rowAffect) {
		checkAffectRows(rowAffect, INSERT_DUPLICATE_ROWS);
	}
}
