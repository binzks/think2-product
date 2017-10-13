package org.think2framework.bean;

import org.think2framework.orm.core.ClassUtils;
import org.think2framework.orm.persistence.Column;
import org.think2framework.orm.persistence.Table;

/**
 * cms系统管理员
 */
@Table(name = "think2_admin", indexes = { "status" }, uniques = { "code" }, comment = "系统管理员")
public class Admin extends BaseCms {

	@Column(nullable = false, comment = "编号")
	private String code;

	@Column(nullable = false, comment = "姓名")
	private String name;

	@Column(nullable = false, comment = "密码")
	private String password;

	@Column(comment = "手机号码")
	private String mobile;

	@Column(comment = "email")
	private String email;

	@Column(name = "role_id", type = ClassUtils.TYPE_INTEGER, comment = "角色id")
	private Integer roleId;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
}
