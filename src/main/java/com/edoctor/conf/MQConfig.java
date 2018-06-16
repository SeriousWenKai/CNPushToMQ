package com.edoctor.conf;

import com.edoctor.bean.DeviceLog;
import com.edoctor.controller.ConsumeMQController;
import com.edoctor.utils.INetUtils;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.edoctor.utils.INetUtils.getLocalhostIP;

@Configuration
public class MQConfig {
    private static Logger LOG = LoggerFactory.getLogger(MQConfig.class);

    public static String mqDestinationName = "DeviceLog.JMSTemplate.queue1";

    // 配置MQ连接工厂信息
    @Profile("FromNginx")
    private ActiveMQConnectionFactory getMessageToMQ() {
        ActiveMQConnectionFactory mqConnectionFactory = new ActiveMQConnectionFactory();
        mqConnectionFactory.setBrokerURL("tcp://localhost:61616");
        mqConnectionFactory.setTrustedPackages(Arrays.asList("com.edoctor.bean"));
        return mqConnectionFactory;
    }

    // 配置MQ连接工厂信息
    @Profile("ToMongo")
    private ActiveMQConnectionFactory getFromMQToMongo() {
        String targetMQAddress = "";
        List<String> localAddress = INetUtils.getMatchPatternIP(getLocalhostIP(), "10\\.10\\.119\\..*");
        targetMQAddress = localAddress.size() > 0 ? localAddress.get(0) : "noHost";
        // 对应不同的插入数据库操作，订阅使用不同的IP
        switch(targetMQAddress) {
            case "10.10.119.9" : targetMQAddress = "10.10.119.7";break;
            case "10.10.119.10" : targetMQAddress = "10.10.119.8";break;
            default : targetMQAddress = "noHost";
        }
        System.out.println("[targetMQAddress] = " + targetMQAddress);
        ActiveMQConnectionFactory mqConnectionFactory = new ActiveMQConnectionFactory();
        mqConnectionFactory.setBrokerURL("tcp://" + targetMQAddress + ":61616");
        mqConnectionFactory.setTrustedPackages(Arrays.asList("com.edoctor.bean"));
        return mqConnectionFactory;
    }

    // 将连接工厂注入，生成池化的带有异步的Pool
    @Profile("FromNginx")
    @Bean
    public PooledConnectionFactory pooledConnectionFactoryToMQ() {
        PooledConnectionFactory connectionFactory = new PooledConnectionFactory();
        connectionFactory.setConnectionFactory(getMessageToMQ());
        connectionFactory.setUseAnonymousProducers(true);
        return connectionFactory;
    }

    // 将连接工厂注入，生成池化的带有异步的Pool
    @Profile("ToMongo")
    @Bean
    public PooledConnectionFactory pooledConnectionFactoryFromMQ() {
        PooledConnectionFactory connectionFactory = new PooledConnectionFactory();
        connectionFactory.setConnectionFactory(getFromMQToMongo());
        connectionFactory.setUseAnonymousProducers(true);
        return connectionFactory;
    }

    // 生成JMS消息访问的模板类，使用JmsOperations简化消息的生成和读取
    @Bean
    public JmsTemplate getJmsTemplate(PooledConnectionFactory cf) {
        JmsTemplate jmsTemplate = new JmsTemplate(cf);
        jmsTemplate.setDefaultDestinationName(mqDestinationName);
        jmsTemplate.setMessageConverter(getJacksonMessageConverter());
        // pubSubDomain = true 为队列模式，false为订阅发布模式
        jmsTemplate.setPubSubDomain(false);
        return jmsTemplate;
    }

    //设置发送和接受的转换器为MappingJackson2MessageConverter
    @Bean
    public MappingJackson2MessageConverter getJacksonMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        Map<String, Class<?>> typeIdMap = new HashMap<>();
        typeIdMap.put("DeviceLog", DeviceLog.class);
        converter.setTypeIdMappings(typeIdMap);
        converter.setTypeIdPropertyName("DeviceLog");
        converter.setEncoding("UTF-8");
        return converter;
    }

    // 异步接受消息的消息监听器-适配器，这个为异步监听接受的处理方法以及类名称
    @Profile("ToMongo")
    @Bean
    MessageListenerAdapter messageListenerAdapter(MappingJackson2MessageConverter mappingJackson2MessageConverter) {
        MessageListenerAdapter adapter = new MessageListenerAdapter();//改：标记为控制器类P478页
        adapter.setDelegate(new ConsumeMQController());
        adapter.setDefaultListenerMethod("insertIntoMongo");
        adapter.setMessageConverter(mappingJackson2MessageConverter);
        return adapter;
    }

    // 异步接受消息的消息监听器-容器
    @Profile("ToMongo")
    @Bean
    DefaultMessageListenerContainer defaultMessageListenerContainer(PooledConnectionFactory connectionFactory, MessageListenerAdapter adapter) {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setDestinationName(mqDestinationName);
        container.setMessageListener(adapter);
        //设置接受MQ消息的事物性回滚
        container.setSessionTransacted(true);
        return container;
    }

}
