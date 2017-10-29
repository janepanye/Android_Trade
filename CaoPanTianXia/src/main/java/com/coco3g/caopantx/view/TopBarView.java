package com.coco3g.caopantx.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coco3g.caopantx.R;
import com.coco3g.caopantx.activity.K_DetailActivity;
import com.coco3g.caopantx.activity.LoginActivity;
import com.coco3g.caopantx.activity.WebActivity;
import com.coco3g.caopantx.data.Global;

public class TopBarView extends RelativeLayout implements OnClickListener {
    View mView = null;
    Context mContext = null;
    ImageView mImageLeft = null;
    RelativeLayout mRelativeLeft, mRelativeRight = null, mRelativeTwoRight = null,mRelativeRight1;
    LinearLayout mLinearMiddle;
    TextView mTxtTitle = null, mTxtMiddleLeft = null, mTxtMiddleRight = null,mTxtTopRight;
    EditText mEditSearch = null;
    //
    OnClickLeftView onclickleftview;
    OnClickRightView onclickrightview = null;
    OnClickTopRight onclicktopright = null;
    OnClickTwoRightView onclicktworightview = null;
    OnDoubleClickTitle ondoubleclicktitle = null;
    OnClickMiddleMenu onclickmiddlemenu = null;
    boolean mOverrideClick = false;
    //
    private GestureDetectorCompat mDetector;

    public TopBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        mContext = context;
        mDetector = new GestureDetectorCompat(mContext, new MyGestureListener());
        initView();
    }

    void initView() {
        LayoutInflater lay = LayoutInflater.from(mContext);
        mView = lay.inflate(R.layout.view_topbar, this);
        mImageLeft = (ImageView) mView.findViewById(R.id.image_topbar_left);
        mRelativeLeft = (RelativeLayout) mView.findViewById(R.id.relative_topbar_left);
        mRelativeRight = (RelativeLayout) mView.findViewById(R.id.relative_topbar_right);
        mRelativeRight1 = (RelativeLayout) mView.findViewById(R.id.relative_topbar_right1);
        mTxtTopRight =(TextView) mView.findViewById(R.id.tv_top_right);
        mRelativeTwoRight = (RelativeLayout) mView.findViewById(R.id.relative_topbar_tworight);
        mLinearMiddle = (LinearLayout) mView.findViewById(R.id.linear_topbar_middle);
        mTxtTitle = (TextView) mView.findViewById(R.id.tv_topbar_title);
        mTxtMiddleLeft = (TextView) mView.findViewById(R.id.tv_topbar_middle_left);
        mTxtMiddleRight = (TextView) mView.findViewById(R.id.tv_topbar_middle_right);
        mRelativeLeft.setOnClickListener(this);
        mRelativeRight.setOnClickListener(this);
        mRelativeTwoRight.setOnClickListener(this);
        mTxtMiddleLeft.setOnClickListener(this);
        mTxtMiddleRight.setOnClickListener(this);
        mTxtTopRight.setOnClickListener(this);
        //
        mTxtMiddleLeft.setSelected(true);
        mTxtMiddleRight.setSelected(false);
        mTxtMiddleLeft.setTextColor(getResources().getColor(R.color.app_theme_color));
        mTxtMiddleRight.setTextColor(getResources().getColor(R.color.white));
    }

    /* 设置顶部栏左侧view */
    public void setLeftView(View view) {
        mRelativeLeft.removeAllViews();
        mRelativeLeft.setVisibility(View.VISIBLE);
        mImageLeft.setVisibility(View.GONE);
        mRelativeLeft.addView(view);
        mRelativeLeft.setFocusableInTouchMode(false);
        mRelativeLeft.setClickable(false);
    }

    /* 隐藏左侧view */
    public void hideLeftView() {
        mRelativeLeft.setVisibility(View.INVISIBLE);
    }

    public void showLeftView() {
        mRelativeLeft.setVisibility(View.VISIBLE);
    }

    /* 设置标题 */
    public void setTitle(String title) {
        mLinearMiddle.setVisibility(View.GONE);
        mRelativeRight1.setVisibility(View.GONE);
        mTxtTitle.setVisibility(View.VISIBLE);
        mTxtTitle.setText(title);
    }

    /**
     * 显示中部菜单
     */
    public void showMiddleMenu() {
        mLinearMiddle.setVisibility(View.VISIBLE);
        mTxtTitle.setVisibility(View.GONE);
        mRelativeRight1.setVisibility(View.VISIBLE);
        mTxtTitle.setText("");
    }

    /* 设置标题可以双击 */
    public void setTitleDobleClickEnable() {
        mTxtTitle.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    /* 设置顶部栏右侧view */
    public void setRightView(View view) {
        mRelativeRight.removeAllViews();
        mRelativeRight.setVisibility(View.VISIBLE);
        mRelativeRight.addView(view);
    }

    public void setTwoRightView(View view) {
        mRelativeTwoRight.removeAllViews();
        mRelativeTwoRight.setVisibility(View.VISIBLE);
        mRelativeTwoRight.addView(view);
    }

    /* 隐藏右侧view */
    public void hideRightView() {
        mRelativeRight.setVisibility(View.INVISIBLE);
    }

    public void hideTwoRightView() {
        mRelativeTwoRight.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.relative_topbar_left:
                if (mOverrideClick) {
                    onClickLeftView();
                } else {
                    ((Activity) mContext).finish();
                }
                break;
            case R.id.relative_topbar_right:
                onClickRightView();
                break;
            case R.id.relative_topbar_tworight:
                onClickTwoRightView();
                break;
            case R.id.tv_top_right:
                onClickTopRight();
                break;
            case R.id.tv_topbar_middle_left:
                mTxtMiddleLeft.setSelected(true);
                mTxtMiddleRight.setSelected(false);
                mTxtMiddleLeft.setTextColor(getResources().getColor(R.color.app_theme_color));
                mTxtMiddleRight.setTextColor(getResources().getColor(R.color.white));
                onClickMiddleMenu(R.id.tv_topbar_middle_left);
                break;
            case R.id.tv_topbar_middle_right:
                mTxtMiddleLeft.setSelected(false);
                mTxtMiddleRight.setSelected(true);
                mTxtMiddleLeft.setTextColor(getResources().getColor(R.color.white));
                mTxtMiddleRight.setTextColor(getResources().getColor(R.color.app_theme_color));
                onClickMiddleMenu(R.id.tv_topbar_middle_right);
                break;

        }
    }

    public void setOnClickLeftListener(OnClickLeftView onclickleftview) {
        mOverrideClick = true;
        this.onclickleftview = onclickleftview;
    }

    public interface OnClickLeftView {
        void onClickLeftView();
    }

    private void onClickLeftView() {
        if (onclickleftview != null) {
            onclickleftview.onClickLeftView();
        }
    }
