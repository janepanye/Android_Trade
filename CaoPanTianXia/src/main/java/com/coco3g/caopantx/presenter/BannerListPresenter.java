package com.coco3g.caopantx.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.coco3g.caopantx.bean.BannerListData;
import com.coco3g.caopantx.bean.BaseData;
import com.coco3g.caopantx.data.DataUrl;
import com.coco3g.caopantx.net.utls.DownLoadDataThread;
import com.coco3g.caopantx.listener.IBannerListener;
import com.coco3g.caopantx.mode.IBannerModel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by lisen on 16/9/5.
 */
public class BannerListPresenter implements IBannerModel {
    Context mContext;

    public BannerListPresenter(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 获取banner列表
     *
     * @param catid
     * @param device
     * @param listener
     */
    @Override
    public void getBannerList(String catid, String device, final IBannerListener listener) {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("catid", catid));
        params.add(new BasicNameValuePair("device", "Android"));
        new DownLoadDataThread(mContext, false, "", "post", new BannerListData())
                .setUrl(DataUrl.GET_BANNER_LIST).addParams(params).setHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case DownLoadDataThread.NET_RESULT_CODE_SUCCESS:
                        listener.onSuccess((BannerListData) msg.obj);
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
