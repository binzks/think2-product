package org.think2framework.bean;

import java.util.List;

public class Workflow {

	private String name; // 工作流名称，唯一

	private String field; // 工作流对应流转字段名称

	private String tag; // 工作流对应字段的标签

	private Integer length; // 工作流对应字段的长度

	private String edit; // 编辑链接

	private String detail; // 详情链接

	private List<Node> nodes; // 工作流节点定义

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public String getEdit() {
		return edit;
	}

	public void setEdit(String edit) {
		this.edit = edit;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}
}
