package org.think2framework.orm.persistence;

import org.think2framework.orm.core.TypeUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 数据库列定义
 */
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface Column {

	/**
	 * 字段名称，默认空的时候取java定义字段的名称
	 * 
	 * @return 字段名称
	 */
	String name() default "";

	/**
	 * 字段类型，默认是文本，如果字段定义是整型或者长整型则自动修改为整型，如果是浮点则修改为浮点
	 * 
	 * @return 字段类型
	 */
	String type() default TypeUtils.FIELD_TEXT;

	/**
	 * 字段是否可空，默认可空
	 * 
	 * @return 字段是否可空
	 */
	boolean nullable() default true;

	/**
	 * 字段别名，主要用于关联表的时候给字段设置
	 * 
	 * @return 字段别名
	 */
	String alias() default "";

	/**
	 * 字段所属关联名称,如果为空表示主表字段
	 * 
	 * @return 字段所属关联名称
	 */
	String join() default "";

	/**
	 * 字段长度，默认50
	 * 
	 * @return 字段长度
	 */
	int length() default 50;

	/**
	 * 字段精度(小数位数)，默认0
	 * 
	 * @return 字段精度(小数位数)
	 */
	int scale() default 0;

	/**
	 * 字段默认值，默认空不设置默认值
	 * 
	 * @return 字段默认值
	 */
	String defaultValue() default "";

	/**
	 * 字段注释
	 * 
	 * @return 字段注释
	 */
	String comment() default "";

}
