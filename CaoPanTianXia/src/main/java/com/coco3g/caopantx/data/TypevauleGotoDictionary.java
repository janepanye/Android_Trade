package com.coco3g.caopantx.data;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.coco3g.caopantx.activity.MoNiTransActivity;
import com.coco3g.caopantx.view.MyScrollWebView;
import com.coco3g.caopantx.R;
import com.coco3g.caopantx.activity.K_DetailActivity;
import com.coco3g.caopantx.activity.LoginActivity;
import com.coco3g.caopantx.activity.WebActivity;
import com.coco3g.caopantx.bean.BaseData;
import com.coco3g.caopantx.bean.TradeNoDataBean;
import com.coco3g.caopantx.listener.IBaseDataListener;
import com.coco3g.caopantx.listener.ITradeNoListener;
import com.coco3g.caopantx.presenter.OrderDetailPresenter;
import com.coco3g.caopantx.presenter.PublicPresenter;
import com.coco3g.caopantx.utils.Coco3gBroadcastUtils;
import com.coco3g.caopantx.view.MyProgressDialog;
import com.uns.pay.example.CallUnsPay;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

/**
 * Created by lisen on 16/3/29.
 */
public class TypevauleGotoDictionary {
    Context mContext;
    private String indexMoNi = "http://coco3g-app/page=moni?";
    private String indexDetail = "http://coco3g-app/page=pro_detail?";
    private String indexLogin = "http://coco3g-app/page=login?";
    private String indexLogout = "http://coco3g-app/page=logout?";
    private String indexClosewindow = "http://coco3g-app/close_window?";
    private String indexAlert = "http://coco3g-app/alert?";
    private String indexConfirm = "http://coco3g-app/confirm?";
    private String indexCloseLoading = "http://coco3g-app/closeLoading?";
    private String indexLoading = "http://coco3g-app/loading?";
    private String indexRefreshwindow = "http://coco3g-app/refresh_window";
    private String indexMoniTrans = "http://coco3g-app/page=jiaoyi_moni?";
    private String indexPay = "http://coco3g-app/page=payonline?";
    private String indexCloseGotoPage = "http://coco3g-app/page=close_goto_page?";
    private String indexShare = "http://coco3g-app/share?";
    private String indexReturnMe = "http://coco3g-app/memberindex?";
    private String indexToQQ = "http://coco3g-app/openqq?";
    //
    MyProgressDialog mProgress;

    public TypevauleGotoDictionary(Context context) {
        mContext = context;
    }

