package org.think2framework.orm.core;

import org.think2framework.exception.NonExistException;
import org.think2framework.exception.SimpleException;
import org.think2framework.orm.bean.EntityColumn;
import org.think2framework.orm.bean.Filter;
import org.think2framework.orm.bean.Join;
import org.think2framework.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhoubin on 16/7/12. 生成通用查询语句,关联,字段,过滤条件
 */
public class SelectHelp {

	public static final String TABLE_ALIAS = "t"; // 查询主表别名

	/**
	 * 生成join sql语句
	 * 
	 * @param joins
	 *            关联
	 * @return sql
	 */
	public static String generateJoins(List<Join> joins) {
		if (null == joins || joins.size() == 0) {
			return "";
		}
		StringBuilder joinSql = new StringBuilder();
		for (Join join : joins) {
			joinSql.append(join.getType().toUpperCase()).append(" ");
			if (StringUtils.isNotBlank(join.getDatabase())) {
				joinSql.append(join.getDatabase()).append(".");
			}
			joinSql.append(join.getTable()).append(" AS ").append(join.getName()).append(" ON ").append(join.getName())
					.append(".").append(join.getKey()).append("=");
			if (StringUtils.isBlank(join.getJoinName())) {
				joinSql.append(TABLE_ALIAS);
			} else {
				joinSql.append(join.getJoinName());
			}
			joinSql.append(".").append(join.getJoinKey());
			if (StringUtils.isNotBlank(join.getFilter())) {
				joinSql.append(" ").append(join.getFilter());
			}
		}
		return joinSql.toString();
	}

	/**
	 * 根据列定义获取列的查询字段sql
	 * 
	 * @param columns
	 *            列定义
	 * @return sql
	 */
	public static String generateColumns(Map<String, EntityColumn> columns) {
		if (null == columns || columns.size() == 0) {
			return "";
		}
		StringBuilder sql = new StringBuilder();
		for (EntityColumn column : columns.values()) {
			sql.append(",").append(StringUtils.isBlank(column.getJoin()) ? TABLE_ALIAS : column.getJoin()).append(".")
					.append(column.getName());
			if (StringUtils.isNotBlank(column.getAlias())) {
				sql.append(" AS ").append(column.getAlias());
			}
		}
		return sql.substring(1);
	}

	/**
	 * 获取字段对应的查询sql,表别名.字段名
	 * 
	 * @param name
	 *            字段名称
	 * @param columns
	 *            主表的列定义
	 * @return 表别名.字段名
	 */
	public static String getColumnKeySql(String name, Map<String, EntityColumn> columns) {
		return getColumnKeySql(name, columns, false);
	}

	/**
	 * 获取字段对应的查询sql,表别名.字段名 AS 别名
	 *
	 * @param name
	 *            字段名称
	 * @param columns
	 *            主表的列定义
	 * @param alias
	 *            是否获取别名
	 * @return 表别名.字段名 AS 别名
	 */
	public static String getColumnKeySql(String name, Map<String, EntityColumn> columns, Boolean alias) {
		EntityColumn column = columns.get(name);
		if (null == column) {
			throw new NonExistException("Failed to generate column key sql, column " + name);
		}
		StringBuilder sql = new StringBuilder();
		sql.append(StringUtils.isBlank(column.getJoin()) ? TABLE_ALIAS : column.getJoin()).append(".")
				.append(column.getName());
		if (StringUtils.isNotBlank(column.getAlias()) && alias) {
			sql.append(" AS ").append(column.getAlias());
		}
		return sql.toString();
	}

	/**
	 * 生成查询条件sql和值
	 * 
	 * @param filters
	 *            过滤
	 * @param columns
	 *            列定义
	 * @return 2个值得数组,第一个sql语句,第二个是值的Object[]
	 */
	public static Object[] generateFilters(List<Filter> filters, Map<String, EntityColumn> columns) {
		Object[] result = new Object[2];
		StringBuilder sql = new StringBuilder();
		List<Object> sqlValues = new ArrayList<>();
		for (Filter filter : filters) {
			List<Object> values = filter.getValues();
			String type = filter.getType();
			sql.append(" AND ").append(getColumnKeySql(filter.getKey(), columns)).append(" ");
			if ("=".equalsIgnoreCase(type)) {
				if (null == values) {
					throw new SimpleException(type + " filter has no filter value");
				}
				sql.append("= ?");
				sqlValues.add(values.get(0));
			} else if ("between".equalsIgnoreCase(type)) {
				if (null == values || values.size() < 2) {
					throw new SimpleException(type + " filter need two filter values");
				}
				sql.append("BETWEEN ? AND ?");
				sqlValues.add(values.get(0));
				sqlValues.add(values.get(1));
			} else if ("in".equalsIgnoreCase(type) || "not in".equalsIgnoreCase(type)) {
				if (null == values) {
					throw new SimpleException(type + " filter need filter values");
				}
				StringBuilder in = new StringBuilder();
				for (Object value : values) {
					in.append(",?");
					sqlValues.add(value);
				}
				sql.append(type.toUpperCase()).append("(").append(in.substring(1)).append(")");
			} else {
				sql.append(type.toUpperCase());
				if (null != values) {
					sql.append(" ?");
					sqlValues.add(values.get(0));
				}
			}
		}
		result[0] = sql.toString();
		result[1] = sqlValues.toArray();
		return result;
	}

}
