package com.timyrobot.httpserver;

import android.text.TextUtils;

import com.timyrobot.system.bean.BaseCommand;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by zhangtingting on 15/11/24.
 */
public class Router {

    public static String ROUTER_PACKAGE_NAME = "com.zhuhp.base.httpserver.router.";



    public static BaseCommand parseHttpSession(NanoHTTPD.IHTTPSession session){
        if(session == null){
            return null;
        }
        String uri = session.getUri();
        if(TextUtils.isEmpty(uri)){
            return null;
        }
        try {
            Class parseClass = Class.forName(ROUTER_PACKAGE_NAME + uri.replaceAll("/", "_").toLowerCase());
            Method parseMethod = parseClass.getDeclaredMethod("parseHttpSession", NanoHTTPD.IHTTPSession.class);
            return (BaseCommand)parseMethod.invoke(parseClass.newInstance(), session);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }
}
