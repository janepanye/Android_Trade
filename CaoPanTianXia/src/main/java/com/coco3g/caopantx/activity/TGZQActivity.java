package com.coco3g.caopantx.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.coco3g.caopantx.data.Global;
import com.coco3g.caopantx.view.TopBarView;
import com.coco3g.caopantx.R;

/**
 * Created by MIN on 16/6/17.
 * 推广赚钱activity
 */
public class TGZQActivity extends BaseActivity {
    private TopBarView mTopBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tgzq);
        initView();
    }

    private void initView() {
        mTopBar = (TopBarView) findViewById(R.id.tgzq_topbar);
        mTopBar.setTitle(getResources().getString(R.string.text_tgzq));
        //设置topbar右侧view
        TextView tv = new TextView(TGZQActivity.this);
        tv.setText(getResources().getString(R.string.text_tgzq_rule));
        tv.setTextColor(getResources().getColor(R.color.white));
        int padding = Global.dipTopx(getApplicationContext(), 10);
        tv.setPadding(padding, padding, padding, padding);
        mTopBar.setRightView(tv);
    }
}
