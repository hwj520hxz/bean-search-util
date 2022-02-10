package com.bean.search.extractor;

import com.bean.search.entity.param.Paging;

import java.util.Map;

/**
 * 分页提取器
 *
 * @author hwj
 *
 */
public interface PageExtractor {

    /**
     * @param paraMap 检索参数
     * @return 分页信息
     */
    Paging extract(Map<String, Object> paraMap);

    /**
     * @return 最大条数的参数名
     * */
    String getSizeName();
}
