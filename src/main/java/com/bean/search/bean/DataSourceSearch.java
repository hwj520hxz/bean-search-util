package com.bean.search.bean;


import java.lang.annotation.*;


/**
 * 用于注解一个可检索 bean 对应的数据库类型，默认为oracle
 * 现阶段主要是在解析的时候区分分页语法
 * @author hwj
 * @since v1.0.0
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface DataSourceSearch {

    String dataSourceType() default "oracle";
}
