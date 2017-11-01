package org.think2framework.mvc.bean;

import org.think2framework.orm.core.TypeUtils;
import org.think2framework.orm.persistence.Column;
import org.think2framework.mvc.security.SessionHelp;
import org.think2framework.mvc.view.persistence.Action;
import org.think2framework.mvc.view.persistence.Cell;
import org.think2framework.mvc.view.persistence.Item;
import org.think2framework.mvc.view.persistence.View;

/**
 * cms表的基础字段
 */
@View
@Action(name = "add", title = "添加", type = "0", href = "/tpl/add", clazz = "purple ace-icon fa fa-plus-circle bigger-130")
@Action(name = "detail", title = "详情", href = "/tpl/detail", clazz = "ace-icon fa fa-search-plus bigger-130")
@Action(name = "edit", title = "修改", href = "/tpl/edit", clazz = "green ace-icon fa fa-pencil bigger-130")
public class BaseCms {

	@Column(nullable = false, length = 11, comment = "主键")
	@Cell(title = "主键", required = true, display = false, add = false, edit = false)
	private Integer id;

	@Column(nullable = false, length = 1, defaultValue = "0", comment = "状态0-启用 99-停用")
	@Cell(title = "状态", required = true, search = true, tag = TypeUtils.FIELD_ITEM_INT, defaultValue = "0")
	@Item(key = "0", value = "启用")
	@Item(key = "99", value = "停用")
	private Integer status;

	@Column(name = "modify_time", length = 10, comment = "最后修改时间")
	@Cell(name = "modify_time", title = "最后修改时间", add = false, edit = false, tag = TypeUtils.FIELD_TIMESTAMP, defaultValue = SessionHelp.DEFAULT_NOW)
	private Integer modifyTime;

	@Column(name = "modify_admin", comment = "最后修改人")
	@Cell(name = "modify_admin", title = "最后修改人", add = false, edit = false, defaultValue = SessionHelp.DEFAULT_LOGIN_CODE)
	private String modifyAdmin;

	@Column(comment = "备注", length = 500)
	@Cell(title = "备注", tag = TypeUtils.FIELD_TEXTAREA, display = false)
	private String remark;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Integer modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getModifyAdmin() {
		return modifyAdmin;
	}

	public void setModifyAdmin(String modifyAdmin) {
		this.modifyAdmin = modifyAdmin;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
