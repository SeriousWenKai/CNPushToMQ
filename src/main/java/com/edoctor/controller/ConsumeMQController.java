package com.edoctor.controller;

import com.edoctor.bean.DeviceLog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ConsumeMQController {
    private static final Logger logger = LogManager.getLogger(ConsumeMQController.class);

    public ConsumeMQController() {
    }

    // 因为Spring与容器之间的关系，无法再次直接注入Dao的Bean，因此这里使用HttpInvoke实现传递参数
    // 读到一条日志的时候执行的操作：1.存入Mongo;2.更新设备在线状态
    public void insertIntoMongo(DeviceLog deviceLog) {
        try {
            // 1.存入Mongo
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:9000/CNPushToMQ/iot/insertToMongo";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<String> entity = new HttpEntity<>(new JSONObject(deviceLog).toString(), headers);
            logger.info(LocalDateTime.now() + " [get message to Mongo] " + deviceLog.getDeviceId());
            restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            // 2.检索缓存
            String checkOnlineStatusKey = "OnlineStatus_" + deviceLog.getDeviceId();
            if (getStringValueFromRedis("OnlineStatus_" + deviceLog.getDeviceId()) == null) {
                // 不存在插入缓存，设置一小时倒计时并将设备状态更新为在线
                logger.info(deviceLog.getDeviceId() + " in redis is not in redis, then will create it");
                addKeyToRedis(checkOnlineStatusKey, "true");
                updateDeviceToOnline(deviceLog.getDeviceId());
            } else {
                // 存在的话，倒计时重置为一小时
                refreshKeyInRedis(checkOnlineStatusKey);
            }
            // 检查Key自然过期1小时时限将由EDoctor应用触发监听MessageListenerAdapter
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }

    @SuppressWarnings("unchecked")
    private void updateDeviceToOnline(String deviceId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:9000/CNPushToMQ/iot/updateDeviceOnlineInfo";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("deviceId", deviceId);
        body.add("isOnline", "true");
        HttpEntity entity = new HttpEntity(body, headers);
        restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
    }

    @SuppressWarnings("unchecked")
    public String getStringValueFromRedis(String key) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:9000/CNPushToMQ/iot/getStringValueFromRedis?key={key}";
        Map<String, String> map = new HashMap<>();
        map.put("key", key);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, map);
        return response.getBody();
    }

    @SuppressWarnings("unchecked")
    public void addKeyToRedis(String key, String value) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:9000/CNPushToMQ/iot/addKeyToRedis?key={key}&value={value}";
        Map<String, String> map = new HashMap<>();
        map.put("key", key);
        map.put("value", value);
        restTemplate.getForEntity(url, String.class, map);
    }

    @SuppressWarnings("unchecked")
    public void refreshKeyInRedis(String key) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:9000/CNPushToMQ/iot/refreshKeyInRedis?key={key}";
        Map<String, String> map = new HashMap<>();
        map.put("key", key);
        restTemplate.getForEntity(url, String.class, map);
    }

}
