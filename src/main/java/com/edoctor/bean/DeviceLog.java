package com.edoctor.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;

@Document
public class DeviceLog {

    @Id
    private String id;
    private String deviceId;
    private String deviceType;
    private String serviceType;
    private String serviceId;
    private HashMap<String, Object> data;
    private  Long eventTime;
    private String serviceInfo;
    private String log_type;

    public DeviceLog() {
    }

    public DeviceLog(String deviceId, String deviceType, String serviceType, String serviceId, HashMap<String, Object> data, Long eventTime, String serviceInfo, String log_type) {
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.serviceType = serviceType;
        this.serviceId = serviceId;
        this.data = data;
        this.eventTime = eventTime;
        this.serviceInfo = serviceInfo;
        this.log_type = log_type;
    }

    public DeviceLog(String deviceType, String deviceId, String serviceType, String serviceId, HashMap<String, Object> data, Long eventTime, String log_type) {
        this.deviceType = deviceType;
        this.deviceId = deviceId;
        this.serviceType = serviceType;
        this.serviceId = serviceId;
        this.data = data;
        this.eventTime = eventTime;
        this.log_type = log_type;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public Long getEventTime() {
        return eventTime;
    }

    public void setEventTime(Long eventTime) {
        this.eventTime = eventTime;
    }

    public String getServiceInfo() {
        return serviceInfo;
    }

    public void setServiceInfo(String serviceInfo) {
        this.serviceInfo = serviceInfo;
    }

    public String getLog_type() {
        return log_type;
    }

    public void setLog_type(String log_type) {
        this.log_type = log_type;
    }

    @Override
    public String toString() {
        return "DeviceLog{" +
                "id='" + id + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", serviceType='" + serviceType + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", data=" + data +
                ", eventTime=" + eventTime +
                ", serviceInfo='" + serviceInfo + '\'' +
                ", log_type=" + log_type +
                '}';
    }
}
