package org.think2framework.orm.persistence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 多关联注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JoinTables {

	/**
	 * 查询关联
	 * 
	 * @return 查询关联
	 */
	JoinTable[] value();
}
