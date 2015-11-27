package com.timyrobot.system.triggersystem;

import com.timyrobot.httpserver.Router;
import com.timyrobot.system.triggersystem.listener.DataReceiver;
import com.zhuhp.base.httpserver.BaseRouter;
import com.zhuhp.base.httpserver.RobotServer;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;

/**
 * Http服务器
 * Created by zhangtingting on 15/9/16.
 */
public enum HttpTrigger {
    INSTANCE;

    private RobotServer mRobotServer;

    private DataReceiver mDataReceiver;

    private RobotServer.HttpMsgReceiver msgReceiver = new RobotServer.HttpMsgReceiver() {
        @Override
        public String onReceive(NanoHTTPD.IHTTPSession session) {
            mDataReceiver.onReceive(Router.parseHttpSession(session));
            return null;
        }
    };
    public void init(DataReceiver dataReceiver) throws IOException {
        mDataReceiver = dataReceiver;
        mRobotServer = new RobotServer();
        mRobotServer.setHttpMsgReceiver(msgReceiver);
        mRobotServer.start();
    }
}
