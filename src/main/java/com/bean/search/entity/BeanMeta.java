package com.bean.search.entity;

import java.util.*;

/**
 * @author ：hwj
 * @version 版本号：V1.0
 * @Description ：SearchBean 的元信息
 */
public class BeanMeta<T> {

    /**
     * 用户 Bean Class
     */
    private final Class<T> beanClass;
    /**
     * 所属数据源
     */
    private final String dataSource;
    /**
     * 表片段（对应@SearchBean->tables）
     * */
    private final SqlSnippet tableSnippet;
    /**
     * 连表条件对应@SearchBean->joinCond）
     * */
    private final SqlSnippet joinCondSnippet;
    /**
     * 分组字段（对应@SearchBean->groupBy）
     * */
    private final SqlSnippet groupBySnippet;
    /**
     * 是否 distinct 结果
     * */
    private final boolean distinct;
    /**
     * 映射: Bean属性 -> 属性元信息（对应实体类的每个字段）
     * */
    private final Map<String, FieldMeta> fieldMetaMap = new HashMap();

    public BeanMeta(Class<T> beanClass, String dataSource, SqlSnippet tableSnippet, SqlSnippet joinCondSnippet, SqlSnippet groupBySnippet, boolean distinct) {
        this.beanClass = beanClass;
        this.dataSource = dataSource;
        this.tableSnippet = tableSnippet;
        this.joinCondSnippet = joinCondSnippet;
        this.groupBySnippet = groupBySnippet;
        this.distinct = distinct;
    }

    /**
     * 添加字段
     **/
    public void addFieldMeta(String field, FieldMeta meta) {
        if(this.fieldMetaMap.containsKey(field)) {
            throw new SecurityException("不可以重复添加字段：" + field);
        } else {
            this.fieldMetaMap.put(field, meta);
        }
    }

    public Class<T> getBeanClass() {
        return this.beanClass;
    }

    public String getDataSource() {
        return this.dataSource;
    }

    public SqlSnippet getTableSnippet() {
        return this.tableSnippet;
    }

    /**
     * 获取连接条件
     **/
    public String getJoinCond() {
        return this.joinCondSnippet.getSnippet();
    }
    /**
     * 获取连接条件参数
     **/
    public List<SqlSnippet.Param> getJoinCondEmbedParams() {
        return this.joinCondSnippet.getParams();
    }
    /**
     * 获取分组SQL片段
     **/
    public String getGroupBy() {
        return this.groupBySnippet.getSnippet();
    }
    /**
     * 获取分组SQL片段参数
     **/
    public List<SqlSnippet.Param> getGroupByEmbedParams() {
        return this.groupBySnippet.getParams();
    }

    public boolean isDistinct() {
        return this.distinct;
    }
    /**
     * 返回不可修改的字段
     **/
    public Set<String> getFieldSet() {
        return Collections.unmodifiableSet(this.fieldMetaMap.keySet());
    }

    public int getFieldCount() {
        return this.fieldMetaMap.size();
    }


    /**
     * 校验字段是否存在
     * @param field 字段
     **/
    public FieldMeta requireFieldMeta(String field) {
        FieldMeta meta = this.getFieldMeta(field);
        if (meta == null) {
            throw new IllegalStateException("No such field named: " + field);
        } else {
            return meta;
        }
    }

    public FieldMeta getFieldMeta(String field) {
        return field != null ? this.fieldMetaMap.get(field) : null;
    }
    /**
     * 获取某字段的 SQL 片段
     * @param field Java 字段名
     * @return SQL 片段
     */
    public String getFieldSql(String field) {
        FieldMeta meta = this.getFieldMeta(field);
        return meta != null ? meta.getFieldSql().getSnippet() : null;
    }

    public Collection<FieldMeta> getFieldMetas() {
        return Collections.unmodifiableCollection(this.fieldMetaMap.values());
    }
}
