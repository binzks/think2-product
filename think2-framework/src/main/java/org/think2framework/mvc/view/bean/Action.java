package org.think2framework.mvc.view.bean;

/**
 * Created by zhoubin on 2017/6/12. 模型的操作按钮
 */
public class Action {

	private String name; // 名称，模型唯一，用于授权

	private String title; // 标题

	private String type; // 按钮类型 0-搜索框上的按钮 1-表格中每条数据的按钮

	private String href; // 按钮点击后的href

	private String clazz; // 按钮类

	public Action() {
	}

	public Action(String name, String title, String type, String href, String clazz) {
		this.name = name;
		this.title = title;
		this.type = type;
		this.href = href;
		this.clazz = clazz;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
}
