package com.coco3g.caopantx.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.coco3g.caopantx.data.Global;

/**
 * Created by coco3g on 16/8/18.
 */
public class FinishMainActivityBroadcast extends BroadcastReceiver {

    private OnFinishMainActivityListener mOnFinishMainActivityListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Global.FINISH_MAINACTIVITY_ACTION)) {
            //注册
            mOnFinishMainActivityListener.finishMainActivity();
            Log.e("广播", "收到finish广播");
        }
    }

    public interface OnFinishMainActivityListener {
        void finishMainActivity();
    }

    public void setOnFinishMainActivityListener(OnFinishMainActivityListener onFinishMainActivityListener) {
        mOnFinishMainActivityListener = onFinishMainActivityListener;
    }

}
