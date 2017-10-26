package org.think2framework.ide.bean;

import org.think2framework.mvc.bean.BaseCms;
import org.think2framework.mvc.view.persistence.Cell;
import org.think2framework.orm.persistence.Column;
import org.think2framework.orm.persistence.Table;

@Table(name = "think2_ide_version", comment = "版本控制")
public class Version extends BaseCms {

	@Column(comment = "版本号")
	@Cell(title = "版本号")
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
