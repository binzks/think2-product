package org.think2framework.view.bean;

import org.think2framework.utils.NumberUtils;
import org.think2framework.utils.StringUtils;
import org.think2framework.view.HtmlTag;
import org.think2framework.view.core.ATag;
import org.think2framework.view.core.SimpleHtmlTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private List<Cell> cells; // 模型单元格定义

	private List<Action> actions; // 模型的按钮定义

	private String tableHeadHtml; // 视图对应的表格数据的头

	private String searchHeadHtml; // 搜索框

	private HtmlTag searchBody; // 搜索项

	private Map<String, HtmlTag> searchItems; // 搜索项

	private List<ATag> listActions; // list页面每行数据的操作按钮

	public View(String name, String title, String moduleId, String uri, Integer size, String pk, List<Cell> cells,
			List<Action> actions) {
		this.name = name;
		this.title = title;
		this.moduleId = moduleId;
		this.uri = uri;
		this.size = size;
		this.pk = pk;
		this.cells = cells;
		this.actions = actions;
		init();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public List<Cell> getCells() {
		return cells;
	}

	public void setCells(List<Cell> cells) {
		this.cells = cells;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	/**
	 * 初始化视图对应的表头和搜索
	 */
	private void init() {
		HtmlTag tableHead = new SimpleHtmlTag("thead");
		searchItems = new HashMap<>();
		listActions = new ArrayList<>();
		HtmlTag searchHead = new SimpleHtmlTag("div", "widget-header");
		searchBody = new SimpleHtmlTag("div", "widget-body");
		searchHead.appendChild(new SimpleHtmlTag("h5", "widget-title", "搜索"));
		HtmlTag searchCol = new SimpleHtmlTag("div", "col-xs-12 align-center");
		if (null != cells && cells.size() > 0) {
			for (Cell cell : cells) {
				if (cell.getDisplay()) {
					tableHead.appendChild(createTh(cell.getTitle(), cell.getWidth()));
				}
				if (cell.getSearch()) {
					searchCol.appendChild(new SimpleHtmlTag("div", "col-xs-1 align-right", cell.getTitle()));
					HtmlTag div = new SimpleHtmlTag("div", "col-xs-3");
					HtmlTag item = new SimpleHtmlTag("input", cell.getName(), "col-xs-12");
					div.appendChild(item);
					searchCol.appendChild(div);
					searchItems.put(cell.getName(), item);
				}
			}
		}
		// 如果有搜索项则添加搜索项和搜索上下拉按钮
		if (searchCol.hasChild()) {
			// 设置搜索内容
			HtmlTag divRow = new SimpleHtmlTag("div", "row");
			divRow.appendChild(searchCol);
			HtmlTag widgetMain = new SimpleHtmlTag("div", "widget-main");
			widgetMain.appendChild(divRow);
			searchBody.appendChild(widgetMain);
			// 设置搜索按钮和下拉上拉按钮
			HtmlTag widgetToolbar = new SimpleHtmlTag("div", "widget-toolbar");
			// 搜索按钮
			widgetToolbar
					.appendChild(new ATag(uri + moduleId + ".page", "ace-icon fa fa-search orange bigger-130", "查询"));
			// 显示隐藏搜索条件按钮
			HtmlTag up = new ATag("#");
			up.setAttribute("data-action", "collapse");
			up.appendChild(new SimpleHtmlTag("i", "ace-icon fa fa-chevron-down bigger-130"));
			widgetToolbar.appendChild(up);
			searchHead.appendChild(widgetToolbar);
		}
		// 设置按钮
		if (null != actions && actions.size() > 0) {
			HtmlTag widgetToolbar = new SimpleHtmlTag("div", "widget-toolbar"); // 模块按钮的toolbar
			int actionCount = 0;
			for (Action action : actions) {
				if ("1".equals(action.getType())) {
					listActions.add(
							new ATag(action.getHref() + moduleId + "-%s.page", action.getClazz(), action.getTitle()));
					actionCount = actionCount + 32;
				} else if ("0".equals(action.getType())) {
					widgetToolbar.appendChild(new ATag(action.getHref(), action.getClazz(), action.getTitle()));
				}
			}
			if (actionCount > 0) {
				// 设置操作列长度，如果是一个按钮操作两个字需要45，大于一个每个32计算
				tableHead.appendChild(createTh("操作", actionCount > 45 ? actionCount : 45));
			}
			if (widgetToolbar.hasChild()) {
				searchHead.appendChild(widgetToolbar);
			}
		}
		tableHeadHtml = tableHead.htmlString();
		searchHeadHtml = searchHead.htmlString();
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
	 * 获取视图对应的表头
	 * 
	 * @return 表头
	 */
	public String getTableHeadHtml() {
		return tableHeadHtml;
	}

	/**
	 * 获取搜索框头
	 * 
	 * @return 搜索框头
	 */
	public String getSearchHeadHtml() {
		return searchHeadHtml;
	}

	/**
	 * 获取搜索内容标签
	 * 
	 * @return 搜索内容
	 */
	public Map<String, HtmlTag> getSearchItems() {
		return searchItems;
	}

	/**
	 * 获取搜索项html
	 * 
	 * @return 搜索项
	 */
	public String getSearchBodyHtml() {
		return searchBody.htmlString();
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
			for (Cell cell : cells) {
				if (cell.getDisplay()) {
					tr.appendChild(new SimpleHtmlTag("td", "center", StringUtils.toString(map.get(cell.getName()))));
				}
			}
			if (listActions.size() > 0) {
				HtmlTag td = new SimpleHtmlTag("td", "center visible-md visible-lg hidden-sm hidden-xs action-buttons");
				StringBuffer actions = new StringBuffer();
				String pkValue = StringUtils.toString(map.get(pk));
				for (ATag aTag : listActions) {
					actions.append(aTag.toDataHtml(pkValue));
				}
				td.setText(actions.toString());
				tr.appendChild(td);
			}
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
}
