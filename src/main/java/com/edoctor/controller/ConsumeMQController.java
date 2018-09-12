package com.edoctor.controller;

import com.edoctor.bean.DeviceLog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

public class ConsumeMQController {
    static int count = 0;
    Logger LOG = LogManager.getLogger(ConsumeMQController.class);

    public ConsumeMQController(){}
    public void insertIntoMongo(DeviceLog deviceLog) {
        RestTemplate restTemplate=new RestTemplate();
        String url="http://localhost:9000/CNPushToMQ/iot/insertToMongo";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<>(new JSONObject(deviceLog).toString(), headers);
        LOG.info(LocalDateTime.now() + " [get message to Mongo] " + deviceLog.getDeviceId());
        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

}
