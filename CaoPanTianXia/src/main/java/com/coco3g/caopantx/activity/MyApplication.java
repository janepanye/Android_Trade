package com.coco3g.caopantx.activity;

import android.app.Application;

import com.coco3g.caopantx.data.Constants;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by lisen on 16/8/16.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        //
        Constants.JPUSH_REGISTERID = JPushInterface.getRegistrationID(this);
    }
}