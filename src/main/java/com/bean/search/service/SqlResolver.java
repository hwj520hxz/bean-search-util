package com.bean.search.service;

import com.bean.search.entity.BeanMeta;
import com.bean.search.entity.SearchParam;
import com.bean.search.entity.SearchSql;

/**
 * @author ：hwj
 * @version 版本号：V1.0
 * @Description ：SQL解析器
 */
public interface SqlResolver {

    /**
     * @param beanMeta 元信息
     * @param searchParam 检索参数
     * @param <T> 泛型
     * @return 检索 SQL
     */
    <T> SearchSql<T> resolve(BeanMeta<T> beanMeta, SearchParam searchParam);
}
