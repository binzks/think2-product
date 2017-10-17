package org.think2framework.view;

import org.think2framework.orm.core.ClassUtils;
import org.think2framework.orm.core.TypeUtils;
import org.think2framework.utils.StringUtils;
import org.think2framework.view.bean.Action;
import org.think2framework.view.bean.Cell;
import org.think2framework.view.core.ATag;
import org.think2framework.view.core.SelectTag;
import org.think2framework.view.core.SimpleHtmlTag;

import java.util.*;

/**
 * Created by zhoubin on 2017/6/12. 视图定义
 */
public class View {

	private String name; // 名称

	private String title; // 标题

	private String moduleId; // 模块id

	private String uri; // list页面搜索的uri

	private Integer size; // list页面每页数量

	private String pk; // 视图对应的模型的主键名称

	private String tableHeadHtml = ""; // 视图对应的表格数据的头html

	private String searchActionHtml = ""; // 模块整体的按钮

	private HtmlTag searchTag; // 搜索页面

	private Map<String, HtmlTag> searchHtmlTags; // 搜索项各个具体的html标签

	private List<ATag> listActions; // list页面每行数据的操作按钮标签

	private Map<String, HtmlTag> listHtmlTags; // list页面表格每行数据的标签

	private Map<String, String> defaultValues; // 设置了默认值的字段

	private HtmlTag addTag; // 添加页面

	private Map<String, HtmlTag> addHtmlTags; // 添加页面标签

	private HtmlTag editTag; // 编辑页面

	private Map<String, HtmlTag> editHtmlTags; // 编辑页面标签

	public View(String name, String title, String moduleId, String uri, Integer size, String pk, List<Cell> cells,
			List<Action> actions) {
		this.name = name;
		this.title = title;
		this.moduleId = moduleId;
		this.uri = uri;
		this.pk = pk;
		this.size = size;
		searchTag = new SimpleHtmlTag("div", "col-xs-12");
		addTag = new SimpleHtmlTag("div");
		editTag = new SimpleHtmlTag("div");
		searchHtmlTags = new LinkedHashMap<>();
		listActions = new ArrayList<>();
		listHtmlTags = new LinkedHashMap<>();
		defaultValues = new HashMap<>();
		addHtmlTags = new LinkedHashMap<>();
		editHtmlTags = new LinkedHashMap<>();
		Integer actionCount = initActions(actions);
		initCells(cells, actionCount);
	}

	public String getName() {
		return name;
	}

	public String getTitle() {
		return title;
	}

	public String getUri() {
		return uri;
	}

	public Integer getSize() {
		return size;
	}

	public String getPk() {
		return pk;
	}

	public String getTableHeadHtml() {
		return tableHeadHtml;
	}

	public String getSearchActionHtml() {
		return searchActionHtml;
	}

	public Map<String, HtmlTag> getSearchHtmlTags() {
		return searchHtmlTags;
	}

	public Map<String, String> getDefaultValues() {
		return defaultValues;
	}

	public Map<String, HtmlTag> getAddHtmlTags() {
		return addHtmlTags;
	}

	public Map<String, HtmlTag> getEditHtmlTags() {
		return editHtmlTags;
	}

	public String getSearchHtml() {
		return searchTag.htmlString();
	}

	public String getAddHtml() {
		return addTag.childrenString();
	}

	public String getEditHtml() {
		return editTag.childrenString();
	}

	private void initCells(List<Cell> cells, Integer actionCount) {
		if (null == cells) {
			return;
		}
		HtmlTag tableHead = new SimpleHtmlTag("thead");
		for (Cell cell : cells) {
			if (cell.getDisplay()) {
				tableHead.appendChild(createTh(cell.getTitle(), cell.getWidth()));
			}
			if (StringUtils.isNotBlank(cell.getDefaultValue())) {
				defaultValues.put(cell.getName(), cell.getDefaultValue());
			}
			HtmlTag tag = createCellHtmlTag(cell);
			if (cell.getSearch()) {
				searchHtmlTags.put(cell.getName(), tag);
				searchTag.appendChild(new SimpleHtmlTag("div", "col-xs-1 align-right", cell.getTitle()));
				HtmlTag div = new SimpleHtmlTag("div", "col-xs-3");
				div.appendChild(tag);
				searchTag.appendChild(div);
			}
			if (cell.getDisplay()) {
				listHtmlTags.put(cell.getName(), tag);
			}
			// 如果是主键则不进行添加或者修改
			if (pk.equalsIgnoreCase(cell.getName())) {
				continue;
			}
			HtmlTag group = new SimpleHtmlTag("div", "form-group");
			group.appendChild(new SimpleHtmlTag("label", "col-sm-3 control-label no-padding-right", cell.getTitle()));
			HtmlTag div = new SimpleHtmlTag("div", "col-sm-6");
			div.appendChild(tag);
			group.appendChild(div);
			if (cell.getAdd()) {
				addHtmlTags.put(cell.getName(), tag);
				addTag.appendChild(group);
			}
			if (cell.getEdit()) {
				editHtmlTags.put(cell.getName(), tag);
				editTag.appendChild(group);
			}
		}
		if (actionCount > 0) {
			tableHead.appendChild(createTh("操作", actionCount));
		}
		tableHeadHtml = tableHead.htmlString();
	}

