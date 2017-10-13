package org.think2framework.bean;

/**
 * 配置一个模型的基本数据库配置，在ModelFactory中如果创建query和writer的时候不传入数据库，则根据这个配置获取数据库名称
 */
public class Database {

	private String query;

	private String writer;

	private String redis;

	public Database() {

	}

	public Database(String query, String writer, String redis) {
		this.query = query;
		this.writer = writer;
		this.redis = redis;
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

}
