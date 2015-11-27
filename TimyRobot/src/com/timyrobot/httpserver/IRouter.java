package com.timyrobot.httpserver;

import com.timyrobot.system.bean.BaseCommand;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by zhangtingting on 15/11/24.
 */
public interface IRouter {
    BaseCommand parseHttpSession(NanoHTTPD.IHTTPSession session);
}
