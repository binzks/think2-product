package org.think2framework.view.persistence;

import java.lang.annotation.*;

/**
 * Created by zhoubin on 16/3/15. 按钮注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(value = Actions.class)
public @interface Action {

	String name(); // 名称，模型唯一，用于授权

	String title() default ""; // 标题

	String type(); // 按钮类型

	String href(); // 按钮点击后的href

	String css(); // 按钮CSS样式

}
