package org.think2framework.mvc.view.persistence;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

/**
 * Created by zhoubin on 16/9/12. 过滤定义
 */
@Target({ METHOD, FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(value = Items.class)
public @interface Item {

	String model() default ""; // 模型名称，如果是从数据源选择则填写，如果不是则不填

	String key(); // 如果模型名称不为空，则表示为从数据源选择数据的取值字段名称，如果模型名称为空表示固定的值的显示值

	String value(); // 如果模型名称不为空，则表示为从数据源选择数据的显示字段名称，如果模型名称为空表示固定值

}
