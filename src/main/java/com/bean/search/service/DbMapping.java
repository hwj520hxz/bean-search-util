package com.bean.search.service;

import com.bean.search.entity.param.Operator;

import java.lang.reflect.Field;

/**
 * 在 {@link SearchBean } 或 {@link DbField } 缺省时
 * 自动与数据库表名与字段名映射
 * @author hwj
 * @since v3.0.0
 */
public interface DbMapping {

    /**
     * 根据 beanClass 获取表名
     * 在 SearchBean 的类上没加 {@link SearchBean } 注解时 或 {@link SearchBean#tables()} 属性为空时，根据该方法自动映射
     * @param beanClass SearchBean 的 Class
     * @return {@link Table }，若返回 null，则表示 beanClass 不支持检索
     */
    Table table(Class<?> beanClass);

    /**
     * 根据 field 获取表列名
     * 在 SearchBean 的某字段上没加 {@link DbField } 注解，同时没加 {@link SearchBean } 注解
     * 或 {@link SearchBean#tables()} 属性为空 或指定了 {@link SearchBean#autoMapTo()} 属性时，根据该方法自动自动映射字段
     * @param field 类字段
     * @return {@link Column }，若返回 null，则表示忽略该字段
     */
    Column column(Field field);

    /**
     * 表信息
     * @since v3.1.0
     */
    class Table {

        /**
         * 所属数据源
         */
        private final String dataSource;

        /**
         * 表片段，对应@SearchBean->tables
         * */
        private final String tables;

        /**
         * 连表条件，对应@SearchBean->joinCond
         * */
        private final String joinCond;

        /**
         * 分组字段，对应@SearchBean->groupBy
         * */
        private final String groupBy;

        /**
         * 是否 distinct 结果
         * */
        private final boolean distinct;


        public Table(String dataSource, String tables, String joinCond, String groupBy, boolean distinct) {
            this.dataSource = dataSource;
            this.tables = tables;
            this.joinCond = joinCond;
            this.groupBy = groupBy;
            this.distinct = distinct;
        }

        public String getDataSource() {
            return dataSource;
        }

        public String getTables() {
            return tables;
        }

        public String getJoinCond() {
            return joinCond;
        }

        public String getGroupBy() {
            return groupBy;
        }

        public boolean isDistinct() {
            return distinct;
        }

    }

    /**
     * 列信息
     * @since v3.1.0
     */
    class Column {

        /**
         * 该字段对应的 SQL 片段
         */
        private final String fieldSql;

        /**
         * 该字段是否可作为检索参数
         */
        private final boolean conditional;

        /**
         * 该字段可作为检索时，被允许的运算符
         */
        private final Operator[] onlyOn;

        public Column(String fieldSql, boolean conditional, Operator[] onlyOn) {
            this.fieldSql = fieldSql;
            this.conditional = conditional;
            this.onlyOn = onlyOn;
        }

        public String getFieldSql() {
            return fieldSql;
        }

        public boolean isConditional() {
            return conditional;
        }

        public Operator[] getOnlyOn() {
            return onlyOn;
        }

    }
}
