package org.think2framework.orm.persistence;

import java.lang.annotation.*;

/**
 * Created by zhoubin on 16/3/15. 关联注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(value = JoinTables.class)
public @interface JoinTable {

	String name(); // 关联名称,模型唯一

	String database() default ""; // 关联表的数据库

	String table(); // 关联表名

	String type() default "left join"; // 关联类型

	String key(); // 关联表的字段

	String joinName() default ""; // 关联的关联名称

	String joinKey(); // 关联的关联的表的字段名称

	String filter(); // 额外的过滤条件

	String comment() default ""; // 注释
}
