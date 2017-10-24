package org.think2framework.ide.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.think2framework.mvc.bean.BaseCms;
import org.think2framework.mvc.view.persistence.Item;
import org.think2framework.orm.core.TypeUtils;
import org.think2framework.orm.persistence.Column;
import org.think2framework.orm.persistence.Table;
import org.think2framework.mvc.view.persistence.Cell;

/**
 * 数据源 支持mysql sqlserver sqlite oracle redis
 */
@Table(name = "think2_ide_datasource", uniques = "name", comment = "系统数据源")
public class Datasource extends BaseCms {

	@JsonIgnore
	@Column(name = "project_id", nullable = false, length = 11, comment = "项目id")
	@Cell(name = "project_id", title = "项目", tag = TypeUtils.FIELD_DATA_ITEM_INT, search = true)
	@Item(model = "org.think2framework.ide.bean.Project", key = "id", value = "name")
	private Integer projectId; // 项目id

	@Column(comment = "数据源名称")
	@Cell(title = "名称")
	private String name; // 数据源名称

	@Column(nullable = false, length = 20, comment = "数据库类型")
	@Cell(title = "数据库类型", tag = TypeUtils.FIELD_ITEM)
	@Item(key = TypeUtils.DATABASE_MYSQL, value = TypeUtils.DATABASE_MYSQL)
	@Item(key = TypeUtils.DATABASE_REDIS, value = TypeUtils.DATABASE_REDIS)
	@Item(key = TypeUtils.DATABASE_SQLITE, value = TypeUtils.DATABASE_SQLITE)
	@Item(key = TypeUtils.DATABASE_ORACLE, value = TypeUtils.DATABASE_ORACLE)
	@Item(key = TypeUtils.DATABASE_SQLSERVER, value = TypeUtils.DATABASE_SQLSERVER)
	private String type; // 数据库类型

	@Column(nullable = false, length = 3, defaultValue = "1", comment = "数据源最小空闲连接")
	@Cell(title = "最小空闲连接")
	private Integer minIdle = 1; // 数据源最小空闲连接

	@Column(nullable = false, length = 3, defaultValue = "2", comment = "数据源最大空闲连接")
	@Cell(title = "最大空闲连接")
	private Integer maxIdle = 2; // 数据源最大空闲连接

	@Column(nullable = false, length = 3, defaultValue = "2", comment = "数据源初始化连接数")
	@Cell(title = "初始化连接数")
	private Integer initialSize = 2; // 数据源初始化连接数

	@Column(nullable = false, length = 5, defaultValue = "300", comment = "数据源超时时间(以秒数为单位)")
	@Cell(title = "超时时间")
	private Integer timeout = 300; // 数据源超时时间(以秒数为单位)

	@Column(nullable = false, comment = "数据库名称")
	@Cell(title = "数据库名称")
	private String db; // 数据库名称

	@Column(nullable = false, length = 100, comment = "数据库地址")
	@Cell(title = "地址")
	private String host; // 数据库地址

	@Column(length = 10, comment = "数据库端口")
	@Cell(title = "端口")
	private Integer port; // 数据库端口

	@Column(comment = "数据库用户名")
	@Cell(title = "用户名")
	private String username; // 数据库用户名

	@Column(comment = "数据库密码")
	@Cell(title = "密码")
	private String password; // 数据库密码

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(Integer minIdle) {
		this.minIdle = minIdle;
	}

	public Integer getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(Integer maxIdle) {
		this.maxIdle = maxIdle;
	}

	public Integer getInitialSize() {
		return initialSize;
	}

	public void setInitialSize(Integer initialSize) {
		this.initialSize = initialSize;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
