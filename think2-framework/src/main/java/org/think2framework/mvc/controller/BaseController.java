package org.think2framework.mvc.controller;

import org.think2framework.exception.SimpleException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 系统基础的控制器
 */
public class BaseController {

	/**
	 * 将字符串写入返回response
	 *
	 * @param response
	 *            返回对象
	 * @param value
	 *            写入值
	 */
	protected static void writeResponse(HttpServletResponse response, String value) {
		writeResponse(response, value, "utf-8");
	}

	/**
	 * 将字符串写入返回response
	 *
	 * @param response
	 *            返回对象
	 * @param value
	 *            写入值
	 * @param encoding
	 *            编码
	 */
	protected static void writeResponse(HttpServletResponse response, String value, String encoding) {
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");// 允许跨域
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Expires", "0");
			response.setHeader("Pragma", "No-cache");
			byte[] bytes = value.getBytes(encoding);
			response.setCharacterEncoding(encoding);
			response.setContentType("text/plain");
			response.setContentLength(bytes.length);
			response.getOutputStream().write(bytes);
		} catch (IOException e) {
			throw new SimpleException(e);
		}
	}

}
