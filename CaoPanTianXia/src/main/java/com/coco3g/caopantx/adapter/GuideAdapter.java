package com.coco3g.caopantx.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coco3g.caopantx.R;
import com.coco3g.caopantx.activity.MainActivity;
import com.coco3g.caopantx.bean.GuideDataBean;
import com.coco3g.caopantx.data.Global;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

/**
 * Created by lisen on 16/8/12.
 */
public class GuideAdapter extends PagerAdapter {
    Context mContext;
    ArrayList<GuideDataBean> mList = new ArrayList<>();
    DisplayImageOptions options;
    RelativeLayout.LayoutParams lp = null;

    public GuideAdapter(Context context) {
        mContext = context;
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .resetViewBeforeLoading(true).displayer(new RoundedBitmapDisplayer(20)).displayer(new FadeInBitmapDisplayer(500)).build();
        lp = new RelativeLayout.LayoutParams(Global.screenWidth, RelativeLayout.LayoutParams.MATCH_PARENT);
    }

    public void setList(ArrayList<GuideDataBean> list) {
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup view, int position, Object object) {
        view.removeView((View) object);
    }

    ViewHolder viewHolder;

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        viewHolder = new ViewHolder();
        LayoutInflater lay = LayoutInflater.from(mContext);
        View v = lay.inflate(R.layout.a_guide_item, null);
        viewHolder.mRelativeBG = (RelativeLayout) v.findViewById(R.id.relative_guide_item);
//        viewHolder.mTxtInfo = (TextView) v.findViewById(R.id.tv_guide_info);
        viewHolder.mTxtComplete = (TextView) v.findViewById(R.id.tv_guide_complete);
        viewHolder.mImagePic = (ImageView) v.findViewById(R.id.image_guide_info);
        viewHolder.mImagePic.setLayoutParams(lp);
        ImageLoader.getInstance().displayImage("drawable://" + mList.get(position).resID, viewHolder.mImagePic, options);
//        viewHolder.mTxtInfo.setText(mList.get(position).title);
//        viewHolder.mTxtInfo.setTextColor(mContext.getResources().getColor(mList.get(position).titleColorId));
//        v.setBackgroundResource(mList.get(position).backgroudId);
        view.addView(v, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (position == getCount() - 1) {
            viewHolder.mTxtComplete.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mTxtComplete.setVisibility(View.GONE);
        }
        viewHolder.mTxtComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MainActivity.class);
                mContext.startActivity(intent);
                ((Activity) mContext).finish();
            }
        });
        return v;
    }

    private class ViewHolder {
        RelativeLayout mRelativeBG;
        public ImageView mImagePic;
        public TextView mTxtComplete;
    }

}
