package org.think2framework.bean;

import org.think2framework.orm.core.ClassUtils;
import org.think2framework.orm.persistence.Column;

/**
 * cms表的基础字段
 */
public class BaseCms {

	@Column(type = ClassUtils.TYPE_INTEGER, nullable = false, length = 11, comment = "主键")
	private Integer id;

	@Column(type = ClassUtils.TYPE_INTEGER, nullable = false, length = 1, comment = "状态0-启用 1-停用")
	private Integer status;

	@Column(name = "modify_time", type = ClassUtils.TYPE_INTEGER, nullable = false, length = 10, comment = "最后修改时间")
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
