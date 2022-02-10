package com.bean.search.service;

import com.bean.search.entity.BeanMeta;
import com.bean.search.entity.SearchParam;
import com.bean.search.entity.param.FetchType;

import java.util.Map;

/***
 * 请求参数解析器接口
 *
 * @author hwj
 * */
public interface ParamResolver {

    /**
     * @param beanMeta 元数据
     * @param fetchType Fetch 类型
     * @param paraMap 原始检索参数
     * @return SearchParam
     * */
    SearchParam resolve(BeanMeta<?> beanMeta, FetchType fetchType, Map<String, Object> paraMap);
}
