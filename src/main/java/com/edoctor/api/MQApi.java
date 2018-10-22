package com.edoctor.api;//package com.edoctor.api;

import com.edoctor.bean.Device;
import com.edoctor.bean.DeviceLog;
import com.edoctor.dao.DeviceDao;
import com.edoctor.dao.RedisDao;
import com.edoctor.enums.NOTIFY_TYPE;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.jms.core.JmsOperations;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/iot")
public class MQApi {

    private static Logger LOG = LoggerFactory.getLogger(MQApi.class);

    private MongoOperations mongo;
    private JmsOperations jmsOperations;
    private DeviceDao deviceDao;
    private RedisDao redisDao;

    @Autowired
    public MQApi(JmsOperations jmsOperations, MongoOperations mongo, DeviceDao deviceDao, RedisDao redisDao) {
        this.jmsOperations = jmsOperations;
        this.mongo = mongo;
        this.deviceDao = deviceDao;
        this.redisDao = redisDao;
    }

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");

    private static String testString = "{\"notifyType\":\"deviceDataChanged\",\"requestId\":null,\"service\":{\"serviceType\":\"Temperature\",\"data\":{\"Temperature\":85},\"eventTime\":\"20180607T203022Z\",\"serviceId\":\"Temperature\"},\"deviceId\":\"92435f3f-579a-40d0-8e3d-3ecaafd3ed22\",\"gatewayId\":\"92435f3f-579a-40d0-8e3d-3ecaafd3ed22\"}";

    @RequestMapping(value = "autoPush", method = RequestMethod.POST, consumes = "application/json")
    public void autoPush(Model model, HttpServletRequest request) {
        JSONObject json = parseHttp2Json(request);
        String notifyType = (String)json.get("notifyType");
        switch(NOTIFY_TYPE.valueOf(notifyType.trim())) {
            case testNotify:testNotify(json);break;
            case deviceAdded:deviceAdded(json);break;
            case deviceInfoChanged:deviceInfoChanged(json);break;
            case deviceDataChanged:deviceDataChanged(json);break;
            case deviceDeleted:deviceDeleted(json);break;
            case deviceEvent:deviceEvent(json);break;
            case messageConfirm:messageConfirm(json);break;
            case commandRsp:commandRsp(json);break;
            case serviceInfoChanged:serviceInfoChanged(json);break;
            case ruleEvent:ruleEvent(json);break;
            case bindDevice:bindDevice(json);break;
            case deviceDatasChanged:deviceDatasChanged(json);break;
        }
    }

    @RequestMapping(value = "insertToMongo", method = RequestMethod.POST, consumes = "application/json")
    public void insertToMQ(@RequestBody DeviceLog deviceLog) {
        // 为了保证日志属性充足，加了deviceType到log中
        Device device = deviceDao.getDeviceByDeviceId(deviceLog.getDeviceId());
        if(device == null) {
            LOG.error("deviceId = " + deviceLog.getDeviceId() + " is null,please notice");
            return;
        } else {
            deviceLog.setDeviceType(device.getDeviceType());
        }

        /**
         * String deviceStatusInRedis = "DeviceErrorStatus" + device.getDeviceId();
         */
        switch(deviceLog.getLog_type()) {
            case "INFO" : deviceDao.updateDeviceRunningStatus(device.getDeviceId(), "NORMAL");break;
            case "WARN" : deviceDao.updateDeviceRunningStatus(device.getDeviceId(), "ABNORMAL");break;
            case "FAULT" : deviceDao.updateDeviceRunningStatus(device.getDeviceId(), "FAULT");break;
            default : deviceLog.setLog_type("[device.getRunningStatus() = " + device.getRunningStatus() + "]ERROR RUNNING STATUS");
        }
        mongo.save(deviceLog);
    }

    @RequestMapping(value = "updateDeviceOnlineInfo", method = RequestMethod.PUT)
    public void updateDeviceOnlineInfo(@RequestParam(name = "deviceId") String deviceId, @RequestParam(name = "isOnline") Boolean isOnline) {
        LOG.info("handleMessage = Serializable,deviceId = " + deviceId + ", offline status = " + isOnline);
        deviceDao.updateDeviceOnlineOffline(deviceId, isOnline);
    }

