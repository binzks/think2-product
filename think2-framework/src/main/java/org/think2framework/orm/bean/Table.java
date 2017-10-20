package org.think2framework.orm.bean;

import java.util.List;

/**
 * 数据库表
 */
public class Table {

	/**
	 * 表名
	 */
	private String name;

	/**
	 * 主键名称
	 */
	private String pk;

	/**
	 * 是否是整型自增长,如果不是则为UUID类型
	 */
	private Boolean autoIncrement;

	/**
	 * 唯一性约束,多个字段用,隔开
	 */
	private String[] uniques;

	/**
	 * 索引,多个字段用,隔开
	 */
	private String[] indexes;

	/**
	 * 注释
	 */
	private String comment;

	/**
	 * 表的列
	 */
	private List<TableColumn> columns;

	public Table() {
	}

	public Table(String name, String pk, Boolean autoIncrement, String[] uniques, String[] indexes, String comment,
			List<TableColumn> columns) {
		this.name = name;
		this.pk = pk;
		this.autoIncrement = autoIncrement;
		this.uniques = uniques;
		this.indexes = indexes;
		this.comment = comment;
		this.columns = columns;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public Boolean getAutoIncrement() {
		return autoIncrement;
	}

	public void setAutoIncrement(Boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<TableColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<TableColumn> columns) {
		this.columns = columns;
	}
}
