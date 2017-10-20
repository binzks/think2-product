package org.think2framework.orm.bean;

import java.util.List;

/**
 * 查询实体的排序
 */
public class Order {

	/**
	 * 排序字段数组
	 */
	private List<String> keys;

	/**
	 * 排序规则asc、desc
	 */
	private String type;

	public Order() {
	}

	public Order(List<String> keys, String type) {
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
