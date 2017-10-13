package org.think2framework.orm.core;

import org.think2framework.orm.bean.EntityColumn;
import org.think2framework.orm.bean.Filter;
import org.think2framework.orm.bean.Order;
import org.think2framework.orm.bean.Table;
import org.springframework.jdbc.core.JdbcTemplate;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;

/**
 * Created by zhoubin on 2017/6/6. 数据库接口
 */
public interface Database {

    /**
     * 获取数据库对应的处理JdbcTemplate，如果是redis则返回null
     *
     * @return JdbcTemplate
     */
    JdbcTemplate getJdbcTemplate();

    /**
     * 获取redis数据库对应的处理jedis连接池，如果不是redis则返回null
     *
     * @return jedisPool
     */
    JedisPool getJedisPool();

    /**
     * 数据库字段的符号,使用keySign把数据库关键字包起来,防止字段为关键字,关键字前符号
     *
     * @return 符号
     */
    String getKeySignBegin();

    /**
     * 数据库字段的符号,使用keySign把数据库关键字包起来,防止字段为关键字,关键字后符号
     *
     * @return 符号
     */
    String getKeySignEnd();

    /**
     * 根据表定义获取mysql创建sql语句
     *
     * @param table 表
     * @return 创建sql
     */
    String toCreate(Table table);

    Object[] toSelect(String table, String joinSql, String fields, List<Filter> filters,
                      Map<String, EntityColumn> columns, List<String> group, List<Order> orders, Integer page, Integer size);
}
