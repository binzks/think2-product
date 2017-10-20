package org.think2framework.mvc.bean;

import org.think2framework.orm.persistence.Column;
import org.think2framework.orm.persistence.Table;
import org.think2framework.mvc.view.persistence.Cell;

/**
 * cms系统管理员
 */
@Table(name = "think2_admin", indexes = { "status" }, uniques = { "code" }, comment = "系统管理员")
public class Admin extends BaseCms {

	@Column(nullable = false, comment = "编号")
	@Cell(title = "编号", required = true, search = true)
	private String code;

	@Column(nullable = false, comment = "姓名")
	@Cell(title = "姓名", required = true, search = true)
	private String name;

	@Column(nullable = false, comment = "密码")
	@Cell(title = "密码", required = true)
	private String password;

	@Column(comment = "手机号码")
	@Cell(title = "手机号码")
	private String mobile;

	@Column(comment = "email")
	@Cell(title = "email")
	private String email;

	public Admin() {
	}

	public Admin(String code, String name, String password) {
		this.code = code;
		this.name = name;
		this.password = password;
	}

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
}
