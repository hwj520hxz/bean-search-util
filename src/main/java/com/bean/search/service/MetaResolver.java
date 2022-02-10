package com.bean.search.service;

import com.bean.search.entity.BeanMeta;

/***
 * Bean 的元信息 解析接口
 * @author hwj
 * @since v3.0.0
 * */
public interface MetaResolver {

    /**
     * @param <T> 泛型
     * @param beanClass 要检索的 bean 类型
     * @return Bean 的元信息
     * */
    <T> BeanMeta<T> resolve(Class<T> beanClass);
}
