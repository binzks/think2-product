package org.think2framework.view.core;

/**
 * a标签
 */
public class ATag extends AbstractHtmlTag {

	public ATag() {
		super("a");
	}

	public ATag(String href) {
		super("a");
		setAttribute("href", href);
	}

	public ATag(String href, String clazz, String title) {
		super("a");
		setAttribute("href", href);
		setAttribute("class", clazz);
		setAttribute("title", title);
	}
}
