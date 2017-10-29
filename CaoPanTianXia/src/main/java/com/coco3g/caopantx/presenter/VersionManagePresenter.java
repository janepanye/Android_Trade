package com.coco3g.caopantx.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.coco3g.caopantx.bean.BaseData;
import com.coco3g.caopantx.bean.VersionUpdateDataBean;
import com.coco3g.caopantx.data.DataUrl;
import com.coco3g.caopantx.listener.IVersionUpdateListener;
import com.coco3g.caopantx.net.utls.DownLoadDataThread;
import com.coco3g.caopantx.mode.IVersionManageModel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by lisen on 16/4/13.
 */
public class VersionManagePresenter implements IVersionManageModel {
    Context mContext;

    public VersionManagePresenter(Context context) {
        mContext = context;
    }

    /**
     * 检查新版本
     *
     * @param listener
     */
    @Override
    public void checkNewVersion(boolean showLoading, String loadingMsg, final IVersionUpdateListener listener) {
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("device", "Android"));
        new DownLoadDataThread(mContext, showLoading, loadingMsg, "post", new VersionUpdateDataBean()).setUrl
                (DataUrl.GET_NEW_VERSION).addParams(params).setHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                switch (msg.what) {
                    case DownLoadDataThread.NET_RESULT_CODE_SUCCESS:
                        listener.onSuccess((VersionUpdateDataBean) msg.obj);
                        break;
                    case DownLoadDataThread.NET_RESULT_CODE_FAILURE:
                        BaseData data = (BaseData) msg.obj;
                        listener.onFailure(data);
                        break;
                    case DownLoadDataThread.NET_RESULT_CODE_ERROR:
                        listener.onError();
                        break;
                }
            }

        }).start();
    }
}
