package org.think2framework.mvc;

import org.think2framework.context.ModelFactory;
import org.think2framework.mvc.bean.Admin;
import org.think2framework.mvc.bean.AdminPower;
import org.think2framework.mvc.bean.Module;
import org.think2framework.orm.Writer;

import java.util.ArrayList;
import java.util.List;

public class MvcSupport {

	/**
	 * 安装mvc，创建需要的表，设置基本数据
	 */
	public static synchronized void install() {
		// 创建系统模块表，并添加基础模块
		Writer moduleWriter = ModelFactory.createWriter(Module.class.getName());
		List<Module> modules = new ArrayList<>();
		if (moduleWriter.createTable()) {
			modules.add(new Module("system", 0, Module.TYPE_GROUP, "fa-cog", "", "系统管理", "", 10, 1));
			modules.add(new Module("system_module", 1, Module.TYPE_MODULE, "", Module.class.getName(), "模块管理",
					"/tpl/list", 10, 1));
			modules.add(new Module("system_admin", 1, Module.TYPE_GROUP, "", "", "管理员管理", "", 10, 1));
			modules.add(new Module("system_admin_info", 3, Module.TYPE_MODULE, "", Admin.class.getName(), "管理员信息",
					"/tpl/list", 10, 1));
			modules.add(new Module("system_admin_power", 3, Module.TYPE_MODULE, "", AdminPower.class.getName(), "管理员权限",
					"/tpl/list", 10, 2));
			moduleWriter.batchInsert(modules);
		}
		// 添加root账号
		Writer adminWriter = ModelFactory.createWriter(Admin.class.getName());
		if (adminWriter.createTable()) {
			Admin admin = new Admin();
			admin.setCode("root");
			admin.setName("超级管理员");
			admin.setPassword("4ad418256efdfae2d275a9d6a8631df8");
			adminWriter.insert(admin);
		}
		// 添加root账号需要的基本全新
		Writer adminPowerWriter = ModelFactory.createWriter(AdminPower.class.getName());
		if (adminPowerWriter.createTable()) {
			List<AdminPower> adminPowers = new ArrayList<>();
			for (int i = 1; i <= modules.size(); i++) {
				adminPowers.add(new AdminPower(1, i));
			}
			adminPowerWriter.batchInsert(adminPowers);
		}
	}
}
