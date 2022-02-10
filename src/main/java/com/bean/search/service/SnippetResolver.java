package com.bean.search.service;

import com.bean.search.entity.SqlSnippet;

/**
 * SQL 片段解析器
 * @author hwj
 * @since v3.0.0
 */
public interface SnippetResolver {

    /**
     * @param fragment SQL 碎片（非空）
     * @return 解析结果
     */
    SqlSnippet resolve(String fragment);
}
