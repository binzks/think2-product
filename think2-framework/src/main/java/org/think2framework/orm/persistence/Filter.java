package org.think2framework.orm.persistence;

import java.lang.annotation.*;

/**
 * 查询过滤条件定义
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(value = Filters.class)
public @interface Filter {

	/**
	 * 过滤关键字
	 * 
	 * @return 过滤关键字
	 */
	String key();

	/**
	 * 过滤类型，默认=
	 * 
	 * @return 过滤类型
	 */
	String type() default "=";

	/**
	 * 过滤值数组，between为两个，其他为一个值
	 * 
	 * @return 过滤值
	 */
	String[] values();
}
