package org.think2framework;

import com.fasterxml.jackson.core.type.TypeReference;
import org.think2framework.bean.*;
import org.think2framework.exception.NonExistException;
import org.think2framework.exception.NonsupportException;
import org.think2framework.orm.OrmFactory;
import org.think2framework.orm.Query;
import org.think2framework.orm.Writer;
import org.think2framework.orm.bean.Entity;
import org.think2framework.orm.bean.EntityColumn;
import org.think2framework.orm.bean.Table;
import org.think2framework.orm.bean.TableColumn;
import org.think2framework.orm.core.ClassUtils;
import org.think2framework.orm.core.SelectHelp;
import org.think2framework.utils.FileUtils;
import org.think2framework.utils.JsonUtils;
import org.think2framework.utils.StringUtils;
import org.think2framework.view.bean.Action;
import org.think2framework.view.bean.Cell;
import org.think2framework.view.bean.Item;
import org.think2framework.view.bean.View;
import org.think2framework.view.core.TagUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.*;

/**
 * Created by zhoubin on 16/7/11. 模型工厂
 */
public class ModelFactory {

	private static final Logger logger = LogManager.getLogger(ModelFactory.class);

	private static String databaseFiles; // 数据库配置文件

	private static String modelFiles; // 模型配置文件

	private static Map<String, Database> databases = new HashMap<>(); // 模型对应的数据库配置

	private static Map<String, List<Cell>> cellsMap = new HashMap<>(); // 模型对应的视图列

	private static Map<String, List<Action>> actionsMap = new HashMap<>(); // 模型对应的按钮定义

	/**
	 * 设置数据库配置文件，如果第一次设置则加载配置
	 *
	 * @param databases
	 *            配置文件
	 */
	public static synchronized void setDatabaseFiles(String databases) {
		if (null == databaseFiles) {
			databaseFiles = databases;
			logger.debug("Set database config file {}", databaseFiles);
			reloadDatabases();
		}
	}

