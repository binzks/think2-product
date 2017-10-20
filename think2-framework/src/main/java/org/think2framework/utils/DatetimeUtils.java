package org.think2framework.utils;

import org.think2framework.exception.SimpleException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间、时间戳工具
 */
public class DatetimeUtils {

	/**
	 * 默认的时间格式
	 */
	private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 获取当前时间戳的long值
	 * 
	 * @return 时间戳long值
	 */
	public static Long now() {
		return System.currentTimeMillis() / 1000;
	}

	/**
	 * 获取当时时间戳的long值,精确到毫秒
	 * 
	 * @return 时间戳long值
	 */
	public static Long nowMillisecond() {
		return System.currentTimeMillis();
	}

	/**
	 * 获取当前时间年份
	 * 
	 * @return 当前年份
	 */
	public static int Year() {
		Calendar now = Calendar.getInstance();
		return now.get(Calendar.YEAR);
	}

	/**
	 * 获取当前时间月份
	 * 
	 * @return 当前月份
	 */
	public static int Month() {
		Calendar now = Calendar.getInstance();
		return now.get(Calendar.MONTH) + 1;
	}

	/**
	 * 获取当前时间日
	 * 
	 * @return 当前日
	 */
	public static int Day() {
		Calendar now = Calendar.getInstance();
		return now.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取当前小时
	 * 
	 * @return 当前小时
	 */
	public static int Hour() {
		Calendar now = Calendar.getInstance();
		return now.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取当前分钟
	 * 
	 * @return 当前分钟
	 */
	public static int Minute() {
		Calendar now = Calendar.getInstance();
		return now.get(Calendar.MINUTE);
	}

	/**
	 * 获取当前秒
	 * 
	 * @return 当前秒
	 */
	public static int Second() {
		Calendar now = Calendar.getInstance();
		return now.get(Calendar.SECOND);
	}

	/**
	 * 获取指定时间字符串的时间戳long值,日期格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @param datetime
	 *            时间字符串
	 * @return 时间戳long值
	 */
	public static Long toLong(String datetime) {
		return toMillisecond(datetime) / 1000;
	}

	/**
	 * 获取指定时间字符串的时间戳long值
	 * 
	 * @param datetime
	 *            时间字符串
	 * @param pattern
	 *            时间格式
	 * @return 时间戳long值
	 */
	public static Long toLong(String datetime, String pattern) {
		return toMillisecond(datetime, pattern) / 1000;
	}

	/**
	 * 获取指定时间字符串的时间戳long值,精确到毫秒,时间格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @param datetime
	 *            时间字符串
	 * @return 时间戳long值
	 */
	public static Long toMillisecond(String datetime) {
		return toMillisecond(datetime, DEFAULT_FORMAT);
	}

	/**
	 * 获取指定时间字符串的时间戳long值,精确到毫秒
	 * 
	 * @param datetime
	 *            时间字符串
	 * @param pattern
	 *            时间格式
	 * @return 时间戳long值
	 */
	public static Long toMillisecond(String datetime, String pattern) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			Date date = simpleDateFormat.parse(datetime.trim());
			return date.getTime();
		} catch (Exception e) {
			throw new SimpleException(e);
		}
	}

	/**
	 * 时间戳转换为时间字符串
	 * 
	 * @param time
	 *            时间戳
	 * @return 时间格式化字符串
	 */
	public static String toString(long time) {
		return toString(time, DEFAULT_FORMAT);
	}

	/**
	 * 时间戳转换为时间字符串
	 * 
	 * @param time
	 *            时间戳
	 * @param pattern
	 *            时间格式
	 * @return 时间格式化字符串
	 */
	public static String toString(long time, String pattern) {
		return millisecondToString(time * 1000, pattern);
	}

	/**
	 * 毫秒级时间戳转换为时间字符串,时间格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @param time
	 *            时间戳,精确到毫秒
	 * @return 时间格式化字符串
	 */
	public static String millisecondToString(long time) {
		return millisecondToString(time, DEFAULT_FORMAT);
	}

	/**
	 * 毫秒级时间戳转换为时间字符串
	 * 
	 * @param time
	 *            时间戳,精确到毫秒
	 * @param pattern
	 *            时间格式
	 * @return 时间格式化字符串
	 */
	public static String millisecondToString(long time, String pattern) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(new Date(time));
	}

}
