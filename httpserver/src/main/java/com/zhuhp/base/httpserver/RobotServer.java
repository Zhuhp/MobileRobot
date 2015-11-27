package com.zhuhp.base.httpserver;

import android.text.TextUtils;
import android.util.Log;

import java.util.Map;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.util.ServerRunner;

/**
 * Created by zhangtingting on 15/11/24.
 */
public class RobotServer extends NanoHTTPD{

    private HttpMsgReceiver mReceiver;

    public RobotServer(){
        this(8080);
    }

    public void setHttpMsgReceiver(HttpMsgReceiver receiver){
        mReceiver = receiver;
    }

    public RobotServer(int port) {
        super(port);
    }

    public void run(){
        ServerRunner.run(RobotServer.class);
    }

    @Override
    public Response serve(IHTTPSession session) {
//        Method method = session.getMethod();
//        String uri = session.getUri();
//        Log.e("HttpServer", method + " '" + uri + "' ");
//
//        String msg = "<html><body><h1>Hello server</h1>\n";
//        Map<String, String> parms = session.getParms();
//        if (parms.get("username") == null) {
//            msg += "<form action='?' method='get'>\n" + "  <p>Your name: <input type='text' name='username'></p>\n" + "</form>\n";
//        } else {
//            msg += "<p>Hello, " + parms.get("username") + "!</p>";
//        }
//
//        msg += "</body></html>\n";
//        String msg = Router.parseHttpSession(session);
        String msg = null;
        if(mReceiver != null){
            msg = mReceiver.onReceive(session);
        }
        if(TextUtils.isEmpty(msg)){
            msg = new BaseRouter().responseMsg("execute ok", 200);
        }
        return newFixedLengthResponse(msg);
    }

    public interface HttpMsgReceiver{
        String onReceive(IHTTPSession session);
    }
}
