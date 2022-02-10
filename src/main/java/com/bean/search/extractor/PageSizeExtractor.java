package com.bean.search.extractor;

import com.bean.search.util.ObjectUtils;

import java.util.Map;

/**
 * @author ：hwj
 * @version 版本号：V1.0
 * @Description ：
 */
public class PageSizeExtractor extends BasePageExtractor {
    /**
     * 偏移条数字段参数名
     */
    private String pageName = "page";

    @Override
    protected long toOffset(Map<String, Object> paraMap, int size) {
        Object value = paraMap.get(pageName);
        Long page = ObjectUtils.toLong(value);
        if (page == null) {
            return 0;
        }
        return Math.max(page - getStart(), 0) * size;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getPageName() {
        return pageName;
    }
}
