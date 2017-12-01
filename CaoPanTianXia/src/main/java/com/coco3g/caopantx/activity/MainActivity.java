package com.coco3g.caopantx.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coco3g.caopantx.bean.BaseData;
import com.coco3g.caopantx.data.DataUrl;
import com.coco3g.caopantx.fragment.FindFragment;
import com.coco3g.caopantx.fragment.RadioFragment;
import com.coco3g.caopantx.fragment.TransactionFragment;
import com.coco3g.caopantx.net.utls.VersionUpdateUtils;
import com.coco3g.caopantx.utils.Coco3gBroadcastUtils;
import com.coco3g.caopantx.view.BottomNavImageView;
import com.coco3g.caopantx.view.MyScrollWebView;
import com.coco3g.caopantx.view.TopBarView;
import com.coco3g.caopantx.R;
import com.coco3g.caopantx.data.Global;
import com.coco3g.caopantx.fragment.HomeFragment;
import com.coco3g.caopantx.fragment.MeFragment;
import com.coco3g.caopantx.listener.IBaseDataListener;
import com.coco3g.caopantx.presenter.PublicPresenter;
import com.coco3g.caopantx.utils.RequestPermissionUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.Timer;
import java.util.TimerTask;



public class MainActivity extends BaseFragmentActivity implements View.OnClickListener {
    private TopBarView mTopBar;
    private FragmentManager mFragmentManager;
//    BaseFragment 改成HomeFragment   PYDO
    private HomeFragment mHomeFragment;
    private TransactionFragment mTransactionFragment;
    private RadioFragment mRadioFragment;
    private FindFragment mDiscoverFragment;
    private MeFragment mMeFragment;
    private BottomNavImageView[] mBottomNav;
    private BottomNavImageView mNavHome, mNavTransaction, mNavRadio, mNavDiscover, mNavMe;
    private TextView mTvGuoJi, mTvGuoNei;
    private ImageView mIvSearch;
    private TextView[] mTransactionNav;
    private int mCurrentIndex = -1;
    private int mCurrentQiHuo = -1;

    public static int selNavItemIndex = -1; //选择的底部导航的哪个item

    DisplayImageOptions options;
    int[] mBottomIcom = new int[]{R.drawable.selector_bottom_nav_home, R.drawable.selector_bottom_nav_tansaction,R.drawable.selector_bottom_nav_radio, R.drawable.selector_bottom_nav_discover, R.drawable.selector_bottom_nav_me};
    String[] mBottomText, mTitles;
    //    public static Context MAIN_CONTEXT = null;
    private static Boolean isExit = false;

