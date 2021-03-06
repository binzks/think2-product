package org.think2framework.context;

import com.fasterxml.jackson.core.type.TypeReference;
import org.think2framework.context.bean.*;
import org.think2framework.exception.NonExistException;
import org.think2framework.exception.SimpleException;
import org.think2framework.mvc.view.View;
import org.think2framework.orm.OrmFactory;
import org.think2framework.orm.Query;
import org.think2framework.orm.Writer;
import org.think2framework.orm.bean.Entity;
import org.think2framework.orm.bean.EntityColumn;
import org.think2framework.orm.bean.Table;
import org.think2framework.orm.bean.TableColumn;
import org.think2framework.orm.core.ClassUtils;
import org.think2framework.orm.core.SelectHelp;
import org.think2framework.orm.core.TypeUtils;
import org.think2framework.utils.*;
import org.think2framework.mvc.view.bean.Action;
import org.think2framework.mvc.view.bean.BaseView;
import org.think2framework.mvc.view.bean.Cell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.*;

/**
 * 模型工厂
 */
public class ModelFactory {

	private static final Logger logger = LogManager.getLogger(ModelFactory.class);

	private static Map<String, Database> databases = new HashMap<>(); // 模型对应的数据库配置

	private static Map<String, BaseView> baseViews = new HashMap<>(); // 模型对应的视图信息

	/**
	 * 从配置文件加载数据源
	 *
	 * @param filePath
	 *            配置文件路径和名称，带*为多个文件
	 * @param clear
	 *            是否清空原先的数据源配置
	 */
	public static synchronized void loadFiles(String filePath, Boolean clear) {
		File[] files = FileUtils.getFiles(filePath);
		if (null == files) {
			return;
		}
		if (clear) {
			OrmFactory.clearDatabases();
		}
		// 重新加载数据库
		for (File file : files) {
			List<Map<String, Object>> dss = JsonUtils.readFileToMapList(file);
			for (Map<String, Object> ds : dss) {
				OrmFactory.appendDatabase(StringUtils.toString(ds.get("type")), StringUtils.toString(ds.get("name")),
						NumberUtils.toInt(ds.get("minIdle"), 1), NumberUtils.toInt(ds.get("maxIdle"), 2),
						NumberUtils.toInt(ds.get("initialSize"), 2), NumberUtils.toInt(ds.get("timeout"), 300),
						StringUtils.toString(ds.get("db")), StringUtils.toString(ds.get("host")),
						NumberUtils.toInt(ds.get("port")), StringUtils.toString(ds.get("username")),
						StringUtils.toString(ds.get("password")));
			}
		}
	}

	/**
	 * 扫描多个包,将所有定义了模型的类追加到工厂
	 *
	 * @param query
	 *            模型对应查询数据源名称
	 * @param writer
	 *            模型对应写入数据源名称，如果为null或者空标识和读取一个数据库
	 * @param packageDirNames
	 *            包名
	 */
	public static synchronized void scanPackages(String query, String writer, String redis, Integer valid,
			String... packageDirNames) {
		for (String name : packageDirNames) {
			List<Class> list = PackageUtils.scanPackage(name);
			for (Class<?> clazz : list) {
				appendClass(query, writer, redis, valid, clazz);
			}
		}
	}

	/**
	 * 追加一个类到模型
	 * 
	 * @param query
	 * @param writer
	 * @param redis
	 * @param valid
	 * @param clazz
	 */
	public static synchronized void appendClass(String query, String writer, String redis, Integer valid,
			String clazz) {
		Class<?> c;
		try {
			c = Class.forName(clazz);
		} catch (ClassNotFoundException e) {
			throw new SimpleException(e);
		}
		appendClass(query, writer, redis, valid, c);
	}

