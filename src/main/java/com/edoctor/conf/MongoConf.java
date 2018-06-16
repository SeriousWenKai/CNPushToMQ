package com.edoctor.conf;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

@Configuration
public class MongoConf extends AbstractMongoConfiguration{
    @Override
    protected String getDatabaseName() {
        return "EDoctor";
    }

    @Override
    public MongoClient mongoClient() {
//        return new MongoClient("59.110.231.87");
        return new MongoClient("10.10.119.4");
    }


}
