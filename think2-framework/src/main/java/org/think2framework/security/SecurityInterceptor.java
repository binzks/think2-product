package org.think2framework.security;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Repository
public class SecurityInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 用户是否登录
		if (SessionHelp.isLogin(request.getSession())) {
			return true;
		} else {
			response.sendRedirect("/think2/admin/welcome.do");
//			if ("/".equals(request.getRequestURI()) || "/think2/admin/welcome.do".equals(request.getRequestURI())) {
//				return true;
//			} else {
//				return false;
//			}
            return false;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}