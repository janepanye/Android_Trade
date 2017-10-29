package com.coco3g.caopantx.net.utls;


import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.coco3g.caopantx.view.MyProgressDialog;
import com.coco3g.caopantx.R;
import com.coco3g.caopantx.data.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by lisen on 16/7/25.
 */
public class SocketRequestUtil extends Thread {
    Context mContext;
    private Handler mHandler = null;
    MyProgressDialog mProgress;
    //
    private String mRequestJson = ""; //请求的json串
    private Socket mSocket;
    private String mConnectionMessage = ""; // 连接后提示
    //
    public final static int NET_RESULT_SUCCESS = 100001; // 接口访问正确，返回正确
    public final static int NET_RESULT_TIMEOUT = 100002; // 接口访问超时
    public final static int NET_RESULT_ERROR = 100000; // 接口错误

    public SocketRequestUtil(Context context, boolean isShowLoading, String loadingMsg) {
        mContext = context;
        if (isShowLoading) {
            if (TextUtils.isEmpty(loadingMsg)) {
                loadingMsg = mContext.getString(R.string.loading);
            }
            mProgress = MyProgressDialog.show(mContext, loadingMsg, false, false);
        }
    }

    public SocketRequestUtil setRequestJson(String json) {
        mRequestJson = json;
        return this;
    }

    public SocketRequestUtil setHandler(Handler handler) {
        mHandler = handler;
        return this;
    }

    private void sendMessage(int what, Object obj) {
        if (mHandler != null) {
            Message msg = new Message();
            msg.what = what;
            msg.obj = obj;
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public void run() {
        super.run();
        try {
            Log.e("mRequestJson", mRequestJson + "--");
            String resultJson = requestSocketByJson(mRequestJson);
            if (TextUtils.isEmpty(resultJson)) { // 接口访问报错
                mConnectionMessage = "连接错误";
            } else if ((NET_RESULT_TIMEOUT + "").equalsIgnoreCase(resultJson)) {
                mConnectionMessage = "服务器请求超时，请重试";
            } else {
//                Log.e("json", resultJson);
//                Gson gson = new Gson();
//                SocketResponseDataBean reponseData = gson.fromJson(resultJson, new SocketResponseDataBean().getClass());
//                String resultCode = reponseData.Data.result;
//                if ("00".equals(resultCode)) { // 正确返回
//                    sendMessage(NET_RESULT_SUCCESS, reponseData);
//                } else { // 错误返回
//                    mConnectionMessage = reponseData.Data.errorMsg;
//                    sendMessage(NET_RESULT_ERROR, reponseData);
//                }
                sendMessage(NET_RESULT_SUCCESS, resultJson);
            }
            try {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (mProgress != null) {
                            mProgress.cancel();
                        }
                        if (!TextUtils.isEmpty(mConnectionMessage)) {
                            Toast.makeText(mContext, mConnectionMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            sendMessage(NET_RESULT_ERROR, null);
            mConnectionMessage = "连接错误";
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    if (mProgress != null) {
                        mProgress.cancel();
                    }
                    Toast.makeText(mContext, mConnectionMessage, Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }
    }

    /**
     * socket请求
     *
     * @param json
     */
    private String requestSocketByJson(String json) {
        try {
            mSocket = new Socket();
            SocketAddress socAddress = new InetSocketAddress(Constants.SOCKET_BASE_URL, Constants.SOCKET_PORT);
            mSocket.connect(socAddress, Constants.SOCKET_TIME_OUT); // 设置超时
//            OutputStream os = mSocket.getOutputStream();
//            os.write((json + "\r\n").getBytes());
//            os.write((json).getBytes());
//            os.flush();
            return responseJsonFromSocket();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            try {
                mSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return NET_RESULT_TIMEOUT + "";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String responseJsonFromSocket() {
        try {
            // 获得输入流
            BufferedReader br = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            String line = null;
            String buffer="";
            while ((line = br.readLine()) != null) {
                buffer = line + buffer;
            }
//            String line = br.readLine();
//            String line = buffer;
            br.close();
            mSocket.close();
            mSocket = null;
            return buffer;
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }
}
