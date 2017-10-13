package org.think2framework.bean;

/**
 * Created by zhoubin on 16/2/16. 常量bean
 */
public class Constant {

	private String name; // 常量名称

	private String display; // 常量显示值

	private String group; // 常量分组名称

	private String value; // 常量值

	public Constant() {

	}

	public Constant(String name, String display, String group, String value) {
		this.name = name;
		this.display = display;
		this.group = group;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
