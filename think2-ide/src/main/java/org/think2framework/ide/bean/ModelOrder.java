package org.think2framework.ide.bean;

import java.util.List;

/**
 * Created by zhoubin on 16/9/27. 排序
 */
public class ModelOrder {

	private List<String> keys;

	private String type;

	public ModelOrder() {
	}

	public ModelOrder(List<String> keys, String type) {
		this.keys = keys;
		this.type = type;
	}

	public List<String> getKeys() {
		return keys;
	}

	public void setKeys(List<String> keys) {
		this.keys = keys;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
