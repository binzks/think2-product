package org.think2framework.context.bean;

/**
 * Created by zhoubin on 16/7/24. 消息
 */
public class Message {

	private String code; // 编号

	private Object data; // 内容,如果需要参数则以?代替

	public Message() {

	}

	public Message(String code, Object data) {
		this.code = code;
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
