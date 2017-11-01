package org.think2framework.mvc.view;

import org.think2framework.mvc.view.bean.Item;
import org.think2framework.mvc.view.core.*;
import org.think2framework.orm.core.ClassUtils;
import org.think2framework.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * html标签工厂
 */
public class HtmlTagFactory {

	/**
	 * 创建一个bool类型选择框
	 * 
	 * @param name
	 *            控件名称
	 * @param required
	 *            是否必选
	 * @return bool类型选择框
	 */
	public static HtmlTag createBool(String name, Boolean required) {
		List<Item> items = new ArrayList<>();
		items.add(new Item("", ClassUtils.FALSE_VALUE.toString(), "否"));
		items.add(new Item("", ClassUtils.TRUE_VALUE.toString(), "是"));
		return createSelect(name, required, items);
	}

	/**
	 * 创建一个选择框
	 *
	 * @param name
	 *            控件名称
	 * @param required
	 *            是否必选
	 * @return 选择框
	 */
	public static HtmlTag createSelect(String name, Boolean required, List<Item> items) {
		SelectTag selectTag = new SelectTag();
		selectTag.setOptions(items);
		selectTag.setAttribute("name", name);
		if (required) {
			selectTag.setAttribute("required", "required");
		}
		return selectTag;
	}

	/**
	 * 创建一个多选框
	 *
	 * @param name
	 *            控件名称
	 * @param required
	 *            是否必选
	 * @return 选择框
	 */
	public static HtmlTag createMultipleSelect(String name, Boolean required, List<Item> items) {
		MultipleSelectTag multipleSelectTag = new MultipleSelectTag();
		multipleSelectTag.setOptions(items);
		multipleSelectTag.setAttribute("name", name);
		if (required) {
			multipleSelectTag.setAttribute("required", "required");
		}
		return multipleSelectTag;
	}

	/**
	 * 创建一个输入框
	 *
	 * @param name
	 *            控件名称
	 * @param required
	 *            是否必选
	 * @param length
	 *            最大长度
	 * @param comment
	 *            注释
	 * @return 输入框
	 */
	public static HtmlTag createInput(String name, Boolean required, Integer length, String comment) {
		HtmlTag htmlTag = new SimpleHtmlTag("input", "col-xs-12 col-sm-12");
		htmlTag.setAttribute("name", name);
		htmlTag.setAttribute("type", "text");
		if (required) {
			htmlTag.setAttribute("required", "required");
		}
		if (null != length && length > 0) {
			htmlTag.setAttribute("maxlength", StringUtils.toString(length));
		}
		if (StringUtils.isNotBlank(comment)) {
			htmlTag.setAttribute("placeholder", comment);
		}
		return htmlTag;
	}

	/**
	 * 创建一个数字输入框
	 *
	 * @param name
	 *            控件名称
	 * @param required
	 *            是否必选
	 * @param length
	 *            最大长度
	 * @param comment
	 *            注释
	 * @return 数字输入框
	 */
	public static HtmlTag createNumber(String name, Boolean required, Integer length, String comment) {
		HtmlTag htmlTag = createInput(name, required, length, comment);
		htmlTag.setAttribute("type", "number");
		return htmlTag;
	}

	/**
	 * 创建一个密码输入框
	 *
	 * @param name
	 *            控件名称
	 * @param required
	 *            是否必选
	 * @param length
	 *            最大长度
	 * @param comment
	 *            注释
	 * @return 密码输入框
	 */
	public static HtmlTag createPassword(String name, Boolean required, Integer length, String comment) {
		HtmlTag htmlTag = createInput(name, required, length, comment);
		htmlTag.setAttribute("type", "password");
		return htmlTag;
	}

	/**
	 * 创建一个时间戳输入框
	 *
	 * @param name
	 *            控件名称
	 * @param required
	 *            是否必选
	 * @return 时间戳输入框
	 */
	public static HtmlTag createTimestamp(String name, Boolean required) {
		TimestampTag timestampTag = new TimestampTag();
		timestampTag.setAttribute("name", name);
		if (required) {
			timestampTag.setAttribute("required", "required");
		}
		return timestampTag;
	}

	/**
	 * 创建一个时间输入框
	 *
	 * @param name
	 *            控件名称
	 * @param required
	 *            是否必选
	 * @return 时间输入框
	 */
	public static HtmlTag createDatetime(String name, Boolean required) {
		DatetimeTag datetimeTag = new DatetimeTag();
		datetimeTag.setAttribute("name", name);
		if (required) {
			datetimeTag.setAttribute("required", "required");
		}
		return datetimeTag;
	}

	/**
	 * 创建一个文件选择框
	 *
	 * @param name
	 *            控件名称
	 * @param required
	 *            是否必选
	 * @return 文件选择框
	 */
	public static HtmlTag createFile(String name, Boolean required) {
		FileTag fileTag = new FileTag();
		fileTag.setAttribute("name", name);
		if (required) {
			fileTag.setAttribute("required", "required");
		}
		return fileTag;
	}

}
