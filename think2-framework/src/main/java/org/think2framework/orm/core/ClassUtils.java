package org.think2framework.orm.core;

import org.think2framework.exception.NonExistException;
import org.think2framework.exception.SimpleException;
import org.think2framework.exception.UndefinedException;
import org.think2framework.orm.bean.*;
import org.think2framework.orm.persistence.Column;
import org.think2framework.orm.persistence.JoinTable;
import org.think2framework.utils.JsonUtils;
import org.think2framework.utils.StringUtils;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by zhoubin on 16/6/2. 类工具,主要处理类字段,读取和写入,以及创建实例
 */
public class ClassUtils {

	public static final String TYPE_STRING = "string"; // string类型

	public static final String TYPE_INTEGER = "integer"; // integer类型

	public static final String TYPE_LONG = "long"; // long类型

	public static final String TYPE_BOOLEAN = "boolean"; // boolean类型

	public static final String TYPE_DOUBLE = "double"; // double类型

	public static final String TYPE_FLOAT = "float"; // float类型

	public static final String TYPE_TEXT = "text"; // text类型

	public static final String TYPE_LONGTEXT = "longtext"; // longtext类型

	public static final String TYPE_JSON = "json"; // json类型

	public static final String ORDER_TYPE_ASC = "ASC"; // 排序类型 asc

	public static final String ORDER_TYPE_DESC = "DESC"; // 排序类型 DESC

	public static final Integer TRUE_VALUE = 1; // bool类型的true值

	public static final Integer FALSE_VALUE = 0; // bool类型的false值

	/**
	 * 获取字段的名称,如果定义了别名以别名为准,如果定义了名称以名称为准,如果都没有以字段本身名称为准
	 * 
	 * @param field
	 *            字段
	 * @return 字段的名称
	 */
	private static String getFieldKey(Field field) {
		String key = field.getName();
		Column column = field.getAnnotation(Column.class);
		if (null != column) {
			if (StringUtils.isNotBlank(column.name())) {
				key = column.name();
			}
			if (StringUtils.isNotBlank(column.alias())) {
				key = column.alias();
			}
		}
		return key;
	}

	/**
	 * 获取一个类的字段，如果存在父类则循环获取父类的字段
	 * 
	 * @param clazz
	 *            类
	 * @return 字段数组
	 */
	public static List<Field> getFields(Class<?> clazz) {
		List<Field> fields = new ArrayList<>();
		Collections.addAll(fields, clazz.getDeclaredFields());
		Class<?> c = clazz.getSuperclass();
		while (Object.class != c) {
			Collections.addAll(fields, c.getDeclaredFields());
			c = c.getSuperclass();
		}
		return fields;
	}

	/**
	 * 从实例获取一个字段，如果不是object对象则从父类获取字段
	 * 
	 * @param clazz
	 *            类
	 * @param name
	 *            字段名称
	 * @return 字段
	 */
	private static Field getField(Class<?> clazz, String name) {
		try {
			return clazz.getDeclaredField(name);
		} catch (NoSuchFieldException e) {
			if (Object.class == clazz) {
				throw new NonExistException(clazz.getName() + " field " + name);
			} else {
				return getField(clazz.getSuperclass(), name);
			}
		}
	}

	/**
	 * 获取实例的一个字段值,如果是map类型使用get,如果是其他则使用反射获取字段值
	 * 
	 * @param instance
	 *            实例
	 * @param name
	 *            字段名称
	 * @return 字段值
	 */
	public static Object getFieldValue(Object instance, String name) {
		Class clazz = instance.getClass();
		Object value;
		if (HashMap.class == clazz || LinkedHashMap.class == clazz || LinkedCaseInsensitiveMap.class == clazz) {
			Map map = (Map) instance;
			value = map.get(name);
		} else {
			Field field = getField(instance.getClass(), name);
			try {
				field.setAccessible(true);
				value = field.get(instance);
			} catch (IllegalAccessException e) {
				throw new SimpleException(e);
			}
		}
		return getDatabaseValue(value);
	}

	/**
	 * 从数据库获取的值转化为java的值,bool类型0，1转为bool值，json数据和复杂类型从json字符串转为对象
	 * 
	 * @param value
	 *            数据库值
	 * @return java值
	 */
	public static Object getJavaValue(Field field, Object value) {
		if (null == value || null == field) {
			return null;
		}
		if (field.getType() == Boolean.class) {
			return TRUE_VALUE.toString().equalsIgnoreCase(StringUtils.toString(value));
		} else if (field.getType().isArray() || (field.getGenericType() instanceof ParameterizedType)) {
			return JsonUtils.readString(StringUtils.toString(value), field.getGenericType());
		} else {
			return value;
		}
	}

	/**
	 * 将一个java值转化为数据库存储的值，如果是bool则转化为整型，如果是数组和其他复杂类型转化成json字符串
	 * 
	 * @param value
	 *            java值
	 * @return 数据库值
	 */
	public static Object getDatabaseValue(Object value) {
		if (null == value) {
			return null;
		}
		if (Boolean.class == value.getClass()) {
			return Boolean.parseBoolean(StringUtils.toString(value)) ? TRUE_VALUE : FALSE_VALUE;
		} else if (ArrayList.class == value.getClass() || value instanceof Object[]
				|| value instanceof ParameterizedType) {
			return JsonUtils.toString(value);
		} else {
			return value;
		}
	}

