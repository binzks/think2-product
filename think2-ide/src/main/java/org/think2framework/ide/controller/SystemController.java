package org.think2framework.ide.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.think2framework.context.ModelFactory;
import org.think2framework.ide.bean.*;
import org.think2framework.mvc.MvcSupport;
import org.think2framework.mvc.bean.AdminPower;
import org.think2framework.mvc.bean.Module;
import org.think2framework.mvc.controller.BaseController;
import org.think2framework.orm.Writer;
import org.think2framework.utils.NumberUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/ide")
public class SystemController extends BaseController {

	@RequestMapping(value = "/db/ct.api")
	public void reloadDatabases(HttpServletRequest request, HttpServletResponse response) {
		String name = request.getParameter("m");
		ModelFactory.createWriter(name).createTable();
		writeResponse(response, "success");
	}

	@RequestMapping(value = "/install.api")
	public void install(HttpServletResponse response) throws IOException {
		MvcSupport.install();
		// 创建ide所需的表
		ModelFactory.createWriter(Project.class.getName()).createTable();
		ModelFactory.createWriter(Version.class.getName()).createTable();
		ModelFactory.createWriter(Datasource.class.getName()).createTable();
		ModelFactory.createWriter(Environment.class.getName()).createTable();
		// 添加ide模块
		Writer moduleWriter = ModelFactory.createWriter(Module.class.getName());
		int environmentId = NumberUtils.toInt(moduleWriter
				.insert(new Module("ide_environment", 0, Module.TYPE_GROUP, "fa-windows", "", "系统环境", "", 10, 2)));
		int modelId = NumberUtils.toInt(moduleWriter
				.insert(new Module("ide_model", 0, Module.TYPE_GROUP, "fa-android", "", "模型管理", "", 10, 3)));
		int projectId = NumberUtils.toInt(moduleWriter
				.insert(new Module("ide_project", 0, Module.TYPE_GROUP, "fa-apple", "", "项目管理", "", 10, 4)));
		moduleWriter.insert(new Module("ide_datasource", environmentId, Module.TYPE_MODULE, "",
				Datasource.class.getName(), "数据源管理", "/tpl/list", 10, 1));
		moduleWriter.insert(new Module("ide_environment_info", environmentId, Module.TYPE_MODULE, "",
				Environment.class.getName(), "环境管理", "/tpl/list", 10, 2));
		moduleWriter.insert(new Module("ide_model_info", modelId, Module.TYPE_MODULE, "", Model.class.getName(), "模型信息",
				"/tpl/list", 10, 1));
		moduleWriter.insert(new Module("ide_project_info", projectId, Module.TYPE_MODULE, "", Project.class.getName(),
				"项目信息", "/tpl/list", 10, 1));
		// 添加root账号需要的基本权限
		Writer adminPowerWriter = ModelFactory.createWriter(AdminPower.class.getName());
		List<AdminPower> adminPowers = new ArrayList<>();
		for (int i = 0; i <= 6; i++) {
			adminPowers.add(new AdminPower(1, environmentId + i));
		}
		adminPowerWriter.batchInsert(adminPowers);
		writeResponse(response, "IDE install success");
	}
	//
	// @RequestMapping(value = "/reloadDatabase.api")
	// public void reloadDatabases(HttpServletRequest request, HttpServletResponse
	// response) {
	// ModelFactory.reloadDatabases();
	// HttpServletUtils.writeResponse(response, "success");
	// }
	//
	// @RequestMapping(value = "/reloadModels.api")
	// public void reloadModels(HttpServletRequest request, HttpServletResponse
	// response) {
	// ModelFactory.reloadModels();
	// HttpServletUtils.writeResponse(response, "success");
	// }

	// @RequestMapping(value = "/reloadConst.api")
	// public void reloadConstant(HttpServletRequest request, HttpServletResponse
	// response) {
	// ConstantFactory.reload();
	// HttpServletUtils.writeResponse(response, "success");
	// }
	//
	// @RequestMapping(value = "/reloadMessages.api")
	// public void reloadMessage(HttpServletRequest request, HttpServletResponse
	// response) {
	// MessageFactory.reload();
	// HttpServletUtils.writeResponse(response, "success");
	// }

}