    @RequestMapping(value = "getStringValueFromRedis", method = RequestMethod.GET)
    public String getStringValueFromRedis(@RequestParam(name = "key") String key) {
        return redisDao.getDeviceOnlineStatus(key);
    }

    @RequestMapping(value = "addKeyToRedis", method = RequestMethod.GET)
    public void addKeyToRedis(@RequestParam(name = "key") String key, @RequestParam(name = "value") String value) {
        redisDao.setDeviceOnlineStatus(key, value);
    }

    @RequestMapping(value = "refreshKeyInRedis", method = RequestMethod.GET)
    public void refreshKeyInRedis(@RequestParam(name = "key") String key) {
        redisDao.refreshKeyInRedis(key);
    }

    private void testNotify(JSONObject json) {
        System.out.println("testNotify");
    }

    private void deviceDatasChanged(JSONObject json) {
        System.out.println("deviceDatasChanged");
    }

    private void bindDevice(JSONObject json) {
        System.out.println("bindDevice");
    }

    private void ruleEvent(JSONObject json) {
        System.out.println("ruleEvent");
    }

    private void serviceInfoChanged(JSONObject json) {
        System.out.println("serviceInfoChanged");
    }

    private void commandRsp(JSONObject json) {
        System.out.println("commandRsp");
    }

    private void messageConfirm(JSONObject json) {
        System.out.println("messageConfirm");
    }

    private void deviceEvent(JSONObject json) {
        System.out.println("deviceEvent");
    }

    private void deviceDeleted(JSONObject json) {
        System.out.println("deviceDeleted");
    }

    // 此为MQ中存入日志的预处理操作
    private void deviceDataChanged(JSONObject json) {
        String deviceId = json.getString("deviceId");
        String notifyType = json.getString("notifyType");
        JSONObject service = json.getJSONObject("service");
        String serviceType = service.getString("serviceType");
        String eventTime = service.getString("eventTime");
        String serviceId = service.getString("serviceId");

        JSONObject data = service.getJSONObject("data");
        Map<String, Object> kValue = data.toMap();
        int log_typeIntValue = (int) kValue.get("log_type");
        kValue.remove("log_type");
        // 加8小时，使得UTC变为UTC+8
        LocalDateTime localDateTime = LocalDateTime.parse(eventTime, dateTimeFormatter).plusHours(8);
        DeviceLog deviceLog = new DeviceLog("IoT", deviceId, serviceType, serviceId, (HashMap<String, Object>) kValue,localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli(), "IntoMQ");
        switch (log_typeIntValue) {
            case 1:
                deviceLog.setLog_type("INFO");
                break;
            case 2:
                deviceLog.setLog_type("WARN");
                break;
            case 3:
                deviceLog.setLog_type("FAULT");
                break;
            default:
                LOG.error("log_type = " + log_typeIntValue + ", which is not 1,2,3.Invalid");
        }
        // 存入了MQ
        jmsOperations.convertAndSend(deviceLog);
        LOG.info(LocalDateTime.now() + ", deviceLog=" + deviceLog + " has sent to MQ");
    }

    private void deviceInfoChanged(JSONObject json) {
        System.out.println("deviceInfoChanged");
    }

    private void deviceAdded(JSONObject json) {
        System.out.println("deviceAdded");
    }

    private JSONObject parseHttp2Json(HttpServletRequest request) {
        JSONObject jsonObject = null;
        try {
            BufferedReader streamReader = new BufferedReader( new InputStreamReader(request.getInputStream(), "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            jsonObject = new JSONObject(responseStrBuilder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


//    @ApiOperation(value = "从队列中读取一条数据", httpMethod = "GET", produces = "application/json")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "success: insert successfully", response = RestMessage.class),
//            @ApiResponse(code = 400, message = "error: mq server down", response = RestMessage.class)
//    })
//    @RequestMapping(value = "getOneObjectFromMQ", method = RequestMethod.GET, produces = "application/json")
//    public RestMessage<DeviceLog> getOneObjectFromMQ() {
//        RestMessage<DeviceLog> restMessage = new RestMessage<>();
//        DeviceLog value = (DeviceLog)jmsOperations.receiveAndConvert();
//        restMessage.setCode(200).setMsg("success: insert successfully").setData(value);
//        return restMessage;
//    }
}
