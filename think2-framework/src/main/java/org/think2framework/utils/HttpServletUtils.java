package org.think2framework.utils;

import org.think2framework.exception.SimpleException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhoubin on 16/7/22. http servlet工具
 */
public class HttpServletUtils {

    public static final String ENCODING = "utf-8"; // 编码

    private final static String IPV4_REGEX = "(?:[01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])(?:\\.(?:[01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])){3}";

    public final static Matcher IPV4_MATCHER = Pattern.compile(IPV4_REGEX).matcher("");

    private static Map<String, String> keys = new HashMap<>(); //请求签名的key的对应编号和值

    /**
     * 批量设置请求签名key
     *
     * @param values 请求签名key map
     */
    public static void setKeys(Map<String, String> values) {
        keys = values;
    }

    /**
     * 追加一个请求签名key，如果原先的key已经存在则覆盖
     *
     * @param code  编号
     * @param value 值
     */
    public static void appendKey(String code, String value) {
        keys.put(code, value);
    }

    /**
     * 清理所有签名key信息
     */
    public static void clearKeys() {
        keys.clear();
    }

    /**
     * 删除一个签名key
     *
     * @param code 签名编号
     */
    public static void clearKey(String code) {
        keys.remove(code);
    }

    /**
     * 创建一个加密请求参数
     *
     * @param code  请求签名编号，用于获取签名key
     * @param value 请求参数
     * @return 加密请求=请求参数&c=签名对象编号&t=当前时间戳&s=签名
     */
    public static String createSignParams(String code, String value) {
        String time = StringUtils.toString(System.currentTimeMillis() / 1000);
        return createSignParams(code, value, time);
    }

    /**
     * 创建一个加密请求参数
     *
     * @param code  请求签名编号，用于获取签名key
     * @param value 请求参数
     * @param time  签名时间戳
     * @return 加密请求=请求参数&c=签名对象编号&t=当前时间戳&s=签名
     */
    public static String createSignParams(String code, String value, String time) {
        return value + "&t=" + time + "&c=" + code + "&s=" + createSign(code, value, time);
    }

    /**
     * 创建一个签名
     *
     * @param code  签名编号
     * @param value 签名内容
     * @param time  时间戳
     * @return 签名
     */
    public static String createSign(String code, String value, String time) {
        String key = keys.get(code);
        if (StringUtils.isBlank(key)) {
            throw new SimpleException("00101" + code);
        }
        //签名规则，key$请求参数@当前时间戳的MD5值
        return EncryptUtils.md5(key + "$" + value + "@" + time);
    }

    /**
     * 校验一个加密请求签名是否正确，请求必须带t=时间戳c=请求签名编号s=签名
     *
     * @param request 请求
     */
    public static void checkSign(HttpServletRequest request) {
        String time = request.getParameter("t"); //请求生成时间
        String code = request.getParameter("c"); //请求签名编号
        String sign = request.getParameter("s"); //请求签名
        if (StringUtils.isBlank(time) || StringUtils.isBlank(code) || StringUtils.isBlank(sign)) {
            throw new SimpleException("00102");
        }
        double t = NumberUtils.toDouble(time);
        double now = System.currentTimeMillis() / 1000;
        if ((t + 600 < now) || (now < t - 600)) {
            throw new SimpleException("00103");
        }
        String value = request.getQueryString();
        value = StringUtils.replace(value, "&s=" + sign, "");
        value = StringUtils.replace(value, "&c=" + code, "");
        value = StringUtils.replace(value, "&t=" + time, "");
        if (!sign.equals(createSign(code, value, time))) {
            throw new SimpleException("00104");
        }
    }

    /**
     * 将字符串写入返回response
     *
     * @param response 返回对象
     * @param value    写入值
     */
    public static void writeResponse(HttpServletResponse response, String value) {
        try {
            response.setHeader("Access-Control-Allow-Origin", "*");// 允许跨域
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Expires", "0");
            response.setHeader("Pragma", "No-cache");
            byte[] bytes = value.getBytes(ENCODING);
            response.setCharacterEncoding(ENCODING);
            response.setContentType("text/plain");
            response.setContentLength(bytes.length);
            response.getOutputStream().write(bytes);
        } catch (IOException e) {
            throw new SimpleException(e);
        }
    }

    /**
     * 获取请求的ip地址
     *
     * @param request 请求
     * @return ip地址
     */
    public static String getRequestIpAddress(HttpServletRequest request) {
        String realIP = request.getHeader("x-forwarded-for");
        if (realIP != null && realIP.length() != 0) {
            while ((realIP != null && realIP.equals("unknow"))) {
                realIP = request.getHeader("x-forwarded-for");
            }
            if (realIP.indexOf(',') >= 0) {
                realIP = realIP.substring(0, realIP.indexOf(','));
            }
        }
        if (realIP == null || realIP.length() == 0 || "unknown".equalsIgnoreCase(realIP)) {
            realIP = request.getHeader("Proxy-Clint-IP");
        }
        if (realIP == null || realIP.length() == 0 || "unknown".equalsIgnoreCase(realIP)) {
            realIP = request.getHeader("WL-Proxy-Clint-IP");
        }
        if (realIP == null || realIP.length() == 0 || "unknown".equalsIgnoreCase(realIP)) {
            realIP = request.getRemoteAddr();
        }
        return realIP;
    }

    public static int ipv4ToInt(String ipv4) {
        if (!IPV4_MATCHER.reset(ipv4).matches()) {
            throw new IllegalArgumentException("IPv4 format ERROR!");
        }
        String[] strs = ipv4.split("\\.");
        int result = 0;
        for (int i = 0, k = strs.length; i < k; i++) {
            result |= Integer.parseInt(strs[i]) << ((k - 1 - i) * 8);
        }
        return result;
    }

    public static String int4Ipv4(int ipv4Int) {
        StringBuilder sb = new StringBuilder(15);
        for (int i = 0; i < 4; i++) {
            if (i > 0) {
                sb.append('.');
            }
            sb.append((ipv4Int >>> ((3 - i) * 8)) & 0xff);
        }
        return sb.toString();
    }

    public static boolean isIpInForbiddenIp(String forbiddenIp, String ip) {
        if (StringUtils.isBlank(forbiddenIp))
            return false;
        if (StringUtils.isBlank(ip))
            return true;
        if (forbiddenIp.equals(ip))
            return true;
        if (forbiddenIp.indexOf("*") >= 0) {
            String[] fip = forbiddenIp.split("\\.");
            String[] fromIp = ip.split("\\.");
            if (fip.length != 4 || fromIp.length != 4)
                return false;
            for (int i = 0; i < 4; i++) {
                String s1 = fip[i];
                String s2 = fromIp[i];
                if (!s1.equals(s2)) {
                    if (!s1.equals("*"))
                        return false;
                }
            }
            return true;
        }
        return false;
    }

}
