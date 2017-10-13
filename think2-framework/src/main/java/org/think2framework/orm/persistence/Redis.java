package org.think2framework.orm.persistence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhoubin on 16/2/23. redis注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Redis {

	String name();// 表名

	int valid() default 0; // 缓存有效时间，0位永久有效

}
