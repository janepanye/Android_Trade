package com.coco3g.caopantx.presenter;

import android.content.Context;
import android.util.Log;

import com.coco3g.caopantx.bean.TransListDataBean;
import com.coco3g.caopantx.listener.ISocketOrderListener;
import com.coco3g.caopantx.bean.HangQingBaseDataBean;
import com.coco3g.caopantx.bean.OrderDataBean;
import com.coco3g.caopantx.data.Constants;
import com.coco3g.caopantx.listener.ISocketHangQingListener;
import com.coco3g.caopantx.listener.ITransListListener;
import com.coco3g.caopantx.mode.ISocketRequestModel;
import com.coco3g.caopantx.net.utls.WebSocketConnection;

import org.java_websocket.drafts.Draft_17;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by lisen on 16/7/25.
 */
public class SocketRequestPresenter implements ISocketRequestModel {
    Context mContext;
    WebSocketConnection mWebSocket;

    /**
     * socket请求presenter
     *
     * @param context
     */
    public SocketRequestPresenter(Context context) {
        mContext = context;
        try {
            mWebSocket = new WebSocketConnection(mContext, new URI(Constants.SOCKET_BASE_URL + Constants.SOCKET_PORT), new Draft_17());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取交易列表中的数据
     *
     * @param listener
     */
    @Override
    public void transList(String param, final ITransListListener listener) {
        Log.e("param", param + "--");
        try {
            mWebSocket.setModeData(new TransListDataBean()).addParam(param).setOnSocketReturnListener(new WebSocketConnection.OnSocketListener() {
                @Override
                public void socketReturn(int code, Object object) {
                    switch (code) {
                        case WebSocketConnection.SOCKET_RESULT_CODE_SUCCESS:
                            listener.onSuccess((TransListDataBean) object);
                            break;
                        case WebSocketConnection.SOCKET_RESULT_CODE_FAILURE:
                            listener.onFailure(null);
                            break;
                    }
                }
            });
            mWebSocket.connectBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取产品详细信息
     *
     * @param param
     * @param listener
     */
    @Override
    public void hangqingData(String param, final ISocketHangQingListener listener) {
        try {
            mWebSocket.setModeData(new HangQingBaseDataBean()).addParam(param).setOnSocketReturnListener(new WebSocketConnection.OnSocketListener() {
                @Override
                public void socketReturn(int code, Object object) {
                    switch (code) {
                        case WebSocketConnection.SOCKET_RESULT_CODE_SUCCESS:
                            listener.onSuccess((HangQingBaseDataBean) object);
                            break;
                        case WebSocketConnection.SOCKET_RESULT_CODE_FAILURE:
                            listener.onFailure(null);
                            break;
                    }
                }
            });
            mWebSocket.connectBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取订单状态
     *
     * @param param
     * @param listener
     */
    @Override
    public void orderData(String param, final ISocketOrderListener listener) {
        try {
            mWebSocket.setModeData(new OrderDataBean()).addParam(param).setOnSocketReturnListener(new WebSocketConnection.OnSocketListener() {
                @Override
                public void socketReturn(int code, Object object) {
                    switch (code) {
                        case WebSocketConnection.SOCKET_RESULT_CODE_SUCCESS:
                            listener.onSuccess((OrderDataBean) object);
                            break;
                        case WebSocketConnection.SOCKET_RESULT_CODE_FAILURE:
                            listener.onFailure(null);
                            break;
                    }
                }
            });
            mWebSocket.connectBlocking();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭socket
     */
    public void closeSocket() {
        if (mWebSocket != null) {
            mWebSocket.close();
        }
    }

    /**
     * 判断是否关闭
     *
     * @return
     */
    public boolean isClose() {
        if (mWebSocket == null) {
            return true;
        }
        return mWebSocket.isClosed();
    }
}
