package org.think2framework.view.core;

import org.think2framework.ModelFactory;
import org.think2framework.orm.Query;
import org.think2framework.utils.StringUtils;
import org.think2framework.view.HtmlTag;
import org.think2framework.view.bean.Item;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 单选框
 */
public class SelectTag extends AbstractHtmlTag implements HtmlTag {

	private String value; // 单选框实际值

	private Map<String, String> options; // 选项

	public SelectTag() {
		super("select");
		options = new LinkedHashMap<>();
		options.put("", "");
		setAttribute("class", "chosen-select form-control");
		setAttribute("data-placeholder", "请选择...");
	}

	/**
	 * 设置一个选项
	 * 
	 * @param key
	 *            选项值
	 * @param value
	 *            选项显示名称
	 */
	public void setOption(String key, String value) {
		options.put(key, value);
	}

	public void setOptions(List<Item> items) {
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

	@Override
	public String htmlString() {
		StringBuffer html = new StringBuffer();
		html.append("<select");
		for (Map.Entry<String, String> entry : attributes.entrySet()) {
			html.append(" ").append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
		}
		html.append(">");
		for (Map.Entry<String, String> entry : options.entrySet()) {
			html.append("<option value=\"").append(entry.getKey()).append("\"");
			if (entry.getKey().equals(value)) {
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
		for (Map.Entry<String, String> entry : options.entrySet()) {
			if (entry.getKey().equals(value)) {
				return entry.getValue();
			}
		}
		return "";
	}

}
