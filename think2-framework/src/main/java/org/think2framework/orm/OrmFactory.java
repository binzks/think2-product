package org.think2framework.orm;

import org.think2framework.exception.NonExistException;
import org.think2framework.exception.NonsupportException;
import org.think2framework.orm.bean.*;
import org.think2framework.orm.core.*;
import org.think2framework.utils.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.JedisPool;

import java.util.*;

/**
 * 数据持久化工厂
 */
public class OrmFactory {

	private static final Logger logger = LogManager.getLogger(OrmFactory.class);

	/**
	 * 存储数据库的map
	 */
	private static Map<String, Database> databases = new HashMap<>();

	/**
	 * 存储数据库表的map
	 */
	private static Map<String, Table> tables = new HashMap<>();

	/**
	 * 存储查询实体的map
	 */
	private static Map<String, Entity> entities = new HashMap<>();

	/**
	 * 清理数据库配置
	 */
	public static synchronized void clearDatabases() {
		databases.clear();
		logger.debug("All databases cleared successfully");
	}

	/**
	 * 清理模型
	 */
	public static synchronized void clearModels() {
		tables.clear();
		entities.clear();
		logger.debug("All tables and entities cleared successfully");
	}

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
	public static synchronized void appendDatabase(String type, String name, Integer minIdle, Integer maxIdle,
			Integer initialSize, Integer timeout, String db, String host, Integer port, String username,
			String password) {
		if (TypeUtils.DATABASE_MYSQL.equalsIgnoreCase(type)) {
			appendMysql(name, minIdle, maxIdle, initialSize, timeout, host, port, db, username, password);
		} else if (TypeUtils.DATABASE_REDIS.equalsIgnoreCase(type)) {
			appendRedis(name, minIdle, maxIdle, timeout, host, port, db, password);
		} else if (TypeUtils.DATABASE_SQLSERVER.equalsIgnoreCase(type)) {
			appendSqlserver(name, minIdle, maxIdle, initialSize, timeout, host, port, db, username, password);
		} else if (TypeUtils.DATABASE_ORACLE.equalsIgnoreCase(type)) {
			appendOracle(name, minIdle, maxIdle, initialSize, timeout, host, port, db, username, password);
		} else if (TypeUtils.DATABASE_SQLITE.equalsIgnoreCase(type)) {
			appendSqlite(name, minIdle, maxIdle, initialSize, timeout, host, port, db, username, password);
		} else {
			throw new NonsupportException("database type " + type);
		}
	}

	/**
	 * 追加一个mysql数据源，如果已经存在则不再重新添加并警告
	 */
	public static synchronized void appendMysql(String name, Integer minIdle, Integer maxIdle, Integer initialSize,
			Integer timeout, String host, Integer port, String database, String username, String password) {
		if (null == databases.get(name)) {
			databases.put(name, new Mysql(minIdle, maxIdle, initialSize, timeout,
					host + (null == port ? "" : ":" + port), database, username, password));
			logger.debug("Append mysql {} {} {}", host, database, username);
		} else {
			logger.warn("Mysql {} is already exist", name);
		}
	}

	/**
	 * 追加一个redis数据源，如果已经存在则不再重新添加并警告
	 */
	public static synchronized void appendRedis(String name, Integer minIdle, Integer maxIdle, Integer timeout,
			String host, Integer port, String database, String password) {
		if (null == databases.get(name)) {
			databases.put(name,
					new org.think2framework.orm.core.Redis(minIdle, maxIdle, timeout, database, host, port, password));
			logger.debug("Append redis {} {}", host, database);
		} else {
			logger.warn("Redis {} is already exist", name);
		}
	}

	/**
	 * 追加一个sqlserver数据源，如果已经存在则不再重新添加并警告
	 */
	public static synchronized void appendSqlserver(String name, Integer minIdle, Integer maxIdle, Integer initialSize,
			Integer timeout, String host, Integer port, String database, String username, String password) {
		if (null == databases.get(name)) {
			databases.put(name, new Sqlserver(minIdle, maxIdle, initialSize, timeout,
					host + (null == port ? "" : ":" + port), database, username, password));
			logger.debug("Append sqlserver {} {} {}", host, database, username);
		} else {
			logger.warn("Sqlserver {} is already exist", name);
		}
	}