	/**
	 * 清理原先加载的数据库配置，重新加载数据库配置
	 */
	public static synchronized void reloadDatabases() {
		if (StringUtils.isBlank(databaseFiles)) {
			return;
		}
		File[] files = FileUtils.getFiles(databaseFiles);
		if (null == files) {
			return;
		}
		// 清理原先加载的数据库
		OrmFactory.clearDatabases();
		// 重新加载数据库
		for (File file : files) {
			List<Datasource> dss = JsonUtils.readFile(file, new TypeReference<List<Datasource>>() {
			});
			for (Datasource ds : dss) {
				if ("mysql".equalsIgnoreCase(ds.getType())) {
					OrmFactory.appendMysql(ds.getName(), ds.getMinIdle(), ds.getMaxIdle(), ds.getInitialSize(),
							ds.getTimeout(), ds.getHost(), ds.getPort(), ds.getDb(), ds.getUsername(),
							ds.getPassword());
				} else if ("redis".equalsIgnoreCase(ds.getType())) {
					OrmFactory.appendRedis(ds.getName(), ds.getMinIdle(), ds.getMaxIdle(), ds.getTimeout(),
							ds.getHost(), ds.getPort(), ds.getDb(), ds.getPassword());
				} else if ("sqlserver".equalsIgnoreCase(ds.getType())) {
					OrmFactory.appendSqlserver(ds.getName(), ds.getMinIdle(), ds.getMaxIdle(), ds.getInitialSize(),
							ds.getTimeout(), ds.getHost(), ds.getPort(), ds.getDb(), ds.getUsername(),
							ds.getPassword());
				} else if ("oracle".equalsIgnoreCase(ds.getType())) {
					OrmFactory.appendOracle(ds.getName(), ds.getMinIdle(), ds.getMaxIdle(), ds.getInitialSize(),
							ds.getTimeout(), ds.getHost(), ds.getPort(), ds.getDb(), ds.getUsername(),
							ds.getPassword());
				} else if ("sqlite".equalsIgnoreCase(ds.getType())) {
					OrmFactory.appendSqlite(ds.getName(), ds.getMinIdle(), ds.getMaxIdle(), ds.getInitialSize(),
							ds.getTimeout(), ds.getHost(), ds.getPort(), ds.getDb(), ds.getUsername(),
							ds.getPassword());
				} else {
					throw new NonsupportException("database type " + ds.getType());
				}
			}
		}
		logger.debug("Reload database config file {}", databaseFiles);
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
	 * 根据item定义生成备注
	 *
	 * @param items
	 *            item
	 * @return 说明
	 */
	private static String getItemComment(List<Item> items) {
		if (null != items && items.size() > 0) {
			StringBuilder comment = new StringBuilder();
			for (Item item : items) {
				String m = item.getModel();
				if (StringUtils.isNotBlank(m)) {
					comment.append(",model=").append(m).append(",key=").append(item.getKey()).append(",value=")
							.append(item.getValue());
				} else {
					comment.append(",").append(item.getKey()).append("-").append(item.getValue());
				}
			}
			return comment.substring(1);
		} else {
			return "";
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
	 * 设置模型配置文件，如果第一次则重新加载配置文件
	 *
	 * @param models
	 *            模型路径和名称
	 */
	public static void setModelFiles(String models) {
		if (null == modelFiles) {
			modelFiles = models;
			logger.debug("Set model config file {}", modelFiles);
			reloadModels();
		}
	}

	/**
	 * 追加一个模型的单元格
	 * 
	 * @param name
	 *            模型名称
	 * @param cells
	 *            模型对应视图单元格
	 */
	private static void setCell(String name, List<Cell> cells) {
		if (null == cellsMap.get(name)) {
			cellsMap.put(name, cells);
			logger.debug("Append cells {} {}", name);
		} else {
			logger.warn("Cells {} is already exist", name);
		}
	}

	/**
	 * 追加一个模型定义的按钮
	 * 
	 * @param name
	 *            模型名称
	 * @param actions
	 *            按钮
	 */
	private static void setAction(String name, List<Action> actions) {
		if (null == actionsMap.get(name)) {
			actionsMap.put(name, actions);
			logger.debug("Append actions {} {}", name);
		} else {
			logger.warn("Actions {} is already exist", name);
		}
	}

	/**
	 * 重新加载模型配置
	 */
	public static synchronized void reloadModels() {
		if (StringUtils.isBlank(modelFiles)) {
			return;
		}
		File[] files = FileUtils.getFiles(modelFiles);
		if (null == files) {
			return;
		}
		databases.clear();
		logger.debug("All databases cleared successfully");
		cellsMap.clear();
		logger.debug("All cells cleared successfully");
		actionsMap.clear();
		logger.debug("All actions cleared successfully");
		OrmFactory.clearModels();
		for (File file : files) {
			List<Model> models = JsonUtils.readFile(file, new TypeReference<List<Model>>() {
			});
			for (Model m : models) {
				List<TableColumn> tableColumns = new ArrayList<>();
				Map<String, EntityColumn> entityColumns = new LinkedHashMap<>();
				List<Cell> cells = new ArrayList<>();
				// 追加系统主键
				if (m.getAutoincrement()) {// 自增长
					tableColumns.add(new TableColumn(m.getPk(), ClassUtils.TYPE_INTEGER, false, 11, 0, "", "主键"));
					cells.add(new Cell(m.getPk(), "主键", TagUtils.TAG_INT, false, "", true, 11, false, false, false,
							false, false, false, "", null));
				} else {// GUID
					tableColumns.add(new TableColumn(m.getPk(), ClassUtils.TYPE_STRING, false, 36, 0, "", "主键"));
					cells.add(new Cell(m.getPk(), "主键", TagUtils.TAG_TEXT, false, "", true, 36, false, false, false,
							false, false, false, "", null));
				}
				entityColumns.put(m.getPk(), new EntityColumn(m.getPk(), "", ""));
				for (Column column : m.getColumns()) {
					String comment = (StringUtils.isBlank(column.getTitle()) ? "" : column.getTitle()) + " "
							+ getItemComment(column.getItems());
					// 如果字段没有关联名称或者为主表别名则表示为表的字段,如果有关联名称表示关联表,不设置到表
					if (StringUtils.isBlank(column.getJoin()) || column.getJoin().equals(SelectHelp.TABLE_ALIAS)) {
						tableColumns.add(new TableColumn(column.getName(), getTypeFromTag(column.getTag()),
								!column.getRequired(), getLengthFromTag(column.getTag(), column.getLength()),
								column.getScale(), column.getDefaultValue(), comment));
					}
					String realName = StringUtils.isBlank(column.getAlias()) ? column.getName() : column.getAlias();
					entityColumns.put(realName,
							new EntityColumn(column.getName(), column.getJoin(), column.getAlias()));
					cells.add(new Cell(realName, column.getTitle(), column.getTag(), column.getRequired(),
							column.getDefaultValue(), column.getCenter(), column.getWidth(), column.getSearch(),
							column.getDisplay(), column.getDetail(), column.getAdd(), column.getEdit(),
							column.getRowFilter(), comment, column.getItems()));
				}
				List<String> indexes = new ArrayList<>();
				if (null != m.getIndexes()) {
					indexes.addAll(Arrays.asList(m.getIndexes()));
				}
				// 工作流字段
				if (StringUtils.isNotBlank(m.getWorkflow())) {
					Workflow workflow = WorkflowFactory.get(m.getWorkflow());
					String defaultValue = ""; // 第一个节点值为默认值
					StringBuffer comment = new StringBuffer();
					for (Node node : workflow.getNodes()) {
						if ("" == defaultValue) {
							defaultValue = node.getValue();
						}
						comment.append(",").append(node.getName()).append("-").append(node.getValue());
					}
					tableColumns.add(new TableColumn(workflow.getField(), workflow.getTag(), true,
							getLengthFromTag(workflow.getTag(), workflow.getLength()), 0, defaultValue,
							"工作流[" + workflow.getName() + "]" + comment.toString().substring(1)));
					entityColumns.put(workflow.getField(), new EntityColumn(workflow.getField(), "", ""));
					indexes.add(workflow.getField());
				}
				if (m.getCms()) {
					appendCmsFields(tableColumns, entityColumns);
				}
				OrmFactory.appendTable(m.getName(), new Table(m.getName(), m.getPk(), m.getAutoincrement(),
						m.getUniques(), indexes.toArray(new String[indexes.size()]), m.getComment(), tableColumns));
				OrmFactory.appendEntity(m.getName(),
						new Entity(m.getTable(), m.getPk(), entityColumns, SelectHelp.generateJoins(m.getJoins()),
								SelectHelp.generateColumns(entityColumns), m.getFilters(), m.getOrders()));
				setCell(m.getName(), cells); // 添加模型对应的单元格
				setAction(m.getName(), m.getActions()); // 模型的按钮
				databases.put(m.getName(), new Database(m.getQuery(),
						StringUtils.isBlank(m.getWriter()) ? m.getQuery() : m.getWriter(), m.getRedis()));
			}
		}
		logger.debug("Reload model config file {}", modelFiles);
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
	public static View createView(String name, String title, String uri, Integer size, String columns, String actions) {
		List<Cell> cells = cellsMap.get(name);
		if (null == cells) {
			throw new NonExistException("Model " + name + " cells");
		}
		List<Action> actionList = actionsMap.get(name);
		if (null == actionList) {
			throw new NonExistException("Model " + name + " actions");
		}
		View newView = new View();
		newView.setName(name);
		newView.setTitle(title);
		newView.setUri(uri);
		newView.setSize(size);
		List<Cell> newCells = new ArrayList<>();
		List<Action> newActions = new ArrayList<>();
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
			newView.setCells(newCells);
		}
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
			newView.setActions(newActions);
		}
		return newView;
	}

}