	/**
	 * 初始化按钮定义
	 * 
	 * @param actions
	 *            按钮
	 * @return 返回每行操作按钮列的长度
	 */
	private Integer initActions(List<Action> actions) {
		if (null == actions) {
			return 0;
		}
		HtmlTag widgetToolbar = new SimpleHtmlTag("div", "widget-toolbar"); // 模块按钮的toolbar
		int actionCount = 0;
		for (Action action : actions) {
			if ("1".equals(action.getType())) {
				listActions
						.add(new ATag(action.getHref() + moduleId + "-%s.page", action.getClazz(), action.getTitle()));
				actionCount = actionCount + 32;
			} else if ("0".equals(action.getType())) {
				widgetToolbar.appendChild(
						new ATag(action.getHref() + moduleId + ".page", action.getClazz(), action.getTitle()));
			}
		}
		if (widgetToolbar.hasChild()) {
			searchActionHtml = widgetToolbar.htmlString();
		}
		return actionCount > 45 ? actionCount : 45;
	}

	/**
	 * 创建一个表头列
	 * 
	 * @param text
	 *            文本
	 * @param width
	 *            长度
	 * @return 表头列html标签
	 */
	private HtmlTag createTh(String text, Integer width) {
		SimpleHtmlTag th = new SimpleHtmlTag("th");
		th.setText(text);
		th.setAttribute("class", "center");
		th.setAttribute("style", "width: " + width + "px;");
		return th;
	}

	/**
	 * 根据数据生成list页面对应的表格内容
	 * 
	 * @param list
	 *            数据
	 * @return 表格内容
	 */
	public String createTableBodyHtml(List<Map<String, Object>> list) {
		HtmlTag tableBody = new SimpleHtmlTag("tbody");
		for (int i = 0; i < list.size(); i++) {
			HtmlTag tr = new SimpleHtmlTag("tr");
			if (i / 2 == 0) {
				tr.setAttribute("class", "odd");
			} else {
				tr.setAttribute("class", "even");
			}
			Map<String, Object> map = list.get(i);
			StringBuffer tdString = new StringBuffer();
			for (Map.Entry<String, HtmlTag> entry : listHtmlTags.entrySet()) {
				entry.getValue().setValue(StringUtils.toString(map.get(entry.getKey())));
				tdString.append("<td class=\"center\">").append(entry.getValue().getText()).append("</td>");
			}
			if (listActions.size() > 0) {
				HtmlTag td = new SimpleHtmlTag("td", "center visible-md visible-lg hidden-sm hidden-xs action-buttons");
				StringBuffer actions = new StringBuffer();
				String pkValue = StringUtils.toString(map.get(pk));
				for (ATag aTag : listActions) {
					actions.append(aTag.toDataHtml(pkValue));
				}
				td.setText(actions.toString());
				tdString.append(td.htmlString());
			}
			tr.setText(tdString.toString());
			tableBody.appendChild(tr);
		}
		return tableBody.htmlString();
	}

	/**
	 * 根据当前页和总数据获取list页面分页html
	 * 
	 * @param page
	 *            当前页
	 * @param totalCount
	 *            总数
	 * @return html
	 */
	public String createPageHtml(Integer page, Integer totalCount) {
		int totalPages = totalCount / size;
		if (totalCount % size > 0) {
			totalPages++;
		}
		HtmlTag total = new SimpleHtmlTag("div", "col-xs-6 dataTables_info",
				"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;共" + totalPages + "页/" + totalCount + "记录");
		HtmlTag ul = new SimpleHtmlTag("ul", "pagination");
		int end = totalPages - page > 4 ? page + 4 : totalPages;
		// 第一页
		if (page > 1) {
			HtmlTag li = new SimpleHtmlTag("li");
			li.appendChild(new ATag(uri + moduleId + ".page", "", "", "ace-icon fa fa-angle-double-left"));
			ul.appendChild(li);
		}
		// 实际分页页数
		for (int i = page; i <= end; i++) {
			HtmlTag li = new SimpleHtmlTag("li");
			if (i == page) {
				li.setAttribute("class", "active");
				li.appendChild(new ATag("javascript:;", "", StringUtils.toString(i), ""));
			} else {
				li.appendChild(new ATag(uri + moduleId + "-" + i + ".page", "", StringUtils.toString(i), ""));
			}
			ul.appendChild(li);
		}
		// 最后一页
		if (end < totalPages) {
			HtmlTag li = new SimpleHtmlTag("li");
			li.appendChild(
					new ATag(uri + moduleId + "-" + totalPages + ".page", "", "", "ace-icon fa fa-angle-double-right"));
			ul.appendChild(li);
		}
		HtmlTag row = new SimpleHtmlTag("div", "row");
		HtmlTag div = new SimpleHtmlTag("div", "col-xs-6 dataTables_paginate paging_simple_numbers");
		div.appendChild(ul);
		row.appendChild(total);
		row.appendChild(div);
		return row.htmlString();
	}

	/**
	 * 根据cell定义创建一组html编辑项
	 * 
	 * @param cell
	 *            cell定义
	 * @return 编辑框
	 */
	private HtmlTag createCellHtmlTag(Cell cell) {
		HtmlTag htmlTag;
		if (TypeUtils.FIELD_BOOL.equalsIgnoreCase(cell.getTag())) {
			SelectTag selectTag = new SelectTag();
			selectTag.setAttribute("name", cell.getName());
			selectTag.setOption(ClassUtils.FALSE_VALUE.toString(), "否");
			selectTag.setOption(ClassUtils.TRUE_VALUE.toString(), "是");
			htmlTag = selectTag;
		} else if (TypeUtils.FIELD_ITEM_INT.equalsIgnoreCase(cell.getTag())
				|| TypeUtils.FIELD_ITEM.equalsIgnoreCase(cell.getTag())) {
			SelectTag selectTag = new SelectTag();
			selectTag.setAttribute("name", cell.getName());
			selectTag.setOptions(cell.getItems());
			htmlTag = selectTag;
		} else {
			htmlTag = new SimpleHtmlTag("input", "col-xs-12 col-sm-12");
			htmlTag.setAttribute("type", "text");
			htmlTag.setAttribute("name", cell.getName());
		}
		return htmlTag;
	}
}
