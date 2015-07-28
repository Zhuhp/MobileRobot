package com.timyrobot.service.userintent.action;

import android.net.Uri;

/**
 * Created by zhangtingting on 15/7/26.
 */
public class Action {
    private Uri uri;
    public String operation;
    public String service;
    public String obj1;

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
