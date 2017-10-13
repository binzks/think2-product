package org.think2framework.utils;

import org.think2framework.exception.SimpleException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;

/**
 * Created by zhoubin on 15/11/12. 发送短信工具类
 */
public class SmsUtils {

    private static String URL = "http://192.168.10.116:8081/mas/services/cmcc_mas_wbs";
    private static String appId = "0007";
    private static String eCode = "211";
    private static String mFormat = "GB2312";
    private static String sMethod = "Normal";
    private static String dRequest = "false";
    private static String aPsw = "zhengsutao";
    private static String ifId = "api";
    private static String ifName = "zhengsutao";

    private static String param = "<x:Envelope xmlns:x=\"http://schemas.xmlsoap.org/soap/envelope/\""
            + " xmlns:ser=\"http://www.csapi.org/service\" xmlns:sms=\"http://www.csapi.org/schema/sms\">"
            + "<x:Header/><x:Body><ser:sendSms><ser:sendSmsRequest><sms:ApplicationID>%s</sms:ApplicationID>"
            + "<sms:DestinationAddresses>tel:%s</sms:DestinationAddresses><sms:ExtendCode>%s</sms:ExtendCode>"
            + "<sms:Message>%s</sms:Message><sms:MessageFormat>%s</sms:MessageFormat>"
            + "<sms:SendMethod>%s</sms:SendMethod><sms:DeliveryResultRequest>%s</sms:DeliveryResultRequest>"
            + "<sms:apPassword>%s</sms:apPassword><sms:intfid>%s</sms:intfid><sms:intfname>%s</sms:intfname>"
            + "</ser:sendSmsRequest></ser:sendSms></x:Body></x:Envelope>";

    public static void config(String url, String applicationID, String extendCode, String messageFormat,
                              String sendMethod, String deliveryResultRequest, String apPassword, String interfaceId,
                              String interfaceName) {
        URL = url;
        appId = applicationID;
        eCode = extendCode;
        mFormat = messageFormat;
        sMethod = sendMethod;
        dRequest = deliveryResultRequest;
        aPsw = apPassword;
        ifId = interfaceId;
        ifName = interfaceName;
    }

    /**
     * 发送移动短信
     *
     * @param phones  手机号码，多个用英文逗号隔开
     * @param message 消息内容
     */
    public static String sendMessage(String phones, String message) {
        try {
            String params = String.format(param, appId, phones, eCode, message, mFormat, sMethod, dRequest, aPsw, ifId,
                    ifName);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(URL);
            httpPost.addHeader("soapAction", "http://www.csapi.org/service/sendSms");
            StringEntity stringEntity = new StringEntity(params, Charset.forName("utf-8"));
            stringEntity.setContentEncoding("utf-8");
            stringEntity.setContentType("text/xml");
            httpPost.setEntity(stringEntity);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new Exception("http response status line " + response.getStatusLine());
                }
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity, "utf-8");
                EntityUtils.consume(entity);
                return result;
            } finally {
                response.close();
            }
        } catch (Exception e) {
            throw new SimpleException(e);
        }
    }

}
