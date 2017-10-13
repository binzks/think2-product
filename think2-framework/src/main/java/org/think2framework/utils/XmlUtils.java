package org.think2framework.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.think2framework.exception.NonExistException;
import org.think2framework.exception.SimpleException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by zhoubin on 15/12/9. xml工具
 */
public class XmlUtils {

	private static final String[][] BASIC_ESCAPE = { { "\"", "&quot;" }, { "&", "&amp;" }, { "<", "&lt;" },
			{ ">", "&gt;" }, { "'", "&apos;" } };

	/**
	 * 对象转字符串
	 *
	 * @param object
	 *            对象
	 * @return xml字符串
	 */
	public static String toString(Object object) {
		try {
			return new XmlMapper().writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new SimpleException(e);
		}
	}

	/**
	 * 字符串转换为单个对象
	 *
	 * @param xml
	 *            字符串
	 * @param valueType
	 *            类
	 * @return 类对象
	 */
	public static <T> T readString(String xml, Class<T> valueType) {
		try {
			return new XmlMapper().readValue(xml, valueType);
		} catch (IOException e) {
			throw new SimpleException(e);
		}
	}

	/**
	 * 字符串转换为复杂对象
	 *
	 * @param xml
	 *            xml字符串
	 * @param valueType
	 *            type类型
	 * @return 对象
	 */
	public static <T> T readString(String xml, Type valueType) {
		try {
			ObjectMapper objectMapper = new XmlMapper();
			return objectMapper.readValue(xml, objectMapper.constructType(valueType));
		} catch (IOException e) {
			throw new SimpleException(e);
		}
	}

	/**
	 * 字符串转换为复杂对象
	 *
	 * @param xml
	 *            字符串
	 * @param valueTypeRef
	 *            复杂类型
	 * @return 对象
	 */
	public static <T> T readString(String xml, TypeReference valueTypeRef) {
		try {
			return new XmlMapper().readValue(xml, valueTypeRef);
		} catch (IOException e) {
			throw new SimpleException(e);
		}
	}

	/**
	 * 将字符串解析成map对象
	 *
	 * @param xml
	 *            字符串
	 * @return map对象
	 */
	public static Map<String, Object> readStringToMap(String xml) {
		return readString(xml, new TypeReference<Map<String, Object>>() {
		});
	}

	/**
	 * 将字符串转化为list数组
	 *
	 * @param xml
	 *            xml字符串
	 * @param valueType
	 *            数组类型
	 * @return 数组
	 */
	public static <T> List<T> readStringToList(String xml, Class<T> valueType) {
		return readString(xml, new TypeReference<List<T>>() {
		});
	}

	/**
	 * 将字符串解析成map数组对象
	 *
	 * @param xml
	 *            xml字符串
	 * @return map数组对象
	 */
	public static List<Map<String, Object>> readStringToMapList(String xml) {
		return readString(xml, new TypeReference<List<Map<String, Object>>>() {
		});
	}

	/**
	 * 读取文件并解析成单个对象
	 *
	 * @param file
	 *            文件
	 * @param valueType
	 *            对象类
	 * @return 对象实例
	 */
	public static <T> T readFile(File file, Class<T> valueType) {
		try {
			return new XmlMapper().readValue(file, valueType);
		} catch (FileNotFoundException e) {
			throw new NonExistException(e);
		} catch (IOException e) {
			throw new SimpleException(e);
		}
	}

	/**
	 * 读取文件并解析成单个对象
	 *
	 * @param file
	 *            文件路径和名称
	 * @param valueType
	 *            对象类
	 * @return 对象实例
	 */
	public static <T> T readFile(String file, Class<T> valueType) {
		return readFile(new File(file), valueType);
	}

	/**
	 * 读取文件并解析成复杂对象,如数组
	 *
	 * @param file
	 *            文件
	 * @param valueTypeRef
	 *            复杂对象类型
	 * @return 对象实例
	 */
	public static <T> T readFile(File file, TypeReference valueTypeRef) {
		try {
			return new XmlMapper().readValue(file, valueTypeRef);
		} catch (FileNotFoundException e) {
			throw new NonExistException(e);
		} catch (IOException e) {
			throw new SimpleException(e);
		}
	}

	/**
	 * 读取文件并解析成复杂对象,如数组
	 *
	 * @param file
	 *            文件路径和名称
	 * @param valueTypeRef
	 *            复杂对象类型
	 * @return 对象实例
	 */
	public static <T> T readFile(String file, TypeReference valueTypeRef) {
		return readFile(new File(file), valueTypeRef);
	}

	/**
	 * 将对象转换成json字符串并写入文件
	 *
	 * @param file
	 *            文件
	 * @param object
	 *            对象
	 */
	public static void writerFile(String file, Object object) {
		try {
			new XmlMapper().writeValue(new File(file), object);
		} catch (IOException e) {
			throw new SimpleException(e);
		}
	}

	/**
	 * 转义xml字符串，将xml特殊符号转义
	 * 
	 * @param input
	 *            原始xml字符串
	 * @return 转义后的xml字符串
	 */
	public static String escape(String input) {
		String xml = input;
		for (int i = 0; i < BASIC_ESCAPE.length; i++) {
			xml = StringUtils.replace(xml, BASIC_ESCAPE[i][0], BASIC_ESCAPE[i][1]);
		}
		return xml;
	}

	/**
	 * 还原xml字符串，将转义的特殊符号还原
	 * 
	 * @param input
	 *            转义后的xml字符串
	 * @return 原始xml字符串
	 */
	public static String unescape(String input) {
		String xml = input;
		for (int i = 0; i < BASIC_ESCAPE.length; i++) {
			xml = StringUtils.replace(xml, BASIC_ESCAPE[i][1], BASIC_ESCAPE[i][0]);
		}
		return xml;
	}

}
