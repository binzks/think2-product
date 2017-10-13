package org.think2framework.orm.persistence;

import java.lang.annotation.*;

/**
 * Created by zhoubin on 16/9/12. 过滤定义
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(value = Filters.class)
public @interface Filter {

	String key(); // 过滤关键字

	String type() default "="; // 过滤类型

	String[] values(); // 过滤值
}
