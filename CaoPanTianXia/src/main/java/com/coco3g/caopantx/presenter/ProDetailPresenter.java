package com.coco3g.caopantx.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.coco3g.caopantx.bean.BaseData;
import com.coco3g.caopantx.data.DataUrl;
import com.coco3g.caopantx.mode.IProDetailMode;
import com.coco3g.caopantx.net.utls.DownLoadDataThread;
import com.coco3g.caopantx.R;
import com.coco3g.caopantx.bean.ProDetailDataBean;
import com.coco3g.caopantx.listener.IBaseDataListener;
import com.coco3g.caopantx.listener.IProDetailListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by lisen on 16/9/2.
 */
public class ProDetailPresenter implements IProDetailMode {
    Context mContext;

    public ProDetailPresenter(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 获取产品详情
     *
     * @param id
     * @param device
     * @param moni
     * @param listener
     */
    @Override
    public void getProDetail(String id, String device, String moni, final IProDetailListener listener) {
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("device", device));
        params.add(new BasicNameValuePair("moni", moni));
        new DownLoadDataThread(mContext, true, mContext.getResources().getString(R.string.loading), "post", new ProDetailDataBean())
                .setUrl(DataUrl.PRO_DETAIL_URL).addParams(params).setHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case DownLoadDataThread.NET_RESULT_CODE_SUCCESS:
                        listener.onSuccess((ProDetailDataBean) msg.obj);
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
     * 极速下单关闭接口
     *
     * @param prono
     * @param id
     * @param moni
     * @param listener
     */
    @Override
    public void closeOrderSet(String prono, String id, String moni, String device, final IBaseDataListener listener) {
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("prono", prono));
        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("moni", moni));
        params.add(new BasicNameValuePair("device", device));
        new DownLoadDataThread(mContext, false, mContext.getResources().getString(R.string.loading), "post", new BaseData())
                .setUrl(DataUrl.CLOSE_JISU_ORDER_URL).addParams(params).setHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case DownLoadDataThread.NET_RESULT_CODE_SUCCESS:
                        listener.onSuccess((BaseData) msg.obj);
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
     * 一键平仓
     *
     * @param wpno
     * @param prono
     * @param catid
     * @param moni
     * @param device
     * @param listener
     */
    @Override
    public void pingCangAll(String wpno, String prono, String catid, String moni, String device, final IBaseDataListener listener) {
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("prono", prono));
        params.add(new BasicNameValuePair("catid", catid));
        params.add(new BasicNameValuePair("moni", moni));
        params.add(new BasicNameValuePair("wpno", wpno));
        params.add(new BasicNameValuePair("device", device));
        new DownLoadDataThread(mContext, true, mContext.getResources().getString(R.string.loading), "post", new BaseData())
                .setUrl(DataUrl.PING_CANG_URL).addParams(params).setHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case DownLoadDataThread.NET_RESULT_CODE_SUCCESS:
                        listener.onSuccess((BaseData) msg.obj);
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
     * 极速下单接口
     *
     * @param setid
     * @param prono
     * @param tid
     * @param moni
     * @param ordertype
     * @param shoushu
     * @param type
     * @param device
     * @param listener
     */
    @Override
    public void quickOrder(String setid, String prono, String tid, String moni, String ordertype, String shoushu, String type, String device, final IBaseDataListener listener) {
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("setid", setid));
        params.add(new BasicNameValuePair("prono", prono));
        params.add(new BasicNameValuePair("tid", tid));
        params.add(new BasicNameValuePair("moni", moni));
        params.add(new BasicNameValuePair("ordertype", ordertype));
        params.add(new BasicNameValuePair("shoushu", shoushu));
        params.add(new BasicNameValuePair("type", type));
        params.add(new BasicNameValuePair("device", device));
        new DownLoadDataThread(mContext, true, mContext.getResources().getString(R.string.loading), "post", new BaseData())
                .setUrl(DataUrl.QUICK_ORDER_URL).addParams(params).setHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case DownLoadDataThread.NET_RESULT_CODE_SUCCESS:
                        listener.onSuccess((BaseData) msg.obj);
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
}
