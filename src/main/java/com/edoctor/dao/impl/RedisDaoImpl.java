package com.edoctor.dao.impl;

import com.edoctor.dao.RedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class RedisDaoImpl implements RedisDao {


    private static final long setDeviceOnlineStatus = 60 * 60 * 1000;//60分钟过期，超时退出

    @Autowired
    RedisTemplate<String, String> redisOpObj;


    @Override
    public void deleteOneKey(String key) {
        redisOpObj.delete(key);
    }


    @Override
    public void setDeviceOnlineStatus(String key, String value) {
        redisOpObj.opsForValue().set(key, value);
        redisOpObj.expire(key, setDeviceOnlineStatus, TimeUnit.MILLISECONDS);//设置超时时间
    }

    @Override
    public String getDeviceOnlineStatus(String key) {
        return redisOpObj.opsForValue().get(key);
    }

    @Override
    public void refreshKeyInRedis(String key) {
        redisOpObj.expire(key,setDeviceOnlineStatus, TimeUnit.MILLISECONDS);//设置超时时间
    }


}
