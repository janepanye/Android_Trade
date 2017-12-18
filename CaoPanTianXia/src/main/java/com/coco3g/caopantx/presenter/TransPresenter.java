package com.coco3g.caopantx.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.coco3g.caopantx.mode.ITransModel;
import com.coco3g.caopantx.net.utls.DownLoadDataThread;
import com.coco3g.caopantx.R;
import com.coco3g.caopantx.bean.BaseData;
import com.coco3g.caopantx.bean.TransListDataBean;
import com.coco3g.caopantx.data.DataUrl;
import com.coco3g.caopantx.listener.ITransListListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by lisen on 16/9/4.
 */
public class TransPresenter implements ITransModel {
    Context mContext;

    public TransPresenter(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 获取交易列表
     *
     * @param catid
     * @param device
     * @param listener
     */
    @Override
    public void getTransList(String catid, String device, String moni, final ITransListListener listener) {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("catid", catid));
        params.add(new BasicNameValuePair("device", "Android"));
        params.add(new BasicNameValuePair("moni", moni));
        new DownLoadDataThread(mContext, true, mContext.getResources().getString(R.string.loading), "post", new TransListDataBean())
                .setUrl(DataUrl.GET_TRANS_LIST).addParams(params).setHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case DownLoadDataThread.NET_RESULT_CODE_SUCCESS:
                        listener.onSuccess((TransListDataBean) msg.obj);
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
