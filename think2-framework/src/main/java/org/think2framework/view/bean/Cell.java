package org.think2framework.view.bean;

import java.util.List;

/**
 * Created by zhoubin on 2017/6/12. 视图单元定义
 */
public class Cell {

	private String name; // 名称，单模块唯一

	private String title; // 标题

	private String tag; // 标签，根据标签操作单元

	private Boolean required = false; // 是否必填项，默认false

	private String defaultValue; // 默认值，now当前时间，user.id登录用户id，user.name登录用户名，其他则填值

	private Boolean center = true; // 列是否居中默认true

	private Integer width = 0; // 列长度，默认0表示自动

	private Boolean search = false; // 是否作为搜索页，默认false，TEXT查询为like

	private Boolean display = true; // 查询页面是否显示列，默认true

	private Boolean detail = true; // 显示详情页面是否显示列，默认true

	private Boolean add = true;// 添加页面是否需要添加列，默认true

	private Boolean edit = true; // 编辑页面是否需要列，默认true

	private Boolean rowFilter = false; // 是否行级过滤，默认false，只有当类型为item或者dataItem的时候才有效

	private String comment; // 注释

	private List<Item> items; // 单元格的item定义，主要用于状态选择、人员选择等选择类

	public Cell() {
	}

	public Cell(String name, String title, String tag, Boolean required, String defaultValue, Boolean center,
			Integer width, Boolean search, Boolean display, Boolean detail, Boolean add, Boolean edit,
			Boolean rowFilter, String comment, List<Item> items) {
		this.name = name;
		this.title = title;
		this.tag = tag;
		this.required = required;
		this.defaultValue = defaultValue;
		this.center = center;
		this.width = width;
		this.search = search;
		this.display = display;
		this.detail = detail;
		this.add = add;
		this.edit = edit;
		this.rowFilter = rowFilter;
		this.comment = comment;
		this.items = items;
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

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Boolean getCenter() {
		return center;
	}

	public void setCenter(Boolean center) {
		this.center = center;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Boolean getSearch() {
		return search;
	}

	public void setSearch(Boolean search) {
		this.search = search;
	}

	public Boolean getDisplay() {
		return display;
	}

	public void setDisplay(Boolean display) {
		this.display = display;
	}

	public Boolean getDetail() {
		return detail;
	}

	public void setDetail(Boolean detail) {
		this.detail = detail;
	}

	public Boolean getAdd() {
		return add;
	}

	public void setAdd(Boolean add) {
		this.add = add;
	}

	public Boolean getEdit() {
		return edit;
	}

	public void setEdit(Boolean edit) {
		this.edit = edit;
	}

	public Boolean getRowFilter() {
		return rowFilter;
	}

	public void setRowFilter(Boolean rowFilter) {
		this.rowFilter = rowFilter;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
}
