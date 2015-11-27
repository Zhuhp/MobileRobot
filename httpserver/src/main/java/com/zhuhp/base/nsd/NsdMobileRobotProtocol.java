package com.zhuhp.base.nsd;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhangtingting on 15/11/26.
 */
public class NsdMobileRobotProtocol {

    public static final String KEY_SERVICE = "service";
    public static final String KEY_IP = "ip_address";
    public static final String KEY_PORT = "ip_port";
    public static final String KEY_RTSP = "rtsp";

    public static final String VALUE_CONTROL_CLIENT_SERVICE = "MOBILE_CONTROL_CLIENT_SERVICE";
    public static final String VALUE_ROBOT_CLIENT_SERVICE = "MOBILE_ROBOT_CLIENT_SERVICE";

    public static String createControlClientProtocol() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(KEY_SERVICE, VALUE_CONTROL_CLIENT_SERVICE);
        return object.toString();
    }

    public static String createRobotClientProtocol(String ip, int port, String rtsp) throws JSONException {
        JSONObject object = new JSONObject();
        object.put(KEY_SERVICE, VALUE_ROBOT_CLIENT_SERVICE);
        object.put(KEY_IP, ip);
        object.put(KEY_PORT, port);
        object.put(KEY_RTSP, rtsp);
        return object.toString();
    }

    public static boolean isMobileControlClientService(JSONObject object){
        if(object == null){
            return false;
        }
        String service = object.optString(KEY_SERVICE);
        if(VALUE_CONTROL_CLIENT_SERVICE.equals(service)){
            return true;
        }
        return false;
    }

    public static boolean isMobileRobotClientService(JSONObject object){
        if(object == null){
            return false;
        }
        String service = object.optString(KEY_SERVICE);
        if(VALUE_ROBOT_CLIENT_SERVICE.equals(service)){
            return true;
        }
        return false;
    }
}
