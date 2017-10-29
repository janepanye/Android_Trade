package com.coco3g.caopantx.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coco3g.caopantx.view.MyScrollWebView;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by lisen on 16/8/20.
 */
public class BaseFragment extends Fragment {
    public MyScrollWebView mWebView;
    SetTitleListener settitlelistener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mWebView.setOnTitleListener(new MyScrollWebView.SetTitleListener() {
//            @Override
//            public void setTitle(String title) {
//                setTitleFromHtml(title);
//            }
//        });
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    //接口监听
    public void setOnTitleListener(SetTitleListener settitlelistener) {
        this.settitlelistener = settitlelistener;
    }

    public interface SetTitleListener {
        void setTitle(String title);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
    }

    /**
     * 读取html中的title信息
     *
     * @param title
     */
    public void setTitleFromHtml(String title) {
        if (settitlelistener != null) {
            settitlelistener.setTitle(title);
        }
    }

    public void switchTrans(int index) {

    }
}
