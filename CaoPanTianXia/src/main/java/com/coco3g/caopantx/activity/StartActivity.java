package com.coco3g.caopantx.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.CookieSyncManager;

import com.coco3g.caopantx.bean.BaseData;
import com.coco3g.caopantx.data.CameraConfig;
import com.coco3g.caopantx.R;
import com.coco3g.caopantx.data.Global;
import com.coco3g.caopantx.listener.IBaseDataListener;
import com.coco3g.caopantx.presenter.PublicPresenter;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.File;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by lisen on 16/2/4.
 */
@SuppressWarnings("ALL")
public class StartActivity extends BaseActivity {
    //    /**
//     * 该机型摄像头的个数
//     **/
    public static int gCameraCount = 0;
    public static CameraConfig gCameraConfig;
    public String SHARE_APP_TAG = "first";
    boolean isFirst = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        if (!isTaskRoot()) {
            Intent mainIntent=getIntent();
            String action=mainIntent.getAction();
            if(mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;//finish()之后该活动会继续执行后面的代码，你可以logCat验证，加return避免可能的exception
            }
        }
        //
        SharedPreferences setting = getSharedPreferences(SHARE_APP_TAG, 0);
        Boolean user_first = setting.getBoolean("FIRST", true);
        if (user_first) {//第一次
            setting.edit().putBoolean("FIRST", false).commit();
            isFirst = false;
        } else {
            isFirst = false;
        }
        //
        CookieSyncManager.createInstance(this);
//        Global.LOGIN_INFO_MAP = Global.readLoginInfo(this);
        logout();
        //
        init();
        gCameraConfig = new CameraConfig(StartActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    private void init() {
        new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                super.run();
                ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(StartActivity.this);
                config.threadPriority(Thread.NORM_PRIORITY - 2);
                config.denyCacheImageMultipleSizesInMemory();
                config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
                config.memoryCacheSize(2 * 1024 * 1024);
                config.diskCacheSize(50 * 1024 * 1024);
                config.memoryCache(new WeakMemoryCache());
                config.imageDownloader(new BaseImageDownloader(StartActivity.this, 5 * 1000, 10 * 1000));
                config.tasksProcessingOrder(QueueProcessingType.LIFO);
                config.writeDebugLogs();
                config.threadPoolSize(3);
                ImageLoader.getInstance().init(config.build());
                /* 如果DCIM/Camera目录不存在，则创建该目录 */
                String cameradir = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/Camera/";
                File file = new File(cameradir);
                if (!file.exists()) {
                    file.mkdirs();
                }
                //
//                Constants.JPUSH_REGISTERID = JPushInterface.getRegistrationID(StartActivity.this);
                /* 获取摄像头信息 */
                gCameraCount = Camera.getNumberOfCameras(); // 得到摄像头的个数
                if (gCameraCount >= 2) {
                    gCameraConfig.initConfig(Camera.CameraInfo.CAMERA_FACING_FRONT); // 前置摄像头
                }
                gCameraConfig.initConfig(Camera.CameraInfo.CAMERA_FACING_BACK); // 后置摄像头
                //
                Global.SDK_VERSION = Global.getAndroidSDKVersion();
                //
                Global.getScreenWH(StartActivity.this); // 获取屏幕尺寸
                try {
                    sleep(1500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Message mess = new Message();
                mHandler.sendMessage(mess);
            }

        }.start();

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
//            if (Global.LOGIN_INFO_MAP != null) {
//                login();
//            } else {
//                if (isFirst) {
//                    Intent intent = new Intent(StartActivity.this, GuideActivity.class);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            }
            if (isFirst) {
                Intent intent = new Intent(StartActivity.this, GuideActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
//                intent.putExtra("notice_data", Global.NOTICE_DATA);
                startActivity(intent);
                finish();
            }
        }
    };

    //退出登录
    public void logout() {
        new PublicPresenter(this).logout(new IBaseDataListener() {
            @Override
            public void onSuccess(BaseData data) {
                Log.e("退出", "退出登录onSuccess");
                Global.USERINFO = null;
                Global.deleteSerializeData(StartActivity.this, Global.LOGIN_INFO);
                Global.LOGIN_INFO_MAP = null;
                // 除cookie
                Global.clearCookie(StartActivity.this);
            }

            @Override
            public void onFailure(BaseData data) {
                Log.e("出错了", "退出登录onFailure");
            }

            @Override
            public void onError() {
                Log.e("onError", "退出登录onError");
            }
        });

    }

//    private void login() {
//        new PublicPresenter(StartActivity.this).login(Global.LOGIN_INFO_MAP.get("username"), Global.LOGIN_INFO_MAP.get("password"), new IUserListenter() {
//            @Override
//            public void onSuccess(UserData data) {
//                Global.USERINFO = data.response;
//                Intent intent = new Intent(StartActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//
//            @Override
//            public void onFailure(BaseData data) {
//
//            }
//
//            @Override
//            public void onError() {
//
//            }
//        });
//    }

}
