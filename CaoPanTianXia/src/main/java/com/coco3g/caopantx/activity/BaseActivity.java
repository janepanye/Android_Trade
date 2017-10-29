package com.coco3g.caopantx.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.coco3g.caopantx.R;
import com.coco3g.caopantx.utils.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends Activity implements View.OnTouchListener{

//    private LockScreenBroadcast mLockScreenBroadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.app_theme_color);

//        //注册锁屏的广播
//        mLockScreenBroadcast = new LockScreenBroadcast(this);
//        IntentFilter mIntentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
//        registerReceiver(mLockScreenBroadcast, mIntentFilter);
//        mLockScreenBroadcast.setReceiveLockScreenBroadcastListener(new LockScreenBroadcast.OnReceiveLockScreenBroadcastListener() {
//            @Override
//            public void finishActivityOrFragment() {
//                Log.e("快点快点", "finish activity");
//                finish();
//            }
//        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
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
        return false;

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(mLockScreenBroadcast);
    }
}
