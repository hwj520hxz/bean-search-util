package com.bean.search.entity.param;

import com.bean.search.util.StringUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author ：hwj
 * @version 版本号：V1.0
 * @Description ：字段参数
 */
public class FieldParam {

    /**
     * 字段名
     */
    private final String name;
    /**
     * 字段运算符
     */
    private Operator operator;
    /**
     * 参数值
     */
    private final List<Value> values;
    /**
     * 是否忽略大小写
     */
    private Boolean ignoreCase;

    public FieldParam(String name, Operator operator) {
        this(name, operator, Collections.emptyList(), false);
    }

    public FieldParam(String name, List<FieldParam.Value> values) {
        this.name = name;
        this.values = values;
    }

    public FieldParam(String name, Operator operator, List<FieldParam.Value> values, boolean ignoreCase) {
        this.name = name;
        this.operator = operator;
        this.values = values;
        this.ignoreCase = ignoreCase;
    }

    public String getName() {
        return this.name;
    }

    public Object[] getValues() {
        this.values.sort(Comparator.comparingInt((v) -> v.index)); // 根据索引排序
        Object[] objects = new Object[this.values.size()];
        for(int i = 0; i < this.values.size(); ++i) {
            objects[i] = this.values.get(i).value;
        }

        return objects;
    }

    public List<Value> getValueList() {
        return this.values;
    }

    public Boolean isIgnoreCase() {
        return this.ignoreCase;
    }

    public Operator getOperator() {
        return this.operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public void setIgnoreCase(Boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    /**
     * 字段参数值
     */
    public static class Value {
        private final Object value;
        private final int index;

        public Value(Object value, int index) {
            this.value = value;
            this.index = index;
        }

        public boolean isEmptyValue() {
            return this.value == null || this.value instanceof String && StringUtils.isBlank((String)this.value);
        }

        public Object getValue() {
            return this.value;
        }
    }
}
