package org.think2framework.support;

import org.think2framework.orm.core.ClassUtils;
import org.think2framework.utils.StringUtils;
import org.think2framework.view.bean.Action;
import org.think2framework.view.bean.Cell;
import org.think2framework.view.persistence.Item;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 视图配置
 */
public class ViewSupport {

	/**
	 * 根据类获取定义的视频单元格
	 * 
	 * @param clazz
	 *            类
	 * @return 视图单元格
	 */
	public static List<Cell> getCells(Class<?> clazz) {
		List<Cell> cells = new ArrayList<>();
		List<Field> fields = ClassUtils.getFields(clazz);
		for (Field field : fields) {
			org.think2framework.view.persistence.Cell cell = clazz
					.getAnnotation(org.think2framework.view.persistence.Cell.class);
			if (null != cell) {
				Item[] items = field.getAnnotationsByType(Item.class);
				List<org.think2framework.view.bean.Item> itemList = new ArrayList<>();
				if (null != items && items.length > 0) {
					for (Item item : items) {
						itemList.add(new org.think2framework.view.bean.Item(item.model(), item.key(), item.value()));
					}
				}
				String name = StringUtils.isBlank(cell.name()) ? field.getName() : cell.name();
				cells.add(new Cell(name, cell.title(), cell.tag(), cell.required(), cell.defaultValue(), cell.add(),
						cell.width(), cell.search(), cell.display(), cell.detail(), cell.add(), cell.edit(),
						cell.rowFilter(), cell.comment(), itemList));
			}
		}
		return cells;
	}

	/**
	 * 根据类获取按钮定义
	 * 
	 * @param clazz
	 *            类
	 * @return 按钮定义
	 */
	public static List<Action> getActions(Class<?> clazz) {
		List<Action> actionList = new ArrayList<>();
		org.think2framework.view.persistence.Action[] actions = clazz
				.getAnnotationsByType(org.think2framework.view.persistence.Action.class);
		if (null != actions && actions.length > 0) {
			for (org.think2framework.view.persistence.Action action : actions) {
				actionList.add(new Action(action.name(), action.title(), action.type(), action.href(), action.clazz()));
			}
		}
		return actionList;
	}
}
