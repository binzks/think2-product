package org.think2framework.mvc.view.bean;

import org.think2framework.orm.core.TypeUtils;

import java.util.List;

/**
 * 视图单元定义
 */
public class Cell {

	/**
	 * 名称，单模块唯一
	 *
	 * @return 名称
	 */
	private String name;

	/**
	 * 标题
	 *
	 * @return 标题
	 */
	private String title;

	/**
	 * 标签，根据标签操作单元，默认问文本
	 *
	 * @return 标签
	 */
	private String tag = TypeUtils.FIELD_TEXT;

	/**
	 * 是否必填项，默认false
	 *
	 * @return 是否必填项
	 */
	private Boolean required;

	/**
	 * 单元格最大长度
	 *
	 * @return 最大长度
	 */
	private Integer length = 0;

	/**
	 * 默认值，now当前时间，user.id登录用户id，user.name登录用户名，其他则填值
	 *
	 * @return 默认值
	 */
	private String defaultValue = "";

	/**
	 * 是否作为搜索项，默认false
	 *
	 * @return 是否作为搜索项
	 */
	private Boolean search = false;

	/**
	 * 查询页面是否显示列，默认true
	 *
	 * @return 查询页面是否显示列
	 */
	private Boolean display = true;

	/**
	 * 显示详情页面是否显示列，默认true
	 *
	 * @return 显示详情页面是否显示列
	 */
	private Boolean detail = true;

	/**
	 * 添加页面是否需要添加列，默认true
	 *
	 * @return 添加页面是否需要添加列
	 */
	private Boolean add = true;

	/**
	 * 编辑页面是否需要列，默认true
	 *
	 * @return 编辑页面是否需要列
	 */
	private Boolean edit = true;

	/**
	 * 是否行级过滤，默认false，只有当类型为item或者dataItem的时候才有效
	 *
	 * @return 是否行级过滤
	 */
	private Boolean rowFilter = false;

	/**
	 * 注释
	 *
	 * @return 注释
	 */
	private String comment = "";

	/**
	 * 单元格的item定义，主要用于状态选择、人员选择等选择类
	 */
	private List<Item> items;

	public Cell() {
	}

	public Cell(String name, String title, String tag, Boolean required, Integer length, String defaultValue,
			Boolean search, Boolean display, Boolean detail, Boolean add, Boolean edit, Boolean rowFilter,
			String comment, List<Item> items) {
		this.name = name;
		this.title = title;
		this.tag = tag;
		this.required = required;
		this.length = length;
		this.defaultValue = defaultValue;
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

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
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
