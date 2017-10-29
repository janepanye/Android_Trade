package com.coco3g.caopantx.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.coco3g.caopantx.utils.SystemBarTintManager;
import com.coco3g.caopantx.R;

public class BaseFragmentActivity extends FragmentActivity implements View.OnTouchListener {

//    private LockScreenBroadcast mLockScreenBroadcast;
//    private OpenScreenBroadCast mReceiveOpenScreenBroadCast;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.app_theme_color);


//        //注册锁屏的广播
//        mLockScreenBroadcast = new LockScreenBroadcast(this);
//        IntentFilter intentClose = new IntentFilter(Intent.ACTION_SCREEN_OFF);
//        registerReceiver(mLockScreenBroadcast, intentClose);
//        //注册解锁的广播
//        mReceiveOpenScreenBroadCast = new OpenScreenBroadCast();
//        IntentFilter intentopen = new IntentFilter(Intent.ACTION_SCREEN_ON);
//        registerReceiver(mReceiveOpenScreenBroadCast, intentopen);

    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

                break;

            default:

                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(mLockScreenBroadcast);
//        unregisterReceiver(mReceiveOpenScreenBroadCast);
    }

}