	/**
	 * 根据类定义追加一个模型
	 * 
	 * @param query
	 *            查询数据源
	 * @param writer
	 *            写入数据源（没有则用查询数据源）
	 * @param redis
	 *            redis数据源
	 * @param valid
	 *            redis缓存时间
	 * @param clazz
	 *            类
	 */
	public static synchronized void appendClass(String query, String writer, String redis, Integer valid,
			Class<?> clazz) {
		Table table = ClassUtils.createTable(clazz);
		Entity entity = ClassUtils.createEntity(clazz);
		if (null != table) {
			String name = clazz.getName();
			OrmFactory.appendTable(name, table);
			OrmFactory.appendEntity(name, entity);
			databases.put(name, new Database(query, StringUtils.isBlank(writer) ? query : writer, redis, valid));
			// 如果view定义存在则添加view，如果没有名称则以模型名称为视图名称
			org.think2framework.mvc.view.persistence.View view = org.think2framework.mvc.view.core.ClassUtils
					.getView(clazz);
			if (null != view) {
				String viewName = StringUtils.isBlank(view.name()) ? name : view.name();
				baseViews.put(viewName,
						new BaseView(entity.getPk(), org.think2framework.mvc.view.core.ClassUtils.createCells(clazz),
								org.think2framework.mvc.view.core.ClassUtils.createActions(clazz)));
			}
		}
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
	 * 追加cms通用字段
	 */
	private static void appendCmsFields(List<TableColumn> tableColumns, Map<String, EntityColumn> entityColumns) {
		tableColumns.add(new TableColumn("modify_admin", TypeUtils.FIELD_INT, false, 11, 0, "", "最后修改人编号"));
		tableColumns.add(new TableColumn("modify_time", TypeUtils.FIELD_INT, false, 15, 0, "", "最后修改时间"));
		tableColumns.add(new TableColumn("comment", TypeUtils.FIELD_TEXT, true, 500, 0, "", "备注"));
		entityColumns.put("modify_admin", new EntityColumn("modify_admin", "", ""));
		entityColumns.put("modify_time", new EntityColumn("modify_time", "", ""));
		entityColumns.put("comment", new EntityColumn("comment", "", ""));
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
				tableColumns.add(new TableColumn(m.getPk(), TypeUtils.FIELD_INT, false, 11, 0, "", "主键"));
			} else {// GUID
				tableColumns.add(new TableColumn(m.getPk(), TypeUtils.FIELD_TEXT, false, 32, 0, "", "主键"));
			}
			entityColumns.put(m.getPk(), new EntityColumn(m.getPk(), "", ""));
			for (org.think2framework.context.bean.Column column : m.getColumns()) {
				TableColumn tableColumn = ClassUtils.createTableColumn(column.getJoin(), column.getName(),
						column.getTag(), !column.getRequired(), column.getLength(), column.getScale(),
						column.getDefaultValue(), StringUtils.isBlank(column.getTitle()) ? "" : column.getTitle());
				if (null != tableColumn) {
					tableColumns.add(tableColumn);
				}
				entityColumns.put(StringUtils.isBlank(column.getAlias()) ? column.getName() : column.getAlias(),
						new EntityColumn(column.getName(), column.getJoin(), column.getAlias()));
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

	/**
	 * 根据名称获取数据库配置
	 *
	 * @param name
	 *            名称
	 * @return 模型
	 */
	private static Database getDatabase(String name) {
		Database database = databases.get(name);
		if (null == database) {
			throw new NonExistException(Database.class.getName() + " " + name);
		}
		return database;
	}

	/**
	 * 根据名称创建一个查询生成器
	 *
	 * @param name
	 *            模型名称
	 * @return 查询生成器
	 */
	public static Query createQuery(String name) {
		return OrmFactory.createQuery(getDatabase(name).getQuery(), name);
	}

	/**
	 * 根据名称创建一个写入生成器
	 *
	 * @param name
	 *            名称
	 * @return 写入生成器
	 */
	public static Writer createWriter(String name) {
		return OrmFactory.createWriter(getDatabase(name).getWriter(), name);
	}

	/**
	 * 根据模型名称创建一个查询生成器，使用redis
	 * 
	 * @param name
	 *            模型名称
	 * @param redis
	 *            redis数据源名称
	 * @param valid
	 *            redis缓存有效期
	 * @return 查询生成器
	 */
	public static Query createQuery(String name, String redis, Integer valid) {
		return OrmFactory.createQuery(getDatabase(name).getQuery(), name, redis, valid);
	}

	/**
	 * 根据模型名称创建一个视图
	 * 
	 * @param name
	 *            模型名称
	 * @param columns
	 *            没有权限的列
	 * @param actions
	 *            没有权限的按钮
	 * @return 视图
	 */
	public static View createView(String name, String title, String moduleId, String uri, Integer size, String columns,
			String actions) {
		BaseView baseView = baseViews.get(name);
		if (null == baseView) {
			throw new NonExistException("Model " + name + " base view");
		}
		List<Cell> newCells = new ArrayList<>();
		List<Action> newActions = new ArrayList<>();
		List<Cell> cells = baseView.getCells();
		if (null != cells && cells.size() > 0) {
			if (StringUtils.isBlank(columns)) {
				newCells.addAll(cells);
			} else {
				columns = "," + columns + ",";
				for (Cell cell : cells) {
					if (!StringUtils.contains(columns, "," + cell.getName() + ",")) {
						newCells.add(cell);
					}
				}
			}
		}
		List<Action> actionList = baseView.getActions();
		if (null != actionList && actionList.size() > 0) {
			if (StringUtils.isBlank(actions)) {
				newActions.addAll(actionList);
			} else {
				actions = "," + actions + ",";
				for (Action action : actionList) {
					if (!StringUtils.contains(actions, "," + action.getName() + ",")) {
						newActions.add(action);
					}
				}
			}
		}
		return new View(name, title, moduleId, uri, size, baseView.getPk(), newCells, newActions);
	}

}
