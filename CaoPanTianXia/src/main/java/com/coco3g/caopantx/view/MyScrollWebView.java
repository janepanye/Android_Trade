package com.coco3g.caopantx.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.andview.refreshview.XRefreshView;
import com.coco3g.caopantx.R;
import com.coco3g.caopantx.activity.MainActivity;
import com.coco3g.caopantx.activity.WebActivity;
import com.coco3g.caopantx.data.DataUrl;
import com.coco3g.caopantx.data.Global;
import com.coco3g.caopantx.data.TypevauleGotoDictionary;
import com.coco3g.caopantx.utils.ImageUtils;
import com.coco3g.caopantx.utils.JsCallMethod;
import com.coco3g.caopantx.zxing.DecodeImage;
import com.google.zxing.Result;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by lisen on 16/2/29 16:16.
 */
public class MyScrollWebView extends RelativeLayout {
    Context mContext;
    View mView;
    public WebView mScrollView;
    public XRefreshView mXRefreshView;
    String mUrl = "";
    SetTitleListener settitlelistener;
    ConfigTopBarMenu configtopbarmenu;
    TypevauleGotoDictionary mTypeVaule;
    public static ArrayList<Activity> ALLWEBACTIVITYLIST = new ArrayList<>();

    public MyScrollWebView(Context context) {
        super(context);
        mContext = context;
    }

    public MyScrollWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mTypeVaule = new TypevauleGotoDictionary(mContext);
        initView();
    }

    public MyScrollWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    private void initView() {
        LayoutInflater lay = LayoutInflater.from(mContext);
        mView = lay.inflate(R.layout.view_scrollwebview, this);
        mScrollView = (WebView) mView.findViewById(R.id.view_scroll_webview);
        mXRefreshView = (XRefreshView) mView.findViewById(R.id.xrefresh_view);
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
                Log.e("刷新", "肯定记得记得看");
            }

            @Override
            public void onLoadMore(boolean isSlience) {
                mScrollView.loadUrl("javascript: pullup()");
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
        mScrollView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.e("title", title);
                if (TextUtils.isEmpty(title) || title.startsWith("http://") || title.startsWith("https://") || title.startsWith("wap.app.njs168.com") || title.startsWith("zql.coco3g.net")) {
                    title = mContext.getString(R.string.app_name);
                }
                setTitleFromHtml(title);
            }
        });
        //webview与javaScript的交互
        mScrollView.addJavascriptInterface(new getHtmlObject(), "CocoObj");
        //识别访问的设备
        mScrollView.getSettings().setUserAgentString(mScrollView.getSettings().getUserAgentString() + ";Coco3gAppAndroid");
