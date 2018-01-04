package com.coco3g.caopantx.fragment;

import com.coco3g.caopantx.R;
import com.coco3g.caopantx.activity.BaseFragment;
import com.coco3g.caopantx.data.Global;
import com.coco3g.caopantx.utils.Coco3gBroadcastUtils;
import com.coco3g.caopantx.view.PagerSlidingTabStrip;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hhh on 2017/10/18.
 */

public class RadioFragment extends BaseFragment{


    private View rootView;
    private PagerSlidingTabStrip tabPager;
    private ViewPager viewPager;
    private String[] title = new String[]{"财经资讯","今日头条","业绩排名"};
    private List<View> views = new ArrayList();
//    private FragmentManager manager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_livestream, null);

        findView();
        initView();
        setListener();

        return rootView;
    }

    private void initView() {
        //模拟的视图
        for (int i = 0;i<title.length;i++) {
            TextView tv = new TextView(getActivity());
            tv.setTextSize(30f);
            tv.setText(title[i]);
            views.add(tv);
        }

//        manager = getChildFragmentManager();
        viewPager.setAdapter(mPagerAdapter);
        tabPager.setViewPager(viewPager);
    }

    private void findView() {
        tabPager = findView(R.id.tabPager);
        viewPager = findView(R.id.viewPager);
    }

    private void setListener() {

    }

    private PagerAdapter mPagerAdapter = new PagerAdapter() {

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position) != null ? views.get(position) : null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    };

    public <T> T findView(int resId) {
        return (T) rootView.findViewById(resId);
    }
}
