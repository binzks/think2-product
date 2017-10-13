package org.think2framework.support;

import com.fasterxml.jackson.core.type.TypeReference;
import org.think2framework.bean.Model;
import org.think2framework.exception.SimpleException;
import org.think2framework.orm.OrmFactory;
import org.think2framework.orm.bean.*;
import org.think2framework.orm.core.ClassUtils;
import org.think2framework.orm.core.SelectHelp;
import org.think2framework.orm.persistence.Column;
import org.think2framework.orm.persistence.JoinTable;
import org.think2framework.orm.persistence.Table;
import org.think2framework.utils.FileUtils;
import org.think2framework.utils.JsonUtils;
import org.think2framework.utils.PackageUtils;
import org.think2framework.utils.StringUtils;
import org.think2framework.view.core.TagUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 数据持久化
 */
public class OrmSupport {

	/**
	 * 追加一列到模型，如果关联为空或者主表则为主表字段记入表列，否则不记录 查询实体如果存在别名则以别名来区分
	 * 
	 * @param join
	 *            字段关联
	 * @param name
	 *            字段实际名称
	 * @param type
	 *            字段类型
	 * @param nullable
	 *            是否可空
	 * @param length
	 *            字段长度
	 * @param scale
	 *            字段长度小数位数
	 * @param defaultValue
	 *            默认值
	 * @param comment
	 *            注释
	 * @param alias
	 *            字段别名
	 * @param tableColumns
	 *            表的列
	 * @param entityColumns
	 *            表的查询实体列
	 */
	private static void appendColumn(String join, String name, String type, Boolean nullable, Integer length,
			Integer scale, String defaultValue, String comment, String alias, List<TableColumn> tableColumns,
			Map<String, EntityColumn> entityColumns) {
		// 如果字段没有关联名称或者为主表别名则表示为表的字段,如果有关联名称表示关联表,不设置到表
		if (StringUtils.isBlank(join) || SelectHelp.TABLE_ALIAS.equals(join)) {
			tableColumns.add(new TableColumn(name, type, nullable, length, scale, defaultValue, comment));
		}
		// 在查询实体中如果有别名则以别名来实际定义列
		entityColumns.put(StringUtils.isBlank(alias) ? name : alias, new EntityColumn(name, join, alias));
	}

	/**
	 * 扫描一个包，加载所有定义的模型
	 * 
	 * @param packageDirNames
	 *            包名
	 * @return 追加的模型的名称列表
	 */
	public static synchronized List<String> scanPackages(String... packageDirNames) {
		List<String> names = new ArrayList<>();
		for (String name : packageDirNames) {
			List<Class> list = PackageUtils.scanPackage(name);
			for (Class<?> clazz : list) {
				String n = appendClass(clazz);
				if (!"".equals(n)) {
					names.add(n);
				}
			}
		}
		return names;
	}

