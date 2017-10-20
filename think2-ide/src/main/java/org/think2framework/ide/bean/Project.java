package org.think2framework.ide.bean;

import org.think2framework.mvc.bean.BaseCms;
import org.think2framework.orm.persistence.Column;
import org.think2framework.orm.persistence.Table;
import org.think2framework.mvc.view.persistence.Cell;

/**
 * 项目
 */
@Table(name = "think2_ide_project", comment = "项目表")
public class Project extends BaseCms {

	@Column(nullable = false, comment = "项目名称")
	@Cell(title = "项目名称")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
