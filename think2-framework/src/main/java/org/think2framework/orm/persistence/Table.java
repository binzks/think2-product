package org.think2framework.orm.persistence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhoubin on 16/2/23. dao注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

	String name();// 表名

	String pk() default "id"; // 主键名称

	boolean autoIncrement() default true; // 是否是整型自增长,如果不是则为UUID类型

	String[] uniques() default {}; // 唯一性约束,多个字段用,隔开

	String[] indexes() default {};// 索引,多个字段用,隔开

	String comment() default ""; // 注释

}
