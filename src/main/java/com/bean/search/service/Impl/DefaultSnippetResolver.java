package com.bean.search.service.Impl;

import com.bean.search.entity.SqlSnippet;
import com.bean.search.exception.SearchException;
import com.bean.search.service.SnippetResolver;
import com.bean.search.util.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 默认内嵌参数解析器
 * @author hwj
 * @since v3.0.0
 */

@Service
public class DefaultSnippetResolver implements SnippetResolver {

    // 动态检索 @SearchBean(tables = ":table: o, user u"),其中:table就是动态表参数
    private String paramPrefix = ":";

    private String[] paramEndFlags = new String[] { " ", "\t", "\n", "\r", "+", "-", "*", "/", "=", "!", ">", "<", ",", ")", "'", "%" };

    private final char[] quotations = new char[] { '\'', '"' };

    @Override
    public SqlSnippet resolve(String fragment) {
        SqlSnippet sqlSnippet = newSqlSnippet();
        int index1 = fragment.indexOf(paramPrefix);
        while (index1 >= 0) {
            int index2 = findParamEndIndex(fragment, index1);
            String sqlName = getSqlName(fragment, index1, index2);
            if (StringUtils.isBlank(sqlName) || sqlName.length() < 2) {
                throw new SearchException("There is a syntax error about embed param: " + fragment);
            }
            SqlSnippet.Param param = newSqlSnippetParam(sqlName);
            boolean endWithPrefix = sqlName.endsWith(paramPrefix);
            if (endWithPrefix) {
                param.setName(sqlName.substring(1, sqlName.length() - paramPrefix.length()));
            } else {
                param.setName(sqlName.substring(1));
            }
            int quotationCount1 = StringUtils.containCount(fragment, 0, index1, quotations);
            int quotationCount2 = StringUtils.containCount(fragment, Math.max(index1, index2), fragment.length(), quotations);
            if ((quotationCount1 + quotationCount2) % 2 != 0) {
                throw new SearchException("There is a syntax error (quotations mismatch): " + fragment);
            }
            int nextIndex = index1 + sqlName.length();
            // 判断虚拟参数是否不在引号内部，并且不是以 :name: 的形式
            if (quotationCount1 % 2 == 0 && !endWithPrefix) {
                param.setJdbcPara(true);
                fragment = fragment.replaceFirst(sqlName, "?");
                // sqlSnippet 长度变短，寻找下标也该相应提前
                nextIndex = nextIndex - sqlName.length() + 1;
            }
            sqlSnippet.addParam(param);
            index1 = fragment.indexOf(paramPrefix, nextIndex);
        }
        sqlSnippet.setSnippet(fragment);
        return sqlSnippet;
    }

    protected SqlSnippet newSqlSnippet() {
        return new SqlSnippet();
    }

    protected SqlSnippet.Param newSqlSnippetParam(String sqlName) {
        return new SqlSnippet.Param(sqlName);
    }

    protected String getSqlName(String sqlSnippet, int index1, int index2) {
        if (index2 > 0) {
            return sqlSnippet.substring(index1, index2);
        } else {
            return sqlSnippet.substring(index1);
        }
    }

    protected int findParamEndIndex(String sqlSnippet, int fromIndex) {
        int index = -1;
        for (String flag : paramEndFlags) {
            int index0 = sqlSnippet.indexOf(flag, fromIndex);
            if (index < 0) {
                index = index0;
            } else if (index0 > 0) {
                index = Math.min(index, index0);
            }
        }
        return index;
    }

    public String getParamPrefix() {
        return paramPrefix;
    }

    public void setParamPrefix(String paramPrefix) {
        this.paramPrefix = paramPrefix;
    }

    public String[] getParamEndFlags() {
        return paramEndFlags;
    }

    public void setParamEndFlags(String[] paramEndFlags) {
        this.paramEndFlags = paramEndFlags;
    }

    public char[] getQuotations() {
        return quotations;
    }
}
