package org.think2framework.controller;

import org.think2framework.ModelFactory;
import org.think2framework.orm.Query;
import org.think2framework.security.SessionHelp;
import org.think2framework.utils.StringUtils;
import org.think2framework.view.HtmlTag;
import org.think2framework.view.bean.Action;
import org.think2framework.view.bean.Cell;
import org.think2framework.view.bean.View;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 系统基础的控制器
 */
public class BaseController {

	/**
	 * 创建基本list页面html，搜索框，表格表头
	 * 
	 * @param model
	 *            spring model
	 * @param request
	 *            请求
	 * @param mid
	 *            模块id
	 * @param uri
	 *            list页面uri
	 * @param cells
	 *            视图单元格定义
	 * @param actions
	 *            按钮
	 */
	protected void createListBaseHtml(Model model, Query query, HttpServletRequest request, String mid, String uri,
			List<Cell> cells, List<Action> actions) {
		// HtmlTag searchHead = TagFactory.createDiv("widget-header");
		// HtmlTag searchBody = TagFactory.createDiv("widget-body");
		// searchHead.appendChild(TagFactory.createH(5, "widget-title", "搜索"));
		// HtmlTag searchCol = TagFactory.createDiv("col-xs-12 align-center");
		// HtmlTag tableHead = TagFactory.createTr();
		// for (Cell cell : cells) {
		// if (cell.getSearch()) {
		// String value = request.getParameter(cell.getName());
		// searchCol.appendChild(TagFactory.createDiv("col-xs-1 align-right",
		// cell.getTitle()));
		// HtmlTag div = TagFactory.createDiv("col-xs-3");
		// div.appendChild(TagFactory.createInput(cell.getName(), "col-xs-12", value));
		// searchCol.appendChild(div);
		// if (StringUtils.isNotBlank(value)) {
		// query.eq(cell.getName(), value);
		// }
		// }
		// if (cell.getDisplay()) {
		// tableHead.appendChild(TagFactory.createTh(cell.getTitle(), cell.getWidth()));
		// }
		// }
		// if (searchCol.hasChild()) {
		// // 设置搜索内容
		// HtmlTag divRow = TagFactory.createDiv("row");
		// divRow.appendChild(searchCol);
		// HtmlTag widgetMain = TagFactory.createDiv("widget-main");
		// widgetMain.appendChild(divRow);
		// searchBody.appendChild(widgetMain);
		// // 设置搜索按钮和下拉上拉按钮
		// HtmlTag widgetToolbar = TagFactory.createDiv("widget-toolbar");
		// // 搜索按钮
		// widgetToolbar.appendChild(
		// TagFactory.createA(uri + mid + ".page", "ace-icon fa fa-search orange
		// bigger-130", "查询"));
		// // 显示隐藏搜索条件按钮
		// HtmlTag up = TagFactory.createA("#", "");
		// up.setAttribute("data-action", "collapse");
		// up.appendChild(TagFactory.createI("ace-icon fa fa-chevron-down bigger-130"));
		// widgetToolbar.appendChild(up);
		// searchHead.appendChild(widgetToolbar);
		// }
		// // 设置按钮
		// if (null != actions && actions.size() > 0) {
		// HtmlTag widgetToolbar = TagFactory.createDiv("widget-toolbar"); //
		// 模块按钮的toolbar
		// int actionCount = 0;
		// for (Action action : actions) {
		// if ("0".equals(action.getType())) {
		// widgetToolbar
		// .appendChild(TagFactory.createA(action.getHref(), action.getClazz(),
		// action.getTitle()));
		// } else if ("1".equals(action.getType())) {
		// actionCount++;
		// }
		// }
		// if (widgetToolbar.hasChild()) {
		// searchHead.appendChild(widgetToolbar);
		// }
		// if (actionCount > 0) {
		// // 设置操作列长度，如果是一个按钮操作两个字需要45，大于一个每个32计算
		// if (actionCount == 1) {
		// actionCount = 45;
		// } else {
		// actionCount = actionCount * 32;
		// }
		// tableHead.appendChild(TagFactory.createTh("操作", actionCount));
		// }
		// }
		// if (searchHead.hasChild() || searchBody.hasChild()) {
		// HtmlTag search = TagFactory.createDiv("widget-box");
		// search.appendChild(searchHead);
		// search.appendChild(searchBody);
		// model.addAttribute("search", search.htmlString());
		// }
		// HtmlTag head = TagFactory.createTHead();
		// head.appendChild(tableHead);
		// model.addAttribute("tableHead", head.htmlString());
	}

	protected void createTableBody(Model model, String mid, String pk, List<Map<String, Object>> list, List<Cell> cells,
			List<Action> actions) {
		// HtmlTag tableBody = TagFactory.createTBody();
		// for (int i = 0; i < list.size(); i++) {
		// HtmlTag tr = TagFactory.createTr();
		// if (i / 2 == 0) {
		// tr.setAttribute("class", "even");
		// } else {
		// tr.setAttribute("class", "odd");
		// }
		// Map<String, Object> map = list.get(i);
		// for (Cell cell : cells) {
		// if (cell.getDisplay()) {
		// tr.appendChild(TagFactory.createTd("center",
		// StringUtils.toString(map.get(cell.getName()))));
		// }
		// }
		// if (null != actions && actions.size() > 0) {
		// for (Action action : actions) {
		// HtmlTag td = TagFactory.createTd("center visible-md visible-lg hidden-sm
		// hidden-xs action-buttons");
		// if ("1".equals(action.getType())) {
		// td.appendChild(TagFactory.createA(
		// action.getHref() + mid + "-" + StringUtils.toString(map.get(pk)) + ".page",
		// action.getClazz(), null, action.getTitle()));
		// }
		// if (td.hasChild()) {
		// tr.appendChild(td);
		// }
		// }
		// }
		// tableBody.appendChild(tr);
		// }
		// model.addAttribute("tableBody", tableBody.htmlString());
	}

	@RequestMapping(value = { "/list{mid}{page}", "/list{mid}-{page}" })
	public String getList(@PathVariable String mid, @PathVariable Integer page, Model model,
			HttpServletRequest request) {
		View view = SessionHelp.getView(mid, request.getSession());
		Query query = ModelFactory.createQuery(view.getName());
		Map<String, HtmlTag> searchItems = view.getSearchItems();
		HtmlTag search = view.getSearch();
		for (Map.Entry<String, HtmlTag> entry : searchItems.entrySet()) {
			String value = request.getParameter(entry.getKey());
			if (StringUtils.isNotBlank(value)) {
				query.eq(entry.getKey(), value);
				entry.getValue().setValue(value);
			}
		}
		// 创建基本的搜索框、模块按钮和表头，并生成查询条件
//		createListBaseHtml(model, query, request, mid, view.getUri(), view.getCells(), view.getActions());
		// 获取数据
		int totalCount = query.queryForCount();
		query.page(null == page ? 0 : page, view.getSize());
		List<Map<String, Object>> list = query.queryForList();
		createTableBody(model, mid, query.getPk(), list, view.getCells(), view.getActions());
		model.addAttribute("search", search.htmlString());
		model.addAttribute("tableHead", view.getTableHeadHtml());
		model.addAttribute("uri", view.getUri() + mid + ".page");
		model.addAttribute("mid", mid);
		model.addAttribute("title", view.getTitle());
		return "template/list";
	}
}
