package com.edoctor.dao.impl;

import com.edoctor.bean.Device;
import com.edoctor.dao.DeviceDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class DeviceDaoImpl implements DeviceDao {

    private static final String getDeviceByDeviceId = "SELECT * FROM device WHERE deviceId = :deviceId";

    private static final String updateDeviceRunningStatus = "UPDATE device SET runningStatus = :runningStatus WHERE deviceId= :deviceId";

    private static final String updateDeviceOnlineOffline = "UPDATE device SET status = :status WHERE deviceId= :deviceId";

    private SessionFactory sessionFactory;

    @Autowired
    public DeviceDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Device getDeviceByDeviceId(String deviceId) {
        Query<Device> query = currentSession().createNativeQuery(getDeviceByDeviceId, Device.class);
        query.setParameter("deviceId", deviceId);
        return query.uniqueResult();
    }

    @Override
    public void updateDeviceRunningStatus(String deviceId, String runningStatus) {
        Query query = currentSession().createNativeQuery(updateDeviceRunningStatus);
        query.setParameter("deviceId", deviceId);
        query.setParameter("runningStatus", runningStatus);
        query.executeUpdate();
    }

    @Override
    public void updateDeviceOnlineOffline(String deviceId, Boolean isOnline) {
        Query query = currentSession().createNativeQuery(updateDeviceOnlineOffline);
        query.setParameter("deviceId", deviceId);
        query.setParameter("status", isOnline ? "ONLINE" : "OFFLINE");
        query.executeUpdate();
    }
}
