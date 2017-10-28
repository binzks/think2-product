package org.think2framework.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.think2framework.context.ModelFactory;
import org.think2framework.orm.Query;
import org.think2framework.orm.Writer;
import org.think2framework.mvc.security.SessionHelp;
import org.think2framework.utils.StringUtils;
import org.think2framework.mvc.view.HtmlTag;
import org.think2framework.mvc.view.View;
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
			} else {
				entry.getValue().setValue("");
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

	/**
	 * 设置默认值
	 * 
	 * @param session
	 *            session获取用户信息
	 * @param defaults
	 *            默认值列表
	 * @param tags
	 *            设置标签
	 */
	private void setDefault(HttpSession session, Map<String, String> defaults, Map<String, HtmlTag> tags) {
		for (Map.Entry<String, String> entry : defaults.entrySet()) {
			String value = SessionHelp.getDefaultValue(entry.getValue(), session);
			HtmlTag htmlTag = tags.get(entry.getKey());
			if (null != htmlTag) {
				htmlTag.setValue(value);
			}
		}
	}

	@RequestMapping(value = "/add{mid}")
	public String add(@PathVariable String mid, Model model, HttpServletRequest request) {
		View view = SessionHelp.getView(mid, request.getSession());
		// 设置默认值
		Map<String, String> defaults = view.getDefaultValues();
		for (Map.Entry<String, HtmlTag> entry : view.getAddHtmlTags().entrySet()) {
			String defaultValue = defaults.get(entry.getKey());
			if (StringUtils.isNotBlank(defaultValue)) {
				entry.getValue().setValue(SessionHelp.getDefaultValue(defaultValue, request.getSession()));
			} else {
				entry.getValue().setValue("");
			}
		}
		model.addAttribute("body", view.getAddHtml());
		model.addAttribute("mid", mid);
		model.addAttribute("title", view.getTitle());
		return "template/add";
	}

	@RequestMapping(value = "/edit{mid}-{id}")
	public String edit(@PathVariable String mid, @PathVariable String id, Model model, HttpServletRequest request) {
		View view = SessionHelp.getView(mid, request.getSession());
		Query query = ModelFactory.createQuery(view.getName());
		query.eq(id);
		Map<String, Object> map = query.queryForMap();
		// 设置默认值
		Map<String, String> defaults = view.getDefaultValues();
		for (Map.Entry<String, HtmlTag> entry : view.getEditHtmlTags().entrySet()) {
			String value = StringUtils.toString(map.get(entry.getKey()));
			if (StringUtils.isNotBlank(value)) {
				entry.getValue().setValue(value);
			} else {
				String defaultValue = defaults.get(entry.getKey());
				if (StringUtils.isNotBlank(defaultValue)) {
					entry.getValue().setValue(SessionHelp.getDefaultValue(defaultValue, request.getSession()));
				} else {
					entry.getValue().setValue(value);
				}
			}
		}
		model.addAttribute("body", view.getEditHtml());
		model.addAttribute("mid", mid);
		model.addAttribute("id", id);
		model.addAttribute("title", view.getTitle());
		return "template/edit";
	}

	@RequestMapping(value = { "save{mid}{id}", "/save{mid}-{id}" }, method = RequestMethod.POST)
	public String save(@PathVariable String mid, @PathVariable String id, HttpServletRequest request) {
		View view = SessionHelp.getView(mid, request.getSession());
		Writer writer = ModelFactory.createWriter(view.getName());
		Map<String, Object> map = new HashMap<>();
		// 设置默认值
		for (Map.Entry<String, String> entry : view.getDefaultValues().entrySet()) {
			String value = SessionHelp.getDefaultValue(entry.getValue(), request.getSession());
			map.put(entry.getKey(), value);
		}
		// 设置字段
		for (Map.Entry<String, HtmlTag> entry : view.getAddHtmlTags().entrySet()) {
			HtmlTag htmlTag = entry.getValue();
			htmlTag.setValue(request.getParameter(entry.getKey()));
			map.put(entry.getKey(), htmlTag.getValue());
		}
		// 新增
		if (StringUtils.isBlank(id)) {
			writer.insert(map);
		} else {// 修改
			map.put(view.getPk(), id);
			writer.update(map);
		}
		return "redirect:" + view.getUri() + mid + ".page";
	}
}