	/**
	 * 根据一个类定义添加数据持久化的表和实体，如果定义了table和列则追加，否则认为不是一个持久化模型
	 * 
	 * @param clazz
	 *            类
	 * @return 追加的模型的名称，如果没有追加则返回空
	 */
	public static synchronized String appendClass(Class<?> clazz) {
		List<Field> fields = ClassUtils.getFields(clazz);
		List<TableColumn> tableColumns = new ArrayList<>();
		Map<String, EntityColumn> entityColumns = new LinkedHashMap<>();
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			if (null == column) {
				continue;
			}
			// 列名如果没有定义则用字段名称
			String name = StringUtils.isBlank(column.name()) ? field.getName() : column.name();
			// 添加列
			appendColumn(column.join(), name, column.type(), column.nullable(), column.length(), column.scale(),
					column.defaultValue(), column.comment(), column.alias(), tableColumns, entityColumns);
		}
		Table table = clazz.getAnnotation(Table.class);
		if (null != table && tableColumns.size() > 0) {
			OrmFactory.appendTable(clazz.getName(), new org.think2framework.orm.bean.Table(table.name(), table.pk(),
					table.autoIncrement(), table.uniques(), table.indexes(), table.comment(), tableColumns));
			List<Filter> filters = new ArrayList<>();
			// 获取类定义的过滤条件
			org.think2framework.orm.persistence.Filter[] filterAnnotations = clazz
					.getAnnotationsByType(org.think2framework.orm.persistence.Filter.class);
			if (null != filterAnnotations) {
				for (org.think2framework.orm.persistence.Filter filterAnnotation : filterAnnotations) {
					filters.add(new Filter(filterAnnotation.key(), filterAnnotation.type(),
							Arrays.asList(filterAnnotation.values())));
				}
			}
			// 获取定义的排序
			List<Order> orders = new ArrayList<>();
			org.think2framework.orm.persistence.Order[] orderAnnotations = clazz
					.getAnnotationsByType(org.think2framework.orm.persistence.Order.class);
			if (null != orderAnnotations) {
				for (org.think2framework.orm.persistence.Order orderAnnotation : orderAnnotations) {
					orders.add(new Order(Arrays.asList(orderAnnotation.keys()), orderAnnotation.type()));
				}
			}
			// 获取关联表
			List<Join> joins = new ArrayList<>();
			JoinTable[] joinTables = clazz.getDeclaredAnnotationsByType(JoinTable.class);
			if (null != joinTables) {
				for (JoinTable joinTable : joinTables) {
					if (joinTable.name().equals(SelectHelp.TABLE_ALIAS)) {
						throw new SimpleException(SelectHelp.TABLE_ALIAS
								+ " is retained as the main table alias, can not be used as a join name");
					}
					joins.add(new Join(joinTable.name(), joinTable.database(), joinTable.table(), joinTable.type(),
							joinTable.key(), joinTable.joinName(), joinTable.joinKey(), joinTable.filter()));
				}
			}
			OrmFactory.appendEntity(clazz.getName(), new Entity(table.name(), table.pk(), entityColumns,
					SelectHelp.generateJoins(joins), SelectHelp.generateColumns(entityColumns), filters, orders));
			return clazz.getName();
		} else {
			return "";
		}
	}

	/**
	 * 根据模型的标签获取数据库类型
	 *
	 * @param value
	 *            标签
	 * @return 数据库类型
	 */
	private static String getTypeFromTag(String value) {
		if (TagUtils.TAG_TEXT.equalsIgnoreCase(value)) {
			return ClassUtils.TYPE_STRING;
		} else if (TagUtils.TAG_DATE.equalsIgnoreCase(value) || TagUtils.TAG_DATETIME.equalsIgnoreCase(value)
				|| TagUtils.TAG_INT.equalsIgnoreCase(value) || TagUtils.TAG_TIMESTAMP.equalsIgnoreCase(value)
				|| TagUtils.TAG_ITEM_INT.equalsIgnoreCase(value)) {
			return ClassUtils.TYPE_INTEGER;
		} else if (TagUtils.TAG_HTML.equalsIgnoreCase(value)) {
			return ClassUtils.TYPE_LONGTEXT;
		} else if (TagUtils.TAG_FLOAT.equalsIgnoreCase(value)) {
			return ClassUtils.TYPE_FLOAT;
		} else if (TagUtils.TAG_BOOL.equalsIgnoreCase(value)) {
			return ClassUtils.TYPE_BOOLEAN;
		} else if (TagUtils.TAG_JSON.equalsIgnoreCase(value)) {
			return ClassUtils.TYPE_JSON;
		} else {
			return ClassUtils.TYPE_STRING;
		}
	}

	/**
	 * 根据标签获取对应字段数据库长度，bool长度1,时间、手机、电话长度15，文件、图片、长文本类型长度为5000，item长度为2，其他为配置长度
	 *
	 * @param tag
	 *            标签
	 * @param length
	 *            配置长度
	 * @return 实际字段长度
	 */
	private static Integer getLengthFromTag(String tag, Integer length) {
		if (TagUtils.TAG_BOOL.equalsIgnoreCase(tag)) {
			return 1;
		} else if (TagUtils.TAG_DATE.equalsIgnoreCase(tag) || TagUtils.TAG_DATETIME.equalsIgnoreCase(tag)
				|| TagUtils.TAG_TIMESTAMP.equalsIgnoreCase(tag) || TagUtils.TAG_MOBILE.equalsIgnoreCase(tag)
				|| TagUtils.TAG_TELEPHONE.equalsIgnoreCase(tag)) {
			return 15;
		} else if (TagUtils.TAG_TEXTAREA.equalsIgnoreCase(tag) || TagUtils.TAG_FILE.equalsIgnoreCase(tag)
				|| TagUtils.TAG_IMAGE.equalsIgnoreCase(tag)) {
			return 500;
		} else if (TagUtils.TAG_ITEM_INT.equalsIgnoreCase(tag)) {
			return 2;
		} else {
			return length;
		}
	}

	/**
	 * 追加cms通用字段
	 */
	private static void appendCmsFields(List<TableColumn> tableColumns, Map<String, EntityColumn> entityColumns) {
		tableColumns.add(new TableColumn("modify_admin", ClassUtils.TYPE_INTEGER, false, 11, 0, "", "最后修改人编号"));
		tableColumns.add(new TableColumn("modify_time", ClassUtils.TYPE_INTEGER, false, 15, 0, "", "最后修改时间"));
		tableColumns.add(new TableColumn("comment", ClassUtils.TYPE_STRING, true, 500, 0, "", "备注"));
		entityColumns.put("modify_admin", new EntityColumn("modify_admin", "", ""));
		entityColumns.put("modify_time", new EntityColumn("modify_time", "", ""));
		entityColumns.put("comment", new EntityColumn("comment", "", ""));
	}

	/**
	 * 追加配置文件模型，如果带*表示多个文件
	 * 
	 * @param filePath
	 *            配置文件
	 * @return 所有追加的模型的名称
	 */
	public static synchronized List<String> appendFiles(String filePath) {
		List<String> names = new ArrayList<>();
		if (StringUtils.isBlank(filePath)) {
			return names;
		}
		File[] files = FileUtils.getFiles(filePath);
		if (null == files) {
			return names;
		}
		for (File file : files) {
			List<Model> models = JsonUtils.readFile(file, new TypeReference<List<Model>>() {
			});
			appendModels(models);
		}
		return names;
	}

	/**
	 * 追加模型
	 * 
	 * @param models
	 *            模型
	 * @return 所有追加的模型的名称
	 */
	public static synchronized List<String> appendModels(List<Model> models) {
		List<String> names = new ArrayList<>();
		for (Model m : models) {
			List<TableColumn> tableColumns = new ArrayList<>();
			Map<String, EntityColumn> entityColumns = new LinkedHashMap<>();
			// 追加系统主键
			if (m.getAutoincrement()) {// 自增长
				tableColumns.add(new TableColumn(m.getPk(), ClassUtils.TYPE_INTEGER, false, 11, 0, "", "主键"));
			} else {// GUID
				tableColumns.add(new TableColumn(m.getPk(), ClassUtils.TYPE_STRING, false, 36, 0, "", "主键"));
			}
			entityColumns.put(m.getPk(), new EntityColumn(m.getPk(), "", ""));
			for (org.think2framework.bean.Column column : m.getColumns()) {
				// 追加列
				appendColumn(column.getJoin(), column.getName(), getTypeFromTag(column.getTag()), !column.getRequired(),
						getLengthFromTag(column.getTag(), column.getLength()), column.getScale(),
						column.getDefaultValue(), StringUtils.isBlank(column.getTitle()) ? "" : column.getTitle(),
						column.getAlias(), tableColumns, entityColumns);
			}
			if (m.getCms()) {
				appendCmsFields(tableColumns, entityColumns);
			}
			OrmFactory.appendTable(m.getName(), new org.think2framework.orm.bean.Table(m.getName(), m.getPk(),
					m.getAutoincrement(), m.getUniques(), m.getIndexes(), m.getComment(), tableColumns));
			OrmFactory.appendEntity(m.getName(),
					new Entity(m.getTable(), m.getPk(), entityColumns, SelectHelp.generateJoins(m.getJoins()),
							SelectHelp.generateColumns(entityColumns), m.getFilters(), m.getOrders()));
			names.add(m.getName());
		}
		return names;
	}

}
