package com.bean.search.dialect;

import com.bean.search.entity.param.Paging;

/**
 * @author ：hwj
 * @version 版本号：V1.0
 * @Description ：MySql 方言实现
 */
public class MySqlDialect implements Dialect{
    @Override
    public void toUpperCase(StringBuilder builder, String dbField) {
        builder.append("upper").append("(").append(dbField).append(")");
    }

    @Override
    public void truncateToDateStr(StringBuilder builder, String dbField) {
        builder.append("date_format(").append(dbField).append(", '%Y-%m-%d')");
    }

    @Override
    public void truncateToDateMinuteStr(StringBuilder builder, String dbField) {
        builder.append("date_format(").append(dbField).append(", '%Y-%m-%d %H:%i')");
    }

    @Override
    public void truncateToDateSecondStr(StringBuilder builder, String dbField) {
        builder.append("date_format(").append(dbField).append(", '%Y-%m-%d %H:%i:%s')");
    }

    @Override
    public PaginateSql forPaginate(String fieldSelectSql, String fromWhereSql, Paging paging) {
        PaginateSql paginateSql = new PaginateSql();
        StringBuilder ret = new StringBuilder();
        ret.append(fieldSelectSql).append(fromWhereSql); // 拼接select、from、where部分SQL
        if (paging != null) {
            ret.append(" limit ?, ?");
            paginateSql.addParam(paging.getOffset());
            paginateSql.addParam(paging.getSize());
        }
        paginateSql.setSql(ret.toString());
        return paginateSql;
    }
}
