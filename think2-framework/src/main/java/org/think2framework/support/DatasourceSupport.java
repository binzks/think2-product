package org.think2framework.support;

import com.fasterxml.jackson.core.type.TypeReference;
import org.think2framework.bean.Datasource;
import org.think2framework.exception.NonsupportException;
import org.think2framework.orm.OrmFactory;
import org.think2framework.utils.FileUtils;
import org.think2framework.utils.JsonUtils;

import java.io.File;
import java.util.List;

/**
 * 数据源配置
 */
public class DatasourceSupport {

	/**
	 * 追加一个数据源
	 * 
	 * @param type
	 *            数据库类型
	 * @param name
	 *            数据源名称
	 * @param minIdle
	 *            数据源最小空闲连接
	 * @param maxIdle
	 *            数据源最大空闲连接
	 * @param initialSize
	 *            数据源初始化连接数
	 * @param timeout
	 *            数据源超时时间(以秒数为单位)
	 * @param db
	 *            数据库名称
	 * @param host
	 *            数据库地址
	 * @param port
	 *            数据库端口
	 * @param username
	 *            数据库用户名
	 * @param password
	 *            数据库密码
	 */
	public static synchronized void append(String type, String name, Integer minIdle, Integer maxIdle,
			Integer initialSize, Integer timeout, String db, String host, Integer port, String username,
			String password) {
		if ("mysql".equalsIgnoreCase(type)) {
			OrmFactory.appendMysql(name, minIdle, maxIdle, initialSize, timeout, host, port, db, username, password);
		} else if ("redis".equalsIgnoreCase(type)) {
			OrmFactory.appendRedis(name, minIdle, maxIdle, timeout, host, port, db, password);
		} else if ("sqlserver".equalsIgnoreCase(type)) {
			OrmFactory.appendSqlserver(name, minIdle, maxIdle, initialSize, timeout, host, port, db, username,
					password);
		} else if ("oracle".equalsIgnoreCase(type)) {
			OrmFactory.appendOracle(name, minIdle, maxIdle, initialSize, timeout, host, port, db, username, password);
		} else if ("sqlite".equalsIgnoreCase(type)) {
			OrmFactory.appendSqlite(name, minIdle, maxIdle, initialSize, timeout, host, port, db, username, password);
		} else {
			throw new NonsupportException("database type " + type);
		}
	}

	/**
	 * 从配置文件加载数据源
	 * 
	 * @param filePath
	 *            配置文件路径和名称，带*为多个文件
	 * @param clear
	 *            是否清空原先的数据源配置
	 */
	public static synchronized void loadFiles(String filePath, Boolean clear) {
		File[] files = FileUtils.getFiles(filePath);
		if (null == files) {
			return;
		}
		if (clear) {
			OrmFactory.clearDatabases();
		}
		// 重新加载数据库
		for (File file : files) {
			List<Datasource> dss = JsonUtils.readFile(file, new TypeReference<List<Datasource>>() {
			});
			for (Datasource ds : dss) {
				append(ds.getType(), ds.getName(), ds.getMinIdle(), ds.getMaxIdle(), ds.getInitialSize(),
						ds.getTimeout(), ds.getDb(), ds.getHost(), ds.getPort(), ds.getUsername(), ds.getPassword());
			}
		}
	}
}
