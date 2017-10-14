package org.think2framework.bean;

import org.think2framework.orm.persistence.Column;
import org.think2framework.orm.persistence.Table;
import org.think2framework.view.persistence.Item;

/**
 * cms系统模块
 */
@Table(name = "think2_module", indexes = { "type", "order" }, uniques = { "name" }, comment = "系统模块表")
public class Module extends BaseCms {

	@Column(nullable = false, comment = "模块名称")
	private String name;

	@Column(name = "parent_id", length = 11, comment = "父节点id")
	private Integer parentId;

	@Item(key = "0", value = "模块组")
	@Item(key = "1", value = "模块")
	@Column(nullable = false, length = 1, comment = "模块类型0-模块组 1-模块")
	private Integer type;

	@Column(comment = "模块组图标，模块可不填")
	private String icon;

	@Column(comment = "模块对应的模型名称")
	private String model;

	@Column(comment = "模块对应视图的标题")
	private String title;

	@Column(comment = "模块链接")
	private String uri;

	@Column(length = 2, comment = "列表页面每页行数")
	private Integer size;

	@Column(length = 2, comment = "排序")
	private Integer order;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}
}
