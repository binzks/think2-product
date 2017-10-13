package org.think2framework;

import com.fasterxml.jackson.core.type.TypeReference;
import org.think2framework.bean.*;
import org.think2framework.exception.NonExistException;
import org.think2framework.exception.SimpleException;
import org.think2framework.orm.OrmFactory;
import org.think2framework.orm.Query;
import org.think2framework.orm.Writer;
import org.think2framework.orm.bean.Entity;
import org.think2framework.orm.bean.EntityColumn;
import org.think2framework.orm.bean.Table;
import org.think2framework.orm.bean.TableColumn;
import org.think2framework.orm.core.ClassUtils;
import org.think2framework.orm.core.SelectHelp;
import org.think2framework.support.OrmSupport;
import org.think2framework.utils.FileUtils;
import org.think2framework.utils.JsonUtils;
import org.think2framework.utils.PackageUtils;
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

	private static Map<String, Database> databases = new HashMap<>(); // 模型对应的数据库配置

	private static Map<String, List<Cell>> cellsMap = new HashMap<>(); // 模型对应的视图列

	private static Map<String, List<Action>> actionsMap = new HashMap<>(); // 模型对应的按钮定义

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
		List<String> names = OrmSupport.scanPackages(packageDirNames);
		for (String name : names) {
			databases.put(name, new Database(query, StringUtils.isBlank(writer) ? query : writer, redis, valid));
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

	public static synchronized void appendClass(String query, String writer, String redis, Integer valid,
			Class<?> clazz) {
		String name = OrmSupport.appendClass(clazz);
		if (!"".equals(name)) {
			databases.put(name, new Database(query, StringUtils.isBlank(writer) ? query : writer, redis, valid));
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
