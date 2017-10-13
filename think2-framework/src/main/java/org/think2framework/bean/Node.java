package org.think2framework.bean;

/**
 * 工作流节点
 */
public class Node {

	private String name; // 节点名称，本工作流唯一

	private String value; // 节点值，本工作流唯一

	private String users; // 节点可操作用户编号

	private String next; // 下一个节点，如果为null表示终点

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getUsers() {
		return users;
	}

	public void setUsers(String users) {
		this.users = users;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}
}
