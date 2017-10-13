package org.think2framework.view.persistence;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

/**
 * Created by zhoubin on 16/9/12. 状态等选择定义
 */
@Target({ METHOD, FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Items {
	Item[] value();
}
