package org.think2framework.orm;

import org.think2framework.exception.SimpleException;
import org.think2framework.orm.bean.Table;
import org.think2framework.orm.bean.TableColumn;
import org.think2framework.orm.core.ClassUtils;
import org.think2framework.utils.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 写入生成器
 */
public class Writer {

	private static final Logger logger = LogManager.getLogger(Writer.class); // log4j日志对象

	/**
	 * 表定义
	 */
	private Table table;

	/**
	 * spring JdbcTemplate
	 */
	private JdbcTemplate jdbcTemplate;

	/**
	 * 表创建sql语句
	 */
	private String createSql;

	/**
	 * 数据库字段的符号,使用keySign把数据库关键字包起来,防止字段为关键字,关键字前符号
	 */
	private String keySignBegin;

	/**
	 * 数据库字段的符号,使用keySign把数据库关键字包起来,防止字段为关键字,关键字后符号
	 */
	private String keySignEnd;

	public Writer(Table table, String keySignBegin, String keySignEnd, String createSql, JdbcTemplate jdbcTemplate) {
		this.table = table;
		this.keySignBegin = keySignBegin;
		this.keySignEnd = keySignEnd;
		this.createSql = createSql;
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * 如果模型对应的表不存在则创建,已经存在则不处理,如果表有变动则返回true,如果没有变动则返回false
	 *
	 * @return 是否创建了表
	 */
	public boolean createTable() {
		Boolean result = false;
		try {
			this.jdbcTemplate
					.queryForList("SELECT 1 FROM " + keySignBegin + this.table.getName() + keySignEnd + " WHERE 1=2");
		} catch (Exception e) {
			if (null != e.getCause()) {
				String msg = e.getCause().getMessage();
				if (msg.contains("no such table") || (msg.contains("Table") && msg.contains("doesn't exist"))) {
					logger.debug("Create table sql:" + createSql);
					this.jdbcTemplate.execute(createSql);
					result = true;
				} else {
					throw new SimpleException(e);
				}
			} else {
				throw new SimpleException(e);
			}
		}
		return result;
	}

	/***
	 * 新增数据，返回新增数据的数据id，如果是map类型则转换为map获取,其他根据字段定义获取
	 *
	 * @param instance
	 *            待新增的数据
	 * @return 返回新增数据的id
	 */
	public Object insert(Object instance) {
		Object[] sqlValues = this.toInsert(instance);
		Object uId = sqlValues[2];
		Object id = insert(sqlValues[0].toString(), null == uId, (Object[]) sqlValues[1]);
		if (null != uId) {
			return uId;
		} else {
			return id;
		}
	}

	/***
	 * 修改数据，以主键为修改条件,数据主键必须有值，如果是map类型则转换为map获取，其他根据字段定义获取，不修改数据值为null的列
	 *
	 * @param instance
	 *            待修改的数据
	 * @return 返回影响的数据行数
	 */
	public int update(Object instance) {
		return update(instance, "");
	}

	/**
	 * 修改数据，数据key字段必须有值，如果是map类型则转换为map获取，其他根据字段定义获取，不修改数据值为null的列
	 *
	 * @param instance
	 *            待修改的数据
	 * @param keys
	 *            过滤字段
	 * @return 返回影响的数据行数
	 */
	public int update(Object instance, String... keys) {
		Object[] sqlValues = this.toUpdate(instance, keys);
		return update((String) sqlValues[0], (Object[]) sqlValues[1]);
	}

	/**
	 * 保存数据,如果数据不存在则新增,存在则修改,判断依据主键
	 *
	 * @param instance
	 *            待保存的数据
	 * @return 新增数据返回id, 修改数据返回-1
	 */
	public Object save(Object instance) {
		return save(instance, "");
	}

	/**
	 * 保存数据,如果数据不存在则新增,存在则修改,判断依据key字段的值
	 *
	 * @param instance
	 *            待保存的数据
	 * @param keys
	 *            过滤字段的名称
	 * @return 新增数据返回id, 修改数据返回-1
	 */
	public Object save(Object instance, String... keys) {
		if (update(instance, keys) == 1) {
			return -1;
		} else {
			return insert(instance);
		}
	}

	/**
	 * 批量保存数据,如果数据不存在则新增,存在则修改,判断依据主键
	 *
	 * @param list
	 *            保存数据数组
	 * @return 保存结果, 新增数据返回id, 修改数据返回空
	 */
	public <T> String[] batchSave(List<T> list) {
		return batchSave(list, "");
	}

	/**
	 * 批量保存数据,如果数据不存在则新增,存在则修改,判断依据key字段值
	 *
	 * @param list
	 *            保存数据数组
	 * @param keys
	 *            关键字段
	 * @return 保存结果, 新增数据返回id, 修改数据返-1
	 */
	public <T> String[] batchSave(List<T> list, String... keys) {
		String[] result = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			result[i] = save(list.get(i), keys).toString();
		}
		return result;
	}

