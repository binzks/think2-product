package org.think2framework.orm.persistence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 多查询过滤定义
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Filters {

	/**
	 * 查询过滤
	 * 
	 * @return 查询过滤
	 */
	Filter[] value();
}
