package com.zhuhp.base.httpserver;

/**
 * Created by zhangtingting on 15/11/27.
 */
public class BaseRouter {

    public String responseMsg(String msg, int code){
        ResponseMsg responseMsg = new ResponseMsg();
        responseMsg.setCode(code);
        responseMsg.setMsg(msg);
        return responseMsg.createJson();
    }

}
