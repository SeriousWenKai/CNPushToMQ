package com.edoctor.api;//package com.edoctor.api;


import com.edoctor.bean.DeviceLog;
import com.edoctor.enums.NOTIFY_TYPE;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private JmsOperations jmsOperations;
    @Autowired
    public MQApi(JmsOperations jmsOperations) {
        this.jmsOperations = jmsOperations;
    }

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");

    private static String testString = "{\"notifyType\":\"deviceDataChanged\",\"requestId\":null,\"service\":{\"serviceType\":\"Temperature\",\"data\":{\"Temperature\":85},\"eventTime\":\"20180607T203022Z\",\"serviceId\":\"Temperature\"},\"deviceId\":\"92435f3f-579a-40d0-8e3d-3ecaafd3ed22\",\"gatewayId\":\"92435f3f-579a-40d0-8e3d-3ecaafd3ed22\"}";

    @RequestMapping(value = "autoPush", method = RequestMethod.POST, produces = "application/json")
    public String getOneObjectFromMQ(Model model, HttpServletRequest request) {
        JSONObject json = parseHttp2Json(request);
        String notifyType = (String)json.get("notifyType");
        System.out.println("notifyType = " + notifyType);
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
        return null;
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

    private void deviceDataChanged(JSONObject json) {
        String deviceId = json.getString("deviceId");
        String notifyType = json.getString("notifyType");
        JSONObject service = json.getJSONObject("service");
        String serviceType = service.getString("serviceType");
        String eventTime = service.getString("eventTime");
        String serviceId = service.getString("serviceId");

        JSONObject data = service.getJSONObject("data");
        Map<String, Object> kValue = data.toMap();
        LocalDateTime localDateTime = LocalDateTime.parse(eventTime, dateTimeFormatter);
        DeviceLog deviceLog = new DeviceLog("IntoMQ", deviceId, serviceType, serviceId, (HashMap<String, Object>) kValue,localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli(), "IntoMQ");
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