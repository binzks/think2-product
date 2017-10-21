package org.think2framework.orm.core;

import org.think2framework.orm.bean.EntityColumn;
import org.think2framework.orm.bean.Filter;
import org.think2framework.orm.bean.Order;
import org.think2framework.orm.bean.Table;
import org.think2framework.utils.NumberUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Map;

/**
 * redis数据库接口实现
 */
public class Redis implements Database {

    private JedisPool jedisPool;

    public Redis(Integer minIdle, Integer maxIdle, Integer timeout, String db, String host, Integer port,
                 String password) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(timeout);
        jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password, NumberUtils.toInt(db));
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return null;
    }

    @Override
    public JedisPool getJedisPool() {
        return jedisPool;
    }

    @Override
    public String getKeySignBegin() {
        return null;
    }

    @Override
    public String getKeySignEnd() {
        return null;
    }

    @Override
    public String toCreate(Table table) {
        return null;
    }

    @Override
    public Object[] toSelect(String table, String joinSql, String fields, List<Filter> filters,
                             Map<String, EntityColumn> columns, List<String> group, List<Order> orders, Integer page, Integer size) {
        return new Object[0];
    }
}