    private LocalBroadcastManager mLocalBroadcastManager;
    private Coco3gBroadcastUtils mCurrBoardCast;
    //处理返回MainActivity的时候的延时操作
    private Handler mHandler = new Handler();
    public static boolean isForeground = false;
    ImageView mImageHomeTopBarRight = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        setContentView(int layoutResID)方法使用xml布局文件的方法的完整签名
        setContentView(R.layout.activity_main);
//        MAIN_CONTEXT = this;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .resetViewBeforeLoading(true)
                .displayer(new RoundedBitmapDisplayer(200)).build();
        //
        mFragmentManager = getSupportFragmentManager();
        mBottomText = new String[]{getResources().getString(R.string.text_bottom_home), getResources().getString(R.string.text_bottom_transaction),getResources().getString(R.string.text_bottom_radio), getResources().getString(R.string.text_bottom_discover), getResources().getString(R.string.text_bottom_me)};//直播
        mTitles = new String[5];//5
        initView();
        //注册锁屏超时广播
//        registerBroadcast();
        requestPermission(); // 申请相关权限
        // 检查新版本
        new VersionUpdateUtils(this).checkUpdate(false, ""); // 检查最新版本
        //
        // 接收到push通知
        if (Global.NOTICE_DATA != null) {
            Intent intent = new Intent(MainActivity.this, WebActivity.class);
            intent.putExtra("url", Global.NOTICE_DATA.txt);
            intent.putExtra("title", "");
            startActivity(intent);
        }
        //
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void initView() {
        mTopBar = (TopBarView) findViewById(R.id.main_topbar);
        mTopBar.hideLeftView();
        //
        mImageHomeTopBarRight = new ImageView(this);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(Global.screenWidth / 14, Global.screenWidth / 14);
        mImageHomeTopBarRight.setLayoutParams(lp);
        ImageLoader.getInstance().displayImage("drawable://" + R.mipmap.pic_user_bg1, mImageHomeTopBarRight, options);

        mNavHome = (BottomNavImageView) findViewById(R.id.main_bottom_home);
        mNavTransaction = (BottomNavImageView) findViewById(R.id.main_bottom_transaction);
        mNavRadio = (BottomNavImageView) findViewById(R.id.main_bottom_radio);
        mNavDiscover = (BottomNavImageView) findViewById(R.id.main_bottom_discover);
        mNavMe = (BottomNavImageView) findViewById(R.id.main_bottom_me);

        mBottomNav = new BottomNavImageView[]{mNavHome, mNavTransaction,mNavRadio, mNavDiscover, mNavMe};
        for (int i = 0; i < mBottomNav.length; i++) {
            mBottomNav[i].setIcon(mBottomIcom[i], mBottomText[i]);
        }

        mNavHome.setOnClickListener(this);
        mNavTransaction.setOnClickListener(this);
        mNavRadio.setOnClickListener(this);
        mNavDiscover.setOnClickListener(this);
        mNavMe.setOnClickListener(this);

//         国际国内外期货监听
        mTopBar.setOnClickMiddleMenuListener(new TopBarView.OnClickMiddleMenu() {
            @Override
            public void onClickMiddleMenu(int resID) {
                switch (resID) {
                    case R.id.tv_topbar_middle_left:
                        if (mTransactionFragment != null) {
                            mTransactionFragment.switchTrans(14);

                        }
                        break;
                    case R.id.tv_topbar_middle_right:
                        if (mTransactionFragment != null) {
                            mTransactionFragment.switchTrans(15);
                        }
                        break;
                }
            }
        });
        mTopBar.setOnClickTopRight(new TopBarView.OnClickTopRight() {
            @Override
            public void onClickTopRight() {
                Intent intent = new Intent(MainActivity.this, WebActivity.class);
                intent.putExtra("title", "");
                intent.putExtra("url", DataUrl.CHICANG);
                startActivity(intent);
            }
        });

        mTopBar.setOnClickRightListener(new TopBarView.OnClickRightView() {
            @Override
            public void onClickTopbarView() {
                Intent intent = new Intent(MainActivity.this, WebActivity.class);
                intent.putExtra("title", "");
                intent.putExtra("url", DataUrl.HELP_PAGE);
                startActivity(intent);
            }
        });

        selNavItem(0);

        //注册一个有登录界面点击返回键,强制返回HomeFragment
        mCurrBoardCast = new Coco3gBroadcastUtils(MainActivity.this);
        mCurrBoardCast.receiveBroadcast(Coco3gBroadcastUtils.RETURN_HOME_FLAG)
                .setOnReceivebroadcastListener(new Coco3gBroadcastUtils.OnReceiveBroadcastListener() {
                    @Override
                    public void receiveReturn(Intent intent) {
                        selNavItem(0);
//                        Log.e("刷新数据", "me");
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;
        //刷新发现Fragment
        if (mDiscoverFragment != null) {
            new Coco3gBroadcastUtils(MainActivity.this).sendBroadcast(Coco3gBroadcastUtils.RETURN_UPDATE_FIND_FLAG, null);
        }
        //刷新我的Fragment
        if (mMeFragment != null) {
            new Coco3gBroadcastUtils(MainActivity.this).sendBroadcast(Coco3gBroadcastUtils.RETURN_UPDATE_ME_FLAG, null);
        }
        //当返回MainActivity的时候,在onResume里面(刷新国内期货或国内期货的数据)
        if (mTransactionFragment != null) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mTransactionFragment.refreshData();
                }
            }, 500);
        }
    }

    //退出登录
    public void logout() {
        new PublicPresenter(this).logout(new IBaseDataListener() {
            @Override
            public void onSuccess(BaseData data) {
                Log.e("退出", "退出登录onSuccess");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_bottom_home:
                selNavItem(selNavItemIndex = 0);
                break;
            case R.id.main_bottom_transaction:
                selNavItem(selNavItemIndex = 1);
                break;
            case R.id.main_bottom_radio:
                selNavItem(selNavItemIndex = 2);
                break;
            case R.id.main_bottom_discover:
                selNavItem(selNavItemIndex = 3);
                break;
            case R.id.main_bottom_me:
                if (Global.USERINFO == null) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    selNavItem(selNavItemIndex = 4);
                }
                break;
        }
    }

    /**
     * 设置选择的哪个导航按钮
     */
    public void selNavItem(int index) {
        if (mCurrentIndex == index) {
            return;
        }
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideAllTransaction(transaction);
        switch (index) {
            case 0:
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    transaction.add(R.id.main_frame, mHomeFragment);
                } else {
                    transaction.show(mHomeFragment);
                }
                mTitles[0] = mBottomText[0];
                break;
            case 1:
                if (mTransactionFragment == null) {
                    mTransactionFragment = new TransactionFragment();
                    transaction.add(R.id.main_frame, mTransactionFragment);
                    //选择国际期货
                    mCurrentQiHuo = 0;

                } else {
                    transaction.show(mTransactionFragment);
                }
                break;
            case 2:
                if (mRadioFragment == null) {
                    mRadioFragment = new RadioFragment();
                    transaction.add(R.id.main_frame, mRadioFragment);
                } else {
                    transaction.show(mRadioFragment);
                }
//                mDiscoverFragment.setOnTitleListener(new BaseFragment.SetTitleListener() {
//                    @Override
//                    public void setTitle(String title) {
//                        mTopBar.setTitle(title);
//                        mTitles[2] = title;
//                    }
//                });
                mTitles[2] = mBottomText[2];
                break;
            case 3:
                if (mDiscoverFragment == null) {
                    mDiscoverFragment = new FindFragment();
                    transaction.add(R.id.main_frame, mDiscoverFragment);
                } else {
                    transaction.show(mDiscoverFragment);
                }
//                mDiscoverFragment.setOnTitleListener(new BaseFragment.SetTitleListener() {
//                    @Override
//                    public void setTitle(String title) {
//                        mTopBar.setTitle(title);
//                        mTitles[2] = title;
//                    }
//                });
                mTitles[3] = mBottomText[3];
                break;
            case 4:
                if (mMeFragment == null) {
                    mMeFragment = new MeFragment();
                    transaction.add(R.id.main_frame, mMeFragment);
                } else {
                    transaction.show(mMeFragment);
                }
//                mMeFragment.setOnTitleListener(new BaseFragment.SetTitleListener() {
//                    @Override
//                    public void setTitle(String title) {
//                        mTopBar.setTitle(title);
//                        mTitles[3] = title;
//                    }
//                });
                mTitles[4] = mBottomText[4];
                break;
        }
        if (index == 0) {
            mTopBar.setRightView(mImageHomeTopBarRight);
        } else {
            mTopBar.hideRightView();
        }
        if (index == 1) {
            mTopBar.showMiddleMenu();
        } else {
            if (!TextUtils.isEmpty(mTitles[index])) {
                mTopBar.setTitle(mTitles[index]);
            }
        }

        //最关键的不要忘了commit
        transaction.commit();
        mCurrentIndex = index;
        for (int i = 0; i < mBottomNav.length; i++) {
            if (i == index) {
                mBottomNav[i].setSelected(i, true);
            } else {
                mBottomNav[i].setSelected(i, false);
            }
        }

    }

    /**
     * 隐藏已经显示过的Fragment
     *
     * @param transaction
     */
    private void hideAllTransaction(FragmentTransaction transaction) {
        if (mHomeFragment != null) {
            transaction.hide(mHomeFragment);
        }
        if (mTransactionFragment != null) {
            transaction.hide(mTransactionFragment);
        }
        if (mRadioFragment != null) {
            transaction.hide(mRadioFragment);
        }
        if (mDiscoverFragment != null) {
            transaction.hide(mDiscoverFragment);
        }
        if (mMeFragment != null) {
            transaction.hide(mMeFragment);
        }
    }

    /**
     * 申请相关权限
     */
    private void requestPermission() {
        new RequestPermissionUtils(this).aleraPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 1);
        new RequestPermissionUtils(this).aleraPermission(Manifest.permission.READ_EXTERNAL_STORAGE, 1);
        new RequestPermissionUtils(this).aleraPermission(Manifest.permission.CALL_PHONE, 1);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitAPP();
            return false;
        } else {
            return true;
        }
    }

    /**
     * 双击退出函数
     */
    private void exitAPP() {
        Timer timer = null;
        TimerTask timerTask = null;
        if (isExit == false) {
            isExit = true;
            Toast.makeText(this, getResources().getString(R.string.exit_app_tip), Toast.LENGTH_SHORT).show();
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            };
            timer.schedule(timerTask, 2000);
        } else {
            realeaseData();
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    private void realeaseData() {
        Global.screenWidth = 0;
        Global.screenHeight = 0;
        Global.LOGIN_INFO_MAP = null;
        Global.NOTICE_DATA = null;
        Global.IMEI = null;
        Global.MODEL = null;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isForeground = false;
        mCurrBoardCast.unregisterBroadcast();
        if (mDiscoverFragment != null) {
            mDiscoverFragment.mCurrBoardCast.unregisterBroadcast();
        }
        if (mTransactionFragment != null) {
            mTransactionFragment.mCurrBoardCast.unregisterBroadcast();
        }
//        if (mRadioFragment != null) {
//            mRadioFragment.mCurrBoardCast.unregisterBroadcast();
//        }
        if (mMeFragment != null) {
            mMeFragment.mCurrBoardCast.unregisterBroadcast();
        }
        for (int i = 0; i < MyScrollWebView.ALLWEBACTIVITYLIST.size(); i++) {
            MyScrollWebView.ALLWEBACTIVITYLIST.get(i).finish();
        }
        MyScrollWebView.ALLWEBACTIVITYLIST.clear();
        MyScrollWebView.ALLWEBACTIVITYLIST = null;
    }
}


