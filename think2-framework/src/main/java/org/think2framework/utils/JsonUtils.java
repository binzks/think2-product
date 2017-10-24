package org.think2framework.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.think2framework.exception.NonExistException;
import org.think2framework.exception.SimpleException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * json工具类,以jackson为基础
 */
public class JsonUtils {

	/**
	 * 获取读取使用的ObjectMapper
	 *
	 * @return ObjectMapper
	 */
	private static ObjectMapper getReadMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		return objectMapper;
	}

	/**
	 * 获取写入使用的ObjectMapper
	 *
	 * @return ObjectMapper
	 */
	private static ObjectMapper getWriterMapper() {
		return new ObjectMapper();
	}

	/**
	 * 对象转字符串
	 *
	 * @param object
	 *            对象
	 * @return json字符串
	 */
	public static String toString(Object object) {
		return toString(object, null);
	}

	/**
	 * 对象转字符串
	 *
	 * @param object
	 *            对象
	 * @param incl
	 *            NON_NULL 属性为NULL 不序列化 NON_EMPTY 属性为 空（“”） 或者为 NULL 都不序列化
	 * @return json字符串
	 */
	public static String toString(Object object, JsonInclude.Include incl) {
		ObjectMapper objectMapper = getWriterMapper();
		try {
			if (null != incl) {
				objectMapper.setSerializationInclusion(incl);
			}
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new SimpleException(e);
		}
	}

	/**
	 * 字符串转换为单个对象
	 *
	 * @param json
	 *            字符串
	 * @param valueType
	 *            类
	 * @return 类对象
	 */
	public static <T> T readString(String json, Class<T> valueType) {
		ObjectMapper objectMapper = getReadMapper();
		try {
			return objectMapper.readValue(json, valueType);
		} catch (IOException e) {
			throw new SimpleException(e);
		}
	}

	/**
	 * 字符串转换为复杂对象
	 *
	 * @param json
	 *            json字符串
	 * @param valueType
	 *            type类型
	 * @return 对象
	 */
	public static <T> T readString(String json, Type valueType) {
		ObjectMapper objectMapper = getReadMapper();
		try {
			return objectMapper.readValue(json, objectMapper.constructType(valueType));
		} catch (IOException e) {
			throw new SimpleException(e);
		}
	}

	/**
	 * 字符串转换为复杂对象
	 *
	 * @param json
	 *            字符串
	 * @param valueTypeRef
	 *            复杂类型
	 * @return 对象
	 */
	public static <T> T readString(String json, TypeReference valueTypeRef) {
		ObjectMapper objectMapper = getReadMapper();
		try {
			return objectMapper.readValue(json, valueTypeRef);
		} catch (IOException e) {
			throw new SimpleException(e);
		}
	}

	/**
	 * 将字符串解析成map对象
	 *
	 * @param json
	 *            json字符串
	 * @return map对象
	 */
	public static Map<String, Object> readStringToMap(String json) {
		return readString(json, new TypeReference<Map<String, Object>>() {
		});
	}

	/**
	 * 将字符串转化为list数组
	 *
	 * @param json
	 *            json字符串
	 * @param valueType
	 *            数组类型
	 * @return 数组
	 */
	public static <T> List<T> readStringToList(String json, Class<T> valueType) {
		return readString(json, new TypeReference<List<T>>() {
		});
	}

	/**
	 * 将字符串解析成map数组对象
	 *
	 * @param json
	 *            json字符串
	 * @return map数组对象
	 */
	public static List<Map<String, Object>> readStringToMapList(String json) {
		return readString(json, new TypeReference<List<Map<String, Object>>>() {
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
		ObjectMapper objectMapper = getReadMapper();
		try {
			return objectMapper.readValue(file, valueType);
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
		ObjectMapper objectMapper = getReadMapper();
		try {
			return objectMapper.readValue(file, valueTypeRef);
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
	 * 读取文件并解析成map数组
	 * 
	 * @param file
	 *            文件路径和名称
	 * @return map数组
	 */
	public static List<Map<String, Object>> readFileToMapList(String file) {
		return readFile(file, new TypeReference<List<Map<String, Object>>>() {
		});
	}

	/**
	 * 读取文件并解析成map数组
	 * 
	 * @param file
	 *            文件
	 * @return map数组
	 */
	public static List<Map<String, Object>> readFileToMapList(File file) {
		return readFile(file, new TypeReference<List<Map<String, Object>>>() {
		});
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
		ObjectMapper objectMapper = getWriterMapper();
		try {
			objectMapper.writeValue(new File(file), object);
		} catch (IOException e) {
			throw new SimpleException(e);
		}
	}
}
