package org.think2framework.controller;

import org.think2framework.ConstantFactory;
import org.think2framework.ModelFactory;
import org.think2framework.bean.Admin;
import org.think2framework.bean.AdminPower;
import org.think2framework.orm.Query;
import org.think2framework.security.SessionHelp;
import org.think2framework.utils.EncryptUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("think2/admin")
public class AdminController {

	private static final Logger logger = LogManager.getLogger(AdminController.class);

	@RequestMapping(value = "/welcome.do")
	public ModelAndView welcome() {
		return new ModelAndView("login");
	}

	@RequestMapping(value = "/index.page")
	public ModelAndView index() {
		return new ModelAndView("index");
	}

	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public ModelAndView enter(HttpServletRequest request) {
		String code = request.getParameter("code");
		String password = request.getParameter("password");
		Query adminQuery = ModelFactory.createQuery(Admin.class.getName());
		adminQuery.eq("code", code);
		Admin admin = adminQuery.queryForObject(Admin.class);
		if (null == admin) {
			return new ModelAndView("login", "msg", "管理员不存在");
		}
		password = EncryptUtils.md5(EncryptUtils.md5(password));
		if (!password.equals(admin.getPassword())) {
			return new ModelAndView("login", "msg", "密码错误");
		}
		if (ConstantFactory.COMMON_DISABLE == admin.getStatus()) {
			return new ModelAndView("login", "msg", "管理员已停用");
		}
		if (0 == admin.getRoleId()) {
			return new ModelAndView("login", "msg", "管理员尚未分配角色");
		}
		Query adminPowerQuery = ModelFactory.createQuery(AdminPower.class.getName());
		adminPowerQuery.eq("admin_id", admin.getId());
		List<AdminPower> adminPowers = adminPowerQuery.queryForList(AdminPower.class);
		if (null == adminPowers || adminPowers.size() == 0) {
			return new ModelAndView("login", "msg", "管理员没有任何权限");
		}
		SessionHelp.initLogin(request.getSession(), code, admin.getName(), adminPowers);
		logger.info("系统管理员{}登录系统成功！", code);
		return new ModelAndView("redirect:/think2/admin/index.page");
	}

	@RequestMapping(value = "/logout.do")
	public ModelAndView quit(HttpServletRequest request) {
		request.getSession().invalidate();
		return new ModelAndView("login");
	}

}
