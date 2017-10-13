package org.think2framework.orm.bean;

import java.util.List;

/**
 * Created by zhoubin on 16/7/5. 查询条件
 */
public class Filter {

	private String key; // 过滤关键字

	private String type; // 过滤类型

	private List<Object> values; // 过滤值

	public Filter() {
	}

	public Filter(String key, String type, List<Object> values) {
		this.key = key;
		this.type = type;
		this.values = values;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Object> getValues() {
		return values;
	}

	public void setValues(List<Object> values) {
		this.values = values;
	}
}
