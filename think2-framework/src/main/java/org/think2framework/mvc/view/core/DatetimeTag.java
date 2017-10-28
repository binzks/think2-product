package org.think2framework.mvc.view.core;

import org.think2framework.mvc.view.HtmlTag;

/**
 * 时间类型标签
 */
public class DatetimeTag extends AbstractHtmlTag implements HtmlTag {

	public DatetimeTag() {
		super("input");
		setAttribute("class", "col-xs-12 col-sm-12");
		setAttribute("type", "text");
		setAttribute("  data-date-format", "YYYY-MM-DD HH:mm:ss");
	}
}