	/**
	 * 批量新增,以第一条数据生成新增sql语句,所有批量数据必须有相同的结构
	 *
	 * @param list
	 *            批量新增数据
	 */
	public <T> int[] batchInsert(List<T> list) {
		String sql = null;
		List<Object[]> batchArgs = new ArrayList<>();
		for (Object object : list) {
			Object[] sqlValues = this.toInsert(object);
			if (StringUtils.isBlank(sql)) {
				sql = (String) sqlValues[0];
			}
			batchArgs.add((Object[]) sqlValues[1]);
		}
		return batchUpdate(sql, batchArgs);
	}

	/**
	 * 批量修改,以第一条数据生成修改sql语句,所有批量数据必须有相同的结构
	 *
	 * @param list
	 *            批量修改数据
	 */
	public <T> int[] batchUpdate(List<T> list) {
		String sql = null;
		List<Object[]> batchArgs = new ArrayList<>();
		for (Object object : list) {
			Object[] sqlValues = this.toUpdate(object);
			if (StringUtils.isBlank(sql)) {
				sql = (String) sqlValues[0];
			}
			batchArgs.add((Object[]) sqlValues[1]);
		}
		return batchUpdate(sql, batchArgs);
	}

	/**
	 * 根据主键值删除数据
	 *
	 * @param id
	 *            要删除数据的id
	 * @return 返回影响的行数
	 */
	public int deleteById(Object id) {
		return delete(Arrays.asList(id));
	}

	/**
	 * 根据字段值删除数据
	 *
	 * @param key
	 *            字段名称
	 * @param value
	 *            字段值
	 * @return 返回影响的行数
	 */
	public int delete(String key, Object value) {
		return delete(Arrays.asList(value), key);
	}

	/**
	 * 删除数据
	 *
	 * @param values
	 *            过滤数据对应的条件字段值
	 * @param keys
	 *            过滤条件字段列表
	 * @return 影响行数
	 */
	public int delete(List<Object> values, String... keys) {
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ").append(this.keySignBegin).append(this.table.getName()).append(this.keySignEnd)
				.append(" WHERE 1=1");
		if (null == keys || keys.length == 0) {
			sql.append(" AND ").append(this.keySignBegin).append(this.table.getPk()).append(this.keySignEnd)
					.append(" =?");
		} else {
			for (String key : keys) {
				sql.append(" AND ").append(this.keySignBegin).append(key).append(this.keySignEnd).append(" =?");
			}
		}
		logger.debug("delete sql: {} values: {}", sql.toString(), values);
		return this.jdbcTemplate.update(sql.toString(), values.toArray());
	}

	/**
	 * 执行一个自增长的sql,主键自增长
	 *
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数值
	 * @return 自增长主键值
	 */
	public Object insert(String sql, Object... args) {
		return insert(sql, true, args);
	}

	/**
	 * 执行一个自定义新增方法,如果自增长则返回自增长的主键值,如果不是自增长则返回空
	 *
	 * @param sql
	 *            sql语句
	 * @param autoIncrement
	 *            主键是否自增长
	 * @param args
	 *            参数值
	 * @return 自增长则返回主键值, 否则返回空
	 */
	public Object insert(String sql, Boolean autoIncrement, Object... args) {
		logger.debug("Insert sql: {} values: {} autoIncrement: {}", sql, args, autoIncrement);
		if (autoIncrement) {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			this.jdbcTemplate.update(con -> {
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				for (int i = 0; i < args.length; i++) {
					ps.setObject(i + 1, ClassUtils.getDatabaseValue(args[i]));
				}
				return ps;
			}, keyHolder);
			return keyHolder.getKey().intValue();
		} else {
			this.jdbcTemplate.update(sql, args);
			return "";
		}
	}

