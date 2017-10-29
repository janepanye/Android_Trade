package com.coco3g.caopantx.activity;

import android.os.Bundle;

import com.coco3g.caopantx.data.DataUrl;
import com.coco3g.caopantx.view.MyScrollWebView;
import com.coco3g.caopantx.view.TopBarView;
import com.coco3g.caopantx.R;

/**
 * Created by MIN on 16/5/24.
 */
public class XieYiActivity extends BaseActivity {
    MyScrollWebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xieyi);

        final TopBarView topBarView = (TopBarView) findViewById(R.id.xieyi_topbar);
        webView = (MyScrollWebView) findViewById(R.id.xieyi_webview);
//        topBarView.setTitle("模玩帮用户协议");
        webView.loadUrl(DataUrl.REGISTER_XIEYI_URL);
        webView.setOnTitleListener(new MyScrollWebView.SetTitleListener() {
            @Override
            public void setTitle(String title) {
                topBarView.setTitle(title);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
