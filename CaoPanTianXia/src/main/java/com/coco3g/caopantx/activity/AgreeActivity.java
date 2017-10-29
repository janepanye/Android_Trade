package com.coco3g.caopantx.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.coco3g.caopantx.bean.BaseData;
import com.coco3g.caopantx.data.DataUrl;
import com.coco3g.caopantx.view.MyScrollWebView;
import com.coco3g.caopantx.view.TopBarView;
import com.coco3g.caopantx.R;
import com.coco3g.caopantx.data.Global;
import com.coco3g.caopantx.listener.IBaseDataListener;
import com.coco3g.caopantx.presenter.PublicPresenter;

/**
 * Created by lisen on 2016/10/17.
 */

public class AgreeActivity extends BaseActivity implements View.OnClickListener {
    TopBarView mTopBar;
    TextView mTxtName;
    String mUrl = "";
    MyScrollWebView webView;
    CheckBox mCheck;
    TextView mTxtAgree;
    String proid = "";
    int mCurrIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree);
        mUrl = getIntent().getStringExtra("url");
        proid = getIntent().getStringExtra("proid");
        mCurrIndex = 0;
        initView();
    }

    private void initView() {
        mTopBar = (TopBarView) findViewById(R.id.topbar_agree);
        mTopBar.setTitle("签署协议");
        webView = (MyScrollWebView) findViewById(R.id.webview_agree);
        mTxtName = (TextView) findViewById(R.id.tv_agree_name);
        mCheck = (CheckBox) findViewById(R.id.cb_agree);
        mTxtAgree = (TextView) findViewById(R.id.tv_agree);
        webView.loadUrl(mUrl);
        webView.setOnTitleListener(new MyScrollWebView.SetTitleListener() {
            @Override
            public void setTitle(String title) {
                mTopBar.setTitle(title);
            }
        });
        mTxtAgree.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_agree:
                if (mCheck.isChecked()) {
                    if (mCurrIndex == 0) {
                        agreeGaoZhiShu("2");
                    } else if (mCurrIndex == 1) {
                        agreeFuWuXieYi("1");
                    }
                } else {
                    Global.showToast("是否同意该协议？", AgreeActivity.this);
                }
                break;
        }
    }

    // 风险告知书确认
    public void agreeGaoZhiShu(String typeid) {
        new PublicPresenter(this).agree(proid, typeid, new IBaseDataListener() {
            @Override
            public void onSuccess(BaseData data) {
                /**
                 * 用户同意了风险告知书后，需要继续签署用户服务协议，所以所有状态清空
                 */
                mCurrIndex = 1;
                mCheck.setChecked(false);
                mTxtName.setText("《用户服务协议》");
                webView.loadUrl(DataUrl.FUWUXIEYI_URL);
            }

            @Override
            public void onFailure(BaseData data) {
            }

            @Override
            public void onError() {
            }
        });

    }

    // 用户服务协议确认
    public void agreeFuWuXieYi(String typeid) {
        new PublicPresenter(this).agree(proid, typeid, new IBaseDataListener() {
            @Override
            public void onSuccess(BaseData data) {
                mCurrIndex = 2;
                finish();
            }

            @Override
            public void onFailure(BaseData data) {
            }

            @Override
            public void onError() {
            }
        });

    }
}
