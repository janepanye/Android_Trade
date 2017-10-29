package com.coco3g.caopantx.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.coco3g.caopantx.bean.BaseData;
import com.coco3g.caopantx.bean.KViewUrlDataBean;
import com.coco3g.caopantx.data.DataUrl;
import com.coco3g.caopantx.listener.IKViewUrlListener;
import com.coco3g.caopantx.net.utls.DownLoadDataThread;
import com.coco3g.caopantx.mode.IKViewUrlModel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by lisen on 16/9/2.
 */
public class KViewUrlPresenter implements IKViewUrlModel {
    Context mContext;

    public KViewUrlPresenter(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 获取K线图url
     *
     * @param prono
     * @param id
     * @param catid
     * @param nums
     * @param device
     * @param listener
     */
    @Override
    public void getKViewUrl(String prono, String id, String catid, String nums, String device, String moni, final IKViewUrlListener listener) {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("prono", prono));
        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("catid", catid));
        params.add(new BasicNameValuePair("nums", nums));
        params.add(new BasicNameValuePair("device", device));
        params.add(new BasicNameValuePair("moni", moni));
        new DownLoadDataThread(mContext, false, "", "post", new KViewUrlDataBean())
                .setUrl(DataUrl.GET_KVIEWL_URL).addParams(params).setHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case DownLoadDataThread.NET_RESULT_CODE_SUCCESS:
                        listener.onSuccess((KViewUrlDataBean) msg.obj);
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
