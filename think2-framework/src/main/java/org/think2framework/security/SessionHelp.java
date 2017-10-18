package org.think2framework.security;

import org.think2framework.ModelFactory;
import org.think2framework.bean.AdminPower;
import org.think2framework.bean.Module;
import org.think2framework.exception.NonsupportException;
import org.think2framework.exception.SimpleException;
import org.think2framework.utils.StringUtils;
import org.think2framework.view.HtmlTag;
import org.think2framework.view.View;
import org.think2framework.view.core.ATag;
import org.think2framework.view.core.SimpleHtmlTag;

import javax.servlet.http.HttpSession;
import java.util.List;

public class SessionHelp {

	/**
	 * 根据session判断是否已经登录的用户
	 * 
	 * @param session
	 *            请求session
	 * @return 是否登录
	 */
	public static Boolean isLogin(HttpSession session) {
		if (null == session || null == session.getAttribute("code")) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 根据权限初始化登录管理员的菜单和权限
	 * 
	 * @param session
	 *            管理员session
	 * @param id
	 *            管理员id
	 * @param code
	 *            管理员编号
	 * @param name
	 *            管理员姓名
	 * @param powers
	 *            管理员权限
	 */
	public static void initLogin(HttpSession session, String id, String code, String name, List<AdminPower> powers) {
		HtmlTag ul = new SimpleHtmlTag("ul", "nav nav-list");
		initMenu(session, powers, ul, 0);
		session.setAttribute("menus", ul.htmlString());
		session.setAttribute("id", id);
		session.setAttribute("code", code);
		session.setAttribute("name", name);
	}

	/**
	 * 初始化模块下的菜单
	 * 
	 * @param session
	 *            设置模块权限到session
	 * 
	 * @param powers
	 *            有权限的模块
	 * @param parent
	 *            父节点
	 * @param parentId
	 *            父节点id
	 */
	private static void initMenu(HttpSession session, List<AdminPower> powers, HtmlTag parent, Integer parentId) {
		for (AdminPower adminPower : powers) {
			if (parentId == adminPower.getModuleParentId()) {
				Integer moduleId = adminPower.getModuleId();
				if (Module.TYPE_GROUP == adminPower.getModuleType()) { // 模块组
					HtmlTag liTag = createGroupTag(adminPower.getModuleTitle(), adminPower.getModuleIcon());
					HtmlTag ulTag = new SimpleHtmlTag("ul", "submenu");
					liTag.appendChild(ulTag);
					initMenu(session, powers, ulTag, moduleId);
					parent.appendChild(liTag);
				} else if (Module.TYPE_MODULE == adminPower.getModuleType()) { // 模块
					parent.appendChild(
							createModuleTag(moduleId, adminPower.getModuleUri(), adminPower.getModuleTitle()));
					session.setAttribute("module_" + moduleId,
							ModelFactory.createView(adminPower.getModuleModel(), adminPower.getModuleTitle(),
									StringUtils.toString(moduleId), adminPower.getModuleUri(),
									adminPower.getModuleSize(), adminPower.getColumns(), adminPower.getActions()));
				} else {
					throw new NonsupportException("模块类型" + adminPower.getModuleType());
				}
			}
		}
	}

	/**
	 * 创建一个模块组菜单，不包含子菜单
	 * 
	 * @param name
	 *            模块组名称
	 * @param icon
	 *            模块组图标
	 * @return 模块组li标签
	 */
	private static HtmlTag createGroupTag(String name, String icon) {
		HtmlTag aTag = new ATag("#", "dropdown-toggle", null);
		aTag.appendChild(new SimpleHtmlTag("i", "menu-icon fa " + icon));
		aTag.appendChild(new SimpleHtmlTag("span", "menu-text", name));
		aTag.appendChild(new SimpleHtmlTag("b", "arrow fa fa-angle-down"));
		HtmlTag liTag = new SimpleHtmlTag("li");
		liTag.appendChild(aTag);
		liTag.appendChild(new SimpleHtmlTag("b", "arrow"));
		return liTag;
	}

	/**
	 * 生出一个模块菜单
	 * 
	 * @param id
	 *            模块id
	 * @param uri
	 *            模块uri
	 * @param name
	 *            模块名称
	 * @return 模块li标签
	 */
	private static HtmlTag createModuleTag(Integer id, String uri, String name) {
		HtmlTag liTag = new SimpleHtmlTag("li");
		liTag.setAttribute("id", "menu_" + uri + id);
		liTag.appendChild(new ATag(uri + id + ".page", "", name, "menu-icon fa fa-caret-right"));
		liTag.appendChild(new SimpleHtmlTag("b", "arrow"));
		return liTag;
	}

	/**
	 * 根据模块id获取session中模块的视图
	 * 
	 * @param id
	 *            模块id
	 * @param session
	 *            用户session
	 * @return 用户模块权限
	 */
	public static View getView(String id, HttpSession session) {
		View view = (View) session.getAttribute("module_" + id);
		if (null == view) {
			throw new SimpleException("管理员尚未获取模块" + id + "授权！");
		}
		return view;
	}

	public static final String DEFAULT_NOW = "now"; // 默认值-当前时间戳

	public static final String DEFAULT_LOGIN_ID = "loginId"; // 默认值-登录用户id

	public static final String DEFAULT_LOGIN_CODE = "loginCode"; // 默认值-登录用户编号

	public static final String DEFAULT_LOGIN_NAME = "loginName"; // 默认值-登录用户姓名

	/**
	 * 获取默认值，如果key是自定义的类型则获取相应值，否则返回key
	 * 
	 * @param key
	 *            默认值key
	 * @param session
	 *            session用户获取登录信息
	 * @return 默认值
	 */
	public static String getDefaultValue(String key, HttpSession session) {
		if (DEFAULT_NOW.equalsIgnoreCase(key)) {
			return StringUtils.toString(System.currentTimeMillis() / 1000);
		} else if (DEFAULT_LOGIN_ID.equalsIgnoreCase(key)) {
			return StringUtils.toString(session.getAttribute("id"));
		} else if (DEFAULT_LOGIN_CODE.equalsIgnoreCase(key)) {
			return StringUtils.toString(session.getAttribute("code"));
		} else if (DEFAULT_LOGIN_NAME.equalsIgnoreCase(key)) {
			return StringUtils.toString(session.getAttribute("name"));
		} else {
			return key;
		}
	}
}
