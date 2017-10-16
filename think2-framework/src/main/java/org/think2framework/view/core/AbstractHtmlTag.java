package org.think2framework.view.core;

import org.think2framework.utils.StringUtils;
import org.think2framework.view.HtmlTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抽象的html标签，公共的内容
 */
public class AbstractHtmlTag implements HtmlTag {

	private String tag; // 设置标签

	private String text; // 文本内容

	private Integer childSize = 0; // 子标签数量

	private Map<String, String> attributes = new HashMap<>();// 属性

	private List<HtmlTag> children = new ArrayList<>(); // 子标签

	public AbstractHtmlTag(String tag) {
		this.tag = tag;
	}

	@Override
	public String htmlString() {
		StringBuffer html = new StringBuffer();
		html.append("<").append(tag);
		for (Map.Entry<String, String> entry : attributes.entrySet()) {
			html.append(" ").append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
		}
		html.append(">").append(childrenString());
		if (StringUtils.isNotBlank(text)) {
			html.append(text);
		}
		html.append("</").append(tag).append(">");
		return html.toString();
	}

	@Override
	public void appendChild(HtmlTag child) {
		if (null != child) {
			children.add(child);
			childSize++;
		}
	}

	@Override
	public List<HtmlTag> getChildren() {
		return children;
	}

	@Override
	public String childrenString() {
		StringBuffer html = new StringBuffer();
		for (HtmlTag htmlTag : children) {
			html.append(htmlTag.htmlString());
		}
		return html.toString();
	}

	@Override
	public Boolean hasChild() {
		return childSize > 0;
	}

	@Override
	public void setAttribute(String name, String value) {
		if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(value)) {
			attributes.put(name, value);
		}
	}

	@Override
	public void setValue(String value) {
		text = value;
	}

	@Override
	public void setText(String value) {
		text = value;
	}

	@Override
	public String getValue() {
		return text;
	}

	@Override
	public String getText() {
		return text;
	}
}
