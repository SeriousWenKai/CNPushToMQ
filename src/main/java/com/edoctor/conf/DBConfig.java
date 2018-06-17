package com.edoctor.conf;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
public class DBConfig {

    @Profile("ToMongo")
    @Bean
    public DataSource mysqlDataSourceTest() {
        BasicDataSource ds = new BasicDataSource();
        ds.setMaxIdle(6);
        ds.setMinIdle(4);
        ds.setInitialSize(10);
        ds.setDefaultAutoCommit(true);
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://123.207.162.21:3306/edoctor?serverTimezone=GMT%2B8&useSSL=false&characterEncoding=utf-8");
        ds.setUsername("chinanet");
        ds.setPassword("EDoctor@666");
        return ds;
    }

}
