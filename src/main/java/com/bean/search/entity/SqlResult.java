package com.bean.search.entity;

import com.bean.search.exception.SearchException;

import java.io.Closeable;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author ：hwj
 * @version 版本号：V1.0
 * @Description ：
 */
public class SqlResult<T> implements Closeable {

    private final SearchSql<T> searchSql;
    private ResultSet listResult; // 查询结果
    private ResultSet clusterResult;
    private boolean clusterNotReady = true;
    private Statement listStatement;  // 数据库查询预编译对象
    private Statement clusterStatement;

    public SqlResult(SearchSql<T> searchSql) {
        this.searchSql = searchSql;
    }

    public void close() throws IOException {
        try {
            if (this.listResult != null) {
                this.listResult.close();
            }

            if (this.clusterResult != null) {
                this.clusterResult.close();
            }

            if (this.listStatement != null) {
                this.listStatement.close();
            }

            if (this.clusterStatement != null) {
                this.clusterStatement.close();
            }

        } catch (SQLException var2) {
            throw new SearchException("Can not close statement or resultSet!", var2);
        }
    }

    public SearchSql<T> getSearchSql() {
        return this.searchSql;
    }

    public ResultSet getListResult() {
        return this.listResult;
    }

    public void setListResult(ResultSet listResult, Statement listStatement) {
        this.listResult = listResult;
        this.listStatement = listStatement;
    }

    public ResultSet getAlreadyClusterResult() throws SQLException {
        if (this.clusterResult != null && this.clusterNotReady) {
            this.clusterResult.next();
            this.clusterNotReady = false;
        }

        return this.clusterResult;
    }

    public void setClusterResult(ResultSet clusterResult, Statement clusterStatement) {
        this.clusterResult = clusterResult;
        this.clusterStatement = clusterStatement;
    }
}
