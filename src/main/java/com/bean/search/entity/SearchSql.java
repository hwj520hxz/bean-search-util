package com.bean.search.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：hwj
 * @version 版本号：V1.0
 * @Description ：检索的 SQL 信息
 */
public class SearchSql<T> {
    /**
     * 待检索数据的元信息
     */
    private final BeanMeta<T> beanMeta;
    /**
     * 需要 Select 的字段
     */
    private final List<String> fetchFields;
    /**
     * 查询数据列表的SQL
     */
    private String listSqlString;
    /**
     * 查询聚族信息的SQL
     */
    private String clusterSqlString;
    /**
     * 查询数据列表的参数
     */
    private final List<Object> listSqlParams = new ArrayList();
    /**
     * 聚族查询的参数
     */
    private final List<Object> clusterSqlParams = new ArrayList();
    /**
     * 总条数别名
     */
    private String countAlias;
    /**
     * 求和字段别名
     */
    private final List<String> summaryAliases = new ArrayList();
    /**
     * 是否应该查询总条数
     */
    private boolean shouldQueryCluster;
    /**
     * 是否应该查询数据列表
     * */
    private boolean shouldQueryList;

    public SearchSql(BeanMeta<T> beanMeta, List<String> fetchFields) {
        this.beanMeta = beanMeta;
        this.fetchFields = fetchFields;
    }

    public BeanMeta<T> getBeanMeta() {
        return this.beanMeta;
    }

    public List<String> getFetchFields() {
        return this.fetchFields;
    }

    public String getListSqlString() {
        return this.listSqlString;
    }

    public void setListSqlString(String sqlString) {
        this.listSqlString = sqlString;
    }

    public List<Object> getListSqlParams() {
        return this.listSqlParams;
    }

    public void addListSqlParam(Object sqlParam) {
        this.listSqlParams.add(sqlParam);
    }

    public void addListSqlParams(List<Object> sqlParams) {
        this.listSqlParams.addAll(sqlParams);
    }

    public String getClusterSqlString() {
        return this.clusterSqlString;
    }

    public void setClusterSqlString(String sqlString) {
        this.clusterSqlString = sqlString;
    }

    public List<Object> getClusterSqlParams() {
        return this.clusterSqlParams;
    }

    public void addClusterSqlParam(Object sqlParam) {
        this.clusterSqlParams.add(sqlParam);
    }

    public String getCountAlias() {
        return this.countAlias;
    }

    public void setCountAlias(String countAlias) {
        this.countAlias = countAlias;
    }

    public void addSummaryAlias(String alias) {
        this.summaryAliases.add(alias);
    }

    public List<String> getSummaryAliases() {
        return this.summaryAliases;
    }

    public boolean isShouldQueryCluster() {
        return this.shouldQueryCluster;
    }

    public void setShouldQueryCluster(boolean shouldQueryCluster) {
        this.shouldQueryCluster = shouldQueryCluster;
    }

    public boolean isShouldQueryList() {
        return this.shouldQueryList;
    }

    public void setShouldQueryList(boolean shouldQueryList) {
        this.shouldQueryList = shouldQueryList;
    }
}
