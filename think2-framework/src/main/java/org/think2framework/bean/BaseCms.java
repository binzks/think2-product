package org.think2framework.bean;

import org.think2framework.orm.persistence.Column;
import org.think2framework.view.persistence.Action;
import org.think2framework.view.persistence.View;

/**
 * cms表的基础字段
 */
@View
@Action(name = "add", title = "添加", type = "0", href = "/tpl/add", clazz = "purple ace-icon fa fa-plus-circle bigger-130")
@Action(name = "detail", title = "详情", href = "/tpl/detail", clazz = "ace-icon fa fa-search-plus bigger-130")
@Action(name = "edit", title = "修改", href = "/tpl/edit", clazz = "green ace-icon fa fa-pencil bigger-130")
public class BaseCms {

	@Column(nullable = false, length = 11, comment = "主键")
	private Integer id;

	@Column(nullable = false, length = 1, defaultValue = "99", comment = "状态0-启用 99-停用")
	private Integer status;

	@Column(name = "modify_time", nullable = false, length = 10, comment = "最后修改时间")
	private Integer modifyTime;

	@Column(name = "modify_admin", nullable = false, comment = "最后修改人")
	private String modifyAdmin;

	@Column(comment = "备注", length = 500)
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
