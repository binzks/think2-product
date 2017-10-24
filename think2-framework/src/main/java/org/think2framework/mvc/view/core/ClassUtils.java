package org.think2framework.mvc.view.core;

import org.think2framework.orm.core.TypeUtils;
import org.think2framework.utils.StringUtils;
import org.think2framework.mvc.view.bean.Action;
import org.think2framework.mvc.view.bean.Cell;
import org.think2framework.mvc.view.persistence.Item;
import org.think2framework.mvc.view.persistence.View;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 类和视图处理工具
 */
public class ClassUtils {

	/**
	 * 根据类获取定义的视频单元格
	 *
	 * @param clazz
	 *            类
	 * @return 视图单元格
	 */
	public static List<Cell> createCells(Class<?> clazz) {
		List<Cell> cells = new ArrayList<>();
		List<Field> fields = org.think2framework.orm.core.ClassUtils.getFields(clazz);
		for (Field field : fields) {
			org.think2framework.mvc.view.persistence.Cell cell = field
					.getAnnotation(org.think2framework.mvc.view.persistence.Cell.class);
			if (null != cell) {
				Item[] items = field.getAnnotationsByType(Item.class);
				List<org.think2framework.mvc.view.bean.Item> itemList = new ArrayList<>();
				if (null != items && items.length > 0) {
					for (Item item : items) {
						itemList.add(
								new org.think2framework.mvc.view.bean.Item(item.model(), item.key(), item.value()));
					}
				}
				String name = StringUtils.isBlank(cell.name()) ? field.getName() : cell.name();
				String tag = cell.tag();
				// 如果是默认的标签，则判断下字段类型是不是整型、浮点，自动设置，其他类型不设置
				if (TypeUtils.FIELD_TEXT.equalsIgnoreCase(tag)) {
					String className = field.getGenericType().getTypeName();
					if (Integer.class.getName().equals(className) || Long.class.getName().equals(className)
							|| Float.class.getName().equals(className)) {
						tag = TypeUtils.FIELD_INT;
					} else if (Float.class.getName().equals(className)) { // 浮点型
						tag = TypeUtils.FIELD_FLOAT;
					}
				}
				cells.add(new Cell(name, cell.title(), tag, cell.required(), cell.defaultValue(), cell.add(),
						cell.width(), cell.search(), cell.display(), cell.detail(), cell.add(), cell.edit(),
						cell.rowFilter(), cell.comment(), itemList));
			}
		}
		return cells;
	}

	/**
	 * 获取类的视图定义，如果没有定义则取父类，取到第一个定义返回
	 * 
	 * @param clazz
	 *            类
	 * @return 视图定义
	 */
	public static View getView(Class<?> clazz) {
		View view = clazz.getAnnotation(View.class);
		if (null == view) {
			Class<?> c = clazz.getSuperclass();
			while (Object.class != c) {
				view = c.getAnnotation(View.class);
				if (null != view) {
					break;
				}
				c = c.getSuperclass();
			}
		}
		return view;
	}

	/**
	 * 所有类以及所有父类的action定义
	 * 
	 * @param clazz
	 *            类
	 * @return action定义
	 */
	private static List<org.think2framework.mvc.view.persistence.Action> getActions(Class<?> clazz) {
		List<org.think2framework.mvc.view.persistence.Action> actions = new ArrayList<>();
		Collections.addAll(actions, clazz.getAnnotationsByType(org.think2framework.mvc.view.persistence.Action.class));
		Class<?> c = clazz.getSuperclass();
		while (Object.class != c) {
			Collections.addAll(actions, c.getAnnotationsByType(org.think2framework.mvc.view.persistence.Action.class));
			c = c.getSuperclass();
		}
		return actions;
	}

	/**
	 * 根据类获取按钮定义
	 *
	 * @param clazz
	 *            类
	 * @return 按钮定义
	 */
	public static List<Action> createActions(Class<?> clazz) {
		List<Action> actionList = new ArrayList<>();
		List<org.think2framework.mvc.view.persistence.Action> actions = getActions(clazz);
		if (null != actions && actions.size() > 0) {
			for (org.think2framework.mvc.view.persistence.Action action : actions) {
				actionList.add(new Action(action.name(), action.title(), action.type(), action.href(), action.clazz()));
			}
		}
		return actionList;
	}

}
