package com.bean.search;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ：hwj
 * @version 版本号：V1.0
 * @Description ：
 */

@SpringBootApplication(exclude={DruidDataSourceAutoConfigure.class})
@MapperScan(basePackages = "com.bosssoft.data.analysis.dao")
public class Startup {
    public static void main(String[] args) {
        SpringApplication.run(Startup.class, args);
    }
}
