package org.think2framework.orm.persistence;

import java.lang.annotation.*;

/**
 * 查询关联注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(value = JoinTables.class)
public @interface JoinTable {

	/**
	 * 关联名称,模型唯一
	 * 
	 * @return 关联名称
	 */
	String name();

	/**
	 * 关联表的数据库，默认空表示主表对应的数据库，如果是多数据库关联则填数据库名称
	 * 
	 * @return 关联表的数据库
	 */
	String database() default "";

	/**
	 * 关联表名
	 * 
	 * @return 关联表名
	 */
	String table();

	/**
	 * 关联类型left join,right join,inner join
	 * 
	 * @return 关联类型
	 */
	String type() default "left join";

	/**
	 * 关联表的字段
	 * 
	 * @return 关联表的字段
	 */
	String key();

	/**
	 * 关联的关联名称，关联查询的时候是否关联其他关联，如果空表示关联主表
	 * 
	 * @return 关联的关联名称
	 */
	String joinName() default "";

	/**
	 * 关联的关联的表的字段名称
	 * 
	 * @return 关联的关联的表的字段名称
	 */
	String joinKey();

	/**
	 * 额外的过滤条件，添加到关联生成后
	 * 
	 * @return 额外的过滤条件
	 */
	String filter() default "";

}
