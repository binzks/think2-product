package org.think2framework.bean;

import org.think2framework.orm.persistence.Column;
import org.think2framework.orm.persistence.JoinTable;
import org.think2framework.orm.persistence.Table;

/**
 * 系统管理员权限
 */
@Table(name = "think2_admin_power", indexes = { "admin_id" }, comment = "系统管理员权限")
@JoinTable(name = "think2_module", table = "think2_module", key = "id", joinKey = "module_id")
public class AdminPower extends BaseCms {

	@Column(name = "admin_id", length = 11, comment = "管理员id")
	private Integer adminId;

	@Column(name = "module_id", length = 11, comment = "模块id")
	private Integer moduleId;

	@Column(length = 200, comment = "没有权限的列")
	private String columns;

	@Column(length = 100, comment = "没有权限的按钮")
	private String actions;

	@Column(length = 200, comment = "行级过滤")
	private String row;

	@Column(name = "parent_id", join = "think2_module", alias = "module_parent_id")
	private Integer moduleParentId;

	@Column(name = "type", join = "think2_module", alias = "module_type")
	private Integer moduleType;

	@Column(name = "icon", join = "think2_module", alias = "module_icon")
	private String moduleIcon;

	@Column(name = "uri", join = "think2_module", alias = "module_uri")
	private String moduleUri;

	@Column(name = "model", join = "think2_module", alias = "module_model")
	private String moduleModel;

	@Column(name = "title", join = "think2_module", alias = "module_title")
	private String moduleTitle;

	@Column(name = "size", join = "think2_module", alias = "module_size")
	private Integer moduleSize;

	public Integer getAdminId() {
		return adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	public Integer getModuleId() {
		return moduleId;
	}

	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}

	public String getColumns() {
		return columns;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}

	public String getActions() {
		return actions;
	}

	public void setActions(String actions) {
		this.actions = actions;
	}

	public String getRow() {
		return row;
	}

	public void setRow(String row) {
		this.row = row;
	}

	public Integer getModuleParentId() {
		return moduleParentId;
	}

	public void setModuleParentId(Integer moduleParentId) {
		this.moduleParentId = moduleParentId;
	}

	public Integer getModuleType() {
		return moduleType;
	}

	public void setModuleType(Integer moduleType) {
		this.moduleType = moduleType;
	}

	public String getModuleIcon() {
		return moduleIcon;
	}

	public void setModuleIcon(String moduleIcon) {
		this.moduleIcon = moduleIcon;
	}

	public String getModuleUri() {
		return moduleUri;
	}

	public void setModuleUri(String moduleUri) {
		this.moduleUri = moduleUri;
	}

	public String getModuleModel() {
		return moduleModel;
	}

	public void setModuleModel(String moduleModel) {
		this.moduleModel = moduleModel;
	}

	public String getModuleTitle() {
		return moduleTitle;
	}

	public void setModuleTitle(String moduleTitle) {
		this.moduleTitle = moduleTitle;
	}

	public Integer getModuleSize() {
		return moduleSize;
	}

	public void setModuleSize(Integer moduleSize) {
		this.moduleSize = moduleSize;
	}
}
