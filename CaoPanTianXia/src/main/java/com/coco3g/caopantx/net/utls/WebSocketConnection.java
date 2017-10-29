package com.coco3g.caopantx.net.utls;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by lisen on 16/8/31.
 */
public class WebSocketConnection extends WebSocketClient {
    Context mContext;
    String mParam;
    Object mObject;
    private Handler mHandler = null;
    public final static int SOCKET_RESULT_CODE_SUCCESS = 100; // 接口访问正确，返回正确
    public final static int SOCKET_RESULT_CODE_FAILURE = 101; // 接口访问正确，返回错误
    OnSocketListener onsocketlistener;

    public WebSocketConnection(Context context, URI serverUri, Draft draft) {
        super(serverUri, draft);
        mContext = context;
    }

    /**
     * 添加也传的参数
     *
     * @param json
     */
    public WebSocketConnection addParam(String json) {
        mParam = json;
        return this;
    }

    /**
     * 设置要返回的数据模型
     *
     * @param object
     */
    public WebSocketConnection setModeData(Object object) {
        mObject = object;
        return this;
    }

//    public WebSocketConnection setHandler(Handler handler) {
//        mHandler = handler;
//        return this;
//    }
//
//    private void sendMessage(int what, Object obj) {
//        if (mHandler != null) {
//            Message msg = new Message();
//            msg.what = what;
//            msg.obj = obj;
//            mHandler.sendMessage(msg);
//        }
//    }

    /**
     * 打开通道，传输数据
     *
     * @param handshakedata
     */
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        send(mParam);
    }

    /**
     * 接收返回数据
     *
     * @param message
     */
    @Override
    public void onMessage(String message) {
//        PYTODO    关掉行情数据打印
//        Log.e("message", message);
        if (TextUtils.isEmpty(message)) { // 接口访问报错
            try {
//                Global.showToast("接口返回错误", mContext);
                returnData(SOCKET_RESULT_CODE_FAILURE, null);
            } catch (Exception e) {
                e.printStackTrace();
                returnData(SOCKET_RESULT_CODE_FAILURE, null);
            }
        } else {
            Gson gson = new Gson();
            try {
                mObject = gson.fromJson(message, mObject.getClass());
                returnData(SOCKET_RESULT_CODE_SUCCESS, mObject);
            } catch (Exception e) {
                e.printStackTrace();
                returnData(SOCKET_RESULT_CODE_FAILURE, null);
            }
        }
    }

    @Override
    public void onFragment(Framedata fragment) {
        Log.e("received fragment: ", new String(fragment.getPayloadData().array()));
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.e("Connection closed by", (remote ? "server" : "client"));
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }

    public void setOnSocketReturnListener(OnSocketListener onsocketlistener) {
        this.onsocketlistener = onsocketlistener;
    }

    public interface OnSocketListener {
        void socketReturn(int code, Object object);
    }

    private void returnData(int code, Object object) {
        if (onsocketlistener != null) {
            onsocketlistener.socketReturn(code, object);
        }
    }

}