	/**
	 * 执行一个自定义sql，不带参数
	 *
	 * @param sql
	 *            sql语句
	 * @return 返回影响的行数数组
	 */
	public int updateSql(String sql) {
		logger.debug("Update sql: ", sql);
		return this.jdbcTemplate.update(sql);
	}

	/**
	 * 执行一个自定义写入sql
	 *
	 * @param sql
	 *            sql语句
	 */
	public int update(String sql, Object... args) {
		logger.debug("Update sql: {} values: {}", sql, args);
		return this.jdbcTemplate.update(sql, args);
	}

	/**
	 * 执行一个自定义批量写入sql
	 *
	 * @param sql
	 *            sql语句
	 * @param batchArgs
	 *            参数值
	 * @return 返回影响的行数数组
	 */
	public int[] batchUpdate(String sql, List<Object[]> batchArgs) {
		logger.debug("batchUpdate sql: {} values: {}", sql, batchArgs);
		return this.jdbcTemplate.batchUpdate(sql, batchArgs);
	}

	/**
	 * 根据对象生成新增的sql和值,如果对象是map则map方式取值,如果不是则反射取值
	 *
	 * @param instance
	 *            新增对象
	 * @return 3个值得数组, 第一个新增sql语句, 第二个是新增的值的Object[], 如果主键不是自增长的, 第三个值为新生成的主键id,
	 *         如果自增长则第三个值为空
	 */
	private Object[] toInsert(Object instance) {
		Object[] result = new Object[3];
		StringBuilder fieldSql = new StringBuilder();
		StringBuilder valueSql = new StringBuilder();
		List<Object> values = new ArrayList<>();
		String pk = table.getPk();
		// 如果不是自增长的则添加一个uuid
		if (!this.table.getAutoIncrement()) {
			fieldSql.append(",").append(this.keySignBegin).append(pk).append(this.keySignEnd);
			valueSql.append(",?");
			String id = UUID.randomUUID().toString();
			values.add(id);
			result[2] = id;
		}
		for (TableColumn column : this.table.getColumns()) {
			// 如果字段不是主键,则添加
			String key = column.getName();
			if (!key.equals(pk)) {
				Object value = ClassUtils.getFieldValue(instance, key);
				// 不新增null值
				if (null == value) {
					continue;
				}
				fieldSql.append(",").append(this.keySignBegin).append(key).append(this.keySignEnd);
				valueSql.append(",?");
				values.add(value);
			}
		}
		result[0] = String.format("INSERT INTO %s%s%s(%s) VALUES (%s)", this.keySignBegin, this.table.getName(),
				this.keySignEnd, fieldSql.toString().substring(1), valueSql.toString().substring(1));
		result[1] = values.toArray();
		return result;
	}

	/**
	 * 根据对象生成修改的sql和值,如果对象是map则map方式取值,如果不是则反射取值
	 *
	 * @param instance
	 *            修改对象
	 * @param keys
	 *            修改关键字段名称
	 * @return 2个值得数组, 第一个修改sql语句, 第二个是新增的值的Object[]
	 */
	private Object[] toUpdate(Object instance, String... keys) {
		Object[] result = new Object[2];
		StringBuilder fieldSql = new StringBuilder();
		List<Object> values = new ArrayList<>();
		String pk = table.getPk();
		for (TableColumn column : table.getColumns()) {
			String key = column.getName();
			// 如果字段不是主键,则修改
			if (!key.equals(pk)) {
				Object value = ClassUtils.getFieldValue(instance, key);
				// 不修改null的字段
				if (null == value) {
					continue;
				}
				fieldSql.append(",").append(this.keySignBegin).append(key).append(this.keySignEnd).append("=?");
				values.add(value);
			}
		}
		StringBuilder keySql = new StringBuilder();
		for (String key : keys) {
			if (StringUtils.isBlank(key)) {
				key = pk;
			}
			keySql.append(" AND ").append(this.keySignBegin).append(key).append(this.keySignEnd).append("=?");
			values.add(ClassUtils.getFieldValue(instance, key));
		}
		result[0] = String.format("UPDATE %s%s%s SET %s WHERE 1=1%s", this.keySignBegin, this.table.getName(),
				this.keySignEnd, fieldSql.substring(1), keySql.toString());
		result[1] = values.toArray();
		return result;
	}

}