//        Log.e("aaaaaa", mScrollView.getSettings().getUserAgentString() + "--");
        //
        mScrollView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // 设置缓存模式
        // 开启DOM storage API 功能  //webview数据(网页缓存、数据缓存)缓存的一种类型
        mScrollView.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        mScrollView.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = mContext.getFilesDir().getAbsolutePath() + "webview";
        // 设置数据库缓存路径
        mScrollView.getSettings().setDatabasePath(cacheDirPath); // API 19 deprecated

        // 开启Application Cache功能
        mScrollView.getSettings().setAppCacheEnabled(true);
        mScrollView.getSettings().setAppCacheMaxSize(5 * 1024 * 1024);
        // 设置Application caches缓存目录   (appCache数据缓存的一种)
        mScrollView.getSettings().setAppCachePath(cacheDirPath);
        //
        mScrollView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, final String url) {
                // TODO Auto-generated method stub
//                if (url.equalsIgnoreCase(DataUrl.TRANSACTION_MONI_NEI)) {
//                    Intent intent = new Intent(mContext, MoNiTransActivity.class);
//                    mContext.startActivity(intent);
//                    return true;
//                }
                if (TextUtils.isEmpty(url)) {
                    return true;
                }
                if (url.startsWith("tel:")) {
                    String phonenum = url.replace("tel:", "");
                    Global.callPhone(mContext, phonenum);
                    return true;
                }
                if (!url.startsWith("http://coco3g-app/")) {
                    if (url.contains("target=self")) {
                        return super.shouldOverrideUrlLoading(view, url);
                    }
                    Intent intent = new Intent(mContext, WebActivity.class);
                    intent.putExtra("url", url);
                    if (url.startsWith(DataUrl.BASE_URL)) {
                        intent.putExtra("title", mContext.getString(R.string.app_name));
                    }
                    mContext.startActivity(intent);
                    return true;
                } else {
                    mTypeVaule.gotoViewChoose(mScrollView, url);
                    return true;
                }

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//                mProgress = MyProgressDialog.show(mContext, mContext.getString(R.string.loading), false, true);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                mXRefreshView.stopRefresh();
                super.onPageFinished(view, url);
//                if (!mUrl.contains("pull_down=1")) {
////                    mScrollView.setMode(PullToRefreshBase.Mode.DISABLED);
//                    mXRefreshView.setPullLoadEnable(false);
//                    mXRefreshView.setPullRefreshEnable(false);
////                    webView.loadUrl(mUrl);
//                }
                //判断是否在交易的页面,如果是可以下拉刷新
                if (MainActivity.selNavItemIndex == 1 || mUrl.contains("pull_down=1")) {
                    mXRefreshView.setPullRefreshEnable(true);
                    mXRefreshView.setDampingRatio(1.8f);
                } else {
                    mXRefreshView.setPullRefreshEnable(false);
                    mXRefreshView.setDampingRatio(100);
                }

                mScrollView.loadUrl("javascript: set_window_height('" + mScrollView.getHeight() + "')");
//                //判断手机系统版本是否大于4.4版本
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    webView.evaluateJavascript("c3_navtive_user.right_button_item.title", new ValueCallback() {
//                        @Override
//                        public void onReceiveValue(Object value) {
//                            String str = (String) value;
//                            ArrayList<String> list = new ArrayList<String>();
//                            Gson gson = new Gson();
//                            list = gson.fromJson(str, new ArrayList<String>().getClass());
//                            if (list != null) {
//                                for (int i = 0; i < list.size(); i++) {
//                                    Log.e("sasas", list.get(i) + "--");
//                                }
//                            }
//                            if (!TextUtils.isEmpty(str)) {
//                                str = str.replaceAll("\"", "");
//                            }
//                            configMenu(str);
//                        }
//                    });
//                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // 接受所有网站的证书
            }

        });

        mScrollView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                WebView wv = (WebView) view;
                final WebView.HitTestResult hr = wv.getHitTestResult();
                if (hr != null && hr.getType() == WebView.HitTestResult.IMAGE_TYPE) {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            Bitmap bitmap = new ImageUtils().returnBitmap(hr.getExtra());
                            if (bitmap != null) {
                                final Result result = DecodeImage.handleQRCodeFormBitmap(bitmap);
                                if (result == null) { // 不是二维码
                                } else { // 是二维码
                                    // http://weixin.qq.com/r/d-O1rf3EPaBCrbnO96Zl
                                    if (result.toString().startsWith("http://weixin.qq.com/")) {
                                        ((Activity) mContext).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                // TODO Auto-generated method stub
//                                                Intent intent = new Intent(mContext, WebActivity.class);
//                                                intent.putExtra("url", result.toString());
//                                                mContext.startActivity(intent);
                                                Uri uri = Uri.parse("weixin://dl/scan");
                                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                mContext.startActivity(intent);
//                                                mScrollView.loadUrl(result.toString());
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }.start();
                }
                return true;
            }
        });
        //
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mXRefreshView.startRefresh();
//            }
//        }, 500);
//        final HashMap<String, String> hs = new HashMap<>();
//        hs.put("cookie", MyHttpUlrConnection.COOKIE);
//        mScrollView.loadUrl(mUrl, hs);
        if (Global.USERINFO != null && !"".equals(Global.USERINFO.userid)) {
//            CookieSyncManager.createInstance(this);
//            CookieManager cookieManager = CookieManager.getInstance();
//            cookieManager.setAcceptCookie(true);
////            cookieManager.removeSessionCookie();// 移除
//            cookieManager.setCookie(url, "userid=" + Global.USERINFO.userid);// cookies是在HttpClient中获得的cookie
//            CookieSyncManager.getInstance().sync();
            CookieSyncManager.createInstance(mContext);
            CookieManager cookieManager = CookieManager.getInstance();
            String cookie = cookieManager.getCookie("cookie");
            cookieManager.setCookie(mUrl, cookie);// cookies是在HttpClient中获得的cookie
            CookieSyncManager.getInstance().sync();
        }
        mScrollView.loadUrl(mUrl);
