package org.think2framework.context.bean;

import org.think2framework.orm.core.TypeUtils;
import org.think2framework.mvc.view.bean.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhoubin on 2017/6/12. 模型的列定义
 */
public class Column {

	/**
	 * 列名
	 */
	private String name;

	/**
	 * 标题
	 */
	private String title;// 列的标题

	private String tag = "text"; // 列的标签

	private Boolean required = false; // 是否必填项，默认false

	private String alias = ""; // 字段别名

	private String join = ""; // 字段所属关联名称,如果为空表示主表

	private Integer length = 50; // 字段长度

	private Integer scale = 0; // 字段精度(小数位数)

	private String defaultValue; // 默认值，now当前时间，user.id登录用户id，user.name登录用户名，其他则填值

	private Boolean center = true; // 列是否居中默认true

	private Integer width = 0; // 列长度，默认0表示自动

	private Boolean search = false; // 是否作为搜索页，默认false，TEXT查询为like

	private Boolean display = true; // 查询页面是否显示列，默认true

	private Boolean detail = true; // 显示详情页面是否显示列，默认true

	private Boolean add = true;// 添加页面是否需要添加列，默认true

	private Boolean edit = true; // 编辑页面是否需要列，默认true

	private Boolean rowFilter = false; // 是否行级过滤，默认false，只有当类型为item或者dataItem的时候才有效

	private List<Item> items;// 单元格的item定义，主要用于状态选择、人员选择等选择类

	/**
	 * 获取模型列名称
	 * 
	 * @return 列名
	 */
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
		// 如果是bool类型则增加item，1-true，0-false
		if (TypeUtils.FIELD_BOOL.equalsIgnoreCase(tag)) {
			defaultValue = "0";
			length = 1;
			items = new ArrayList<>();
			items.add(new Item("", "是", "1"));
			items.add(new Item("", "否", "0"));
		}
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getJoin() {
		return join;
	}

	public void setJoin(String join) {
		this.join = join;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getScale() {
		return scale;
	}

	public void setScale(Integer scale) {
		this.scale = scale;
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

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

}
