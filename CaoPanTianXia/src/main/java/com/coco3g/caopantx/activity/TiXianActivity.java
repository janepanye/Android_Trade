package com.coco3g.caopantx.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.coco3g.caopantx.view.TopBarView;
import com.coco3g.caopantx.R;
import com.coco3g.caopantx.data.Global;

/**
 * Created by MIN on 16/6/16.
 */
public class TiXianActivity extends BaseActivity {
    private TopBarView mTopBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tixian);
        initView();
    }

    private void initView() {
        mTopBar = (TopBarView) findViewById(R.id.tixian_topbar);
        mTopBar.setTitle(getResources().getString(R.string.text_tixian));
        TextView tv = new TextView(this);
        tv.setText(getResources().getString(R.string.text_tixian_jilu));
        tv.setTextColor(getResources().getColor(R.color.white));
        int padding = Global.dipTopx(getApplicationContext(), 10);
        tv.setPadding(padding, padding, padding, padding);
        mTopBar.setRightView(tv);
    }

}