	/**
	 * 追加一个sqlite数据源，如果已经存在则不再重新添加并警告
	 */
	public static synchronized void appendSqlite(String name, Integer minIdle, Integer maxIdle, Integer initialSize,
			Integer timeout, String host, Integer port, String database, String username, String password) {
		if (null == databases.get(name)) {
			databases.put(name, new Sqlite(minIdle, maxIdle, initialSize, timeout,
					host + (null == port ? "" : ":" + port), database, username, password));
			logger.debug("Append sqlite {} {} {}", host, database, username);
		} else {
			logger.warn("Sqlite {} is already exist", name);
		}
	}

	/**
	 * 追加一个oracle数据源，如果已经存在则不再重新添加并警告
	 */
	public static synchronized void appendOracle(String name, Integer minIdle, Integer maxIdle, Integer initialSize,
			Integer timeout, String host, Integer port, String database, String username, String password) {
		if (null == databases.get(name)) {
			databases.put(name, new Oracle(minIdle, maxIdle, initialSize, timeout,
					host + (null == port ? "" : ":" + port), database, username, password));
			logger.debug("Append oracle {} {} {}", host, database, username);
		} else {
			logger.warn("Oracle {} is already exist", name);
		}
	}

	/**
	 * 追加一个表格，如果已经存在则不追加并警告
	 *
	 * @param name
	 *            名称
	 * @param table
	 *            表格
	 */
	public static synchronized void appendTable(String name, Table table) {
		if (null == tables.get(name)) {
			tables.put(name, table);
			logger.debug("Append table {}", name);
		} else {
			logger.warn("Table {} is already exist", name);
		}
	}

	/**
	 * 追加一个实体，如果已经存在则不追加并警告
	 *
	 * @param name
	 *            名称
	 * @param entity
	 *            实体
	 */
	public static synchronized void appendEntity(String name, Entity entity) {
		if (null == entities.get(name)) {
			entities.put(name, entity);
			logger.debug("Append entity {}", name);
		} else {
			logger.warn("Entity {} is already exist", name);
		}
	}

	/**
	 * 根据名称获取数据库
	 *
	 * @param name
	 *            数据源名称
	 * @return 数据库
	 */
	private static Database getDatabase(String name) {
		Database database = databases.get(name);
		if (null == database) {
			throw new NonExistException(Database.class.getName() + " " + name);
		}
		return database;
	}

	/**
	 * 创建一个redis处理器
	 *
	 * @param name
	 *            redis数据源名称
	 * @return redis
	 */
	public static Redis createRedis(String name) {
		if (StringUtils.isBlank(name)) {
			return null;
		}
		Database database = getDatabase(name);
		try {
			JedisPool jedisPool = database.getJedisPool();
			if (null == jedisPool) {
				throw new NonsupportException("Datasource " + name + " get redis");
			}
			return new Redis(jedisPool);
		} catch (NonsupportException e) {
			throw new NonsupportException("Datasource " + name + " get redis");
		} catch (Exception e) {
			logger.error("Create jedis {} error {}", name, e.getMessage());
			return null;
		}
	}

	/**
	 * 根据模型名称创建一个查询生成器
	 *
	 * @param database
	 *            数据库名称
	 * @param name
	 *            模型名称
	 * @param redis
	 *            redis数据源名称
	 * @param valid
	 *            redis缓存有效期
	 * @return 查询生成器
	 */
	public static Query createQuery(String database, String name, String redis, Integer valid) {
		Entity entity = entities.get(name);
		if (null == entity) {
			throw new NonExistException(Entity.class.getName() + " " + name);
		}
		return new Query(entity, getDatabase(database), createRedis(redis), valid);
	}

	/**
	 * 根据模型名称创建一个查询生成器
	 *
	 * @param database
	 *            数据库名称
	 * @param name
	 *            模型名称
	 * @return 查询生成器
	 */
	public static Query createQuery(String database, String name) {
		return createQuery(database, name, null, 0);
	}

	/**
	 * 根据模型名称创建一个写入生成器
	 *
	 * @param name
	 *            模型名称
	 * @return 写入生成器
	 */
	public static Writer createWriter(String database, String name) {
		Table table = tables.get(name);
		if (null == table) {
			throw new NonExistException(Table.class.getName() + " " + name);
		}
		Database db = getDatabase(database);
		return new Writer(table, db.getKeySignBegin(), db.getKeySignEnd(), db.toCreate(table), db.getJdbcTemplate());
	}

}
