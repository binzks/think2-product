package org.think2framework.orm.bean;

/**
 * 数据库表的列
 */
public class TableColumn {

	/**
	 * 字段名称
	 */
	private String name;

	/**
	 * 字段类型
	 */
	private String type;

	/**
	 * 字段是否可空
	 */
	private Boolean nullable;

	/**
	 * 字段长度
	 */
	private Integer length;

	/**
	 * 字段精度(小数位数)
	 */
	private Integer scale;

	/**
	 * 字段默认值
	 */
	private Object defaultValue;

	/**
	 * 字段注释
	 */
	private String comment;

	public TableColumn() {

	}

	public TableColumn(String name, String type, Boolean nullable, Integer length, Integer scale, Object defaultValue,
			String comment) {
		this.name = name;
		this.type = type;
		this.nullable = nullable;
		this.length = length;
		this.scale = scale;
		this.defaultValue = defaultValue;
		this.comment = comment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getNullable() {
		return nullable;
	}

	public void setNullable(Boolean nullable) {
		this.nullable = nullable;
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

	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
