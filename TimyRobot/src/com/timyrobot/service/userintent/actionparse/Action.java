package com.timyrobot.service.userintent.actionparse;

import android.net.Uri;

/**
 * Created by zhangtingting on 15/7/26.
 */
public class Action {
    private Uri uri;
    public String operation;
    public String service;
    public String obj1;
    public String obj2;
    public String obj3;

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return service+";"+operation+";"+obj1+";"+obj2+";"+obj3;
    }
}
