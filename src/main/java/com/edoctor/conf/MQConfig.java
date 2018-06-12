package com.edoctor.conf;

import com.edoctor.bean.DeviceLog;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MQConfig {

    // 配置MQ连接工厂信息
    private ActiveMQConnectionFactory getAMQFactory() {
        ActiveMQConnectionFactory mqConnectionFactory = new ActiveMQConnectionFactory();
        mqConnectionFactory.setBrokerURL("tcp://localhost:61616");
        mqConnectionFactory.setTrustedPackages(Arrays.asList("com.edoctor.bean"));
        return mqConnectionFactory;
    }

    // 将连接工厂注入，生成池化的带有异步的Pool
    @Bean
    public PooledConnectionFactory pooledConnectionFactory() {
        PooledConnectionFactory connectionFactory = new PooledConnectionFactory();
        connectionFactory.setConnectionFactory(getAMQFactory());
        connectionFactory.setUseAnonymousProducers(true);
        return connectionFactory;
    }

    // 生成JMS消息访问的模板类，使用JmsOperations简化消息的生成和读取
    @Bean
    public JmsTemplate getJmsTemplate(PooledConnectionFactory cf) {
        JmsTemplate jmsTemplate = new JmsTemplate(cf);
        jmsTemplate.setDefaultDestinationName("DeviceLog.JMSTemplate.queue1");
        jmsTemplate.setMessageConverter(getJacksonMessageConverter());
        // pubSubDomain = true 为队列模式，false为订阅发布模式
        jmsTemplate.setPubSubDomain(false);
        return jmsTemplate;
    }

    //设置发送和接受的转换器为MappingJackson2MessageConverter
    private MappingJackson2MessageConverter getJacksonMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        Map<String, Class<?>> typeIdMap = new HashMap<>();
        typeIdMap.put("DeviceLog", DeviceLog.class);
        converter.setTypeIdMappings(typeIdMap);
        converter.setTypeIdPropertyName("DeviceLog");
        converter.setEncoding("UTF-8");
        return converter;
    }

}
