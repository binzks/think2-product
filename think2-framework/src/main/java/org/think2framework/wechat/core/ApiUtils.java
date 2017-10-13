package org.think2framework.wechat.core;

import com.fasterxml.jackson.core.type.TypeReference;
import org.think2framework.utils.*;
import org.think2framework.wechat.exception.WechatErrorException;

import java.util.*;

/**
 * Created by zhoubin on 16/8/2. 微信工具,主要是调用各接口获取数据
 */
public class ApiUtils {

	private static final String BASE_URL = "https://sh.api.weixin.qq.com"; // 基本微信域名,上海接入点,提高速度

	// 获取accessToken接口地址
	private static final String ACCESS_TOKEN = BASE_URL
			+ "/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

	// 获取用户的基本信息
	private static final String USER_INFO = BASE_URL + "/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN";

	// 获取所有关注号的openid
	private static final String OPEN_ID_LIST = BASE_URL + "/cgi-bin/user/get?access_token=%s";

	// 批量获取用户信息
	private static final String USER_LIST = BASE_URL + "/cgi-bin/user/info/batchget?access_token=%s";

	// 获取jsapi的ticket
	private static final String JS_API_TICKET = BASE_URL + "/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";

	/**
	 * 根据微信接口json字符串返回数据,如果返回结果为错误则抛出异常,否则返回数据map对象
	 * 
	 * @param json
	 *            json字符串
	 * @return json的map对象
	 */
	private static Map<String, Object> getResult(String json) {
		Map<String, Object> map = JsonUtils.readString(json, new TypeReference<Map<String, Object>>() {
		});
		if (map.containsKey("errcode")
				&& !WechatErrorException.ERROR_CODE_SUCCESS.equals(map.get("errcode").toString())) {
			throw new WechatErrorException(map.get("errcode").toString());
		} else {
			return map;
		}
	}

	/**
	 * 获取微信接口数据,并判断返回结果是否有错误,有错误抛出异常,没有错误返回结果map对象
	 * 
	 * @param url
	 *            get请求url
	 * @return 接口返回数据map
	 */
	private static Map<String, Object> getData(String url) {
		String data = HttpClientUtils.get(url);
		return getResult(data);
	}

	/**
	 * 获取微信接口数据,并判断返回结果是否有错误,有错误抛出异常,没有错误返回结果map对象
	 * 
	 * @param url
	 *            post请求url
	 * @param data
	 *            post json数据
	 * @return 接口返回数据map
	 */
	private static Map<String, Object> postData(String url, Object data) {
		String json = HttpClientUtils.postJson(url, JsonUtils.toString(data));
		return getResult(json);
	}

	/**
	 * 获取access_token
	 *
	 * @return access_token
	 */
	public static String getNewAccessToken(String appId, String secret) {
		String url = String.format(ACCESS_TOKEN, appId, secret);
		Map<String, Object> data = getData(url);
		return data.get("access_token").toString();
	}

	/**
	 * 获取用户openid列表
	 *
	 * @param accessToken
	 *            访问token
	 * @param nextOpenId
	 *            下一个openid
	 * @return openid map数据
	 */
	public static Map<String, Object> getOpenIds(String accessToken, String nextOpenId) {
		String url = String.format(OPEN_ID_LIST, accessToken);
		if (StringUtils.isNotBlank(nextOpenId)) {
			url += "&next_openid=" + nextOpenId;
		}
		return getData(url);
	}

	/**
	 * 获取微信号所有关注用户openid
	 *
	 * @param accessToken
	 *            访问token
	 * @return openid列表
	 */
	public static List<String> getAllOpenIds(String accessToken) {
		List<String> list = new ArrayList<>();
		Map<String, Object> map = getOpenIds(accessToken, null); // 获取第一批openid
		// 如果获取的openid数量大于0则一直循环获取直到没有数据,每次获取最大为10000条
		while (NumberUtils.toInt(map.get("count")) > 0) {
			// 解析openid列表添加到结果集
			Map<String, Object> data = (Map<String, Object>) map.get("data");
			List<String> openIds = (List<String>) data.get("openid");
			list.addAll(openIds);
			// 获取下一个openid,如果下一个openid存在则获取下一批数据
			String nextOpenId = StringUtils.toString(map.get("next_openid"));
			if (StringUtils.isNotBlank(nextOpenId)) {
				// 将获取的下一批数据加载到map中,进行递归加载直到没有数据
				map = getOpenIds(accessToken, nextOpenId);
			} else { // 如果没有下一个openid表示没有数据了,手动设置map数量为0
				map.put("count", 0);
			}
		}
		return list;
	}

	/**
	 * 获取单个用户信息
	 *
	 * @param accessToken
	 *            访问token
	 * @param openId
	 *            用户openid
	 * @return 用户信息json字符串
	 */
	public static Map<String, Object> getUserInfo(String accessToken, String openId) {
		String url = String.format(USER_INFO, accessToken, openId);
		return getData(url);
	}

	/**
	 * 批量获取用户信息列表,不能超过100个用户
	 *
	 * @param accessToken
	 *            访问token
	 * @param openIds
	 *            用户openid列表
	 * @return 用户信息json
	 */
	public static Map<String, Object> getUserList(String accessToken, List<String> openIds) {
		List<Map<String, String>> list = new ArrayList<>();
		for (String openId : openIds) {
			Map<String, String> user = new LinkedHashMap<>();
			user.put("openid", openId);
			user.put("lang", "zh-CN");
			list.add(user);
		}
		Map<String, Object> param = new HashMap<>();
		param.put("user_list", list);
		String url = String.format(USER_LIST, accessToken);
		return postData(url, param);
	}

	/**
	 * 获取一个js api ticket
	 * 
	 * @param accessToken
	 *            访问token
	 * @return ticket
	 */
	public static String getNewJsApiTicket(String accessToken) {
		String url = String.format(JS_API_TICKET, accessToken);
		Map<String, Object> map = getData(url);
		return map.get("ticket").toString();
	}

	/**
	 * 获取使用JS-SDK的页面的签名
	 * 
	 * @param jsApiTicket
	 *            jsapi_ticket
	 * @param nonceStr
	 *            随机字符串
	 * @param timestamp
	 *            时间戳
	 * @param url
	 *            当前网页的URL，不包含#及其后面部分
	 * @return 签名字符串
	 */
	public static String getJsSignature(String jsApiTicket, String nonceStr, String timestamp, String url) {
		String key = String.format("jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s", jsApiTicket, nonceStr, timestamp,
				url);
		return EncryptUtils.sha1(key);
	}

}
