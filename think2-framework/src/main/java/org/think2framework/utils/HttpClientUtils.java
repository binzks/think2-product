package org.think2framework.utils;

import org.think2framework.exception.SimpleException;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * http请求工具类
 */
public class HttpClientUtils {

	/**
	 * http get请求，UTF-8编码格式，返回字符串
	 *
	 * @param url
	 *            请求url
	 * @return 返回值字符串
	 */
	public static String get(String url) {
		return get(url, null, UtilsConst.DEFAULT_ENCODING);
	}

	/**
	 * http get请求，UTF-8编码格式，返回字符串
	 *
	 * @param url
	 *            请求url
	 * @param params
	 *            请求参数
	 * @return 返回值字符串
	 */
	public static String get(String url, Map<String, Object> params) {
		return get(url, params, UtilsConst.DEFAULT_ENCODING);
	}

	/**
	 * http get请求，返回字符串
	 *
	 * @param url
	 *            请求url
	 * @param encoding
	 *            编码格式
	 * @return 返回值字符串
	 */
	public static String get(String url, String encoding) {
		return get(url, null, encoding);
	}

	/**
	 * map转换为http请求的参数
	 *
	 * @param value
	 *            map参数
	 * @return http请求参数
	 */
	public static List<NameValuePair> mapToNameValuePairs(Map<String, Object> value) {
		List<NameValuePair> nameValuePairs = null;
		if (null != value && value.size() > 0) {
			nameValuePairs = new ArrayList<>();
			for (Map.Entry<String, Object> entry : value.entrySet()) {
				nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
			}
		}
		return nameValuePairs;
	}

	/**
	 * 获取http get的请求url
	 *
	 * @param url
	 *            url
	 * @param params
	 *            参数
	 * @param encoding
	 *            编码
	 * @return url拼接参数
	 * @throws Exception
	 *             异常
	 */
	private static String getUrl(String url, Map<String, Object> params, String encoding) throws Exception {
		String result = url;
		if (null != params && params.size() > 0) {
			List<NameValuePair> nameValuePairs = mapToNameValuePairs(params);
			String p = new UrlEncodedFormEntity(nameValuePairs, encoding).toString();
			if (null != p) {
				if (result.contains("?")) {
					result += "&" + p;
				} else {
					result += "?" + p;
				}
			}
		}
		return result;
	}

