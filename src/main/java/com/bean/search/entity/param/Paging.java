package com.bean.search.entity.param;

/**
 * @author ：hwj
 * @version 版本号：V1.0
 * @Description ：分页参数
 */
public class Paging {
    /**
     * 分页：最大条数（用于分页）
     */
    private int size;
    /**
     * 分页：查询偏移条数（用于分页）
     */
    private long offset;

    public Paging(int size, long offset) {
        this.size = size;
        this.offset = offset;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getOffset() {
        return this.offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }
}
