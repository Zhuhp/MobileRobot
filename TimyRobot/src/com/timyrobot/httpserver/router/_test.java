package com.timyrobot.httpserver.router;

import com.timyrobot.httpserver.IRouter;
import com.timyrobot.system.bean.BaseCommand;
import com.timyrobot.system.bean.HttpCommand;
import com.zhuhp.base.httpserver.BaseRouter;
import com.zhuhp.base.httpserver.ResponseMsg;

import fi.iki.elonen.NanoHTTPD;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by zhangtingting on 15/11/24.
 */
public class _test extends BaseRouter implements IRouter {
    @Override
    public BaseCommand parseHttpSession(NanoHTTPD.IHTTPSession session) {
        BaseCommand command = new HttpCommand();
        command.setVoiceContent("hello!");
        command.setIsNeedVoiceRecon(false);
        command.setIsNeedTuling(false);
        command.setIsNeedUnderstandText(false);
        return command;
    }

    public interface TestService{

        @GET("/test")
        Call<ResponseMsg> test(@Query("param") String param);
    }
}
