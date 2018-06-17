package com.edoctor.bean;

import javax.persistence.*;

@Entity
@Table(name = "device")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String deviceId;
    private String nodeId;
    private String name;
    private String description;
    private String manufactureId;
    private String manufacturerName;
    private String mac;
    private String location;
    private String deviceType;
    private String model;
    private String swVersion;
    private String fwVersion;
    private String hwVersion;
    private String protocolType;
    private String signalStrength;
    private String bridgeId;
    private String supportedSecurity;
    private String isSecurity;
    private String sigVersion;
    private String runningStatus;
    private String status;
    private String statusDetail;
    private String mute;
    private Double lng;
    private Double lat;
    private Integer owner_id;
    private String company_name;
    private String area;

    public Device() {
    }

    public Device(String deviceId) {
        this.deviceId = deviceId;
    }

    public Device(String name, String description, double lng, double lat, int owner_id, String area) {
        this.name = name;
        this.description = description;
        this.lng = lng;
        this.lat = lat;
        this.owner_id = owner_id;
        this.area = area;
    }

    public Device(String deviceId, String name, String description, double lng, double lat, int owner_id, String area) {
        this.deviceId = deviceId;
        this.name = name;
        this.description = description;
        this.lng = lng;
        this.lat = lat;
        this.owner_id = owner_id;
        this.area = area;
    }

    public Device(String deviceId, String nodeId, String name, String description, String manufactureId, String manufacturerName, String mac, String location, String deviceType, String model, String swVersion, String fwVersion, String hwVersion, String protocolType, String signalStrength, String bridgeId, String supportedSecurity, String isSecurity, String sigVersion, String runningStatus, String status, String statusDetail, String mute, Double lng, Double lat, Integer owner_id, String area) {
        this.deviceId = deviceId;
        this.nodeId = nodeId;
        this.name = name;
        this.description = description;
        this.manufactureId = manufactureId;
        this.manufacturerName = manufacturerName;
        this.mac = mac;
        this.location = location;
        this.deviceType = deviceType;
        this.model = model;
        this.swVersion = swVersion;
        this.fwVersion = fwVersion;
        this.hwVersion = hwVersion;
        this.protocolType = protocolType;
        this.signalStrength = signalStrength;
        this.bridgeId = bridgeId;
        this.supportedSecurity = supportedSecurity;
        this.isSecurity = isSecurity;
        this.sigVersion = sigVersion;
        this.runningStatus = runningStatus;
        this.status = status;
        this.statusDetail = statusDetail;
        this.mute = mute;
        this.lng = lng;
        this.lat = lat;
        this.owner_id = owner_id;
        this.area = area;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManufactureId() {
        return manufactureId;
    }

    public void setManufactureId(String manufactureId) {
        this.manufactureId = manufactureId;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSwVersion() {
        return swVersion;
    }

    public void setSwVersion(String swVersion) {
        this.swVersion = swVersion;
    }

    public String getFwVersion() {
        return fwVersion;
    }

    public void setFwVersion(String fwVersion) {
        this.fwVersion = fwVersion;
    }

    public String getHwVersion() {
        return hwVersion;
    }

    public void setHwVersion(String hwVersion) {
        this.hwVersion = hwVersion;
    }

    public String getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }

    public String getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(String signalStrength) {
        this.signalStrength = signalStrength;
    }

    public String getBridgeId() {
        return bridgeId;
    }

    public void setBridgeId(String bridgeId) {
        this.bridgeId = bridgeId;
    }

    public String getSupportedSecurity() {
        return supportedSecurity;
    }

    public void setSupportedSecurity(String supportedSecurity) {
        this.supportedSecurity = supportedSecurity;
    }

    public String getIsSecurity() {
        return isSecurity;
    }

    public void setIsSecurity(String isSecurity) {
        this.isSecurity = isSecurity;
    }

    public String getSigVersion() {
        return sigVersion;
    }

    public void setSigVersion(String sigVersion) {
        this.sigVersion = sigVersion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDetail() {
        return statusDetail;
    }

    public void setStatusDetail(String statusDetail) {
        this.statusDetail = statusDetail;
    }

    public String getMute() {
        return mute;
    }

    public void setMute(String mute) {
        this.mute = mute;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Integer getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(Integer owner_id) {
        this.owner_id = owner_id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(String runningStatus) {
        this.runningStatus = runningStatus;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", deviceId='" + deviceId + '\'' +
                ", nodeId='" + nodeId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", manufactureId='" + manufactureId + '\'' +
                ", manufacturerName='" + manufacturerName + '\'' +
                ", mac='" + mac + '\'' +
                ", location='" + location + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", model='" + model + '\'' +
                ", swVersion='" + swVersion + '\'' +
                ", fwVersion='" + fwVersion + '\'' +
                ", hwVersion='" + hwVersion + '\'' +
                ", protocolType='" + protocolType + '\'' +
                ", signalStrength='" + signalStrength + '\'' +
                ", bridgeId='" + bridgeId + '\'' +
                ", supportedSecurity='" + supportedSecurity + '\'' +
                ", isSecurity='" + isSecurity + '\'' +
                ", sigVersion='" + sigVersion + '\'' +
                ", runningStatus='" + runningStatus + '\'' +
                ", status='" + status + '\'' +
                ", statusDetail='" + statusDetail + '\'' +
                ", mute='" + mute + '\'' +
                ", lng=" + lng +
                ", lat=" + lat +
                ", owner_id=" + owner_id +
                ", company_name='" + company_name + '\'' +
                ", area='" + area + '\'' +
                '}';
    }
}
