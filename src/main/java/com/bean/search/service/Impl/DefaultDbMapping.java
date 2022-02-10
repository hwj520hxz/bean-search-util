package com.bean.search.service.Impl;

import com.bean.search.bean.DbField;
import com.bean.search.bean.DbIgnore;
import com.bean.search.bean.SearchBean;
import com.bean.search.entity.param.Operator;
import com.bean.search.exception.SearchException;
import com.bean.search.service.DbMapping;
import com.bean.search.util.StringUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

/***
 * 默认的数据库映射解析器
 * @author hwj
 * @since v3.1.0 从 DefaultMetaResolver 里分离出来
 */

@Service
public class DefaultDbMapping implements DbMapping {

    private static final Operator[] EMPTY_OPERATORS = {};

    // 表名前缀
    private String tablePrefix;

    // 表与列是否是大写风格
    private boolean upperCase = false;

    /**
     * 解析表片段
     * @param beanClass 所要解析的实体
     **/
    @Override
    public Table table(Class<?> beanClass) {
        SearchBean bean = beanClass.getAnnotation(SearchBean.class);
        if (bean != null) {
            return new Table(bean.dataSource().trim(),
                    tables(beanClass, bean),
                    bean.joinCond().trim(),
                    bean.groupBy().trim(),
                    bean.distinct()
            );
        }
        return new Table(null, toTableName(beanClass), "", "", false);
    }

    @Override
    public Column column(Field field) {
        String fieldSql = dbFieldSql(field);
        if (fieldSql == null) {
            return null;
        }
        DbField dbField = field.getAnnotation(DbField.class);
        if (dbField != null) {
            boolean conditional = dbField.conditional();
            Operator[] onlyOn = dbField.onlyOn();
            return new Column(fieldSql, conditional, onlyOn);
        }
        return new Column(fieldSql, true, EMPTY_OPERATORS);
    }

    protected String tables(Class<?> beanClass, SearchBean bean) {
        String tables = bean.tables();
        if (StringUtils.isBlank(tables)) {
            return toTableName(beanClass);
        }
        return tables.trim();
    }

    protected String toTableName(Class<?> beanClass) {
        String className = beanClass.getSimpleName(); // 获取实体类名
        String tables = StringUtils.toUnderline(className); // 将驼峰格式转化为下划线格式
        if (upperCase) {
            tables = tables.toUpperCase();
        }
        if (tablePrefix != null) {
            return tablePrefix + tables;
        }
        return tables;
    }

    /**
     * 将实体类字段转换为查询SQL的字段
     * @param field 实体类字段
     * @return 表名.表字段
     **/
    protected String dbFieldSql(Field field) {
        Class<?> beanClass = field.getDeclaringClass();
        DbField dbField = field.getAnnotation(DbField.class);
        if (field.getAnnotation(DbIgnore.class) != null) {
            if (dbField == null) {
                return null;
            }
            throw new SearchException("[" + beanClass.getName() + ": " + field.getName() + "] is annotated by @DbField and @DbIgnore, which are mutually exclusive.");
        }
        if (dbField != null) {
            String fieldSql = dbField.value().trim();
            if (StringUtils.isNotBlank(fieldSql)) {
                if (fieldSql.toLowerCase().startsWith("select ")) {
                    return "(" + fieldSql + ")";
                }
                return fieldSql;
            }
        }
        SearchBean bean = beanClass.getAnnotation(SearchBean.class);
        // 没加 @SearchBean 注解，或者加了但没给 tables 赋值，则可以自动映射列名，因为此时默认为单表映射
        if (bean == null || StringUtils.isBlank(bean.tables())) {
            // 默认使用下划线风格的字段映射
            return toColumnName(field);
        }
        String tab = bean.autoMapTo();
        if (StringUtils.isBlank(tab)) {
            return null;
        }
        return tab.trim() + "." + toColumnName(field);
    }

    private String toColumnName(Field field) {
        String column = StringUtils.toUnderline(field.getName());
        return upperCase ? column.toUpperCase() : column;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        if (StringUtils.isNotBlank(tablePrefix)) {
            this.tablePrefix = tablePrefix.trim();
        }
    }

    public boolean isUpperCase() {
        return upperCase;
    }

    public void setUpperCase(boolean upperCase) {
        this.upperCase = upperCase;
    }
}
