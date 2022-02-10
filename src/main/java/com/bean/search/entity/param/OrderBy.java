package com.bean.search.entity.param;

/**
 * @author ：hwj
 * @version 版本号：V1.0
 * @Description ：排序参数
 */
public class OrderBy {
    /**
     * 排序字段（用于排序）
     */
    private String sort;
    /**
     * 排序方法：desc, asc（用于排序）
     */
    private String order;

    public OrderBy(String sort, String order) {
        this.sort = sort;
        this.order = order;
    }

    public String getSort() {
        return this.sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return this.order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
