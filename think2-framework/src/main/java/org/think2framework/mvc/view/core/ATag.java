package org.think2framework.mvc.view.core;

import org.think2framework.utils.StringUtils;

/**
 * a标签
 */
public class ATag extends AbstractHtmlTag {

	private String href; // href

	public ATag() {
		super("a");
	}

	public ATag(String href) {
		super("a");
		this.href = href;
		setAttribute("href", href);
	}

	public ATag(String href, String clazz, String title) {
		super("a");
		this.href = href;
		setAttribute("href", href);
		setAttribute("class", clazz);
		setAttribute("title", title);
	}

	/**
	 * 生成一个a标签，带i标签
	 * 
	 * @param href
	 *            href
	 * @param clazz
	 *            a标签类
	 * @param text
	 *            a标签的文本值
	 * @param iClass
	 *            i标签的类
	 */
	public ATag(String href, String clazz, String text, String iClass) {
		super("a");
		this.href = href;
		setAttribute("href", href);
		setAttribute("class", clazz);
		setText(text);
		if (StringUtils.isNotBlank(iClass)) {
			appendChild(new SimpleHtmlTag("i", iClass));
		}
	}

	/**
	 * 对于已经设置了href并且其中带有%s参数的，返回将%s替换为传入值的html，主要是list页面每行数据按钮
	 * 
	 * @param value
	 *            替换实际值
	 * @return html
	 */
	public String toDataHtml(String value) {
		setAttribute("href", String.format(href, value));
		return htmlString();
	}
}
