package org.think2framework.mvc.view;

import org.think2framework.mvc.view.bean.Item;
import org.think2framework.mvc.view.core.DatetimeTag;
import org.think2framework.mvc.view.core.SelectTag;
import org.think2framework.mvc.view.core.SimpleHtmlTag;
import org.think2framework.mvc.view.core.TimestampTag;
import org.think2framework.orm.core.ClassUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * html标签工厂
 */
public class HtmlTagFactory {

	public static HtmlTag createBool(String name, Boolean required) {
		List<Item> items = new ArrayList<>();
		items.add(new Item("", ClassUtils.FALSE_VALUE.toString(), "否"));
		items.add(new Item("", ClassUtils.TRUE_VALUE.toString(), "是"));
		return createSelect(name, required, items);
	}

	public static HtmlTag createSelect(String name, Boolean required, List<Item> items) {
		SelectTag selectTag = new SelectTag();
		selectTag.setOptions(items);
		selectTag.setAttribute("name", name);
		if (required) {
			selectTag.setAttribute("required", "required");
		}
		return selectTag;
	}

	public static HtmlTag createInput(String name, Boolean required) {
		HtmlTag htmlTag = new SimpleHtmlTag("input", "col-xs-12 col-sm-12");
		htmlTag.setAttribute("name", name);
		htmlTag.setAttribute("type", "text");
		if (required) {
			htmlTag.setAttribute("required", "required");
		}
		return htmlTag;
	}

	public static HtmlTag createNumber(String name, Boolean required) {
		HtmlTag htmlTag = createInput(name, required);
		htmlTag.setAttribute("type", "number");
		return htmlTag;
	}

	public static HtmlTag createPassword(String name, Boolean required) {
		HtmlTag htmlTag = createInput(name, required);
		htmlTag.setAttribute("type", "password");
		return htmlTag;
	}

	public static HtmlTag createTimestamp(String name, Boolean required) {
		TimestampTag timestampTag = new TimestampTag();
		timestampTag.setAttribute("name", name);
		if (required) {
			timestampTag.setAttribute("required", "required");
		}
		return timestampTag;
	}

	public static HtmlTag createDatetime(String name, Boolean required) {
		DatetimeTag datetimeTag = new DatetimeTag();
		datetimeTag.setAttribute("name", name);
		if (required) {
			datetimeTag.setAttribute("required", "required");
		}
		return datetimeTag;
	}

}
