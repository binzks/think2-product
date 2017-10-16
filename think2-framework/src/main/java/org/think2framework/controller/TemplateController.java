package org.think2framework.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.think2framework.ModelFactory;
import org.think2framework.orm.Query;
import org.think2framework.security.SessionHelp;
import org.think2framework.utils.StringUtils;
import org.think2framework.view.HtmlTag;
import org.think2framework.view.View;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 模板控制器
 */
@Controller
@RequestMapping(value = "/tpl")
public class TemplateController extends BaseController {

	@RequestMapping(value = { "/list{mid}{page}", "/list{mid}-{page}" })
	public String list(@PathVariable String mid, @PathVariable Integer page, Model model, HttpServletRequest request) {
		View view = SessionHelp.getView(mid, request.getSession());
		Query query = ModelFactory.createQuery(view.getName());
		Map<String, HtmlTag> searchItems = view.getSearchHtmlTags();
		for (Map.Entry<String, HtmlTag> entry : searchItems.entrySet()) {
			String value = request.getParameter(entry.getKey());
			if (StringUtils.isNotBlank(value)) {
				query.eq(entry.getKey(), value);
				entry.getValue().setValue(value);
			}
		}
		// 获取数据
		int totalCount = query.queryForCount();
		int currentPage = null == page ? 1 : page;
		query.page(currentPage, view.getSize());
		List<Map<String, Object>> list = query.queryForList();
		model.addAttribute("actions", view.getSearchActionHtml());
		model.addAttribute("search", view.getSearchHtml());
		model.addAttribute("head", view.getTableHeadHtml());
		model.addAttribute("body", view.createTableBodyHtml(list));
		model.addAttribute("page", view.createPageHtml(currentPage, totalCount));
		model.addAttribute("uri", view.getUri() + mid + ".page");
		model.addAttribute("mid", mid);
		model.addAttribute("title", view.getTitle());
		return "template/list";
	}

	@RequestMapping(value = "/add{mid}")
	public String add(@PathVariable String mid, Model model, HttpServletRequest request) {
		View view = SessionHelp.getView(mid, request.getSession());
		// model.addAttribute("body", view.);
		model.addAttribute("body", view.getAddHtml());
		model.addAttribute("uri", view.getUri() + mid + ".page");
		model.addAttribute("mid", mid);
		model.addAttribute("title", view.getTitle());
		return "template/add";
	}
}
