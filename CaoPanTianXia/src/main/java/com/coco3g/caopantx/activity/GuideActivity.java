package com.coco3g.caopantx.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.coco3g.caopantx.adapter.GuideAdapter;
import com.coco3g.caopantx.R;
import com.coco3g.caopantx.bean.GuideDataBean;
import com.coco3g.caopantx.view.CircleIndicator;

import java.util.ArrayList;

/**
 * Created by MIN on 16/6/17.
 */
public class GuideActivity extends Activity {
    ViewPager mViewpager;
    CircleIndicator indicator;
    GuideAdapter mAdapter;
    int[] resIDs;
//    String[] mTitles;
//    int[] colorIDs;
//    int[] backgroudIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        resIDs = new int[]{R.mipmap.pic_start_1, R.mipmap.pic_start_2, R.mipmap.pic_start_3, R.mipmap.pic_start_4, R.mipmap.pic_start_5};
//        mTitles = new String[]{getString(R.string.guide_title_1), getString(R.string.guide_title_2), getString(R.string.guide_title_3)};
//        colorIDs = new int[]{R.color.guide_title_color_1, R.color.guide_title_color_2, R.color.guide_title_color_3};
//        backgroudIDs = new int[]{R.color.guide_bg_1, R.color.guide_bg_2, R.color.guide_bg_3};
        mAdapter = new GuideAdapter(this);
        initView();
    }

    private void initView() {
        mViewpager = (ViewPager) findViewById(R.id.viewpager_guide);
        indicator = (CircleIndicator) findViewById(R.id.indicator_guide);
        //
        ArrayList<GuideDataBean> list = new ArrayList<>();
        for (int i = 0; i < resIDs.length; i++) {
            GuideDataBean data = new GuideDataBean();
            data.resID = resIDs[i];
//            data.title = mTitles[i];
//            data.titleColorId = colorIDs[i];
//            data.backgroudId = backgroudIDs[i];
            list.add(data);
        }
        mAdapter.setList(list);
        mViewpager.setAdapter(mAdapter);
        indicator.setViewPager(mViewpager);
        mViewpager.setCurrentItem(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
