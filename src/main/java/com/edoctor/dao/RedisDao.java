package com.edoctor.dao;

public interface RedisDao {
    void deleteOneKey(String key);

    void setDeviceOnlineStatus(String key, String value);
    
    String getDeviceOnlineStatus(String key);

    void refreshKeyInRedis(String key);

    void refreshDeviceErrorStatusInRedis(String key, String value);

    String getDeviceErrorStatus(String key);

}
