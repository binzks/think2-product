package org.think2framework.security;

import org.think2framework.utils.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class QuickSessionListener implements HttpSessionListener {

	private static final Logger logger = LogManager.getLogger(QuickSessionListener.class);

	@Override
	public void sessionCreated(HttpSessionEvent httpSessionEvent) {
		logger.debug("session create");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
		HttpSession session = httpSessionEvent.getSession();
		String code = StringUtils.toString(session.getAttribute("code"));
		String name = StringUtils.toString(session.getAttribute("name"));
		if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(name)) {
			logger.info("系统管理员{},{}注销成功！", code, name);
		}
	}
}
