package org.think2framework.orm;

import org.think2framework.orm.bean.Entity;
import org.think2framework.orm.bean.Filter;
import org.think2framework.orm.bean.Order;
import org.think2framework.orm.core.ClassUtils;
import org.think2framework.orm.core.Database;
import org.think2framework.orm.core.SelectHelp;
import org.think2framework.orm.core.TypeUtils;
import org.think2framework.utils.JsonUtils;
import org.think2framework.utils.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by zhoubin on 16/2/29. 查询生成器
 */
public class Query {

	private static final Logger logger = LogManager.getLogger(Query.class);

	private List<Filter> filters; // 过滤条件

	private int page; // 分页第几页

	private int size; // 每页大小

	private List<String> groups; // 分组字段

	private List<Order> orders; // 排序

	private Entity entity; // 查询实体

	private JdbcTemplate jdbcTemplate; // spring JdbcTemplate

	private String columnSql; // 查询的字段sql

	private Redis redis; // redis处理工具

	private Integer valid = 0; // redis缓存的有效时间，单位秒，0位无期限

	private Database database; // 处理数据库

	public Query(Entity entity, Database database, Redis redis, Integer valid) {
		this.entity = entity;
		this.database = database;
		this.jdbcTemplate = database.getJdbcTemplate();
		this.redis = redis;
		this.valid = valid;
		// 添加默认过滤条件为实体的过滤条件，如果实体过滤条件为null则创建过滤条件对象
		this.filters = new ArrayList<>();
		if (null != entity.getFilters()) {
			this.filters.addAll(entity.getFilters());
		}
		// 添加默认排序
		this.orders = new ArrayList<>();
		if (null != entity.getOrders()) {
			orders.addAll(entity.getOrders());
		}
	}

	/**
	 * 获取主键名称
	 * 
	 * @return 主键
	 */
	public String getPk() {
		return entity.getPk();
	}

	/**
	 * 获取查询的字段sql，如果未设置则查询全部字段
	 *
	 * @return 查询字段sql
	 */
	private String getColumnSql() {
		return StringUtils.isBlank(columnSql) ? entity.getColumnSql() : columnSql;
	}

	/**
	 * 获取数量
	 *
	 * @param field
	 *            统计字段名
	 * @return 数量
	 */
	public int queryForCount(String field) {
		String key = field;
		if (!"*".equals(key)) {
			key = SelectHelp.getColumnKeySql(field, entity.getColumns(), false);
		}
		Object[] sqlValues = database.toSelect(entity.getTable(), entity.getJoinSql(), "COUNT(" + key + ")", filters,
				entity.getColumns(), groups, orders, page, size);
		Integer result = queryForObject((String) sqlValues[0], Integer.class, (Object[]) sqlValues[1]);
		if (null == result) {
			result = 0;
		}
		return result;
	}

	/**
	 * 获取数量
	 *
	 * @return 数量
	 */
	public int queryForCount() {
		return queryForCount("*");
	}

	/**
	 * 获取单体map数据
	 *
	 * @return map数据
	 */
	public Map<String, Object> queryForMap() {
		Object[] sqlValues = database.toSelect(entity.getTable(), entity.getJoinSql(), this.getColumnSql(), filters,
				entity.getColumns(), groups, orders, page, size);
		return queryForMap((String) sqlValues[0], (Object[]) sqlValues[1]);
	}

	/**
	 * 获取单体map数据，如果开启redis缓存则使用缓存机制
	 *
	 * @param key
	 *            redis关键字
	 *
	 * @return map数据
	 */
	public Map<String, Object> queryForMap(String key) {
		Map<String, Object> map = null;
		if (null != redis) {
			map = redis.getMap(key);
		}
		if (null == map) {
			map = queryForMap();
			if (null != map && !map.isEmpty() && null != redis) {
				redis.set(key, valid, map);
			}
		}
		return map;
	}

	/**
	 * 获取单体对象数据
	 *
	 * @param requiredType
	 *            数据对象类
	 * @return 数据对象
	 */
	public <T> T queryForObject(Class<T> requiredType) {
		Object[] sqlValues = database.toSelect(entity.getTable(), entity.getJoinSql(), this.getColumnSql(), filters,
				entity.getColumns(), groups, orders, page, size);
		return queryForObject((String) sqlValues[0], (Object[]) sqlValues[1], requiredType);
	}

	/**
	 * 获取单体对象数据，如果开启redis缓存则使用缓存机制
	 *
	 * @param key
	 *            redis关键字
	 *
	 * @param requiredType
	 *            数据对象类
	 * @return 数据对象
	 */
	public <T> T queryForObject(String key, Class<T> requiredType) {
		T object = null;
		if (null != redis) {
			object = redis.get(key, requiredType);
		}
		if (null == object) {
			object = queryForObject(requiredType);
			if (null != object && null != redis) {
				redis.set(key, valid, object);
			}
		}
		return object;
	}