	/**
	 * 根据sql的resultSet和类创建一个类的实例
	 * 
	 * @param clazz
	 *            类
	 * @param rs
	 *            sql结果集
	 * @return 类实例
	 */
	public static <T> T createInstance(Class<T> clazz, ResultSet rs) {
		try {
			T instance = clazz.newInstance();
			List<Field> fields = getFields(clazz);
			for (Field field : fields) {
				String name = getFieldKey(field);
				try {
					field.setAccessible(true);
					field.set(instance, getJavaValue(field, rs.getObject(name)));
				} catch (SQLException e) {
					if (e.getSQLState().equals("S1000")) { // 表示ResultSet是空的,没有数据
						return null;
					} else if (!e.getSQLState().equals("S0022")) { // S0022表示字段name不存在
						throw new SimpleException(e);
					}
				}
			}
			return instance;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new SimpleException(e);
		}
	}

	private static org.think2framework.orm.persistence.Table getTableAnnotation(Class<?> clazz) {
		org.think2framework.orm.persistence.Table table = clazz
				.getAnnotation(org.think2framework.orm.persistence.Table.class);
		if (null == table) {
			throw new UndefinedException(
					"Class " + clazz.getName() + " " + org.think2framework.orm.persistence.Table.class.getName());
		}
		return table;
	}

	/**
	 * 根据类创建一个table
	 * 
	 * @param clazz
	 *            类
	 * @return table
	 */
	public static Table createTable(Class<?> clazz) {
		org.think2framework.orm.persistence.Table table = getTableAnnotation(clazz);
		List<Field> fields = getFields(clazz);
		List<TableColumn> columns = new ArrayList<>();
		if (null != fields) {
			for (Field field : fields) {
				Column columnAnnotation = field.getAnnotation(Column.class);
				if (null != columnAnnotation) {
					// 如果字段没有关联名称或者为主表别名则表示为表的字段,如果有关联名称表示关联表,不设置到表
					if (StringUtils.isBlank(columnAnnotation.join())
							|| columnAnnotation.join().equals(SelectHelp.TABLE_ALIAS)) {
						TableColumn tableColumn = new TableColumn(
								StringUtils.isBlank(columnAnnotation.name()) ? field.getName()
										: columnAnnotation.name(),
								columnAnnotation.type(), columnAnnotation.nullable(), columnAnnotation.length(),
								columnAnnotation.scale(), columnAnnotation.defaultValue(), columnAnnotation.comment());
						columns.add(tableColumn);
					}
				}
			}
		}
		return new Table(table.name(), table.pk(), table.autoIncrement(), table.uniques(), table.indexes(),
				table.comment(), columns);
	}

	/**
	 * 根据类创建一个查询实体
	 * 
	 * @param clazz
	 *            类
	 * @return 查询实体
	 */
	public static Entity createEntity(Class<?> clazz) {
		org.think2framework.orm.persistence.Table table = getTableAnnotation(clazz);
		Entity entity = new Entity();
		entity.setTable(table.name());
		entity.setPk(table.pk());
		// 获取类定义的过滤条件
		org.think2framework.orm.persistence.Filter[] filterAnnotations = clazz
				.getAnnotationsByType(org.think2framework.orm.persistence.Filter.class);
		if (null != filterAnnotations) {
			List<Filter> filters = new ArrayList<>();
			for (org.think2framework.orm.persistence.Filter filterAnnotation : filterAnnotations) {
				filters.add(new Filter(filterAnnotation.key(), filterAnnotation.type(),
						Arrays.asList(filterAnnotation.values())));
			}
			entity.setFilters(filters);
		}
		// 获取定义的排序
		org.think2framework.orm.persistence.Order[] orderAnnotations = clazz
				.getAnnotationsByType(org.think2framework.orm.persistence.Order.class);
		if (null != orderAnnotations) {
			List<Order> orders = new ArrayList<>();
			for (org.think2framework.orm.persistence.Order orderAnnotation : orderAnnotations) {
				orders.add(new Order(Arrays.asList(orderAnnotation.keys()), orderAnnotation.type()));
			}
			entity.setOrders(orders);
		}
		// 获取关联表
		JoinTable[] joinTables = clazz.getDeclaredAnnotationsByType(JoinTable.class);
		if (null != joinTables) {
			List<Join> joins = new ArrayList<>();
			for (JoinTable joinTable : joinTables) {
				if (joinTable.name().equals(SelectHelp.TABLE_ALIAS)) {
					throw new SimpleException(SelectHelp.TABLE_ALIAS
							+ " is retained as the main table alias, can not be used as a join name");
				}
				joins.add(new Join(joinTable.name(), joinTable.database(), joinTable.table(), joinTable.type(),
						joinTable.key(), joinTable.joinName(), joinTable.joinKey(), joinTable.filter()));
			}
			entity.setJoinSql(SelectHelp.generateJoins(joins));
		} else {
			entity.setJoinSql("");
		}
		List<Field> fields = getFields(clazz);
		Map<String, EntityColumn> columns = new LinkedHashMap<>();
		if (null != fields) {
			for (Field field : fields) {
				Column columnAnnotation = field.getAnnotation(Column.class);
				columns.put(
						StringUtils.isBlank(columnAnnotation.alias()) ? columnAnnotation.name()
								: columnAnnotation.alias(),
						new EntityColumn(columnAnnotation.name(), columnAnnotation.join(), columnAnnotation.alias()));
			}
			entity.setColumns(columns);
			entity.setColumnSql(SelectHelp.generateColumns(columns));
		} else {
			entity.setColumnSql("");
		}
		return entity;
	}
}
