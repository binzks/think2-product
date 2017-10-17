package org.think2framework.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.think2framework.ModelFactory;
import org.think2framework.orm.Query;
import org.think2framework.orm.Writer;
import org.think2framework.security.SessionHelp;
import org.think2framework.utils.StringUtils;
import org.think2framework.view.HtmlTag;
import org.think2framework.view.View;
import org.think2framework.view.bean.Cell;
import org.think2framework.view.core.SelectTag;

import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
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
		model.addAttribute("title", view.getTitle());
		return "template/list";
	}

	@RequestMapping(value = "/add{mid}")
	public String add(@PathVariable String mid, Model model, HttpServletRequest request) {
		View view = SessionHelp.getView(mid, request.getSession());
		model.addAttribute("body", view.getAddHtml());
		model.addAttribute("mid", mid);
		model.addAttribute("title", view.getTitle());
		return "template/add";
	}

	@RequestMapping(value = { "save{mid}{id}", "/save{mid}-{id}" }, method = RequestMethod.POST)
	public String save(@PathVariable String mid, @PathVariable String id, Model model, HttpServletRequest request) {
		View view = SessionHelp.getView(mid, request.getSession());
		Writer writer = ModelFactory.createWriter(view.getName());
		Map<String, Object> map = new HashMap<>();
		// 设置默认值
		for (Map.Entry<String, String> entry : view.getDefaultValues().entrySet()) {
			String value = SessionHelp.getDefaultValue(entry.getValue(), request.getSession());
			map.put(entry.getKey(), value);
		}
		// 新增
		if (StringUtils.isBlank(id)) {
			// 设置添加的字段
			for (Map.Entry<String, HtmlTag> entry : view.getAddHtmlTags().entrySet()) {
				map.put(entry.getKey(), request.getParameter(entry.getKey()));
			}
			writer.insert(map);
		} else {// 修改
			// 设置修改的字段
			for (Map.Entry<String, HtmlTag> entry : view.getEditHtmlTags().entrySet()) {
				map.put(entry.getKey(), request.getParameter(entry.getKey()));
			}
			writer.update(map);
		}
		model.addAttribute("body", view.getAddHtml());
		model.addAttribute("uri", view.getUri() + mid + ".page");
		model.addAttribute("title", view.getTitle());
		return "redirect:" + view.getUri() + mid + ".page";
	}
}
