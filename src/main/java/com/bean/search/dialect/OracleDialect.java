package com.bean.search.dialect;

import com.bean.search.entity.param.Paging;

/**
 * @author ：hwj
 * @version 版本号：V1.0
 * @Description ：Oracle 方言实现
 */
public class OracleDialect implements Dialect {
    public OracleDialect() {
    }

    @Override
    public void toUpperCase(StringBuilder builder, String dbField) {
        builder.append("upper").append("(").append(dbField).append(")");
    }

    @Override
    public void truncateToDateStr(StringBuilder builder, String dbField) {
        builder.append("to_char(").append(dbField).append(", 'yy-mm-dd')");
    }

    @Override
    public void truncateToDateMinuteStr(StringBuilder builder, String dbField) {
        builder.append("to_char(").append(dbField).append(", 'yy-mm-dd hh24:mi')");
    }

    @Override
    public void truncateToDateSecondStr(StringBuilder builder, String dbField) {
        builder.append("to_char(").append(dbField).append(", 'yy-mm-dd hh24:mi:ss')");
    }

    @Override
    public PaginateSql forPaginate(String fieldSelectSql, String fromWhereSql, Paging paging) {
        PaginateSql paginateSql = new PaginateSql();
        if (paging == null) { // 无分页查询
            paginateSql.setSql(fieldSelectSql + fromWhereSql);
            return paginateSql;
        } else {
            String rowAlias;
            for(rowAlias = "row_"; fromWhereSql.contains(rowAlias); rowAlias = rowAlias + "_") {
            }
            StringBuilder builder = new StringBuilder();
            String tableAlias;
            for(tableAlias = "table_"; fromWhereSql.contains(tableAlias); tableAlias = tableAlias + "_") {
            }
            String rownumAlias;
            for(rownumAlias = "rownum_"; fieldSelectSql.contains(rownumAlias); rownumAlias = rownumAlias + "_") {
            }
            // 拼接带分页的SQL
            builder.append("select * from (select ").append(rowAlias).append(".*, rownum ").append(rownumAlias);
            builder.append(" from (").append(fieldSelectSql).append(fromWhereSql);
            builder.append(") ").append(rowAlias).append(" where rownum <= ?) ").append(tableAlias);
            builder.append(" where ").append(tableAlias).append(".").append(rownumAlias).append(" > ?");
            int size = paging.getSize();
            long offset = paging.getOffset();
            paginateSql.addParam(offset + (long)size);
            paginateSql.addParam(offset);
            paginateSql.setSql(builder.toString());
            return paginateSql;
        }
    }
}
