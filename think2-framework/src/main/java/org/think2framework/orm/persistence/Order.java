package org.think2framework.orm.persistence;


import org.think2framework.orm.core.ClassUtils;

import java.lang.annotation.*;

/**
 * Created by zhoubin on 16/9/12. 过滤定义
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(value = Orders.class)
public @interface Order {

	String[] keys(); // 排序字段

	String type() default ClassUtils.ORDER_TYPE_DESC; // 排序类型

}
