package org.think2framework.orm.core;

import org.think2framework.orm.bean.EntityColumn;
import org.think2framework.orm.bean.Filter;
import org.think2framework.orm.bean.Order;
import org.think2framework.orm.bean.Table;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;

/**
 * Created by zhoubin on 2017/6/6. sqlserver
 */
public class Sqlserver implements Database {

    private JdbcTemplate jdbcTemplate;

    public Sqlserver(Integer minIdle, Integer maxIdle, Integer initialSize, Integer timeout, String address,
                     String database, String username, String password) {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setMinIdle(minIdle);
        basicDataSource.setMaxIdle(maxIdle);
        basicDataSource.setInitialSize(initialSize);
        basicDataSource.setRemoveAbandonedOnBorrow(true);
        basicDataSource.setRemoveAbandonedTimeout(timeout);
        basicDataSource.setLogAbandoned(true);
        basicDataSource.setValidationQuery("SELECT 1");
        basicDataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        basicDataSource.setUrl("jdbc:sqlserver://" + address + ";databaseName=" + database);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);
        this.jdbcTemplate = new JdbcTemplate(basicDataSource);
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    public JedisPool getJedisPool() {
        return null;
    }

    @Override
    public String getKeySignBegin() {
        return "[";
    }

    @Override
    public String getKeySignEnd() {
        return "]";
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
