package com.edoctor.dao;

import com.edoctor.bean.Device;

public interface DeviceDao {

    Device getDeviceByDeviceId(String deviceId);

    void updateDeviceRunningStatus(String deviceId, String runningStatus);

    void updateDeviceOnlineOffline(String deviceId, Boolean isOnline);
}