//
 //
    //新加的
    public void setOnClickTopRight(OnClickTopRight onclicktopright) {
        this.onclicktopright = onclicktopright;
    }

    private void onClickTopRight() {
        if (onclicktopright != null) {
            onclicktopright.onClickTopRight();
        }
    }

    public interface OnClickTopRight {
        void onClickTopRight();
    }

    //
    public void setOnClickRightListener(OnClickRightView onclicktopbarview) {
        this.onclickrightview = onclicktopbarview;
    }

    private void onClickRightView() {
        if (onclickrightview != null) {
            onclickrightview.onClickTopbarView();
        }
    }

    public interface OnClickRightView {
        void onClickTopbarView();
    }

    public void setOnDoubleClickTitleListener(OnDoubleClickTitle ondoubleclicktitle) {
        this.ondoubleclicktitle = ondoubleclicktitle;
    }

    public interface OnDoubleClickTitle {
        void onDoubleTitle();
    }

    private void doubleClickTitle() {
        if (ondoubleclicktitle != null) {
            ondoubleclicktitle.onDoubleTitle();
        }
    }

    //
    public void setOnClickTwoRightListener(OnClickTwoRightView onclicktwotopbarview) {
        this.onclicktworightview = onclicktwotopbarview;
    }

    private void onClickTwoRightView() {
        if (onclicktworightview != null) {
            onclicktworightview.onClickTwoTopbarView();
        }
    }

    public interface OnClickTwoRightView {
        void onClickTwoTopbarView();
    }

    /**
     * @param onclickmiddlemenu
     * 设置点击交易导航页面,toolbar中间的国际、国内期货的监听
     */

    public void setOnClickMiddleMenuListener(OnClickMiddleMenu onclickmiddlemenu) {
        this.onclickmiddlemenu = onclickmiddlemenu;
    }

    public interface OnClickMiddleMenu {
        void onClickMiddleMenu(int resID);
    }

    private void onClickMiddleMenu(int resID) {
        if (onclickmiddlemenu != null) {
            onclickmiddlemenu.onClickMiddleMenu(resID);
        }
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            doubleClickTitle();
            return true;
        }
    }
}
