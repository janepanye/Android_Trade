package com.coco3g.caopantx.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.coco3g.caopantx.activity.LoginActivity;
import com.coco3g.caopantx.data.Global;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by coco3g on 16/8/18.
 */
public class LockScreenBroadcast extends BroadcastReceiver {
    private Context mContext;
    public static boolean mIsOpenScreen = true;
    private LocalBroadcastManager mLocalBroadcastManager;
    private int count = 0;

    public LockScreenBroadcast(Context context) {
        mContext = context;
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            mIsOpenScreen = false;
            //计时5s之后推出登录,并挑战到登录界面
            timeToLoginActivity();
        }
    }


    //锁屏时间到时,关闭MainActivity,退出登录,切换到登录界面
    public void timeToLoginActivity() {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ++count;
                boolean isFirst = true;
                if (count * 1000 >= 1800 * 1000 && isFirst) {
                    isFirst = false;
                    //跳转到登录界面
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    //不能这么用,当锁屏退出时,清空map,再次进入startActivity给Global.LOGIN_INFO_MAP赋值,这时是空,
                    //点击我的,登录,保存用户信息,当再次锁屏退出时,读取用户信息就不能通过---,只能readlogininfo
//                    String username =  Global.readLoginInfo(mContext).get("username");
//                    intent.putExtra("username",username);
                    LoginActivity.fromLockScreenBroadcast = true;
                    mContext.startActivity(intent);

                    //发送一个关闭MainActivity的广播
                    mLocalBroadcastManager = LocalBroadcastManager.getInstance(mContext);
                    Intent intentFinish = new Intent(Global.FINISH_MAINACTIVITY_ACTION);
                    mLocalBroadcastManager.sendBroadcast(intentFinish);

                    //取消计时
                    timer.cancel();

                } else if (mIsOpenScreen) {
                    timer.cancel();
                }
            }
        }, 0, 1000);
    }


}
