package org.think2framework.ide;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.think2framework.context.ModelFactory;
import org.think2framework.ide.bean.Datasource;
import org.think2framework.ide.bean.Project;
import org.think2framework.mvc.bean.Admin;
import org.think2framework.mvc.bean.AdminPower;
import org.think2framework.mvc.bean.Module;
import org.think2framework.orm.OrmFactory;
import org.think2framework.orm.Writer;
import org.think2framework.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Configuration implements ApplicationContextAware {

	private String name; // 数据源名称

	private String type; // 数据库类型

	private Integer minIdle = 1; // 数据源最小空闲连接

	private Integer maxIdle = 2; // 数据源最大空闲连接

	private Integer initialSize = 2; // 数据源初始化连接数

	private Integer timeout = 300; // 数据源超时时间(以秒数为单位)

	private String db; // 数据库名称

	private String host; // 数据库地址

	private Integer port = 3306; // 数据库端口

	private String username; // 数据库用户名

	private String password; // 数据库密码

	private String packages; // 自动扫描的包

	private static Boolean initialized = false; // 是否已经初始化过setApplicationContext会加载多次，所以判断

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (!initialized) {
			System.setProperty("jsse.enableSNIExtension", "false");
			OrmFactory.appendDatabase(type, name, minIdle, maxIdle, initialSize, timeout, db, host, port, username,
					password);
			if (StringUtils.isNotBlank(packages)) {
				ModelFactory.scanPackages(name, name, null, 0, StringUtils.split(packages, ","));
			}
			initSystem();
			initIDE();
			initialized = true;
		}
	}

	/**
	 * 初始化系统信息，创建表和基础数据
	 */
	private void initSystem() {
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
			// modules.add(new Module("system_model", 1, Module.TYPE_MODULE, "",
			// Model.class.getName(), "模型管理",
			// "/tpl/list", 10, 1));
			modules.add(new Module("ide", 0, Module.TYPE_GROUP, "fa-windows", "", "IDE", "", 10, 2));
			modules.add(new Module("ide_project", 6, Module.TYPE_MODULE, "", Project.class.getName(), "项目管理",
					"/tpl/list", 10, 1));
			modules.add(new Module("ide_project_datasource", 6, Module.TYPE_MODULE, "", Datasource.class.getName(),
					"数据源管理", "/tpl/list", 10, 2));
			moduleWriter.batchInsert(modules);
		}
		Writer adminWriter = ModelFactory.createWriter(Admin.class.getName());
		if (adminWriter.createTable()) {
			Admin admin = new Admin();
			admin.setCode("root");
			admin.setName("超级管理员");
			admin.setPassword("4ad418256efdfae2d275a9d6a8631df8");
			adminWriter.insert(admin);
		}
		Writer adminPowerWriter = ModelFactory.createWriter(AdminPower.class.getName());
		if (adminPowerWriter.createTable()) {
			List<AdminPower> adminPowers = new ArrayList<>();
			for (int i = 1; i <= modules.size(); i++) {
				adminPowers.add(new AdminPower(1, i));
			}
			adminPowerWriter.batchInsert(adminPowers);
		}
	}

	private void initIDE() {
		ModelFactory.createWriter(Project.class.getName()).createTable();
		ModelFactory.createWriter(Datasource.class.getName()).createTable();
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setMinIdle(Integer minIdle) {
		this.minIdle = minIdle;
	}

	public void setMaxIdle(Integer maxIdle) {
		this.maxIdle = maxIdle;
	}

	public void setInitialSize(Integer initialSize) {
		this.initialSize = initialSize;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public void setDb(String db) {
		this.db = db;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPackages(String packages) {
		this.packages = packages;
	}

}
