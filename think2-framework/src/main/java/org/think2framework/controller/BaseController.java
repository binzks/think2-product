package org.think2framework.controller;

import org.think2framework.view.bean.Action;
import org.think2framework.view.bean.Cell;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

/**
 * 系统基础的控制器
 */
public class BaseController {

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

//	@RequestMapping(value = { "/list{mid}{page}", "/list{mid}-{page}" })
//	public String getList(@PathVariable String mid, @PathVariable Integer page, Model model,
//			HttpServletRequest request) {
//		View view = SessionHelp.getView(mid, request.getSession());
//		Query query = ModelFactory.createQuery(view.getName());
//		Map<String, HtmlTag> searchItems = view.getSearchItems();
//		for (Map.Entry<String, HtmlTag> entry : searchItems.entrySet()) {
//			String value = request.getParameter(entry.getKey());
//			if (StringUtils.isNotBlank(value)) {
//				query.eq(entry.getKey(), value);
//				entry.getValue().setValue(value);
//			}
//		}
//		// 获取数据
//		int totalCount = query.queryForCount();
//		int currentPage = null == page ? 1 : page;
//		query.page(currentPage, view.getSize());
//		List<Map<String, Object>> list = query.queryForList();
//		createTableBody(model, mid, query.getPk(), list, view.getCells(), view.getActions());
//		model.addAttribute("searchHead", view.getSearchHeadHtml());
//		model.addAttribute("searchBody", view.getSearchBodyHtml());
//		model.addAttribute("tableHead", view.getTableHeadHtml());
//		model.addAttribute("tableBody", view.createTableBodyHtml(list));
//		model.addAttribute("page", view.createPageHtml(currentPage, totalCount));
//		model.addAttribute("uri", view.getUri() + mid + ".page");
//		model.addAttribute("mid", mid);
//		model.addAttribute("title", view.getTitle());
//		return "template/list";
//	}
}
