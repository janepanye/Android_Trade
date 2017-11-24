package com.coco3g.caopantx.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coco3g.caopantx.R;

    /** 自定义Tabbar栏上图下字  */

public class BottomNavImageView extends LinearLayout {
    Context mContext;
    View view;
    ImageView mImageIcon;
    TextView mTxtTitle;

    public BottomNavImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater lay = LayoutInflater.from(mContext);
        view = lay.inflate(R.layout.a_bottom_nav, this);
        mImageIcon = (ImageView) view.findViewById(R.id.image_bottom_nav_item_icon);
        mTxtTitle = (TextView) view.findViewById(R.id.tv_bottom_nav_item_title);
    }

    public void setIcon(int resID, String title) {
        mImageIcon.setImageResource(resID);
        mTxtTitle.setText(title);
    }

    public void setSelected(int index, boolean selected) {

        super.setSelected(selected);
    }

}
