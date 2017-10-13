package org.think2framework.orm.persistence;

import org.think2framework.orm.core.ClassUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by zhoubin on 16/7/11. 数据库列定义
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Column {

	String name() default ""; // 字段名称

	String type() default ClassUtils.TYPE_STRING; // 字段类型

	boolean nullable() default true; // 字段是否可空

	String alias() default ""; // 字段别名

	String join() default ""; // 字段所属关联名称,如果为空表示主表

	int length() default 50; // 字段长度

	int scale() default 0; // 字段精度(小数位数)

	String defaultValue() default ""; // 字段默认值

	String comment() default ""; // 字段注释

}
