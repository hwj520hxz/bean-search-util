package com.bean.search.service;

import com.bean.search.entity.SearchSql;
import com.bean.search.entity.SqlResult;

/**
 * @author ：hwj
 * @version 版本号：V1.0
 * @Description ：SQL执行器
 */
public interface SqlExecutor {

    /**
     * @param <T> 泛型
     * @param searchSql 检索 SQL
     * @return 执行结果
     */
    <T> SqlResult<T> execute(SearchSql<T> searchSql);

}
