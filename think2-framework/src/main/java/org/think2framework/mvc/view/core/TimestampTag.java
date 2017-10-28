package org.think2framework.mvc.view.core;

import org.think2framework.mvc.view.HtmlTag;
import org.think2framework.utils.DatetimeUtils;
import org.think2framework.utils.NumberUtils;
import org.think2framework.utils.StringUtils;

/**
 * 时间戳标签
 */
public class TimestampTag extends DatetimeTag implements HtmlTag {

    @Override
    public void setValue(String value) {
        // 如果带-表示不是数字的时间戳，当初字符串直接设置
        if (StringUtils.contains(value, "-")) {
            this.value = value;
        } else {
            // 时间戳转成时间字符串
            this.value = DatetimeUtils.toString(NumberUtils.toLong(value, 0));
        }
        attributes.put("value", this.value);
    }

    @Override
    public String getValue() {
        return StringUtils.toString(DatetimeUtils.toLong(value));
    }
}
