package org.think2framework.context.bean;

/**
 * 配置一个模型的基本数据库配置，在ModelFactory中如果创建query和writer的时候不传入数据库，则根据这个配置获取数据库名称
 */
public class Database {

	private String query; // 读取数据源名称

	private String writer; // 写入数据源名称

	private String redis; // 缓存redis数据源名称

	private Integer valid = 0; // redis缓存有效期，单位秒，0为永久有效

	public Database() {

	}

	public Database(String query, String writer, String redis, Integer valid) {
		this.query = query;
		this.writer = writer;
		this.redis = redis;
		this.valid = valid;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getRedis() {
		return redis;
	}

	public void setRedis(String redis) {
		this.redis = redis;
	}

	public Integer getValid() {
		return valid;
	}

	public void setValid(Integer valid) {
		this.valid = valid;
	}
}
