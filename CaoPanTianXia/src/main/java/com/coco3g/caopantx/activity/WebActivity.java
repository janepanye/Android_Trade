package com.coco3g.caopantx.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coco3g.caopantx.bean.ConfigMenuDataBean;
import com.coco3g.caopantx.bean.JsCallBackDataBean;
import com.coco3g.caopantx.data.Global;
import com.coco3g.caopantx.net.utls.ShareAppUtils;
import com.coco3g.caopantx.utils.Coco3gBroadcastUtils;
import com.coco3g.caopantx.view.MyScrollWebView;
import com.coco3g.caopantx.view.TopBarView;
import com.coco3g.caopantx.R;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.HashMap;

/**
 * Created by lisen on 16/3/3 16:34.
 */
public class WebActivity extends BaseActivity {
    public static final int SHARE_CLIENT = 1;
    private int mShareType = SHARE_CLIENT;
    TopBarView mTopBar;
    MyScrollWebView mScrollWebView;
    String url;
    Coco3gBroadcastUtils mCurrBoardCast;
    ShareAppUtils mShareAppUtils;
    String mTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        if (MyScrollWebView.ALLWEBACTIVITYLIST != null) {
            MyScrollWebView.ALLWEBACTIVITYLIST.add(this);
        }
//        mScrollWebView= (MyScrollWebView) findViewById(R.id.view_my_webview);
        url = getIntent().getStringExtra("url");
        mTitle = getIntent().getStringExtra("title");
//        showRightView = getIntent().getBooleanExtra("showrightview", false);
//        if (!TextUtils.isEmpty(url)) {
//            mScrollWebView.loadUrl(url);
//            return;
//        }
        initView();
        //
        mCurrBoardCast = new Coco3gBroadcastUtils(this);
        mCurrBoardCast.receiveBroadcast(Coco3gBroadcastUtils.SHARE_FLAG)
                .setOnReceivebroadcastListener(new Coco3gBroadcastUtils.OnReceiveBroadcastListener() {
                    @Override
                    public void receiveReturn(Intent intent) {
                        Bundle bundle = intent.getBundleExtra("data");
                        HashMap<String, String> hashmap = (HashMap<String, String>) bundle.getSerializable("shareinfo");
                        try {
                            String type = hashmap.get("type");
                            if ("weixin".equalsIgnoreCase(type)) {
                                mShareAppUtils = new ShareAppUtils(WebActivity.this, 1, hashmap.get("url"), hashmap.get("title"), hashmap.get("content"),
                                        hashmap.get("thumb"));
                            } else if ("pengyou".equalsIgnoreCase(type)) {
                                mShareAppUtils = new ShareAppUtils(WebActivity.this, 2, hashmap.get("url"), hashmap.get("title"), hashmap.get("content"),
                                        hashmap.get("thumb"));
                            } else if ("qq".equalsIgnoreCase(type)) {
                                mShareAppUtils = new ShareAppUtils(WebActivity.this, 3, hashmap.get("url"), hashmap.get("title"), hashmap.get("content"),
                                        hashmap.get("thumb"));
                            } else if ("sms".equalsIgnoreCase(type)) {
                                intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + ""));
                                intent.putExtra("sms_body", hashmap.get("url"));
                                startActivity(intent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String result = "";
        if (requestCode == Global.RETURN_PAY_CODE) {
            Bundle bun = data.getExtras();
            result = bun.getString("para");
            System.out.println("PAY>>>>>" + result);
            String[] retStr = result.split("\\|");
            if (null != retStr && retStr[0].equals("0000")) {
                Toast.makeText(getApplicationContext(), "支付完成", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), retStr[1], Toast.LENGTH_SHORT).show();
            }

        }
//        else if (requestCode == requestCodeBank) {
//            if (data != null) {
//                TextView city_name = (TextView) findViewById(R.id.bank_name);
//                city_name.setText(data.getStringExtra("bank_name"));
//                onSaveCode(getApplicationContext(), data.getStringExtra("bank_id"));
//            }
//        }
    }

    private void initView() {
        mTopBar = (TopBarView) findViewById(R.id.topbar_my_webview);
        if (!TextUtils.isEmpty(mTitle)) {
            mTopBar.setTitle(mTitle);
        }
        mScrollWebView = (MyScrollWebView) findViewById(R.id.view_my_webview);
//        mWebView = mScrollWebView.getRefreshableView();
        mScrollWebView.setOnTitleListener(new MyScrollWebView.SetTitleListener() {
            @Override
            public void setTitle(String title) {
                mTopBar.setTitle(title);
            }
        });
        mScrollWebView.setOnConfigMenuListener(new MyScrollWebView.ConfigTopBarMenu() {
            @Override
            public void configmenu(String json, String callback) {
                Gson gson = new Gson();
                ConfigMenuDataBean[] list = gson.fromJson(json, new ConfigMenuDataBean[]{}.getClass());
                if (list != null) {
                    if (list.length == 1) {
                        configTopBarMenuFromHtml(list[0].title, list[0].returnTag, callback);
                    } else if (list.length == 2) {
                        configTopBarMenuFromHtml(list[0].title, list[0].returnTag, callback);
                        configTopBarTwoMenuFromHtml(list[1].title, list[1].returnTag, callback);
                    }
//                    configTopBarMenuFromHtml(list[0].title);
                }
            }
        });
        mScrollWebView.loadUrl(url);
    }

    /**
     * 根据html中的js，配置菜单
     *
     * @param title
     */
    private void configTopBarMenuFromHtml(final String title, final String returnTag, final String callback) {
        if (TextUtils.isEmpty(title)) {
            return;
        }
        if (title.startsWith("http://") || title.startsWith("https://")) {
            ImageLoader.getInstance().loadImage(title, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mTopBar.getHeight() * 2 / 3, mTopBar.getHeight() * 2 / 3);
                    ImageView imageView = new ImageView(WebActivity.this);
//                    imageView.setImageBitmap(bitmap);
                    imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
                    imageView.setLayoutParams(lp);
                    int padding = Global.dipTopx(WebActivity.this, 10);
                    imageView.setPadding(padding * 3, padding, padding * 3, padding);
                    mTopBar.setRightView(imageView);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            JsCallBackDataBean calldata = new JsCallBackDataBean();
                            calldata.returnTag = returnTag;
                            Gson gson = new Gson();
                            String json = gson.toJson(calldata);
                            String js = "javascript: c3_navtive_user.callback('" + callback + "','" + json + "');";
                            mScrollWebView.execJsUrl(js);
                        }
                    });
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
        } else {
            TextView tv = new TextView(this);
            tv.setGravity(Gravity.START | Gravity.CENTER);
            tv.setText(title);
            tv.setTextSize(14f);
            int padding = Global.dipTopx(this, 10);
            tv.setPadding(padding, padding, padding, padding);
            tv.setTextColor(getResources().getColor(R.color.white));
            mTopBar.setRightView(tv);
            mTopBar.setOnClickRightListener(new TopBarView.OnClickRightView() {
                @Override
                public void onClickTopbarView() {
//                    mScrollWebView.execJsUrl("javascript: c3_navtive_user.right_button_item.callback()");
                    JsCallBackDataBean calldata = new JsCallBackDataBean();
                    calldata.returnTag = returnTag;
                    Gson gson = new Gson();
                    String json = gson.toJson(calldata);
                    String js = "javascript: c3_navtive_user.callback('" + callback + "','" + json + "');";
                    mScrollWebView.execJsUrl(js);
                }
            });
        }
    }

    /**
     * 根据html中的js，配置菜单
     *
     * @param title
     */
    private void configTopBarTwoMenuFromHtml(final String title, final String returnTag, final String callback) {
        if (TextUtils.isEmpty(title)) {
            return;
        }
        if (title.startsWith("http://") || title.startsWith("https://")) {
            ImageLoader.getInstance().loadImage(title, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mTopBar.getHeight() * 2 / 3, mTopBar.getHeight() * 2 / 3);
                    ImageView imageView = new ImageView(WebActivity.this);
//                    imageView.setImageBitmap(bitmap);
                    imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
                    imageView.setLayoutParams(lp);
                    int padding = Global.dipTopx(WebActivity.this, 10);
                    imageView.setPadding(padding * 3, padding, padding * 3, padding);
                    mTopBar.setTwoRightView(imageView);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            JsCallBackDataBean calldata = new JsCallBackDataBean();
                            calldata.returnTag = returnTag;
                            Gson gson = new Gson();
                            String json = gson.toJson(calldata);
                            String js = "javascript: c3_navtive_user.callback('" + callback + "','" + json + "');";
                            mScrollWebView.execJsUrl(js);
                        }
                    });
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
        } else {
            TextView tv = new TextView(this);
            tv.setGravity(Gravity.START | Gravity.CENTER);
            tv.setText(title);
            tv.setTextSize(14f);
            int padding = Global.dipTopx(this, 10);
            tv.setPadding(padding, padding, padding, padding);
            tv.setTextColor(getResources().getColor(R.color.white));
            mTopBar.setTwoRightView(tv);
            mTopBar.setOnClickTwoRightListener(new TopBarView.OnClickTwoRightView() {
                @Override
                public void onClickTwoTopbarView() {
                    JsCallBackDataBean calldata = new JsCallBackDataBean();
                    calldata.returnTag = returnTag;
                    Gson gson = new Gson();
                    String json = gson.toJson(calldata);
                    String js = "javascript: c3_navtive_user.callback('" + callback + "','" + json + "');";
                    mScrollWebView.execJsUrl(js);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCurrBoardCast != null) {
            mCurrBoardCast.unregisterBroadcast();
        }
        mScrollWebView.clearMemory();
//        try {
//            if (MyScrollWebView.ALLWEBACTIVITYLIST != null) {
//                MyScrollWebView.ALLWEBACTIVITYLIST.remove(this);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}
