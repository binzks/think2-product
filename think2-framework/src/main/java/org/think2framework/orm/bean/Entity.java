package org.think2framework.orm.bean;

import java.util.List;
import java.util.Map;

/**
 * Created by zhoubin on 16/7/12. 实体bean
 */
public class Entity {

	private String table; // 主表名称

	private String pk; // 主键名称

	private Map<String, EntityColumn> columns; // 列

	private String joinSql; // 关联sql

	private String columnSql; // 默认查询列sql

	private List<Filter> filters; // 默认过滤条件

	private List<Order> orders; // 默认排序

	public Entity() {
	}

	public Entity(String table, String pk, Map<String, EntityColumn> columns, String joinSql, String columnSql,
			List<Filter> filters, List<Order> orders) {
		this.table = table;
		this.pk = pk;
		this.columns = columns;
		this.joinSql = joinSql;
		this.columnSql = columnSql;
		this.filters = filters;
		this.orders = orders;
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

	public Map<String, EntityColumn> getColumns() {
		return columns;
	}

	public void setColumns(Map<String, EntityColumn> columns) {
		this.columns = columns;
	}

	public String getJoinSql() {
		return joinSql;
	}

	public void setJoinSql(String joinSql) {
		this.joinSql = joinSql;
	}

	public String getColumnSql() {
		return columnSql;
	}

	public void setColumnSql(String columnSql) {
		this.columnSql = columnSql;
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
}
