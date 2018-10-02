package com.edoctor.conf;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
public class DBConfig {

    @Bean
    @Profile({"FromNginx", "ToMongo"})
    public DataSource FromNginxAndToMongo() {
        BasicDataSource ds = new BasicDataSource();
        ds.setMaxIdle(6);
        ds.setMinIdle(4);
        ds.setInitialSize(10);
        ds.setDefaultAutoCommit(true);
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://10.10.119.5:3306/edoctor?serverTimezone=GMT%2B8&useSSL=false&characterEncoding=utf-8");
        ds.setUsername("chinanet");
        ds.setPassword("EDoctor@666");
        return ds;
    }

    @Bean
    @Profile("test")
    public DataSource test() {
        BasicDataSource ds = new BasicDataSource();
        ds.setMaxIdle(6);
        ds.setMinIdle(4);
        ds.setInitialSize(10);
        ds.setDefaultAutoCommit(true);
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/edoctor?serverTimezone=GMT%2B8&useSSL=false&useUnicode=true&character_set_server=utf8mb4&&characterEncoding=utf-8");
        ds.setUsername("root");
        ds.setPassword("123456");
        return ds;
    }

}
