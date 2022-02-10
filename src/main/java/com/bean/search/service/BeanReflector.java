package com.bean.search.service;

import com.bean.search.entity.BeanMeta;

import java.util.List;
import java.util.function.Function;

/**
 * Bean 反射器
 *
 * @author hwj
 *
 * */
public interface BeanReflector {

    /**
     * @param <T> bean 类型
     * @param beanMeta 元信息
     * @param fetchFields Bean 中需要反射赋值的字段
     * @param valueGetter 数据库字段值获取器（根据字段别名获取）
     * @return 反射的对象
     */
    <T> T reflect(BeanMeta<T> beanMeta, List<String> fetchFields, Function<String, Object> valueGetter);
}
