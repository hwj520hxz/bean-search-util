package com.bean.search.service.Impl;

import com.bean.search.entity.BeanMeta;
import com.bean.search.entity.FieldMeta;
import com.bean.search.entity.SqlSnippet;
import com.bean.search.exception.SearchException;
import com.bean.search.service.DbMapping;
import com.bean.search.service.MetaResolver;
import com.bean.search.service.SnippetResolver;
import com.bean.search.util.StringUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/***
 * 默认元信息解析器
 * @author hwj
 * @since v3.0.0
 */

@Service
public class DefaultMetaResolver implements MetaResolver {

    private final Map<Class<?>, BeanMeta<?>> cache = new ConcurrentHashMap<>();

    private SnippetResolver snippetResolver = new DefaultSnippetResolver();


    private DbMapping dbMapping;


    public DefaultMetaResolver() {
        this(new DefaultDbMapping());
    }

    public DefaultMetaResolver(DbMapping dbMapping) {
        this.dbMapping = dbMapping;
    }

    /**
     * 实体解析
     * @param beanClass 要解析的实体类
     **/
    @Override
    public <T> BeanMeta<T> resolve(Class<T> beanClass) {
        // 从cache中查询是否有该实体，有的话就直接返回不再解析
        @SuppressWarnings("unchecked")
        BeanMeta<T> beanMeta = (BeanMeta<T>) cache.get(beanClass);
        if (beanMeta != null) {
            return beanMeta;
        }
        // 同步完成实体的解析
        synchronized (cache) {
            // 解析实体
            beanMeta = resolveMetadata(beanClass);
            cache.put(beanClass, beanMeta);
            return beanMeta;
        }
    }

    /**
     *
     * @param beanClass 所要解析的实体
     **/
    protected <T> BeanMeta<T> resolveMetadata(Class<T> beanClass) {
        DbMapping.Table table = dbMapping.table(beanClass);
        if (table == null) {
            throw new SearchException("The class [" + beanClass.getName() + "] can not be searched, because it can not be resolved by " + dbMapping.getClass());
        }
        BeanMeta<T> beanMeta = new BeanMeta<>(beanClass, table.getDataSource(),
                snippetResolver.resolve(table.getTables()),
                snippetResolver.resolve(table.getJoinCond()),
                snippetResolver.resolve(table.getGroupBy()),
                table.isDistinct());
        // 字段解析
        Field[] fields = beanClass.getDeclaredFields();
        for (int index = 0; index < fields.length; index++) {
            Field field = fields[index];
            // TODO fields[index] -> field
            DbMapping.Column column = dbMapping.column(field);
            if (column == null) {
                continue;
            }
            FieldMeta fieldMeta = resolveFieldMeta(beanMeta, column, field, index);
            beanMeta.addFieldMeta(field.getName(), fieldMeta);
        }
        if (beanMeta.getFieldCount() == 0) {
            throw new SearchException("[" + beanClass.getName() + "] is not a valid SearchBean, because there is no field mapping to database.");
        }
        return beanMeta;
    }

    protected FieldMeta resolveFieldMeta(BeanMeta<?> beanMeta, DbMapping.Column column, Field field, int index) {
        Method setter = getSetterMethod(beanMeta.getBeanClass(), field);
        SqlSnippet snippet = snippetResolver.resolve(column.getFieldSql());
        // 注意：Oracle 数据库的别名不能以下划线开头
        return new FieldMeta(beanMeta, field, setter, snippet, "c_" + index, column.isConditional(), column.getOnlyOn());
    }

    protected Method getSetterMethod(Class<?> beanClass, Field field) {
        String fieldName = field.getName();
        try {
            return beanClass.getMethod("set" + StringUtils.firstCharToUpperCase(fieldName), field.getType());
        } catch (Exception e) {
            throw new SearchException("[" + beanClass.getName() + ": " + fieldName + "] is annotated by @DbField, but there is no correctly setter for it.", e);
        }
    }

    public SnippetResolver getSnippetResolver() {
        return snippetResolver;
    }

    public void setSnippetResolver(SnippetResolver snippetResolver) {
        this.snippetResolver = Objects.requireNonNull(snippetResolver);
    }

    public DbMapping getDbMapping() {
        return dbMapping;
    }

    public void setDbMapping(DbMapping dbMapping) {
        this.dbMapping = Objects.requireNonNull(dbMapping);
    }
}