	/**
	 * 获取数据对象数组
	 *
	 * @param elementType
	 *            数据对象类
	 * @return 数据数组
	 */
	public <T> List<T> queryForList(Class<T> elementType) {
		Object[] sqlValues = database.toSelect(entity.getTable(), entity.getJoinSql(), this.getColumnSql(), filters,
				entity.getColumns(), groups, orders, page, size);
		return queryForList((String) sqlValues[0], (Object[]) sqlValues[1], elementType);
	}

	/**
	 * 获取数据对象数组，如果开启redis缓存则使用缓存机制
	 *
	 * @param key
	 *            redis关键字
	 *
	 * @param elementType
	 *            数据对象类
	 * @return 数据数组
	 */
	public <T> List<T> queryForList(String key, Class<T> elementType) {
		List<T> list = null;
		if (null != redis) {
			list = redis.getList(key, elementType);
		}
		if (null == list) {
			list = queryForList(elementType);
			if (null != list && list.size() > 0 && null != redis) {
				redis.set(key, valid, list);
			}
		}
		return list;
	}

	/**
	 * 获取map数据数组
	 *
	 * @return map数组
	 */
	public List<Map<String, Object>> queryForList() {
		Object[] sqlValues = database.toSelect(entity.getTable(), entity.getJoinSql(), this.getColumnSql(), filters,
				entity.getColumns(), groups, orders, page, size);
		return queryForList((String) sqlValues[0], (Object[]) sqlValues[1]);
	}

	/**
	 * 获取数据，返回map数组的json字符串，如果有redis缓存则走缓存机制
	 * 
	 * @param key
	 *            redis关键字
	 * @return map数组的json字符串
	 */
	public String query(String key) {
		String data = null;
		if (null != redis) {
			data = redis.get(key);
		}
		if (null == data) {
			List<Map<String, Object>> list = queryForList();
			data = JsonUtils.toString(list);
			if (null != list && list.size() > 0 && null != redis) {
				redis.set(key, valid, list);
			}
		}
		return data;
	}

	/**
	 * 获取map数据数组，如果开启redis缓存则使用缓存机制
	 *
	 * @param key
	 *            redis关键字
	 * @return map数组
	 */
	public List<Map<String, Object>> queryForList(String key) {
		List<Map<String, Object>> list = null;
		if (null != redis) {
			list = redis.getMapList(key);
		}
		if (null == list) {
			list = queryForList();
			if (null != list && list.size() > 0 && null != redis) {
				redis.set(key, valid, list);
			}
		}
		return list;
	}

