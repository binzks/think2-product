package org.think2framework.controller;

import org.think2framework.ConstantFactory;
import org.think2framework.MessageFactory;
import org.think2framework.ModelFactory;
import org.think2framework.orm.Writer;
import org.think2framework.utils.HttpServletUtils;
import org.think2framework.utils.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/quick/api")
public class ApiController {

	@RequestMapping(value = "/ct.api")
	public void createTable(HttpServletRequest request, HttpServletResponse response) {
		String table = StringUtils.toString(request.getParameter("t"));
		Writer writer = ModelFactory.createWriter(table);
		writer.createTable();
		HttpServletUtils.writeResponse(response, "success");
	}
//
//	@RequestMapping(value = "/reloadDatabase.api")
//	public void reloadDatabases(HttpServletRequest request, HttpServletResponse response) {
//		ModelFactory.reloadDatabases();
//		HttpServletUtils.writeResponse(response, "success");
//	}
//
//	@RequestMapping(value = "/reloadModels.api")
//	public void reloadModels(HttpServletRequest request, HttpServletResponse response) {
//		ModelFactory.reloadModels();
//		HttpServletUtils.writeResponse(response, "success");
//	}

	@RequestMapping(value = "/reloadConst.api")
	public void reloadConstant(HttpServletRequest request, HttpServletResponse response) {
		ConstantFactory.reload();
		HttpServletUtils.writeResponse(response, "success");
	}

	@RequestMapping(value = "/reloadMessages.api")
	public void reloadMessage(HttpServletRequest request, HttpServletResponse response) {
		MessageFactory.reload();
		HttpServletUtils.writeResponse(response, "success");
	}

}
