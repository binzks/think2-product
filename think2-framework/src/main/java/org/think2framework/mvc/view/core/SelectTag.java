package org.think2framework.mvc.view.core;

import org.think2framework.context.ModelFactory;
import org.think2framework.orm.Query;
import org.think2framework.utils.StringUtils;
import org.think2framework.mvc.view.HtmlTag;
import org.think2framework.mvc.view.bean.Item;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 单选框
 */
public class SelectTag extends AbstractHtmlTag implements HtmlTag {

	/**
	 * 单选框实际值
	 */
	protected String value;

	/**
	 * 选项
	 */
	private Map<String, String> options;

	/**
	 * 选项定义，如果是固定选项的则为null，存储数据表示有数据源选项，需要刷新数据
	 */
	private List<Item> items;

	public SelectTag() {
		super("select");
		options = new LinkedHashMap<>();
		options.put("", "");
		setAttribute("class", "chosen-select form-control");
		setAttribute("data-placeholder", "请选择...");
	}

	/**
	 * 设置选项，如果选项中带模型，则设置items，在每次获取html的时候时候刷新数据，如果没有则一次性添加后不再修改
	 * 
	 */
	public void setOptions(List<Item> items) {
		if (null == items) {
			return;
		}
		for (Item item : items) {
			if (StringUtils.isBlank(item.getModel())) {
				options.put(item.getKey(), item.getValue());
			} else {
				this.items = items;
				break;
			}
		}
	}

	/**
	 * 刷新选项，如果items有表示数据源刷新，否则表示固定选项不需要刷新
	 */
	private void refreshOptions() {
		if (null == items) {
			return;
		}
		options.clear();
		for (Item item : items) {
			if (StringUtils.isBlank(item.getModel())) {
				options.put(item.getKey(), item.getValue());
			} else {
				Query query = ModelFactory.createQuery(item.getModel());
				List<Map<String, Object>> list = query.queryForList();
				for (Map<String, Object> map : list) {
					options.put(StringUtils.toString(map.get(item.getKey())),
							StringUtils.toString(map.get(item.getValue())));
				}
			}
		}
	}

	/**
	 * 校验value在已经设置的标签值是不是存在
	 * 
	 * @param value
	 *            待校验值
	 * @return 是否存在
	 */
	protected Boolean checkValue(String value) {
		return StringUtils.equalsIgnoreCase(value, this.value);
	}

	@Override
	public String htmlString() {
		refreshOptions();
		StringBuffer html = new StringBuffer();
		html.append("<select");
		for (Map.Entry<String, String> entry : attributes.entrySet()) {
			html.append(" ").append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
		}
		html.append(">");
		for (Map.Entry<String, String> entry : options.entrySet()) {
			String key = entry.getKey();
			html.append("<option value=\"").append(key).append("\"");
			if (checkValue(entry.getKey())) {
				html.append(" selected");
				text = entry.getValue();
			}
			html.append(">").append(entry.getValue()).append("</option>");
		}
		html.append(childrenString());
		if (StringUtils.isNotBlank(text)) {
			html.append(text);
		}
		html.append("</select>");
		return html.toString();
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String getText() {
	    refreshOptions();
		StringBuffer text = new StringBuffer();
		for (Map.Entry<String, String> entry : options.entrySet()) {
			if (checkValue(entry.getKey())) {
				text.append(",").append(entry.getValue());
			}
		}
		if (text.length() > 0) {
			return text.substring(1);
		} else {
			return "";
		}
	}

}
