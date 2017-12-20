package com.coco3g.caopantx.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.andview.refreshview.XRefreshView;
import com.coco3g.caopantx.data.Global;
import com.coco3g.caopantx.R;


/**
 * Created by lisen on 16/2/29 16:16.
 */
public class KViewWebView extends RelativeLayout {
    Context mContext;
    View mView;
    public WebView mScrollView;
    public XRefreshView mXRefreshView;
    String mUrl = "";
    int webviewContentWidth;

    public KViewWebView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public KViewWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        initView();
    }

    public KViewWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initView();
    }


//    PYTODO
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void initView() {
        LayoutInflater lay = LayoutInflater.from(getContext());
        mView = lay.inflate(R.layout.view_kview_webview, this,true);
        mScrollView = (WebView) mView.findViewById(R.id.view_kview_webview);
        mXRefreshView = (XRefreshView) mView.findViewById(R.id.xrefresh_kview);
        //可以下拉刷新
        mXRefreshView.setPullRefreshEnable(false);
        //不可上拉刷新
        mXRefreshView.setPullLoadEnable(false);
        //刷新完成headview停留的时间
        mXRefreshView.setPinnedTime(50);
        //设置下拉,上拉的监听(只有在刷新的时候才加载webview)
        mXRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                mScrollView.loadUrl(mUrl);
            }

            @Override
            public void onLoadMore(boolean isSlience) {
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void loadUrl(String url) {
        this.mUrl = url;
//        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        系统有一个默认的设置，我们可以通过WebView.getSettings来得到这个设置
        mScrollView.getSettings().setSupportZoom(true);
        mScrollView.getSettings().setJavaScriptEnabled(true);
//        mScrollView.addJavascriptInterface(new JavaScriptInterface(), "HTMLOUT");
        //识别访问的设备
        mScrollView.getSettings().setUserAgentString("Coco3gApp");
        //
        mScrollView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // 设置缓存模式
        // 开启DOM storage API 功能  //webview数据(网页缓存、数据缓存)缓存的一种类型
        mScrollView.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        mScrollView.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = getContext().getFilesDir().getAbsolutePath() + "webview";
        // 设置数据库缓存路径
        mScrollView.getSettings().setDatabasePath(cacheDirPath); // API 19 deprecated
        // 开启Application Cache功能
        mScrollView.getSettings().setAppCacheEnabled(true);
        mScrollView.getSettings().setAppCacheMaxSize(5 * 1024 * 1024);
        // 设置Application caches缓存目录   (appCache数据缓存的一种)
        mScrollView.getSettings().setAppCachePath(cacheDirPath);
        //
        mScrollView.setBackgroundColor(Color.parseColor("#101318"));
        mScrollView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, final String url) {
                // TODO Auto-generated method stub
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
//                mScrollView.loadUrl("javascript：window.HTMLOUT.getContentWidth(document.getElementsByTagName('html')[0].scrollWidth);");
                //
                mScrollView.setVisibility(View.VISIBLE);
                mXRefreshView.stopRefresh();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // 接受所有网站的证书
            }

        });
//        屏蔽掉长按事件 因为webview长按时将会调用系统的复制控件:
        mScrollView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
        if (Global.USERINFO != null) {
            CookieManager cookieManager = CookieManager.getInstance();
            String cookie = cookieManager.getCookie("cookie");
            cookieManager.setCookie(mUrl, cookie);// cookies是在HttpClient中获得的cookie
            CookieSyncManager.getInstance().sync();
        }
        //
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mXRefreshView.startRefresh();
//            }
//        }, 500);
        mScrollView.loadUrl(mUrl);
    }




    public void clearMemory() {
        if(mScrollView != null) {
            this.removeView(mScrollView);
            mScrollView.stopLoading();
            mScrollView.getSettings().setJavaScriptEnabled(false);
            mScrollView.clearCache(true);
            mScrollView.clearHistory();
            mScrollView.removeAllViews();
            mScrollView.destroy();
            mScrollView = null;
        }
        mXRefreshView = null;
    }


//    class JavaScriptInterface {
//        public void getContentWidth(String value) {
//            if (value != null) {
//                webviewContentWidth = Integer.parseInt(value);
//                Log.e("height", "Result from javascript： " + webviewContentWidth);
//                Toast.makeText(mContext, "ContentWidth of webpage is: " + webviewContentWidth + "px", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
}
