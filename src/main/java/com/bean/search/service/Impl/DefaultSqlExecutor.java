package com.bean.search.service.Impl;

import com.bean.search.entity.BeanMeta;
import com.bean.search.entity.SearchSql;
import com.bean.search.entity.SqlResult;
import com.bean.search.exception.SearchException;
import com.bean.search.service.SqlExecutor;
import com.bean.search.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author ：hwj
 * @version 版本号：V1.0
 * @Description ：
 */

@Service
public class DefaultSqlExecutor implements SqlExecutor {

    protected Logger log = LoggerFactory.getLogger(DefaultSqlExecutor.class);

    @Autowired
    private DataSource dataSource; // 数据库连接对象
    private final Map<String, DataSource> dataSourceMap = new ConcurrentHashMap();
    private boolean transactional = false; // 是否开启事务
    /**
     * 2：支持允许非重复读取和幻像读取的事务
     * 1：支持允许脏读，不可重复读和幻像读的事务
     * 4：支持仅允许幻像读取的事务
     * 0：不支持事务
     * 8：支持事务，不允许脏读，不可重复读和幻像读
     **/
    private int transactionIsolation = 2; // 事务隔离级别

    /**
     * 无参构造函数
     **/
    public DefaultSqlExecutor() {
    }
    /**
     * 构造函数
     * @param dataSource 数据库连接对象
     **/
    public DefaultSqlExecutor(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> SqlResult<T> execute(SearchSql<T> searchSql) {
        // 如果查询结果不为空则直接返回查询结果
        if (!searchSql.isShouldQueryList() && !searchSql.isShouldQueryCluster()) {
            return new SqlResult(searchSql);
        } else {
            Connection connection;
            try {
                connection =  this.getConnection(searchSql.getBeanMeta()); // 数据库连接查询
            } catch (SQLException var5) {
                throw new SearchException("Can not get connection from dataSource!", var5);
            }
            try {
                return this.doExecute(searchSql, connection);
            } catch (SQLException var4) {
                this.closeConnection(connection);
                throw new SearchException("A exception occurred when query!", var4);
            }

        }
    }

    protected Connection getConnection(BeanMeta<?> beanMeta) throws SQLException {
        String name = beanMeta.getDataSource();
        if (StringUtils.isBlank(name)) {
            if (this.dataSource == null) {
                throw new SearchException("There is no default dataSource for " + beanMeta.getBeanClass());
            } else {
                return this.dataSource.getConnection();
            }
        } else {
            DataSource dataSource = this.dataSourceMap.get(name);
            if (dataSource == null) {
                throw new SearchException("There is no dataSource named " + name + " for " + beanMeta.getBeanClass());
            } else {
                return dataSource.getConnection(); // 连接数据库
            }
        }
    }

    protected <T> SqlResult<T> doExecute(SearchSql<T> searchSql, final Connection connection) throws SQLException {
        if (this.transactional) {
            connection.setAutoCommit(false); // 是否开启事务
            connection.setTransactionIsolation(this.transactionIsolation); // 设置连接的事务隔离级别
            connection.setReadOnly(true); // 设置只读
        }

        SqlResult result = new SqlResult<T>(searchSql) {
            public void close() {
                try {
                    try {
                        super.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } finally {
                    DefaultSqlExecutor.this.closeConnection(connection);
                }
            }
        };

        try {
            String sql;
            List params;
            if (searchSql.isShouldQueryList()) {
                sql = searchSql.getListSqlString();
                params = searchSql.getListSqlParams();
                this.writeLog(sql, params);
                this.executeListSqlAndCollectResult(connection, sql, params, result); // 数据库查询
            }

            if (searchSql.isShouldQueryCluster()) {
                sql = searchSql.getClusterSqlString();
                params = searchSql.getClusterSqlParams();
                this.writeLog(sql, params);
                this.executeClusterSqlAndCollectResult(connection, sql, params, result);
            }
        } finally {
            if (this.transactional) { // 如果开启事务需要执行commit
                connection.commit();
                connection.setReadOnly(false);
            }

        }

        return result;
    }

    protected void writeLog(String sql, List<Object> params) {
        this.log.debug("bean-searcher - sql ---- {}", sql);
        this.log.debug("bean-searcher - params - {}", params);
    }

    /**
     *
     * @param connection 数据库连接对象
     * @param sql 查询语句
     * @param params 查询参数
     * @param sqlResult 查询结果
     **/
    protected void executeListSqlAndCollectResult(Connection connection, String sql, List<Object> params, SqlResult<?> sqlResult) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql); // 数据库查询对象
        this.setStatementParams(statement, params); // 设置参数
        ResultSet resultSet = statement.executeQuery(); // 返回结果集
        sqlResult.setListResult(resultSet, statement);
    }

    protected void executeClusterSqlAndCollectResult(Connection connection, String sqlString, List<Object> sqlParams, SqlResult<?> sqlResult) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sqlString);
        this.setStatementParams(statement, sqlParams);
        ResultSet resultSet = statement.executeQuery();
        sqlResult.setClusterResult(resultSet, statement);
    }

    /**
     * SQL参数注入
     **/
    protected void setStatementParams(PreparedStatement statement, List<Object> params) throws SQLException {
        for(int i = 0; i < params.size(); ++i) {
            statement.setObject(i + 1, params.get(i));
        }

    }

    protected void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }

        } catch (SQLException var3) {
            throw new SearchException("Can not close connection!", var3);
        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = (DataSource) Objects.requireNonNull(dataSource);
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }

    public void setDataSource(String name, DataSource dataSource) {
        if (name != null && dataSource != null) {
            this.dataSourceMap.put(name.trim(), dataSource);
        }

    }
    @Deprecated
    public void addDataSource(String name, DataSource dataSource) {
        this.setDataSource(name, dataSource);
    }

    public Map<String, DataSource> getDataSourceMap() {
        return this.dataSourceMap;
    }

    public void setTransactional(boolean transactional) {
        this.transactional = transactional;
    }

    public boolean isTransactional() {
        return this.transactional;
    }

    public int getTransactionIsolation() {
        return this.transactionIsolation;
    }

    public void setTransactionIsolation(int level) {
        this.transactionIsolation = level;
    }
}
