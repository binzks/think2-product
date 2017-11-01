package org.think2framework.ide.bean;

import org.think2framework.mvc.bean.BaseCms;
import org.think2framework.mvc.view.persistence.Cell;
import org.think2framework.mvc.view.persistence.Item;
import org.think2framework.orm.core.TypeUtils;
import org.think2framework.orm.persistence.Column;
import org.think2framework.orm.persistence.Table;

@Table(name = "think2_ide_environment", uniques = { "name" }, comment = "系统环境表")
public class Environment extends BaseCms {

	@Column(comment = "环境名称")
	@Cell(title = "环境名称", required = true)
	private String name;

	@Column(comment = "环境对应的服务器地址")
	@Cell(title = "服务器地址")
	private String host;

	@Column(comment = "服务器部署根目录", length = 200)
	@Cell(title = "根目录")
	private String directory;

	@Column(name = "datasource_id", comment = "数据源id", nullable = false, length = 11)
	@Cell(name = "datasource_id", title = "数据源", required = true, tag = TypeUtils.FIELD_DATA_MULTIPLE)
	@Item(model = "org.think2framework.ide.bean.Datasource", key = "id", value = "name")
	private String dsId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getDsId() {
		return dsId;
	}

	public void setDsId(String dsId) {
		this.dsId = dsId;
	}
}
