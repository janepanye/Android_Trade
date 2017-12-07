package com.coco3g.caopantx.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.internal.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.coco3g.caopantx.R;

/**
 * Created by Qinf on 2017/12/4.
 */

public class MyTemplate extends LinearLayout implements View.OnClickListener {

    private LayoutInflater mInflater;

    private TextView tvHeadTitel, tvMore,
            tvOneHead, tvOneCenter, tvOneFoot,
            tvTwoHead, tvTwoCenter, tvTwoFoot,
            tvThreeHead, tvThreeCenter, tvThreeFoot;

    private TextOnClickListener ll;

    public MyTemplate(Context context) {
        this(context, null);
    }

    public MyTemplate(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTemplate(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mInflater = LayoutInflater.from(context);

        initView();


        if (attrs != null) {
            TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.MyTemplate, defStyleAttr, 0);

            String headTitel = a.getString(R.styleable.MyTemplate_headTitle);
            setHeadTitel(headTitel);

            String headMore = a.getString(R.styleable.MyTemplate_headMore);
            setHeadMore(headMore);

            String oneHead = a.getString(R.styleable.MyTemplate_oneHead);
            setOneHeadText(oneHead);

            String twoHead = a.getString(R.styleable.MyTemplate_twoHead);
            setTwoHeadText(twoHead);

            String threeHead = a.getString(R.styleable.MyTemplate_threeHead);
            setThreeHeadText(threeHead);

            String oneCenter = a.getString(R.styleable.MyTemplate_oneCenter);
            setOneCenterText(oneCenter);

            String twoCenter = a.getString(R.styleable.MyTemplate_twoCenter);
            setTwoCenterText(twoCenter);

            String threeCenter = a.getString(R.styleable.MyTemplate_threeCenter);
            setThreeCenterText(threeCenter);

            String oneFoot = a.getString(R.styleable.MyTemplate_oneFoot);
            setOneFootText(oneFoot);

            String twoFoot = a.getString(R.styleable.MyTemplate_twoFoot);
            setTwoFootText(twoFoot);

            String threeFoot = a.getString(R.styleable.MyTemplate_threeFoot);
            setThreeFootText(threeFoot);


            Drawable headIcon = a.getDrawable(R.styleable.MyTemplate_headIcon);
            if (headIcon != null) {
                setHeadLeftIcon(headIcon);
            }

            a.recycle();
        }
    }

    private void initView() {
        View itemView = mInflater.inflate(R.layout.template, this, true);
        tvHeadTitel = (TextView) itemView.findViewById(R.id.head_title);
        tvMore = (TextView) itemView.findViewById(R.id.head_more);

        LinearLayout llOne = (LinearLayout) itemView.findViewById(R.id.one);
        LinearLayout llTwo = (LinearLayout) itemView.findViewById(R.id.two);
        LinearLayout llThree = (LinearLayout) itemView.findViewById(R.id.three);

        tvOneHead = (TextView) llOne.findViewById(R.id.item_head);
        tvOneCenter = (TextView) llOne.findViewById(R.id.item_center);
        tvOneFoot = (TextView) llOne.findViewById(R.id.item_foot);

        tvTwoHead = (TextView) llTwo.findViewById(R.id.item_head);
        tvTwoCenter = (TextView) llTwo.findViewById(R.id.item_center);
        tvTwoFoot = (TextView) llTwo.findViewById(R.id.item_foot);

        tvThreeHead = (TextView) llThree.findViewById(R.id.item_head);
        tvThreeCenter = (TextView) llThree.findViewById(R.id.item_center);
        tvThreeFoot = (TextView) llThree.findViewById(R.id.item_foot);

        tvMore.setOnClickListener(this);

    }


    public void setHeadMore(int resId) {
        setHeadMore(getResources().getString(resId));
    }

    public void setHeadMore(String str) {
        tvMore.setText(str);
    }

    public void setHeadTitel(int resId) {
        setHeadTitel(getResources().getString(resId));
    }

    public void setHeadTitel(String str) {
        tvHeadTitel.setText(str);
    }


    public void setOneHeadText(int resId) {
        setOneHeadText(getResources().getString(resId));
    }

    public void setOneHeadText(String str) {
        tvOneHead.setText(str);
    }

    public void setTwoHeadText(int resId) {
        setTwoHeadText(getResources().getString(resId));
    }

    public void setTwoHeadText(String str) {
        tvTwoHead.setText(str);
    }

    public void setThreeHeadText(int resId) {
        setThreeHeadText(getResources().getString(resId));
    }

    public void setThreeHeadText(String str) {
        tvThreeHead.setText(str);
    }

    public void setOneCenterText(int resId) {
        setOneCenterText(getResources().getString(resId));
    }

    public void setOneCenterText(String str) {
        tvOneCenter.setText(str);

    }



    public void setOneCenterColor(int resId) {
        tvOneCenter.setTextColor(getResources().getColor(resId));
    }

    public void setTwoCenterText(int resId) {
        setTwoCenterText(getResources().getString(resId));
    }

    public void setTwoCenterText(String str) {
        tvTwoCenter.setText(str);
    }

    public void setTwoCenterColor(int resId) {
        tvTwoCenter.setTextColor(getResources().getColor(resId));
    }

    public void setThreeCenterText(int resId) {
        setThreeCenterText(getResources().getString(resId));
    }

    public void setThreeCenterText(String str) {
        tvThreeCenter.setText(str);
    }

    public void setThreeCenterColor(int resId) {
        tvThreeCenter.setTextColor(getResources().getColor(resId));
    }


    public void setOneFootText(int resId) {
        setOneFootText(getResources().getString(resId));
    }

    public void setOneFootText(String str) {
        tvOneFoot.setText(str+"%");
//        tvOneFoot.setTextColor();

    }


    public void setOneFootColor(int resId) {
        tvOneFoot.setTextColor(getResources().getColor(resId));
    }


    public void setTwoFootText(int resId) {
        setTwoFootText(getResources().getString(resId));
    }

    public void setTwoFootText(String str) {
        tvTwoFoot.setText(str+"%");
    }

    public void setTwoFootColor(int resId) {
        tvTwoFoot.setTextColor(getResources().getColor(resId));
    }

    public void setThreeFootText(int resId) {
        setThreeFootText(getResources().getString(resId));
    }

    public void setThreeFootText(String str) {
        tvThreeFoot.setText(str+"%");
    }

    public void setThreeFootColor(int resId) {
        tvThreeFoot.setTextColor(getResources().getColor(resId));
    }


    public void setOnMoreListener(TextOnClickListener ll) {
        this.ll = ll;
    }

    public void setHeadLeftIcon(Drawable left) {
        left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
        tvHeadTitel.setCompoundDrawables(left, null, null, null);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.head_more) {
            if (ll != null) {
                ll.onMoreClick(v);
            }
        }
    }

    interface TextOnClickListener {

        void onMoreClick(View view);

    }
}