	/**
	 * 根据请求返回相应获取返回字符串
	 * 
	 * @param response
	 *            请求响应
	 * @param encoding
	 *            编码格式
	 * @return http请求返回值
	 * @throws Exception
	 *             解析异常
	 */
	private static String getResponseString(CloseableHttpResponse response, String encoding) throws Exception {
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception("http response status line " + response.getStatusLine());
		}
		HttpEntity entity = response.getEntity();
		String result = EntityUtils.toString(entity, encoding);
		EntityUtils.consume(entity);
		return result;
	}

	/**
	 * http get请求，返回字符串
	 *
	 * @param url
	 *            请求url
	 * @param params
	 *            请求参数
	 * @param encoding
	 *            编码格式
	 * @return 返回值字符串
	 */
	public static String get(String url, Map<String, Object> params, String encoding) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(getUrl(url, params, encoding));
			CloseableHttpResponse response = httpClient.execute(httpGet);
			try {
				return getResponseString(response, encoding);
			} finally {
				response.close();
			}
		} catch (Exception e) {
			throw new SimpleException(e);
		}
	}

	/**
	 * http get请求，返回byte数组
	 *
	 * @param url
	 *            请求url
	 * @return 返回byte数组
	 */
	public static byte[] getByteArray(String url) {
		return getByteArray(url, null, UtilsConst.DEFAULT_ENCODING);
	}

	/**
	 * http get请求，返回byte数组
	 *
	 * @param url
	 *            请求url
	 * @param params
	 *            请求参数
	 * @return 返回byte数组
	 */
	public static byte[] getByteArray(String url, Map<String, Object> params, String encoding) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(getUrl(url, params, encoding));
			CloseableHttpResponse response = httpClient.execute(httpGet);
			try {
				if (response.getStatusLine().getStatusCode() != 200) {
					throw new Exception("http get response status line " + response.getStatusLine());
				}
				HttpEntity entity = response.getEntity();
				byte[] result = EntityUtils.toByteArray(entity);
				EntityUtils.consume(entity);
				return result;
			} finally {
				response.close();
			}
		} catch (Exception e) {
			throw new SimpleException(e);
		}
	}

	/***
	 * http post请求，UTF-8编码格式，返回字符串
	 *
	 * @param url
	 *            请求url
	 * @return 返回值字符串
	 */
	public static String post(String url) {
		return post(url, null, UtilsConst.DEFAULT_ENCODING);
	}

	/***
	 * http post请求，UTF-8编码格式，返回字符串
	 *
	 * @param url
	 *            请求url
	 * @param params
	 *            请求参数
	 * @return 返回值字符串
	 */
	public static String post(String url, Map<String, Object> params) {
		return post(url, params, UtilsConst.DEFAULT_ENCODING);
	}

	/**
	 * http post请求，返回字符串
	 *
	 * @param url
	 *            请求url
	 * @param params
	 *            请求参数
	 * @param encoding
	 *            编码格式
	 * @return 返回值字符串
	 */
	public static String post(String url, Map<String, Object> params, String encoding) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			if (null != params && params.size() > 0) {
				List<NameValuePair> nameValuePairs = mapToNameValuePairs(params);
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, encoding));
			}
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				return getResponseString(response, encoding);
			} finally {
				response.close();
			}
		} catch (Exception e) {
			throw new SimpleException(e);
		}
	}

	/**
	 * http post一个请求
	 *
	 * @param url
	 *            请求url
	 * @param params
	 *            请求参数
	 * @param contentType
	 *            http contentType
	 * @param header
	 *            http 除了contentType的其他header
	 * @return 返回字符串
	 */
	public static String post(String url, String params, String contentType, Map<String, String> header) {
		return post(url, params, UtilsConst.DEFAULT_ENCODING, contentType, header);
	}

	/**
	 * http post一个请求
	 * 
	 * @param url
	 *            请求url
	 * @param params
	 *            请求参数
	 * @param encoding
	 *            编码
	 * @param contentType
	 *            http contentType
	 * @param header
	 *            http 除了contentType的其他header
	 * @return 返回字符串
	 */
	public static String post(String url, String params, String encoding, String contentType,
			Map<String, String> header) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Content-type", contentType + "; charset=" + encoding);
			if (null != header) {
				for (Map.Entry<String, String> entry : header.entrySet()) {
					httpPost.addHeader(entry.getKey(), entry.getValue());
				}
			}
			StringEntity entity = new StringEntity(params, Charset.forName(encoding));
			entity.setContentEncoding(encoding);
			entity.setContentType(contentType);
			httpPost.setEntity(entity);
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				return getResponseString(response, encoding);
			} finally {
				response.close();
			}
		} catch (Exception e) {
			throw new SimpleException(e);
		}
	}

	/**
	 * http post一个json字符串数据,utf-8编码
	 *
	 * @param url
	 *            url
	 * @param params
	 *            参数字符串
	 * @return 返回字符串
	 */
	public static String postJson(String url, String params) {
		return postJson(url, params, UtilsConst.DEFAULT_ENCODING);
	}

	/**
	 * http post一个json字符串数据
	 *
	 * @param url
	 *            url
	 * @param params
	 *            参数字符串
	 * @param encoding
	 *            编码
	 * @return 返回字符串
	 */
	public static String postJson(String url, String params, String encoding) {
		return post(url, params, encoding, "application/json", null);
	}

	/**
	 * 获取一个xml字符串对应key值得字符串，如果没有则返回空
	 *
	 * @param xml
	 *            xml字符串
	 * @param key
	 *            xml key
	 * @return 字符串
	 */
	private static String getSubXmlString(String xml, String key) {
		int begin = StringUtils.indexOf(xml, "<" + key + ">");
		begin += key.length() + 2;
		int end = StringUtils.lastIndexOf(xml, key + ">");
		if (begin >= 0 && end >= 0) {
			String data = StringUtils.substring(xml, begin, end);
			return StringUtils.substring(data, 0, StringUtils.lastIndexOf(data, "</"));
		} else {
			return "";
		}
	}

	/**
	 * 创建soap请求的body
	 * 
	 * @param namespace
	 *            命名空间
	 * @param method
	 *            请求方法
	 * @param soapAction
	 *            请求action
	 * @param params
	 *            请求参数
	 * @return body字符串
	 */
	private static String createSoapBody(String namespace, String method, String soapAction,
			Map<String, Object> params) {
		String body = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" "
				+ "%s><soap:Header/><soap:Body>%s</soap:Body></soap:Envelope>";
		StringBuffer requestData = new StringBuffer();
		if (null != params && params.size() > 0) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				requestData.append("<" + entry.getKey() + ">" + entry.getValue() + "</" + entry.getKey() + ">");
			}
		}
		if (StringUtils.isBlank(soapAction)) {
			body = String.format(body, "xmlns:p=\"" + namespace + "\"",
					"<p:" + method + ">" + requestData.toString() + "</p:" + method + ">");
		} else {
			body = String.format(body, "xmlns:m=\"method\" xmlns:p=\"" + namespace + "\"",
					"<m:" + method + "><p:p>" + requestData.toString() + "</p:p></m:" + method + ">");
		}
		return body;
	}

	/**
	 * 调用Webservice方法，返回字符串数据
	 *
	 * @param url
	 *            接口地址，去掉WSDL
	 * @param namespace
	 *            接口的命名空间
	 * @param method
	 *            调用方面名称
	 * @param soapAction
	 *            soapAction
	 * @param params
	 *            调用参数
	 * @return 字符串
	 */
	public static String soap(String url, String namespace, String method, String soapAction,
			Map<String, Object> params) {
		return soap(url, namespace, method, soapAction, params, null, UtilsConst.DEFAULT_ENCODING);
	}

	/**
	 * 调用Webservice方法，返回字符串数据，如果key不为null或者空，则返回结果中key标签对应的值
	 *
	 * @param url
	 *            接口地址，去掉WSDL
	 * @param namespace
	 *            接口的命名空间
	 * @param method
	 *            调用方面名称
	 * @param soapAction
	 *            soapAction
	 * @param params
	 *            调用参数
	 * @param key
	 *            获取返回接口的key标签值
	 * @return 字符串
	 */
	public static String soap(String url, String namespace, String method, String soapAction,
			Map<String, Object> params, String key) {
		return soap(url, namespace, method, soapAction, params, key, UtilsConst.DEFAULT_ENCODING);
	}

	/**
	 * 调用Webservice方法，返回字符串数据，如果key不为null或者空，则返回结果中key标签对应的值
	 *
	 * @param url
	 *            接口地址，去掉WSDL
	 * @param namespace
	 *            接口的命名空间
	 * @param method
	 *            调用方面名称
	 * @param soapAction
	 *            soapAction
	 * @param params
	 *            调用参数
	 * @param key
	 *            获取返回接口的key标签值
	 * @param encoding
	 *            编码
	 * @return 字符串
	 */
	public static String soap(String url, String namespace, String method, String soapAction,
			Map<String, Object> params, String key, String encoding) {
		Map<String, String> header = new HashMap<>();
		if (StringUtils.isNotBlank(soapAction)) {
			header.put("soapAction", soapAction);
		}
		String result = post(url, createSoapBody(namespace, method, soapAction, params), encoding, "text/xml", header);
		if (StringUtils.isNotBlank(key)) {
			result = getSubXmlString(result, key);
		}
		return result;
	}

	/**
	 * 调用Webservice方法，返回map数据
	 *
	 * @param url
	 *            接口地址，去掉WSDL
	 * @param namespace
	 *            接口的命名空间
	 * @param method
	 *            调用方面名称
	 * @param params
	 *            调用参数
	 * @return key map
	 */
	public static Map<String, Object> soapForMap(String url, String namespace, String method,
			Map<String, Object> params) {
		return soapForMap(url, namespace, method, params, null, UtilsConst.DEFAULT_ENCODING);
	}

	/**
	 * 调用Webservice方法，返回map数据，如果key不为null或者空，则返回结果中key标签对应的map
	 *
	 * @param url
	 *            接口地址，去掉WSDL
	 * @param namespace
	 *            接口的命名空间
	 * @param method
	 *            调用方面名称
	 * @param params
	 *            调用参数
	 * @param key
	 *            获取返回接口的key标签值
	 * @return key map
	 */
	public static Map<String, Object> soapForMap(String url, String namespace, String method,
			Map<String, Object> params, String key) {
		return soapForMap(url, namespace, method, params, key, UtilsConst.DEFAULT_ENCODING);
	}

	/**
	 * 调用Webservice方法，返回map数据，如果key不为null或者空，则返回结果中key标签对应的map
	 *
	 * @param url
	 *            接口地址，去掉WSDL
	 * @param namespace
	 *            接口的命名空间
	 * @param method
	 *            调用方面名称
	 * @param params
	 *            调用参数
	 * @param key
	 *            获取返回接口的key标签值
	 * @param encoding
	 *            编码
	 * @return key map
	 */
	public static Map<String, Object> soapForMap(String url, String namespace, String method,
			Map<String, Object> params, String key, String encoding) {
		return soapForMap(url, namespace, method, null, params, key, encoding);
	}

	/**
	 * 调用Webservice方法，返回map数据
	 *
	 * @param url
	 *            接口地址，去掉WSDL
	 * @param namespace
	 *            接口的命名空间
	 * @param method
	 *            调用方面名称
	 * @param soapAction
	 *            soapAction
	 * @param params
	 *            调用参数
	 * @return key map
	 */
	public static Map<String, Object> soapForMap(String url, String namespace, String method, String soapAction,
			Map<String, Object> params) {
		return soapForMap(url, namespace, method, soapAction, params, null, UtilsConst.DEFAULT_ENCODING);
	}

	/**
	 * 调用Webservice方法，返回map数据，如果key不为null或者空，则返回结果中key标签对应的map
	 *
	 * @param url
	 *            接口地址，去掉WSDL
	 * @param namespace
	 *            接口的命名空间
	 * @param method
	 *            调用方面名称
	 * @param soapAction
	 *            soapAction
	 * @param params
	 *            调用参数
	 * @param key
	 *            获取返回接口的key标签值
	 * @return key map
	 */
	public static Map<String, Object> soapForMap(String url, String namespace, String method, String soapAction,
			Map<String, Object> params, String key) {
		return soapForMap(url, namespace, method, soapAction, params, key, UtilsConst.DEFAULT_ENCODING);
	}

	/**
	 * 调用Webservice方法，返回map数据，如果key不为null或者空，则返回结果中key标签对应的map
	 *
	 * @param url
	 *            接口地址，去掉WSDL
	 * @param namespace
	 *            接口的命名空间
	 * @param method
	 *            调用方面名称
	 * @param soapAction
	 *            soapAction
	 * @param params
	 *            调用参数
	 * @param key
	 *            获取返回接口的key标签值
	 * @param encoding
	 *            编码
	 * @return key map
	 */
	public static Map<String, Object> soapForMap(String url, String namespace, String method, String soapAction,
			Map<String, Object> params, String key, String encoding) {
		String result = soap(url, namespace, method, soapAction, params, key, encoding);
		// 如果返回字符串为空则返回空map，如果包含< </ >则认为是xml字符串转换为map，如果不包含则认为是json字符串处理
		if (StringUtils.isBlank(result)) {
			return new HashMap<>();
		} else if (StringUtils.contains(result, "<") && StringUtils.contains(result, "</")
				&& StringUtils.contains(result, ">")) {
			return XmlUtils.readStringToMap(result);
		} else {
			return JsonUtils.readStringToMap(result);
		}
	}

}