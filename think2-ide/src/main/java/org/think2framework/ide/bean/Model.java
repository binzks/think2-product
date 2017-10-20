package org.think2framework.ide.bean;

import java.util.List;

import org.think2framework.mvc.bean.BaseCms;
import org.think2framework.bean.Column;
import org.think2framework.orm.bean.Filter;
import org.think2framework.orm.bean.Join;
import org.think2framework.orm.bean.Order;
import org.think2framework.orm.persistence.Table;
import org.think2framework.mvc.view.bean.Action;

/**
 * Created by zhoubin on 16/7/11. 模型
 */
@Table(name = "think2_model")
public class Model extends BaseCms{

	private String name; // 模型名称

	private String table; // 主表表名

	private String pk = "id"; // 主表主键名称

	private Boolean autoincrement = true; // 主表是否自增长

	private String workflow; // 工作流编号

	private Boolean cms = false; // 是否追加cms通用字段

	private String[] uniques; // 主表的唯一性约束

	private String[] indexes; // 主表索引

	private List<Column> columns; // 模型列定义

	private List<Action> actions; // 模型的按钮定义

	private List<Join> joins; // 关联表

	private List<Filter> filters; // 过滤条件

	private List<Order> orders; // 排序

	private String comment; // 注释

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public Boolean getAutoincrement() {
		return autoincrement;
	}

	public void setAutoincrement(Boolean autoincrement) {
		this.autoincrement = autoincrement;
	}

	public String getWorkflow() {
		return workflow;
	}

	public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}

	public Boolean getCms() {
		return cms;
	}

	public void setCms(Boolean cms) {
		this.cms = cms;
	}

	public String[] getUniques() {
		return uniques;
	}

	public void setUniques(String[] uniques) {
		this.uniques = uniques;
	}

	public String[] getIndexes() {
		return indexes;
	}

	public void setIndexes(String[] indexes) {
		this.indexes = indexes;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	public List<Join> getJoins() {
		return joins;
	}

	public void setJoins(List<Join> joins) {
		this.joins = joins;
	}

	public List<Filter> getFilters() {
		return filters;
	}

	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
