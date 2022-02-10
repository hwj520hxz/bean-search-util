package com.bean.search.dialect;

import com.bean.search.entity.param.Paging;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：hwj
 * @version 版本号：V1.0
 * @Description ：方言
 */
public interface Dialect {

    /**
     * 转大写
     **/
    void toUpperCase(StringBuilder var1, String var2);
    /**
     * 日期格式化（年-月-日）
     **/
    void truncateToDateStr(StringBuilder var1, String var2);
    /**
     * 日期格式化（年-月-日-时-分）
     **/
    void truncateToDateMinuteStr(StringBuilder var1, String var2);
    /**
     * 日期格式化（年-月-日-时-分-秒）
     **/
    void truncateToDateSecondStr(StringBuilder var1, String var2);
    /**
     * 分页
     * @param var1 SQL的查询字段部分
     * @param var2 SQL的from和where部分
     * @param var3 SQL分页的偏移量offset和size
     **/
    Dialect.PaginateSql forPaginate(String var1, String var2, Paging var3);

    /**
     * SQL分页
     **/
    public static class PaginateSql {
        private String sql; // 查询SQL
        private final List<Object> params = new ArrayList(2); // 参数，这里指的是分页的页码和每页大小

        public PaginateSql() {
        }

        public String getSql() {
            return this.sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }

        public List<Object> getParams() {
            return this.params;
        }

        /**
         * 添加参数
         **/
        public void addParam(Object param) {
            this.params.add(param);
        }

    }

}