//        webView.loadUrl("javascript: c3_navtive_user.right_button_item.title");
    }

    public class getHtmlObject {
        //如果你的程序目标平台是17或者是更高，你必须要在暴露给网页可调用的方法加上@JavascriptInterface注释。
        @JavascriptInterface
        public void AppAction(String action, String json, String callback) {
            try {
                Class classes = Class.forName("com.coco3g.zhong.utils.JsCallMethod");
                Method method = classes.getDeclaredMethod(action, String.class, String.class);
                JsCallMethod jsCallMethod = new JsCallMethod();
                jsCallMethod.setOnJsReturnDataListener(onJsReturnInterface);
                method.invoke(jsCallMethod, json, callback);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

    }

    public void save() {
//        webView.loadUrl("javascript:edit_info()");
        mScrollView.loadUrl("javascript:$('#dosubmit').click();");
        mScrollView.destroy();
    }

    public void update() {
        mScrollView.reload();
    }

    public void execJsUrl(String jsStr) {
        mScrollView.loadUrl(jsStr);
    }


    //接口监听
    public void setOnTitleListener(SetTitleListener settitlelistener) {
        this.settitlelistener = settitlelistener;
    }

    public interface SetTitleListener {
        void setTitle(String title);
    }

    /**
     * 读取html中的title信息
     *
     * @param title
     */
    private void setTitleFromHtml(String title) {
        if (settitlelistener != null) {
            settitlelistener.setTitle(title);
        }
    }

    public void setOnConfigMenuListener(ConfigTopBarMenu configtopbarmenu) {
        this.configtopbarmenu = configtopbarmenu;
    }

    public interface ConfigTopBarMenu {
        void configmenu(String json, String callback);
    }

    private void configTopMenu(String json, String callback) {
        if (configtopbarmenu != null) {
            configtopbarmenu.configmenu(json, callback);
        }
    }

    JsCallMethod.OnJsReturnInterface onJsReturnInterface = new JsCallMethod.OnJsReturnInterface() {

        @Override
        public void returnData(final String json, final String callback) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    configTopMenu(json, callback);
                }
            });

        }
    };

    /**
     * 清除WebView缓存
     */
    public void clearWebViewCache() {

        //清理Webview缓存数据库
        try {
            mContext.deleteDatabase("webview.db");
            mContext.deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //WebView 缓存文件
        File appCacheDir = new File(mContext.getFilesDir().getAbsolutePath() + "webview");

        File webviewCacheDir = new File(mContext.getCacheDir().getAbsolutePath() + "/webviewCache");

        //删除webview 缓存目录
        if (webviewCacheDir.exists()) {
            deleteFile(webviewCacheDir);
        }
        //删除webview 缓存 缓存目录
        if (appCacheDir.exists()) {
            deleteFile(appCacheDir);
        }

    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
        }
    }

    public void clearMemory() {
        mScrollView.removeAllViews();
        mScrollView.destroy();
    }
}
