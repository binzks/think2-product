package org.think2framework.orm.persistence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 多查询排序定义
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Orders {

	/**
	 * 排序
	 * 
	 * @return 排序
	 */
	Order[] value();
}
