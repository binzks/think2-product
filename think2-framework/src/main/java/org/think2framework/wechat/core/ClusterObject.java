package org.think2framework.wechat.core;

/**
 * Created by zhoubin on 16/8/3. 集群微信对象,使用数据库表管理微信access_token和jsapi_ticket,
 * 需要另外的程序定时刷新access_token和jsapi_ticket,此对象只缓存和获取数据库中的值,可以进行集群部署
 */
public class ClusterObject extends AbstractObject {

	private String accessToken; // 缓存的access_token

	private Long accessTokenTime; // 获取access_token的时间戳

	private String jsApiTicket; // 缓存的jsApiTicket

	private Long jsApiTicketTime; // 获取jsApiTicket的时间戳

	public ClusterObject(String appId, String secret, Integer expiresIn, String nonceStr) {
		super(appId, secret, expiresIn, nonceStr);
	}

	@Override
	public String getAccessToken() {
		if (System.currentTimeMillis() / 1000 > (accessTokenTime + expiresIn)) {
			//this.accessToken = ApiUtils.getNewAccessToken(appId, secret);
			this.accessTokenTime = System.currentTimeMillis() / 1000;
		}
		return this.accessToken;
	}

	@Override
	public String getJsApiTicket() {
		if (System.currentTimeMillis() / 1000 > (jsApiTicketTime + expiresIn)) {
			//this.jsApiTicket = ApiUtils.getNewJsApiTicket(accessToken);
			this.jsApiTicketTime = System.currentTimeMillis() / 1000;
		}
		return this.jsApiTicket;
	}
}
