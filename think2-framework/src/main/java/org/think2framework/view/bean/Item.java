package org.think2framework.view.bean;

/**
 * Created by zhoubin on 2017/6/12. cell的选择定义，支持固定的几个下拉项或者从数据源选择数据
 */
public class Item {

	private String model; // 模型名称，如果是从数据源选择则填写，如果不是则不填

	private String key; // 如果模型名称不为空，则表示为从数据源选择数据的取值字段名称，如果模型名称为空表示固定的值的显示值

	private String value; // 如果模型名称不为空，则表示为从数据源选择数据的显示字段名称，如果模型名称为空表示固定值

	public Item() {
	}

	public Item(String model, String key, String value) {
		this.model = model;
		this.key = key;
		this.value = value;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