	/**
	 * 执行一个自定义查询,获取简单字段对象(String Integer等)
	 *
	 * @param sql
	 *            sql语句
	 * @param requiredType
	 *            类型
	 * @param args
	 *            参数
	 * @return 参数对象
	 */
	public <T> T queryForObject(String sql, Class<T> requiredType, Object... args) {
		logger.debug("queryForObject sql: {} values: {} requiredType: {}", sql, args, requiredType);
		try {
			return jdbcTemplate.queryForObject(sql, requiredType, args);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * 执行一个自定义查询,获取简单字段对象(String Integer等)，如果开启redis缓存则使用缓存机制
	 * 
	 * @param key
	 *            redis关键字
	 * @param sql
	 *            sql语句
	 * @param requiredType
	 *            类型
	 * @param args
	 *            参数
	 * @return 参数对象
	 */
	public <T> T queryForObject(String key, String sql, Class<T> requiredType, Object... args) {
		T object = null;
		if (null != redis) {
			object = redis.get(key, requiredType);
		}
		if (null == object) {
			object = queryForObject(sql, requiredType, args);
			if (null != object && null != redis) {
				redis.set(key, valid, object);
			}
		}
		return object;
	}

	/**
	 * 执行一个自定义sql,获取单体类对象
	 *
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数值
	 * @param requiredType
	 *            类
	 * @return 类对象
	 */
	public <T> T queryForObject(String sql, Object[] args, Class<T> requiredType) {
		logger.debug("queryForObject sql: {} values: {} requiredType: {}", sql, args, requiredType);
		try {
			return jdbcTemplate.query(sql, args, rs -> {
				rs.next();
				return ClassUtils.createInstance(requiredType, rs);
			});
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * 执行一个自定义sql,获取单体类对象，如果开启redis缓存则使用缓存机制
	 *
	 * @param key
	 *            redis关键字
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数值
	 * @param requiredType
	 *            类
	 * @return 类对象
	 */
	public <T> T queryForObject(String key, String sql, Object[] args, Class<T> requiredType) {
		T object = null;
		if (null != redis) {
			object = redis.get(key, requiredType);
		}
		if (null == object) {
			object = queryForObject(sql, args, requiredType);
			if (null != object && null != redis) {
				redis.set(key, valid, object);
			}
		}
		return object;
	}

	/**
	 * 执行一个自定义sql,获取map对象
	 *
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数值
	 * @return map
	 */
	public Map<String, Object> queryForMap(String sql, Object... args) {
		logger.debug("queryForMap sql: {} values: {}", sql, args);
		try {
			return jdbcTemplate.queryForMap(sql, args);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * 执行一个自定义sql,获取map对象，如果开启redis缓存则使用缓存机制
	 * 
	 * @param key
	 *            redis关键字
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数值
	 * @return map
	 */
	public Map<String, Object> queryForMap(String key, String sql, Object... args) {
		Map<String, Object> map = null;
		if (null != redis) {
			map = redis.getMap(key);
		}
		if (null == map) {
			map = queryForMap(sql, args);
			if (null != map && !map.isEmpty() && null != redis) {
				redis.set(key, valid, map);
			}
		}
		return map;
	}

	/**
	 * 执行一个自定义sql,获取对象数组
	 *
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数值
	 * @param elementType
	 *            类
	 * @return 类对象数组
	 */
	public <T> List<T> queryForList(String sql, Object[] args, Class<T> elementType) {
		logger.debug("queryForList Object sql: {} values: {} elementType: {}", sql, args, elementType);
		try {
			List<T> list = jdbcTemplate.query(sql, args, (rs, rowNum) -> {
				return ClassUtils.createInstance(elementType, rs);
			});
			return list;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * 执行一个自定义sql,获取对象数组，如果开启redis缓存则使用缓存机制
	 * 
	 * @param key
	 *            redis关键字
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数值
	 * @param elementType
	 *            类
	 * @return 类对象数组
	 */
	public <T> List<T> queryForList(String key, String sql, Object[] args, Class<T> elementType) {
		List<T> list = null;
		if (null != redis) {
			list = redis.getList(key, elementType);
		}
		if (null == list) {
			list = queryForList(sql, args, elementType);
			if (null != list && list.size() > 0 && null != redis) {
				redis.set(key, valid, list);
			}
		}
		return list;
	}

	/**
	 * 执行一个自定义sql,获取map数组
	 *
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数
	 * @return map数组
	 */
	public List<Map<String, Object>> queryForList(String sql, Object... args) {
		logger.debug("queryForList Map sql: {} values: {}", sql, args);
		try {
			return jdbcTemplate.queryForList(sql, args);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * 执行一个sql语句，获取map数组，如果开启redis缓存则使用缓存机制
	 * 
	 * @param key
	 *            redis缓存key
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数
	 * @return map数组
	 */
	public List<Map<String, Object>> queryForList(String key, String sql, Object... args) {
		List<Map<String, Object>> list = null;
		if (null != redis) {
			list = redis.getMapList(key);
		}
		if (null == list) {
			list = queryForList(sql, args);
			if (null != list && list.size() > 0 && null != redis) {
				redis.set(key, valid, list);
			}
		}
		return list;
	}

	/**
	 * 设置自定义查询字段，不进行相关校验，由使用者自定义任何形式的字段
	 *
	 * @param fields
	 *            自定义字段
	 */
	public void customFields(String fields) {
		this.columnSql = fields;
	}

	/**
	 * 批量设置要查询的字段，设置的时候清空之前设置的字段
	 *
	 * @param names
	 *            要查询的字段名称
	 */
	public void fields(String... names) {
		StringBuilder sql = new StringBuilder();
		for (String name : names) {
			sql.append(",").append(SelectHelp.getColumnKeySql(name, entity.getColumns(), true));
		}
		columnSql = StringUtils.substring(sql.toString(), 1);
	}

	/**
	 * 清空所有查询设置
	 */
	public void clear() {
		this.filters.clear();
		this.columnSql = "";
	}

	/**
	 * 添加分页
	 *
	 * @param page
	 *            第几页
	 * @param size
	 *            每页大小
	 */
	public void page(int page, int size) {
		this.page = page;
		this.size = size;
	}

	/**
	 * 添加group by条件，没有顺序，并且字段名不能重复,多次调用以最后一次为准
	 *
	 * @param keys
	 *            group by字段名称
	 */
	public void group(String... keys) {
		this.groups = Arrays.asList(keys);
	}

	/**
	 * 添加倒叙排序，按照添加先后顺序排序
	 *
	 * @param keys
	 *            order by字段名称
	 */
	public void desc(String... keys) {
		this.orders.add(new Order(Arrays.asList(keys), TypeUtils.ORDER_TYPE_DESC));
	}

	/**
	 * 添加正序排序，按照添加先后顺序排序
	 *
	 * @param keys
	 *            order by字段名称
	 */
	public void asc(String... keys) {
		this.orders.add(new Order(Arrays.asList(keys), TypeUtils.ORDER_TYPE_ASC));
	}

	/**
	 * 追加一个过滤条件
	 *
	 * @param key
	 *            过滤字段名称
	 * @param type
	 *            过滤类型
	 * @param values
	 *            过滤值
	 */
	public void filter(String key, String type, Object... values) {
		this.filters.add(new Filter(key, type, Arrays.asList(values)));
	}

	/**
	 * and主键等于条件
	 *
	 * @param value
	 *            过滤值
	 */
	public void eq(Object value) {
		this.filters.add(new Filter(entity.getPk(), "=", Arrays.asList(value)));
	}

	/**
	 * and等于过滤条件
	 *
	 * @param key
	 *            过滤字段名
	 * @param value
	 *            过滤值
	 */
	public void eq(String key, Object value) {
		this.filters.add(new Filter(key, "=", Arrays.asList(value)));
	}

	/**
	 * and不等于过滤条件
	 *
	 * @param key
	 *            过滤字段名
	 * @param value
	 *            过滤值
	 */
	public void ne(String key, Object value) {
		this.filters.add(new Filter(key, "<>", Arrays.asList(value)));
	}

	/**
	 * and大于过滤条件
	 *
	 * @param key
	 *            过滤字段名
	 * @param value
	 *            过滤值
	 */
	public void gt(String key, Object value) {
		this.filters.add(new Filter(key, ">", Arrays.asList(value)));
	}

	/**
	 * and大于等于过滤条件
	 *
	 * @param key
	 *            过滤字段名
	 * @param value
	 *            过滤值
	 */
	public void ge(String key, Object value) {
		this.filters.add(new Filter(key, ">=", Arrays.asList(value)));
	}

	/**
	 * and小于过滤条件
	 *
	 * @param key
	 *            过滤字段名
	 * @param value
	 *            过滤值
	 */
	public void lt(String key, Object value) {
		this.filters.add(new Filter(key, "<", Arrays.asList(value)));
	}

	/**
	 * and小于等于过滤条件
	 *
	 * @param key
	 *            过滤字段名
	 * @param value
	 *            过滤值
	 */
	public void le(String key, Object value) {
		this.filters.add(new Filter(key, "<=", Arrays.asList(value)));
	}

	/**
	 * and null过滤条件
	 *
	 * @param key
	 *            过滤字段名
	 */
	public void isNull(String key) {
		this.filters.add(new Filter(key, "is null", null));
	}

	/**
	 * and not null过滤条件
	 *
	 * @param key
	 *            过滤字段名
	 */
	public void notNull(String key) {
		this.filters.add(new Filter(key, "is not null", null));
	}

	/**
	 * and in过滤条件
	 *
	 * @param key
	 *            过滤字段名
	 * @param values
	 *            过滤值
	 */
	public void in(String key, Object... values) {
		this.filters.add(new Filter(key, "in", Arrays.asList(values)));
	}

	/**
	 * and not in过滤条件
	 *
	 * @param key
	 *            过滤字段名
	 * @param values
	 *            过滤值
	 */
	public void notIn(String key, Object... values) {
		this.filters.add(new Filter(key, "not in", Arrays.asList(values)));
	}

	/**
	 * and between过滤条件
	 *
	 * @param key
	 *            过滤字段名
	 * @param begin
	 *            开始值
	 * @param end
	 *            结束值
	 */
	public void between(String key, Object begin, Object end) {
		this.filters.add(new Filter(key, "between", Arrays.asList(begin, end)));
	}

	/**
	 * and like过滤条件，过滤值两边都加上%过滤
	 *
	 * @param key
	 *            过滤字段名
	 * @param value
	 *            过滤值
	 */
	public void like(String key, Object value) {
		this.filters.add(new Filter(key, "like", Arrays.asList(value)));
	}

	/**
	 * and not like过滤条件，过滤值两边都加上%过滤
	 *
	 * @param key
	 *            过滤字段名
	 * @param value
	 *            过滤值
	 */
	public void notLike(String key, Object value) {
		this.filters.add(new Filter(key, "not like", Arrays.asList(value)));
	}

	/**
	 * and like过滤条件，左匹配
	 *
	 * @param key
	 *            过滤字段名
	 * @param value
	 *            过滤值
	 */
	public void leftLike(String key, Object value) {
		this.filters.add(new Filter(key, "like", Arrays.asList("%" + value)));
	}

	/**
	 * and like过滤条件，右匹配
	 *
	 * @param key
	 *            过滤字段名
	 * @param value
	 *            过滤值
	 */
	public void rightLike(String key, Object value) {
		this.filters.add(new Filter(key, "like", Arrays.asList(value + "%")));
	}

}