    public TypevauleGotoDictionary gotoViewChoose(final WebView webview, String value) {
        Intent intent = null;
        if (!value.startsWith(indexCloseGotoPage)) {
            try {
                value = URLDecoder.decode(value, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        // coco3g://page=close_goto_page?url=coco3g://page=login?
        int position = value.indexOf("?");
        String newUrl = value.substring(position + 1);
        final HashMap<String, String> hashMap = Global.parseCustomUrl(newUrl);
        if (value.startsWith(indexMoNi)) {
            intent = new Intent(mContext, MoNiTransActivity.class);
            mContext.startActivity(intent);
            webview.loadUrl("javascript:c3_navtive_user.callback('" + hashMap.get("callbackTag") + "')");
        } else if (value.startsWith(indexLogin)) {
            intent = new Intent(mContext, LoginActivity.class);
            mContext.startActivity(intent);
            try {
                webview.loadUrl("javascript:c3_navtive_user.callback('" + hashMap.get("callbackTag") + "')");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (value.startsWith(indexLogout)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            builder.setMessage("确认注销吗？");
            builder.setTitle("提示");
            builder.setPositiveButton(mContext.getString(R.string.confirm), new AlertDialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    logout();
                    //
                    try {
                        webview.loadUrl("javascript:c3_navtive_user.callback('" + hashMap.get("callbackTag") + "')");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });
            builder.setNegativeButton("取消", new AlertDialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub

                }
            });
            builder.create().show();
        } else if (value.startsWith(indexDetail)) {
            intent = new Intent(mContext, K_DetailActivity.class);
            intent.putExtra("id", hashMap.get("tid"));
            intent.putExtra("device", "Android");
            intent.putExtra("moni", hashMap.get("moni"));
            mContext.startActivity(intent);
            try {
                webview.loadUrl("javascript:c3_navtive_user.callback('" + hashMap.get("callbackTag") + "')");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (value.startsWith(indexClosewindow)) {
            ((Activity) mContext).finish();
            new Coco3gBroadcastUtils(mContext).sendBroadcast(Coco3gBroadcastUtils.RETURN_UPDATE_FLAG, null);
            try {
                webview.loadUrl("javascript:c3_navtive_user.callback('" + hashMap.get("callbackTag") + "')");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (value.startsWith(indexAlert)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            builder.setMessage(hashMap.get("message"));
            builder.setTitle(hashMap.get("title"));
            builder.setPositiveButton(mContext.getString(R.string.confirm), new AlertDialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    try {
                        webview.loadUrl("javascript:c3_navtive_user.callback('" + hashMap.get("callbackTag") + "')");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });
            builder.create().show();
        } else if (value.startsWith(indexConfirm)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            builder.setMessage(hashMap.get("message"));
            builder.setTitle(hashMap.get("title"));
            builder.setPositiveButton(mContext.getString(R.string.confirm), new AlertDialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    try {
                        webview.loadUrl("javascript:c3_navtive_user.callback('" + hashMap.get("callbackTag") + "')");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });
            builder.setNegativeButton("取消", new AlertDialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub

                }
            });
            builder.create().show();
        } else if (value.startsWith(indexCloseLoading)) {
            closeProgressDia();
        } else if (value.startsWith(indexLoading)) {
            String title = hashMap.get("title");
            if (!TextUtils.isEmpty(title)) {
                showProgressDia(title);
            }
        } else if (value.startsWith(indexRefreshwindow)) {
            new Coco3gBroadcastUtils(mContext).sendBroadcast(Coco3gBroadcastUtils.RETURN_UPDATE_FLAG, null);
            try {
                webview.loadUrl("javascript:c3_navtive_user.callback('" + hashMap.get("callbackTag") + "')");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (value.startsWith(indexMoniTrans)) {
            intent = new Intent(mContext, MoNiTransActivity.class);
            mContext.startActivity(intent);
            try {
                webview.loadUrl("javascript:c3_navtive_user.callback('" + hashMap.get("callbackTag") + "')");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (value.startsWith(indexPay)) {
            Log.e("orderid", hashMap.get("orderid") + "--");
            getOrderDetail(hashMap.get("orderid"));
        } else if (value.startsWith(indexCloseGotoPage)) {
            ((Activity) mContext).finish();
            String newurl = hashMap.get("url");
            try {
                newurl = URLDecoder.decode(newurl, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (!newurl.startsWith("http://coco3g-app/")) {
                intent = new Intent(mContext, WebActivity.class);
                intent.putExtra("url", newurl);
                mContext.startActivity(intent);
                try {
                    webview.loadUrl("javascript:c3_navtive_user.callback('" + hashMap.get("callbackTag") + "')");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                gotoViewChoose(null, newurl);
            }
        } else if (value.startsWith(indexShare)) {
//            String type = hashMap.get("type");
            Bundle bundle = new Bundle();
            bundle.putSerializable("shareinfo", hashMap);
            new Coco3gBroadcastUtils(mContext).sendBroadcast(Coco3gBroadcastUtils.SHARE_FLAG, bundle);
//            if ("weixin".equalsIgnoreCase(type)) {
//
//            } else if ("pengyou".equalsIgnoreCase(type)) {
//
//            } else if ("qq".equalsIgnoreCase(type)) {
//
//            } else if ("sms".equalsIgnoreCase(type)) {
//
//            }
        } else if (value.startsWith(indexReturnMe)) {
            if (MyScrollWebView.ALLWEBACTIVITYLIST != null && MyScrollWebView.ALLWEBACTIVITYLIST.size() > 0) {
                for (int i = 0; i < MyScrollWebView.ALLWEBACTIVITYLIST.size(); i++) {
                    MyScrollWebView.ALLWEBACTIVITYLIST.get(i).finish();
                }
                MyScrollWebView.ALLWEBACTIVITYLIST.clear();
            }
        } else if (value.startsWith(indexToQQ)) {
            String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + hashMap.get("qq");
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
        return this;
    }

    OnReturnClickListener onReturnClickListener;

    public interface OnReturnClickListener {
        void returnClick(Bundle bundle);
    }

    public void setOnShareClickListener(OnReturnClickListener onReturnClickListener) {
        this.onReturnClickListener = onReturnClickListener;
    }

    private void returnClick(Bundle bundle) {
        if (onReturnClickListener != null) {
            onReturnClickListener.returnClick(bundle);
        }
    }

    /**
     * 显示加载进度条
     *
     * @param title
     */
    private void showProgressDia(String title) {
        //显示ProgressDialog
        mProgress = MyProgressDialog.show(mContext, title, false, true);
    }

    /**
     * 关闭加载进度条
     */
    private void closeProgressDia() {
        try {
            if (mProgress != null || mProgress.isShowing()) {
                mProgress.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getOrderDetail(String orderid) {
        new OrderDetailPresenter(mContext).getOrderInfo(orderid, new ITradeNoListener() {
            @Override
            public void onSuccess(TradeNoDataBean data) {
                HashMap<String, Object> hashmap = data.response;
                String param = "accountId==" + hashmap.get("accountId") + "&data==" + hashmap.get("data") + "&amount==" + hashmap.get("amount") +
                        "&orderId==" + hashmap.get("orderId") + "&tradeName==" + hashmap.get("goodsname")
                        + "&backResponseUrl==" + hashmap.get("backResponseUrl") + "&mac==" + hashmap.get("sign");
//                Intent payintent = new Intent(mContext, BaofooPayActivity.class);
//                payintent.putExtra(BaofooPayActivity.PAY_TOKEN, data.response.tradeNo);
//                payintent.putExtra(BaofooPayActivity.PAY_BUSINESS, true);
//                ((Activity) mContext).startActivityForResult(payintent, OrderService.REQUEST_CODE_BAOFOO_SDK);
                CallUnsPay mcCallUnsPay = new CallUnsPay((Activity) mContext, "com.example.authpay", "AuthPay.apk");
                if (!mcCallUnsPay.isApkInstalled()) {
                    //没有安装
                    boolean isdunp = mcCallUnsPay.dumpApkFromAssets();
                    if (isdunp) {
                        mcCallUnsPay.InstallAPK();
                    } else { // apk包不存在assets目录下
                        Toast.makeText(mContext, "认证支付不存在。。。", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else { //已经安装了直接调用。
                    Uri uri = Uri.parse("authpay://payforp2p");
                    Intent intent = new Intent("com.uns.pay.AUTHPAY", uri);
                    intent.putExtra("params", param);
                    ((Activity) mContext).startActivityForResult(intent, Global.RETURN_PAY_CODE);
                }

            }

            @Override
            public void onFailure(BaseData data) {

            }

            @Override
            public void onError() {

            }
        });
    }

    //退出登录
    public void logout() {
        new PublicPresenter(mContext).logout(new IBaseDataListener() {
            @Override
            public void onSuccess(BaseData data) {
                Log.e("退出", "退出登录onSuccess");
                Global.USERINFO = null;
                Global.deleteSerializeData(mContext, Global.LOGIN_INFO);
                Global.LOGIN_INFO_MAP = null;
                //
                Intent intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(intent);
                // 除cookie
                Global.clearCookie(mContext);
                ((Activity) mContext).finish();
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
}
