package com.bean.search.bean;

import com.bean.search.entity.param.Operator;

import java.lang.annotation.*;

/**
 * 用于注解一个可检索 bean 的属性
 * 来指定属性对应数据库的哪张表的哪个字段，可与 {@link SearchBean } 配合使用
 * 不可与 {@link DbIgnore } 在同一字段上使用
 * v3.0.0 后该注解可以缺省，缺省时, 如果 @SearchBean 注解同时缺省 tables 或指定了 autoMapTo，则根据 {@link com.bean.search.service.DbMapping } 自动映射到表字段
 * @author hwj
 * @since v1.0.0
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})

public @interface DbField {

    /**
     * 用于指定属性对应数据库的哪张表的哪个字段，例如:
     * u.username
     * 别名 u 由  @SearchBean 注解的 tables 值指定
     * v3.0.0 后可空，为空时以字段名映射列名
     * @return 数据库字段
     * */
    String value() default "";

    /**
     * @since v3.0.0
     * @return 该字段是否可以被作为检索条件
     */
    boolean conditional() default true;

    /**
     * 用于指定该字段只允许接受的运算符，为空时，表示任意运算符都接受
     * @since v3.0.0
     * @return Operator[]
     */
    Operator[] onlyOn() default {};
}
