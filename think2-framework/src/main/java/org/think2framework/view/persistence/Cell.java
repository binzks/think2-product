package org.think2framework.view.persistence;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by zhoubin on 16/7/11. 数据cell定义
 */
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface Cell {

	String name() default ""; // 名称，单模块唯一

	String title(); // 标题

	String tag() default "text"; // 标签，根据标签操作单元，默认问文本

	boolean required() default false; // 是否必填项，默认false

	String defaultValue() default ""; // 默认值，now当前时间，user.id登录用户id，user.name登录用户名，其他则填值

	boolean center() default true; // 列是否居中默认true

	int width() default 0; // 列长度，默认0表示自动

	boolean search() default false; // 是否作为搜索页，默认false，TEXT查询为like

	boolean display() default true; // 查询页面是否显示列，默认true

	boolean detail() default true; // 显示详情页面是否显示列，默认true

	boolean add() default true;// 添加页面是否需要添加列，默认true

	boolean edit() default true; // 编辑页面是否需要列，默认true

	boolean rowFilter() default false; // 是否行级过滤，默认false，只有当类型为item或者dataItem的时候才有效

	String comment() default ""; // 注释

}
