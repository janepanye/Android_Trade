package com.coco3g.caopantx.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by coco3g on 16/8/18.
 */
public class OpenScreenBroadCast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            //设置LockScreenBroadcast里面的布尔变量
            LockScreenBroadcast.mIsOpenScreen = true;
        }
    }


}
