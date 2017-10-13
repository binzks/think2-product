package org.think2framework.view;

import java.util.List;

/**
 * html标签
 */
public interface HtmlTag {

	/**
	 * 标签的字符串内容
	 * 
	 * @return 字符串
	 */
	String htmlString();

	/**
	 * 追加一个子标签
	 * 
	 * @param child
	 *            子标签
	 */
	void appendChild(HtmlTag child);

	/**
	 * 获取标签的所有子标签
	 * 
	 * @return 所有子标签
	 */
	List<HtmlTag> getChildren();

	/**
	 * 获取所有子标签的字符串
	 * 
	 * @return 子标签字符串
	 */
	String childrenString();

	/**
	 * 是否有子标签
	 * 
	 * @return
	 */
	Boolean hasChild();

	/**
	 * 设置一个通用属性
	 * 
	 * @param name
	 *            名称
	 * @param value
	 *            值
	 */
	void setAttribute(String name, String value);

	/**
	 * 设置标签的值
	 * 
	 * @param value
	 *            值
	 */
	void setValue(String value);

	/**
	 * 设置标签的文本内容
	 * 
	 * @param value
	 *            文本
	 */
	void setText(String value);

	/**
	 * 获取标签的实际值
	 * 
	 * @return 值
	 */
	String getValue();

	/**
	 * 获取标签的文本内容
	 * 
	 * @return 文本
	 */
	String getText();
}
