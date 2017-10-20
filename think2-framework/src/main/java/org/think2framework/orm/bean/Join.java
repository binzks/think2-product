package org.think2framework.orm.bean;

/**
 * 查询实体的关联条件
 */
public class Join {

	/**
	 * 关联的名称，用于标识关联，获取关联表的表名和别名
	 */
	private String name;

	/**
	 * 关联表所在数据库名称
	 */
	private String database;

	/**
	 * 关联的表名称
	 */
	private String table;

	/**
	 * 关联类型 left join, right join, inner join
	 */
	private String type = "left join";

	/**
	 * 关联表的字段名称
	 */
	private String key;

	/**
	 * 关联的主表的关联名称，如果null或者空表示关联主表，如果不为null表示关联实体的关联表
	 */
	private String joinName;

	/**
	 * 关联的主表的字段名称
	 */
	private String joinKey;

	/**
	 * 额外的过滤条件，手动加上在join后
	 */
	private String filter;

	public Join() {
	}

	public Join(String name, String database, String table, String type, String key, String joinName, String joinKey,
			String filter) {
		this.name = name;
		this.database = database;
		this.table = table;
		this.type = type;
		this.key = key;
		this.joinName = joinName;
		this.joinKey = joinKey;
		this.filter = filter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getJoinName() {
		return joinName;
	}

	public void setJoinName(String joinName) {
		this.joinName = joinName;
	}

	public String getJoinKey() {
		return joinKey;
	}

	public void setJoinKey(String joinKey) {
		this.joinKey = joinKey;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}
}
