package org.think2framework.view.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.think2framework.view.HtmlTag;
import org.think2framework.view.core.ATag;
import org.think2framework.view.core.SimpleHtmlTag;

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

	private List<Cell> cells; // 模型单元格定义

	private List<Action> actions; // 模型的按钮定义

	@JsonIgnore
	private String tableHeadHtml = null; // 视图对应的表格数据的头

	@JsonIgnore
	private HtmlTag search; // 搜索框

	@JsonIgnore
	private Map<String, HtmlTag> searchItems; // 搜索项

	public View() {
	}

	public View(String name, List<Cell> cells, List<Action> actions) {
		this.name = name;
		this.cells = cells;
		this.actions = actions;
	}

	public View(String name, String title, String moduleId, String uri, Integer size, List<Cell> cells,
			List<Action> actions) {
		this.name = name;
		this.title = title;
		this.moduleId = moduleId;
		this.uri = uri;
		this.size = size;
		this.cells = cells;
		this.actions = actions;
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
		th.setAttribute("style", "width: " + width + "px;");
		return th;
	}

	/**
	 * 获取视图对应的表头
	 * 
	 * @return 表头
	 */
	public String getTableHeadHtml() {
		if (null == tableHeadHtml) {
			HtmlTag tableHead = new SimpleHtmlTag("thead");
			for (Cell cell : cells) {
				if (cell.getDisplay()) {
					tableHead.appendChild(createTh(cell.getTitle(), cell.getWidth()));
				}
			}
			// 设置按钮
			if (null != actions && actions.size() > 0) {
				int actionCount = 0;
				for (Action action : actions) {
					if ("1".equals(action.getType())) {
						actionCount++;
					}
				}
				if (actionCount > 0) {
					// 设置操作列长度，如果是一个按钮操作两个字需要45，大于一个每个32计算
					if (actionCount == 1) {
						actionCount = 45;
					} else {
						actionCount = actionCount * 32;
					}
					tableHead.appendChild(createTh("操作", actionCount));
				}
			}
			tableHeadHtml = tableHead.htmlString();
		}
		return tableHeadHtml;
	}

	/**
	 * 获取视图对应的搜索头，包含按钮
	 * 
	 * @return 搜索头
	 */
	public HtmlTag getSearch() {
		if (null == search) {
			search = new SimpleHtmlTag("div", "widget-box");
			searchItems = new HashMap<>();
			HtmlTag searchHead = new SimpleHtmlTag("div", "widget-header");
			HtmlTag searchBody = new SimpleHtmlTag("div", "widget-body");
			searchHead.appendChild(new SimpleHtmlTag("h5", "widget-title", "搜索"));
			Boolean hasSearch = false;
			HtmlTag searchCol = new SimpleHtmlTag("div", "col-xs-12 align-center");
			for (Cell cell : cells) {
				if (cell.getSearch()) {
					searchCol.appendChild(new SimpleHtmlTag("div", "col-xs-1 align-right", cell.getTitle()));
					HtmlTag div = new SimpleHtmlTag("div", "col-xs-3");
					HtmlTag item = new SimpleHtmlTag("input", cell.getName(), "col-xs-12");
					div.appendChild(item);
					searchCol.appendChild(div);
					searchItems.put(cell.getName(), item);
					hasSearch = true;
				}
			}
			if (searchCol.hasChild()) {
				// 设置搜索内容
				HtmlTag divRow = new SimpleHtmlTag("div", "row");
				divRow.appendChild(searchCol);
				HtmlTag widgetMain = new SimpleHtmlTag("div", "widget-main");
				widgetMain.appendChild(divRow);
				searchBody.appendChild(widgetMain);
			}
			// 如果有搜索条件则添加查询按钮和下拉上拉按钮
			if (hasSearch) {
				// 设置搜索按钮和下拉上拉按钮
				HtmlTag widgetToolbar = new SimpleHtmlTag("div", "widget-toolbar");
				// 搜索按钮
				widgetToolbar.appendChild(
						new ATag(uri + moduleId + ".page", "ace-icon fa fa-search orange bigger-130", "查询"));
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
				for (Action action : actions) {
					if ("0".equals(action.getType())) {
						widgetToolbar.appendChild(new ATag(action.getHref(), action.getClazz(), action.getTitle()));
					}
				}
				if (widgetToolbar.hasChild()) {
					searchHead.appendChild(widgetToolbar);
				}
			}
			search.appendChild(searchHead);
			search.appendChild(searchBody);
		}
		return search;
	}

	/**
	 * 获取搜索内容标签
	 * 
	 * @return 搜索内容
	 */
	public Map<String, HtmlTag> getSearchItems() {
		if (null == searchItems) {
			getSearch();
		}
		return searchItems;
	}
}
