package org.think2framework.view.core;

/**
 * 简单的通用html标签，只有基本属性设置的
 */
public class SimpleHtmlTag extends AbstractHtmlTag {

	public SimpleHtmlTag(String tag) {
		super(tag);
	}

	public SimpleHtmlTag(String tag, String clazz) {
		super(tag);
		setAttribute("class", clazz);
	}

	public SimpleHtmlTag(String tag, String clazz, String text) {
		super(tag);
		setAttribute("class", clazz);
		setText(text);
	}
}
