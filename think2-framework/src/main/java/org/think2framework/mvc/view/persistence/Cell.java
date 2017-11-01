package org.think2framework.mvc.view.persistence;

import org.think2framework.orm.core.TypeUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 数据cell定义
 */
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface Cell {

	/**
	 * 名称，单模块唯一
	 * 
	 * @return 名称
	 */
	String name() default "";

	/**
	 * 标题
	 * 
	 * @return 标题
	 */
	String title();

	/**
	 * 标签，根据标签操作单元，默认问文本
	 * 
	 * @return 标签
	 */
	String tag() default TypeUtils.FIELD_TEXT;

	/**
	 * 是否必填项，默认false
	 * 
	 * @return 是否必填项
	 */
	boolean required() default false;

	/**
	 * 单元格最大长度
	 *
	 * @return 最大长度
	 */
	int length() default 0;

	/**
	 * 默认值，now当前时间，user.id登录用户id，user.name登录用户名，其他则填值
	 * 
	 * @return 默认值
	 */
	String defaultValue() default "";

	/**
	 * 是否作为搜索项，默认false
	 * 
	 * @return 是否作为搜索项
	 */
	boolean search() default false;

	/**
	 * 查询页面是否显示列，默认true
	 * 
	 * @return 查询页面是否显示列
	 */
	boolean display() default true;

	/**
	 * 显示详情页面是否显示列，默认true
	 * 
	 * @return 显示详情页面是否显示列
	 */
	boolean detail() default true;

	/**
	 * 添加页面是否需要添加列，默认true
	 * 
	 * @return 添加页面是否需要添加列
	 */
	boolean add() default true;

	/**
	 * 编辑页面是否需要列，默认true
	 * 
	 * @return 编辑页面是否需要列
	 */
	boolean edit() default true;

	/**
	 * 是否行级过滤，默认false，只有当类型为item或者dataItem的时候才有效
	 * 
	 * @return 是否行级过滤
	 */
	boolean rowFilter() default false;

	/**
	 * 注释
	 * 
	 * @return 注释
	 */
	String comment() default "";

}
