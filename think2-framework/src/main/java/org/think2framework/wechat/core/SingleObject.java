package org.think2framework.wechat.core;


import org.think2framework.utils.StringUtils;

/**
 * Created by zhoubin on 16/8/3.
 * 单个微信对象,使用对象本身内存管理微信access_token和jsapi_ticket,不能进行集群部署
 */
public class SingleObject extends AbstractObject {

    private String accessToken; // 缓存的access_token

    private Long accessTokenTime; // 获取access_token的时间戳

    private String jsApiTicket; // 缓存的jsApiTicket

    private Long jsApiTicketTime; // 获取jsApiTicket的时间戳

    public SingleObject(String appId, String secret, Integer expiresIn, String nonceStr) {
        super(appId, secret, expiresIn, nonceStr);
    }

    @Override
    public String getAccessToken() {
        if (StringUtils.isBlank(accessToken) || (System.currentTimeMillis() / 1000 > (accessTokenTime + expiresIn))) {
            this.accessToken = ApiUtils.getNewAccessToken(appId, secret);
            this.accessTokenTime = System.currentTimeMillis() / 1000;
        }
        return this.accessToken;
    }

    @Override
    public String getJsApiTicket() {
        if (StringUtils.isBlank(jsApiTicket) || (System.currentTimeMillis() / 1000 > (jsApiTicketTime + expiresIn))) {
            this.jsApiTicket = ApiUtils.getNewJsApiTicket(getAccessToken());
            this.jsApiTicketTime = System.currentTimeMillis() / 1000;
        }
        return this.jsApiTicket;
    }
}
