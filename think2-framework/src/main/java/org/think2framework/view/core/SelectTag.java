package org.think2framework.view.core;

import org.think2framework.ModelFactory;
import org.think2framework.orm.Query;
import org.think2framework.utils.StringUtils;
import org.think2framework.view.HtmlTag;
import org.think2framework.view.bean.Item;

import java.util.List;
import java.util.Map;

/**
 * 单选框
 */
public class SelectTag extends AbstractHtmlTag implements HtmlTag {

	private String value; // 单选框实际值

	public SelectTag() {
		super("select");
		setAttribute("class", "chosen-select form-control");
		setAttribute("data-placeholder", "请选择...");
	}

	public void setOptions(List<Item> items) {
		StringBuffer html = new StringBuffer();
		for (Item item : items) {
			if (StringUtils.isBlank(item.getModel())) {
				html.append("<option value=\"").append(item.getKey()).append("\"").append(">").append(item.getValue())
						.append("</option>");
			} else {
				Query query = ModelFactory.createQuery(item.getModel());
				List<Map<String, Object>> list = query.queryForList();
				for (Map<String, Object> map : list) {
					html.append("<option value=\"").append(StringUtils.toString(map.get(item.getKey()))).append("\"")
							.append(">").append(StringUtils.toString(map.get(item.getValue()))).append("</option>");
				}
			}
		}
		setText(html.toString());
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String getValue() {
		return value;
	}

}
