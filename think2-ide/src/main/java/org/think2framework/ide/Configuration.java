package org.think2framework.ide;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.think2framework.context.ModelFactory;
import org.think2framework.orm.OrmFactory;
import org.think2framework.utils.StringUtils;

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
			initialized = true;
		}
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
