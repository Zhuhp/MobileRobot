package com.timyrobot.httpserver.router;

import com.timyrobot.httpserver.IRouter;
import com.timyrobot.system.bean.BaseCommand;
import com.timyrobot.system.bean.HttpCommand;
import com.zhuhp.base.httpserver.BaseRouter;
import com.zhuhp.base.httpserver.ResponseMsg;

import java.util.Map;

import fi.iki.elonen.NanoHTTPD;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by zhangtingting on 15/11/25.
 */
public class _control extends BaseRouter implements IRouter {
    @Override
    public BaseCommand parseHttpSession(NanoHTTPD.IHTTPSession session) {
        String msg = "";
        Map<String, String> params = session.getParms();
        msg = params.get("param");
        BaseCommand command = new HttpCommand();
        command.setVoiceContent(msg);
        command.setIsNeedVoiceRecon(false);
        command.setIsNeedTuling(false);
        command.setIsNeedUnderstandText(false);
        return command;
    }

    public interface ControlService{
        @GET("/control")
        Call<ResponseMsg> test(@Query("param") String param);
    }
}
