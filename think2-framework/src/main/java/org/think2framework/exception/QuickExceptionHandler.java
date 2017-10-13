package org.think2framework.exception;

import org.think2framework.MessageFactory;
import org.think2framework.security.SessionHelp;
import org.think2framework.utils.HttpServletUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                            O\ = /O
//                        ____/`---'\____
//                      .   ' \\| |// `.
//                       / \\||| : |||// \
//                     / _||||| -:- |||||- \
//                       | | \\\ - /// | |
//                     | \_| ''\---/'' | |
//                      \ .-\__ `-` ___/-. /
//                   ___`. .' /--.--\ `. . __
//                ."" '< `.___\_<|>_/___.' >'"".
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |
//                 \ \ `-. \_ __\ /__ _/ .-` / /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//
//         .............................................
//                  佛祖镇楼                  BUG辟易
//          佛曰:
//                  写字楼里写字间，写字间里程序员；
//                  程序人员写程序，又拿程序换酒钱。
//                  酒醒只在网上坐，酒醉还来网下眠；
//                  酒醉酒醒日复日，网上网下年复年。
//                  但愿老死电脑间，不愿鞠躬老板前；
//                  奔驰宝马贵者趣，公交自行程序员。
//                  别人笑我忒疯癫，我笑自己命太贱；
//                  不见满街漂亮妹，哪个归得程序员？

/**
 * Created by zhoubin on 16/7/24. 统一的异常处理,返回Json或者错误页面
 */
public class QuickExceptionHandler implements HandlerExceptionResolver {

	private static Logger logger = LogManager.getLogger(QuickExceptionHandler.class); // log4j日志对象

	@Override
	public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object o, Exception e) {
		logger.error(e);
		// 如果存在json表示cms账号返回错误页面，否则表示api接口返回json错误数据
		if (SessionHelp.isLogin(httpServletRequest.getSession())) {
			Map<String, Object> map = new HashMap<>();
			map.put("msg", e.getMessage());
			return new ModelAndView("error", map); // 返回一个新的ModelAndView，返回为200，否则返回500
		} else {
			HttpServletUtils.writeResponse(httpServletResponse, MessageFactory.getJsonMessage(e.getMessage()));
			return new ModelAndView();
		}
	}

}
