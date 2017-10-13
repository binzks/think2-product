package org.think2framework.wechat.core;

import java.util.List;
import java.util.Map;

/**
 * Created by zhoubin on 16/8/3. 抽象对象
 */
public abstract class AbstractObject implements WechatObject {

	protected String appId; // 微信appId

	protected String secret; // 微信秘钥

	protected Integer expiresIn; // token和ticket有效期,单位秒,默认7000秒

	protected String nonceStr; // 使用JS-JDK的页面生成签名的默认随机字符串

	public AbstractObject(String appId, String secret, Integer expiresIn, String nonceStr) {
		this.appId = appId;
		this.secret = secret;
		this.expiresIn = expiresIn;
		this.nonceStr = nonceStr;
	}

	@Override
	public String getAppId() {
		return this.appId;
	}

	@Override
	public List<String> getAllOpenIds() {
		return ApiUtils.getAllOpenIds(getAccessToken());
	}

	@Override
	public Map<String, Object> getOpenIds(String nextOpenId) {
		return ApiUtils.getOpenIds(getAccessToken(), nextOpenId);
	}

	@Override
	public Map<String, Object> getUserInfo(String openId) {
		return ApiUtils.getUserInfo(getAccessToken(), openId);
	}

	@Override
	public Map<String, Object> getUserList(List<String> openIds) {
		return ApiUtils.getUserList(getAccessToken(), openIds);
	}

	@Override
	public String getJsSignature(String timestamp, String url) {
		return getJsSignature(this.nonceStr, timestamp, url);
	}

	@Override
	public String getJsSignature(String nonceStr, String timestamp, String url) {
		return ApiUtils.getJsSignature(getJsApiTicket(), nonceStr, timestamp, url);
	}
}
