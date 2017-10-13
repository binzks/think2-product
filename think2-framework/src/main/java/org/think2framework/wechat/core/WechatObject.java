package org.think2framework.wechat.core;

import java.util.List;
import java.util.Map;

/**
 * Created by zhoubin on 16/8/3. 微信对象接口
 */
public interface WechatObject {

	/**
	 * 获取微信的appid
	 * 
	 * @return appid
	 */
	String getAppId();

	/**
	 * 获取access_token
	 * 
	 * @return 获取微信access_token
	 */
	String getAccessToken();

	/**
	 * 获取jsapi_ticket
	 *
	 * @return jsapi_ticket
	 */
	String getJsApiTicket();

	/**
	 * 获取微信号所有关注用户openid
	 *
	 * @return openid列表
	 */
	List<String> getAllOpenIds();

	/**
	 * 获取一批用户的openid
	 *
	 * @param nextOpenId
	 *            下一个openid,如果取第一个传入null或者空
	 * @return 用户openid map
	 */
	Map<String, Object> getOpenIds(String nextOpenId);

	/**
	 * 获取单个用户信息
	 *
	 * @param openId
	 *            用户openid
	 * @return 用户信息map
	 */
	Map<String, Object> getUserInfo(String openId);

	/**
	 * 批量获取用户信息列表,不能超过100个用户,使用对象管理的access_token获取
	 *
	 * @param openIds
	 *            用户openid列表
	 * @return 用户信息map
	 */
	Map<String, Object> getUserList(List<String> openIds);

	/**
	 * 获取一个使用JS-JDK的页面的签名
	 *
	 * @param timestamp
	 *            时间戳
	 * @param url
	 *            当前网页的URL，不包含#及其后面部分
	 * @return 签名字符串
	 */
	String getJsSignature(String timestamp, String url);

	/**
	 * 获取一个使用JS-JDK的页面的签名
	 * 
	 * @param nonceStr
	 *            随机字符串
	 * @param timestamp
	 *            时间戳
	 * @param url
	 *            当前网页的URL，不包含#及其后面部分
	 * @return 签名字符串
	 */
	String getJsSignature(String nonceStr, String timestamp, String url);
}
