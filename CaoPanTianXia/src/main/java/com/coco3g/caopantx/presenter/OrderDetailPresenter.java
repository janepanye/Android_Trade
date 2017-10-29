package com.coco3g.caopantx.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.coco3g.caopantx.bean.BaseData;
import com.coco3g.caopantx.bean.OrderDetailDataBean;
import com.coco3g.caopantx.bean.TradeNoDataBean;
import com.coco3g.caopantx.data.DataUrl;
import com.coco3g.caopantx.listener.IOrderDetailListener;
import com.coco3g.caopantx.mode.IOrderDetailModel;
import com.coco3g.caopantx.net.utls.DownLoadDataThread;
import com.coco3g.caopantx.listener.ITradeNoListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by lisen on 16/9/6.
 */
public class OrderDetailPresenter implements IOrderDetailModel {
    Context mContext;

    public OrderDetailPresenter(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 获取订单详情
     *
     * @param orderid
     * @param listener
     */
    @Override
    public void getOrderDetail(String orderid, final IOrderDetailListener listener) {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("orderid", orderid));
        params.add(new BasicNameValuePair("device", "Android"));
        new DownLoadDataThread(mContext, false, "", "post", new OrderDetailDataBean())
                .setUrl(DataUrl.ORDER_DETAIL).addParams(params).setHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case DownLoadDataThread.NET_RESULT_CODE_SUCCESS:
                        listener.onSuccess((OrderDetailDataBean) msg.obj);
                        break;
                    case DownLoadDataThread.NET_RESULT_CODE_FAILURE:
                        listener.onFailure((BaseData) msg.obj);
                        break;
                    case DownLoadDataThread.NET_RESULT_CODE_ERROR:
                        listener.onError();
                        break;
                }
            }
        }).start();
    }

    /**
     * 获取银生宝交易信息
     *
     * @param orderid
     * @param listener
     */
    @Override
    public void getOrderInfo(String orderid, final ITradeNoListener listener) {
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("orderid", orderid));
        params.add(new BasicNameValuePair("device", "Android"));
        new DownLoadDataThread(mContext, false, "", "post", new TradeNoDataBean())
                .setUrl(DataUrl.GET_TRADENO).addParams(params).setHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case DownLoadDataThread.NET_RESULT_CODE_SUCCESS:
                        listener.onSuccess((TradeNoDataBean) msg.obj);
                        break;
                    case DownLoadDataThread.NET_RESULT_CODE_FAILURE:
                        listener.onFailure((BaseData) msg.obj);
                        break;
                    case DownLoadDataThread.NET_RESULT_CODE_ERROR:
                        listener.onError();
                        break;
                }
            }
        }).start();
    }

//    /**
//     * 获取交易号
//     *
//     * @param orderid
//     * @param listener
//     */
//    @Override
//    public void getTradeNo(String orderid, final ITradeNoListener listener) {
//        ArrayList<NameValuePair> params = new ArrayList<>();
//        params.add(new BasicNameValuePair("orderid", orderid));
//        params.add(new BasicNameValuePair("device", "Android"));
//        new DownLoadDataThread(mContext, false, "", "post", new TradeNoDataBean())
//                .setUrl(DataUrl.GET_TRADENO).addParams(params).setHandler(new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                switch (msg.what) {
//                    case DownLoadDataThread.NET_RESULT_CODE_SUCCESS:
//                        listener.onSuccess((TradeNoDataBean) msg.obj);
//                        break;
//                    case DownLoadDataThread.NET_RESULT_CODE_FAILURE:
//                        listener.onFailure((BaseData) msg.obj);
//                        break;
//                    case DownLoadDataThread.NET_RESULT_CODE_ERROR:
//                        listener.onError();
//                        break;
//                }
//            }
//        }).start();
//    }
}