//public class MainActivity extends BaseFragmentActivity implements View.OnClickListener {
//    private TopBarView mTopBar;
//    private FragmentManager mFragmentManager;
//    private BaseFragment mHomeFragment;
//    private TransactionFragment mTransactionFragment;
//    private FindFragment mDiscoverFragment;
//    private MeFragment mMeFragment;
//    private BottomNavImageView[] mBottomNav;
//    private BottomNavImageView mNavHome, mNavTransaction, mNavDiscover, mNavMe;
//    private TextView mTvGuoJi, mTvGuoNei;
//    private ImageView mIvSearch;
//    private TextView[] mTransactionNav;
//    private int mCurrentIndex = -1;
//    private int mCurrentQiHuo = -1;
//
//    public static int selNavItemIndex = -1; //选择的底部导航的哪个item
//
//    DisplayImageOptions options;
//    int[] mBottomIcom = new int[]{R.drawable.selector_bottom_nav_home, R.drawable.selector_bottom_nav_tansaction, R.drawable.selector_bottom_nav_discover, R.drawable.selector_bottom_nav_me};
//    String[] mBottomText, mTitles;
//    //    public static Context MAIN_CONTEXT = null;
//    private static Boolean isExit = false;
//
//    private LocalBroadcastManager mLocalBroadcastManager;
//    private Coco3gBroadcastUtils mCurrBoardCast;
//    //处理返回MainActivity的时候的延时操作
//    private Handler mHandler = new Handler();
//    public static boolean isForeground = false;
//    ImageView mImageHomeTopBarRight = null;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
////        setContentView(int layoutResID)方法使用xml布局文件的方法的完整签名
//        setContentView(R.layout.activity_main);
////        MAIN_CONTEXT = this;
//        options = new DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .considerExifParams(true)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//                .resetViewBeforeLoading(true)
//                .displayer(new RoundedBitmapDisplayer(200)).build();
//        //
//        mFragmentManager = getSupportFragmentManager();
//        mBottomText = new String[]{getResources().getString(R.string.text_bottom_home), getResources().getString(R.string.text_bottom_transaction), getResources().getString(R.string.text_bottom_discover), getResources().getString(R.string.text_bottom_me)};//直播
//        mTitles = new String[4];//5
//        initView();
//        //注册锁屏超时广播
////        registerBroadcast();
//        requestPermission(); // 申请相关权限
//        // 检查新版本
//        new VersionUpdateUtils(this).checkUpdate(false, ""); // 检查最新版本
//        //
//        // 接收到push通知
//        if (Global.NOTICE_DATA != null) {
//            Intent intent = new Intent(MainActivity.this, WebActivity.class);
//            intent.putExtra("url", Global.NOTICE_DATA.txt);
//            intent.putExtra("title", "");
//            startActivity(intent);
//        }
//        //
//        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//        startActivity(intent);
//    }
//
//    private void initView() {
//        mTopBar = (TopBarView) findViewById(R.id.main_topbar);
//        mTopBar.hideLeftView();
//        //
//        mImageHomeTopBarRight = new ImageView(this);
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(Global.screenWidth / 14, Global.screenWidth / 14);
//        mImageHomeTopBarRight.setLayoutParams(lp);
//        ImageLoader.getInstance().displayImage("drawable://" + R.mipmap.pic_user_bg1, mImageHomeTopBarRight, options);
//        //
////        mTvGuoJi = (TextView) findViewById(R.id.main_tv_guoji);
////        mTvGuoNei = (TextView) findViewById(R.id.main_tv_guonei);
////        mIvSearch = (ImageView) findViewById(R.id.main_iv_search);
////        mTransactionBar = (RelativeLayout) findViewById(R.id.main_rl_transaction);
//        mNavHome = (BottomNavImageView) findViewById(R.id.main_bottom_home);
//        mNavTransaction = (BottomNavImageView) findViewById(R.id.main_bottom_transaction);
//        mNavDiscover = (BottomNavImageView) findViewById(R.id.main_bottom_discover);
//        mNavMe = (BottomNavImageView) findViewById(R.id.main_bottom_me);
//
//        mBottomNav = new BottomNavImageView[]{mNavHome, mNavTransaction, mNavDiscover, mNavMe};
//        for (int i = 0; i < mBottomNav.length; i++) {
//            mBottomNav[i].setIcon(mBottomIcom[i], mBottomText[i]);
//        }
//
//        mNavHome.setOnClickListener(this);
//        mNavTransaction.setOnClickListener(this);
//        mNavDiscover.setOnClickListener(this);
//        mNavMe.setOnClickListener(this);
//
//        mTopBar.setOnClickMiddleMenuListener(new TopBarView.OnClickMiddleMenu() {
//            @Override
//            public void onClickMiddleMenu(int resID) {
//                switch (resID) {
//                    case R.id.tv_topbar_middle_left:
//                        if (mTransactionFragment != null) {
//                            mTransactionFragment.switchTrans(14);
//                        }
//                        break;
//                    case R.id.tv_topbar_middle_right:
//                        if (mTransactionFragment != null) {
//                            mTransactionFragment.switchTrans(15);
//                        }
//                        break;
//                }
//            }
//        });
//        mTopBar.setOnClickTopRight(new TopBarView.OnClickTopRight() {
//            @Override
//            public void onClickTopRight() {
//                Intent intent = new Intent(MainActivity.this, WebActivity.class);
//                intent.putExtra("title", "");
//                intent.putExtra("url", DataUrl.CHICANG);
//                startActivity(intent);
//            }
//        });
//        mTopBar.setOnClickRightListener(new TopBarView.OnClickRightView() {
//            @Override
//            public void onClickTopbarView() {
//                Intent intent = new Intent(MainActivity.this, WebActivity.class);
//                intent.putExtra("title", "");
//                intent.putExtra("url", DataUrl.HELP_PAGE);
//                startActivity(intent);
//            }
//        });
//        selNavItem(0);
//
//        //注册一个有登录界面点击返回键,强制返回HomeFragment
//        mCurrBoardCast = new Coco3gBroadcastUtils(MainActivity.this);
//        mCurrBoardCast.receiveBroadcast(Coco3gBroadcastUtils.RETURN_HOME_FLAG)
//                .setOnReceivebroadcastListener(new Coco3gBroadcastUtils.OnReceiveBroadcastListener() {
//                    @Override
//                    public void receiveReturn(Intent intent) {
//                        selNavItem(0);
////                        Log.e("刷新数据", "me");
//                    }
//                });
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        isForeground = true;
//        //刷新发现Fragment
//        if (mDiscoverFragment != null) {
//            new Coco3gBroadcastUtils(MainActivity.this).sendBroadcast(Coco3gBroadcastUtils.RETURN_UPDATE_FIND_FLAG, null);
//        }
//        //刷新我的Fragment
//        if (mMeFragment != null) {
//            new Coco3gBroadcastUtils(MainActivity.this).sendBroadcast(Coco3gBroadcastUtils.RETURN_UPDATE_ME_FLAG, null);
//        }
//        //当返回MainActivity的时候,在onResume里面(刷新国内期货或国内期货的数据)
//        if (mTransactionFragment != null) {
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mTransactionFragment.refreshData();
//                }
//            }, 500);
//        }
//    }
//
//    //退出登录
//    public void logout() {
//        new PublicPresenter(this).logout(new IBaseDataListener() {
//            @Override
//            public void onSuccess(BaseData data) {
//                Log.e("退出", "退出登录onSuccess");
//            }
//
//            @Override
//            public void onFailure(BaseData data) {
//                Log.e("出错了", "退出登录onFailure");
//            }
//
//            @Override
//            public void onError() {
//                Log.e("onError", "退出登录onError");
//            }
//        });
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.main_bottom_home:
//                selNavItem(selNavItemIndex = 0);
//                break;
//            case R.id.main_bottom_transaction:
//                selNavItem(selNavItemIndex = 1);
//                break;
//            case R.id.main_bottom_discover:
//                selNavItem(selNavItemIndex = 2);
//                break;
//            case R.id.main_bottom_me:
//                if (Global.USERINFO == null) {
//                    Intent intent = new Intent(this, LoginActivity.class);
//                    startActivity(intent);
//                } else {
//                    selNavItem(selNavItemIndex = 3);
//                }
//                break;
//        }
//    }
//
//    /**
//     * 设置选择的哪个导航按钮
//     */
//    public void selNavItem(int index) {
//        if (mCurrentIndex == index) {
//            return;
//        }
//        FragmentTransaction transaction = mFragmentManager.beginTransaction();
//        hideAllTransaction(transaction);
//        switch (index) {
//            case 0:
//                if (mHomeFragment == null) {
//                    mHomeFragment = new HomeFragment();
//                    transaction.add(R.id.main_frame, mHomeFragment);
//                } else {
//                    transaction.show(mHomeFragment);
//                }
//                mTitles[0] = mBottomText[0];
//                break;
//            case 1:
//                if (mTransactionFragment == null) {
//                    mTransactionFragment = new TransactionFragment();
//                    transaction.add(R.id.main_frame, mTransactionFragment);
//                    //选择国际期货
//                    mCurrentQiHuo = 0;
//
//                } else {
//                    transaction.show(mTransactionFragment);
//                }
//                break;
//            case 2:
//                if (mDiscoverFragment == null) {
//                    mDiscoverFragment = new FindFragment();
//                    transaction.add(R.id.main_frame, mDiscoverFragment);
//                } else {
//                    transaction.show(mDiscoverFragment);
//                }
////                mDiscoverFragment.setOnTitleListener(new BaseFragment.SetTitleListener() {
////                    @Override
////                    public void setTitle(String title) {
////                        mTopBar.setTitle(title);
////                        mTitles[2] = title;
////                    }
////                });
//                mTitles[2] = mBottomText[2];
//                break;
//            case 3:
//                if (mMeFragment == null) {
//                    mMeFragment = new MeFragment();
//                    transaction.add(R.id.main_frame, mMeFragment);
//                } else {
//                    transaction.show(mMeFragment);
//                }
////                mMeFragment.setOnTitleListener(new BaseFragment.SetTitleListener() {
////                    @Override
////                    public void setTitle(String title) {
////                        mTopBar.setTitle(title);
////                        mTitles[3] = title;
////                    }
////                });
//                mTitles[3] = mBottomText[3];
//                break;
//        }
//        if (index == 0) {
//            mTopBar.setRightView(mImageHomeTopBarRight);
//        } else {
//            mTopBar.hideRightView();
//        }
//        if (index == 1) {
//            mTopBar.showMiddleMenu();
//        } else {
//            if (!TextUtils.isEmpty(mTitles[index])) {
//                mTopBar.setTitle(mTitles[index]);
//            }
//        }
//
//        //最关键的不要忘了commit
//        transaction.commit();
//        mCurrentIndex = index;
//        for (int i = 0; i < mBottomNav.length; i++) {
//            if (i == index) {
//                mBottomNav[i].setSelected(i, true);
//            } else {
//                mBottomNav[i].setSelected(i, false);
//            }
//        }
//
//    }
//
//    /**
//     * 隐藏已经显示过的Fragment
//     *
//     * @param transaction
//     */
//    private void hideAllTransaction(FragmentTransaction transaction) {
//        if (mHomeFragment != null) {
//            transaction.hide(mHomeFragment);
//        }
//        if (mTransactionFragment != null) {
//            transaction.hide(mTransactionFragment);
//        }
//        if (mDiscoverFragment != null) {
//            transaction.hide(mDiscoverFragment);
//        }
//        if (mMeFragment != null) {
//            transaction.hide(mMeFragment);
//        }
//    }
//
//    /**
//     * 申请相关权限
//     */
//    private void requestPermission() {
//        new RequestPermissionUtils(this).aleraPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 1);
//        new RequestPermissionUtils(this).aleraPermission(Manifest.permission.READ_EXTERNAL_STORAGE, 1);
//        new RequestPermissionUtils(this).aleraPermission(Manifest.permission.CALL_PHONE, 1);
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            exitAPP();
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    /**
//     * 双击退出函数
//     */
//    private void exitAPP() {
//        Timer timer = null;
//        TimerTask timerTask = null;
//        if (isExit == false) {
//            isExit = true;
//            Toast.makeText(this, getResources().getString(R.string.exit_app_tip), Toast.LENGTH_SHORT).show();
//            timer = new Timer();
//            timerTask = new TimerTask() {
//                @Override
//                public void run() {
//                    isExit = false;
//                }
//            };
//            timer.schedule(timerTask, 2000);
//        } else {
//            realeaseData();
//            finish();
//            android.os.Process.killProcess(android.os.Process.myPid());
//        }
//    }
//
//    private void realeaseData() {
//        Global.screenWidth = 0;
//        Global.screenHeight = 0;
//        Global.LOGIN_INFO_MAP = null;
//        Global.NOTICE_DATA = null;
//        Global.IMEI = null;
//        Global.MODEL = null;
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        isForeground = false;
//        mCurrBoardCast.unregisterBroadcast();
//        if (mDiscoverFragment != null) {
//            mDiscoverFragment.mCurrBoardCast.unregisterBroadcast();
//        }
//        if (mTransactionFragment != null) {
//            mTransactionFragment.mCurrBoardCast.unregisterBroadcast();
//        }
//        if (mMeFragment != null) {
//            mMeFragment.mCurrBoardCast.unregisterBroadcast();
//        }
//        for (int i = 0; i < MyScrollWebView.ALLWEBACTIVITYLIST.size(); i++) {
//            MyScrollWebView.ALLWEBACTIVITYLIST.get(i).finish();
//        }
//        MyScrollWebView.ALLWEBACTIVITYLIST.clear();
//        MyScrollWebView.ALLWEBACTIVITYLIST = null;
//    }
//}
