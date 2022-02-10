package com.bean.search.filter;

import com.bean.search.entity.BeanMeta;

import java.util.Map;

/**
 * 检索参数过滤器
 * @author hwj
 */
public interface ParamFilter {

    /**
     * @param beanMeta 元信息
     * @param paraMap 过滤前的检索参数
     * @param <T> 泛型
     * @return 过滤后的检索参数
     */
    <T> Map<String, Object> doFilter(BeanMeta<T> beanMeta, Map<String, Object> paraMap);
}
