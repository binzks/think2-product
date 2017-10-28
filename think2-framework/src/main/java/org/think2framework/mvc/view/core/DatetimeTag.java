package org.think2framework.mvc.view.core;

import org.think2framework.mvc.view.HtmlTag;
import org.think2framework.utils.StringUtils;

import java.util.Map;

/**
 * 时间类型标签
 */
public class DatetimeTag extends AbstractHtmlTag implements HtmlTag {

	public DatetimeTag() {
		super("");
		setAttribute("id", "datetime-picker" + System.currentTimeMillis());
		setAttribute("type", "text");
		setAttribute("data-date-format", "YYYY-MM-DD HH:mm:ss");
		setAttribute("class", "form-control");
	}

	@Override
	public String htmlString() {
		StringBuffer html = new StringBuffer();
		html.append("<div class=\"input-group\"><input ");
		for (Map.Entry<String, String> entry : attributes.entrySet()) {
			html.append(" ").append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
		}
		html.append(">").append(childrenString());
		if (StringUtils.isNotBlank(text)) {
			html.append(text);
		}
		html.append("<span class=\"input-group-addon\"><i class=\"fa fa-clock-o bigger-110\"></i></span></div>");
		return html.toString();
	}

}
