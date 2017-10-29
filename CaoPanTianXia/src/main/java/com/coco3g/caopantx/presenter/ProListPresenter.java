package com.coco3g.caopantx.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.coco3g.caopantx.bean.BaseData;
import com.coco3g.caopantx.data.DataUrl;
import com.coco3g.caopantx.net.utls.DownLoadDataThread;
import com.coco3g.caopantx.bean.ProChildListDataBean;
import com.coco3g.caopantx.listener.IProListListener;
import com.coco3g.caopantx.mode.IProListModel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by lisen on 16/9/5.
 */
public class ProListPresenter implements IProListModel {
    Context mContext;

    public ProListPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void getProList(String catid, String device, final IProListListener listener) {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("catid", catid));
        params.add(new BasicNameValuePair("device", "Android"));
        new DownLoadDataThread(mContext, false, "", "post", new ProChildListDataBean())
                .setUrl(DataUrl.GET_PRO_LIST).addParams(params).setHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case DownLoadDataThread.NET_RESULT_CODE_SUCCESS:
                        listener.onSuccess((ProChildListDataBean) msg.obj);
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
