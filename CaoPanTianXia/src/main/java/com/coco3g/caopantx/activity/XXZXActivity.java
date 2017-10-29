package com.coco3g.caopantx.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.coco3g.caopantx.R;
import com.coco3g.caopantx.adapter.XXZXListViewAdapter;

import java.util.ArrayList;

/**
 * Created by MIN on 16/6/17.
 * 信息中心activity
 */
public class XXZXActivity extends BaseActivity implements View.OnClickListener {
    private TextView mIsRead, mNotRead;
    private ImageView mBack;
    private ListView mReadList;
    private TextView[] mXXZXNav;
    private int mCurrentSel = -1;
    private XXZXListViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xxzx);
        initView();
        initData();
    }

    private void initView() {
        mIsRead = (TextView) findViewById(R.id.xxzx_tv_read);
        mNotRead = (TextView) findViewById(R.id.xxzx_tv_notread);
        mBack = (ImageView) findViewById(R.id.xxzx_iv_back);
        mXXZXNav = new TextView[]{mNotRead, mIsRead};
        mReadList = (ListView) findViewById(R.id.xxzx_listview_read);

        mIsRead.setOnClickListener(this);
        mNotRead.setOnClickListener(this);
        mBack.setOnClickListener(this);
    }

    //测试数据
    private ArrayList<String> isReadList = new ArrayList<String>();
    private ArrayList<String> notReadList = new ArrayList<String>();

    private void initData() {
        for (int i = 0; i < 5; i++) {
            notReadList.add("未读" + i);
            isReadList.add("已读" + i);
        }
        mAdapter = new XXZXListViewAdapter(XXZXActivity.this);


        currentList(0);
    }

    //---------------------一道华丽的分割线---------------------------
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.xxzx_iv_back:
                finish();
                break;
            case R.id.xxzx_tv_notread:
                currentList(0);
                break;
            case R.id.xxzx_tv_read:
                currentList(1);
                break;
        }
    }

    /**
     * 当前显示的是已读还是未读
     */
    private void currentList(int index) {
        if (index == -1) {
            return;
        } else {
            mCurrentSel = index;
            for (int i = 0; i < mXXZXNav.length; i++) {
                if (i == index) {
                    mXXZXNav[i].setSelected(true);
                    mXXZXNav[i].setTextColor(getResources().getColor(R.color.app_theme_color));
                    if (i == 0) {
                        mAdapter.setList(notReadList);
                        mReadList.setAdapter(mAdapter);
                    } else if (i == 1) {
                        mAdapter.setList(isReadList);
                        mReadList.setAdapter(mAdapter);
                    }
                } else {
                    mXXZXNav[i].setSelected(false);
                    mXXZXNav[i].setTextColor(getResources().getColor(R.color.white));
                }
            }
        }
    }
}
