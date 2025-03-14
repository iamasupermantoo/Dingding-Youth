package com.youshi.zebra.core.utils;

import com.dorado.framework.model.PageView;

/**
 * 
 * 根据cursor，分页查询，由service实现此接口
* 
* Date: Jun 2, 2016
* 
 * @author wangsch
 *
 * @param <K> cursor类型
 * @param <V> 查询出的实体类型
 * @param <E> 额外参数的类型
 * 
 * @see com.github.phantomthief.util.GetByCursorDAO<Id, Entity>
 */
public interface RetrieveByCursor<K extends Number, V, E> {

	/**
	 * 分页查询，按id降序
	 * @param cursor 游标
	 * @param limit 页大小，需要显示的条目数。
	 * 		内部会查询limit+1条，多出的一条取出id，作为ResultView中的nextCursor的值
	 * @param additionParam 查询参数
	 * @return ResultView实例
	 */
    PageView<V, K> getByCursor(K cursor, int limit, E additionParam);
}
