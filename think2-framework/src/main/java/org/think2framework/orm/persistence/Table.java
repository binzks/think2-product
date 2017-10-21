package org.think2framework.orm.persistence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据库表定义
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

	/**
	 * 表名
	 * 
	 * @return 表名
	 */
	String name();

	/**
	 * 主键名称，默认id
	 * 
	 * @return 主键名称
	 */
	String pk() default "id";

	/**
	 * 是否是整型自增长,如果不是则为UUID类型，默认true
	 * 
	 * @return 是否是整型自增长
	 */
	boolean autoIncrement() default true;

	/**
	 * 唯一性约束,可以定义多个，如果一个约束有多个字段用,隔开
	 * 
	 * @return 唯一性约束
	 */
	String[] uniques() default {};

	/**
	 * 索引,可以定义多个，如果一个索引多个字段用,隔开
	 * 
	 * @return 索引
	 */
	String[] indexes() default {};

	/**
	 * 注释
	 * 
	 * @return 注释
	 */
	String comment() default "";

}
