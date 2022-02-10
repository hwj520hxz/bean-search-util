package com.bean.search.service;

import com.bean.search.entity.SearchSql;

import java.util.Map;

/**
 * Sql 拦截器
 * @author hwj
 * @since v3.0.0
 */
public interface SqlInterceptor {

    /**
     * Sql 拦截
     * @param <T> 泛型
     * @param searchSql 检索 SQL 信息（非空）
     * @param paraMap 原始检索参数（非空）
     * @return 新的检索 SQL（非空）
     */
    <T> SearchSql<T> intercept(SearchSql<T> searchSql, Map<String, Object> paraMap);
}
