package com.coco3g.caopantx.net.utls;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.coco3g.caopantx.R;
import com.coco3g.caopantx.bean.BaseData;
import com.coco3g.caopantx.data.Global;
import com.coco3g.caopantx.view.MyProgressDialog;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;

import java.util.ArrayList;

public class DownLoadDataThread extends Thread {
    Context mContext;
    private String mUrl = "";
    // private HashMap<String, Object> paramsMap = new HashMap<String,
    // Object>();
    private Object mObject = null;
    // private Object mOtherObject = null;
    private Handler mHandler = null;
    MyHttpUlrConnection httpConn = new MyHttpUlrConnection();
    String mRequestMethod = "";
    ArrayList<NameValuePair> mParams = new ArrayList<NameValuePair>();
    String mFileName = "", mFilePath = "";
    //    ArrayList<NameValuePair> mParamsImage = new ArrayList<>();
//    int what = 0;
    int mValue1 = 0;
    int mValue2 = -1;
    MyProgressDialog mProgress;
    //
    public final static int NET_RESULT_CODE_SUCCESS = 100; // 接口访问正确，返回正确
    public final static int NET_RESULT_CODE_FAILURE = 101; // 接口访问正确，返回错误
    public final static int NET_RESULT_CODE_ERROR = 102; // 接口访问错误
    //
    String mUserid = "";

    /**
     * 接口访问
     *
     * @param context       上下文
     * @param isShowLoading 是否显示loading
     * @param loadingMsg    loading提示信息
     * @param requestMethod 访问方式
     * @param object        返回数据模型
     */
    public DownLoadDataThread(Context context, boolean isShowLoading, String loadingMsg, String requestMethod, Object object) {
        mContext = context;
        if (isShowLoading) {
            if (TextUtils.isEmpty(loadingMsg)) {
                loadingMsg = mContext.getString(R.string.loading);
            }
            mProgress = MyProgressDialog.show(mContext, loadingMsg, false, true);
        }
        this.mRequestMethod = requestMethod;
        this.mObject = object;
    }

    /**
     * 设置接口地址
     *
     * @param url
     * @return
     */
    public DownLoadDataThread setUrl(String url) {
        this.mUrl = url;

        return this;
    }

    /**
     * 添加要提交的表单
     *
     * @param params
     * @return
     */
    public DownLoadDataThread addParams(ArrayList<NameValuePair> params) {
        mParams = params;

        return this;
    }

    /**
     * 添加要上传的文件
     *
     * @param name
     * @param path
     * @return
     */
    public DownLoadDataThread addFileNameFilePath(String name, String path) {
        mFileName = name;
        mFilePath = path;
        return this;
    }

    public DownLoadDataThread setHandler(Handler handler) {
        mHandler = handler;
        return this;
    }

    public DownLoadDataThread setValue1(int value1) {
        this.mValue1 = value1;
        return this;
    }

    public DownLoadDataThread setValue2(int value2) {
        this.mValue2 = value2;
        return this;
    }

    private void sendMessage(int what, int arg1, int arg2, Object obj) {
        if (mHandler != null) {
            Message msg = new Message();
            msg.what = what;
            msg.arg1 = arg1;
            msg.arg2 = arg2;
            msg.obj = obj;
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        String[] resultStr = null;
        try {
            if (mRequestMethod.equalsIgnoreCase("get")) {
                resultStr = httpConn.requestJson(mUrl);
            } else if (mRequestMethod.equalsIgnoreCase("post")) {
                resultStr = httpConn.requestJson(mUrl, mParams);
            } else if (mRequestMethod.equalsIgnoreCase("upload")) {
                resultStr = httpConn.requestJson(mUrl, mParams, mFileName, mFilePath);
            }
            if (resultStr == null || TextUtils.isEmpty(resultStr[0]) || TextUtils.isEmpty(resultStr[1])) { // 接口访问报错
                try {
                    sendMessage(NET_RESULT_CODE_ERROR, mValue1, mValue2, null);
                    String resultCode = resultStr[1];
                    if ("404".equals(resultCode)) {
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                Global.showToast("接口：" + mUrl + "\t404", mContext);
                            }
                        });
                    }
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            if (mProgress != null) {
                                mProgress.cancel();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }

            String resultJson = resultStr[0];
            Log.e("json", resultJson);
            Gson gson = new Gson();
            String resultCode = resultStr[1];
            if ("200".equals(resultCode)) { // 正确返回
                mObject = gson.fromJson(resultJson, mObject.getClass());
                sendMessage(NET_RESULT_CODE_SUCCESS, mValue1, mValue2, mObject);
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        try {
                            BaseData data = (BaseData) mObject;
                            if (!TextUtils.isEmpty(data.message)) {
                                Global.showToast(data.message, mContext);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else if ("400".equals(resultCode)) { // 错误返回
                final BaseData data = gson.fromJson(resultJson, new BaseData().getClass());
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        try {
                            if (!TextUtils.isEmpty(data.message)) {
                                Global.showToast(data.message, mContext);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                sendMessage(NET_RESULT_CODE_FAILURE, mValue1, mValue2, data);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            sendMessage(NET_RESULT_CODE_ERROR, mValue1, mValue2, null);
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    if (mProgress != null) {
                        mProgress.cancel();
                    }
                    Global.showToast(mContext.getString(R.string.error_net), mContext);
                }
            });
            return;
        }
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    if (mProgress != null) {
                        mProgress.cancel();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
