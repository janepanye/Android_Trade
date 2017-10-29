package com.coco3g.caopantx.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.coco3g.caopantx.view.TopBarView;
import com.coco3g.caopantx.R;
import com.coco3g.caopantx.adapter.JFSCListViewAdapter;
import com.coco3g.caopantx.data.Global;

import java.util.ArrayList;

/**
 * Created by MIN on 16/6/17.
 * 积分商城activity
 */
public class JFSCActivity extends BaseActivity {
    private TopBarView mTopBar;
    private TextView mKeYongJF, mDongJieJF;
    private ListView mListView;
    private JFSCListViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jfsc);
        initView();
        initData();
    }

    private void initView() {
        mTopBar = (TopBarView) findViewById(R.id.jfsc_topbar);
        mTopBar.setTitle(getResources().getString(R.string.text_jfsc));
        //设置topbar右侧view
        TextView tv = new TextView(JFSCActivity.this);
        tv.setText(getResources().getString(R.string.text_jfsc_mingxi));
        tv.setTextColor(getResources().getColor(R.color.white));
        int padding = Global.dipTopx(getApplicationContext(), 10);
        tv.setPadding(padding, padding, padding, padding);
        mTopBar.setRightView(tv);
        mKeYongJF = (TextView) findViewById(R.id.jfsc_tv_keyongjifen);
        mDongJieJF = (TextView) findViewById(R.id.jfsc_dongjiejifen);
        mListView = (ListView) findViewById(R.id.jfsc_listview);
    }

    private ArrayList<String> mList = new ArrayList<String>();

    private void initData() {
        for (int i = 0; i < 3; i++) {
            mList.add("测试" + i);
        }
        mAdapter = new JFSCListViewAdapter(JFSCActivity.this, mList);
        mListView.setAdapter(mAdapter);
    }
}
