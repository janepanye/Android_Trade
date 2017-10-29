package com.coco3g.caopantx.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

/**
 * Created by lisen on 16/5/13.
 */
public class Coco3gBroadcastUtils {
    Context mContext;
    public static final String RETURN_UPDATE_FLAG = "return_update"; // 返回刷新
    public static final String RETURN_UPDATE_ME_FLAG = "return_update_me"; // 返回刷新“我的”界面
    public static final String RETURN_UPDATE_FIND_FLAG = "return_update_find"; // 返回刷新“发现”界面
    public static final String RETURN_UPDATE_TRANSLIST_FLAG = "return_update_translist"; // 返回刷新“交易”界面
    public static final String RETURN_HOME_FLAG = "return_home_fragment"; // 返回“首页”界面
    public static final String SHARE_FLAG = "share_flag"; // 分享
    //
    private ReceiveBroadCast receiveBroadCast; // 广播实例
    OnReceiveBroadcastListener onreceivebroadcastlistener = null;

    public Coco3gBroadcastUtils(Context mContext) {
        this.mContext = mContext;
        receiveBroadCast = new ReceiveBroadCast();
    }

    /**
     * 发送广播
     *
     * @param broadcastflag
     * @param bundle        携带数据
     */
    public void sendBroadcast(String broadcastflag, Bundle bundle) {
        Intent intent = new Intent(broadcastflag);
        if (bundle != null) {
            intent.putExtra("data", bundle);
        }
        mContext.sendBroadcast(intent);
    }

    public Coco3gBroadcastUtils receiveBroadcast(String broadcastflag) {
        IntentFilter filter = new IntentFilter("coco3gbroadcast");
        filter.addAction(broadcastflag);
        mContext.registerReceiver(receiveBroadCast, filter);
        return this;
    }

    private class ReceiveBroadCast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 得到广播中得到的数据，并显示出来
//            if ("paycomplete".equals(intent.getAction())) {
//                onPayComplete(1);
//            }
            onReceiveReturn(intent);
        }
    }

    public void setOnReceivebroadcastListener(OnReceiveBroadcastListener onreceivebroadcastlistener) {
        this.onreceivebroadcastlistener = onreceivebroadcastlistener;
    }

    public interface OnReceiveBroadcastListener {
        void receiveReturn(Intent intent);
    }

    private void onReceiveReturn(Intent intent) {
        if (onreceivebroadcastlistener != null) {
            onreceivebroadcastlistener.receiveReturn(intent);
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mContext.unregisterReceiver(receiveBroadCast);
//                }
//            }, 1000);
        }
    }

    /**
     * 注销广播
     */
    public void unregisterBroadcast() {
        if (receiveBroadCast != null) {
            mContext.unregisterReceiver(receiveBroadCast);
        }
    }
}
