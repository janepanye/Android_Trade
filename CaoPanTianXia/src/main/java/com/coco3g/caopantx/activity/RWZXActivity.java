package com.coco3g.caopantx.activity;

import android.os.Bundle;

/**
 * Created by MIN on 16/6/17.
 * 任务中心activity
 */
public class RWZXActivity extends BaseActivity {
//    private TopBarView mTopBar;
//    private ExpandViewPager mExViewPager;
//    private ArrayList<View> mListPager;
//    private String[] mListTitle;
//    private MRRWView mrrwView;
//    private XSRWView xsrwView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_rwzx);
//        mListTitle = new String[]{"新手任务", "每日任务"};
//        mTopBar = (TopBarView) findViewById(R.id.rwzx_topbar);
//        mExViewPager = (ExpandViewPager) findViewById(R.id.rwzx_expandviewpager);
//        //topbar右侧view
//        mTopBar.setTitle(getResources().getString(R.string.text_rwzx));
//        ImageView iv = new ImageView(RWZXActivity.this);
//        iv.setImageResource(R.mipmap.pic_transaction_search);
//        int padding = Global.dipTopx(RWZXActivity.this, 10);
//        iv.setPadding(padding, padding, padding, padding);
//        mTopBar.setRightView(iv);
//        mTopBar.setOnClickTopbar(new TopBarView.OnClickTopbarView() {
//            @Override
//            public void onClickTopbarView(String flag) {
//                if (flag.equals("right")) {
//                    ShuoMingDialog();
//                }
//            }
//        });
//
//        //添加view
//        mListPager = new ArrayList<View>();
//        xsrwView = new XSRWView(RWZXActivity.this, null);
//        mListPager.add(xsrwView);
//        mrrwView = new MRRWView(RWZXActivity.this, null);
//        mListPager.add(mrrwView);
//        mExViewPager.setTopBarColor(R.color.white)
//                .setTabTextColor(R.color.app_theme_color, R.color.color_line_bg).setTitleSize(16)
//                //.setIndicatorBackground(R.drawable.tabwidget_btn_bg)
//                .setTitles(mListTitle).setViewList(mListPager);
//        mExViewPager.initDataAndView();
    }

//    /**
//     * 规则说明dialog
//     */
//    private void ShuoMingDialog() {
//        LayoutInflater inflater = LayoutInflater.from(this);
//        View view = inflater.inflate(R.layout.view_dialog, null);
//        new AlertDialog.Builder(RWZXActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
//                .setView(view).create().show();
//    }
}
