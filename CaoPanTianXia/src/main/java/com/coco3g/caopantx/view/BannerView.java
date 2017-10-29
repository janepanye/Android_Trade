package com.coco3g.caopantx.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.coco3g.caopantx.activity.WebActivity;
import com.coco3g.caopantx.bean.BannerListData;
import com.coco3g.caopantx.data.Global;
import com.coco3g.caopantx.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BannerView extends RelativeLayout implements OnPageChangeListener {
    Context mContext;
    View mView = null;
    ArrayList<ImageView> mPageViewList = new ArrayList<ImageView>();
    ViewPager mViewpagerBanner;
    MyViewPagerAdapter mPagerAdapter;
    BannerSelectPointView mPoints = null;
    public DisplayImageOptions options;
    //
    Timer mTimer = null;
    TimerTask mTask = null;
    int mTimerDuration = 3 * 1000;
    boolean mIsBannerScroll = true; // 控制banner是否停止滚动
    int mCurrPagerItemPosition = 0;
    int mScreenRatio = 4; // 占据的屏幕比例
    private final static int MSG_PAGER_SCROLL_CONTROLL = 0; // viewpager
    float dx, dy;
    float startX = 0, startY = 0;
    float endX, endY;
    private final static int BannerRequestCode = 0;
    ArrayList<BannerListData.Banner> mCurrBannerList = new ArrayList<>();
    PageChangeListener pagechangelistener;

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        mContext = context;
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.pic_default_icon).showImageForEmptyUri(R.mipmap.pic_default_icon)
                .showImageOnFail(R.mipmap.pic_default_icon).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT).resetViewBeforeLoading(false).displayer(new FadeInBitmapDisplayer(500)).build();
        initView();
    }

//    public BannerView setScreenRatio(int ratio) {
//        mScreenRatio = ratio;
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//        ;
////		if (mScreenRatio == 3) {
////			lp = new LayoutParams(Global.screenWidth, Global.screenWidth / mScreenRatio);
////		} else {
////			lp = new LayoutParams(Global.screenWidth, Global.screenWidth / 2);
////		}
//        addView(mView, lp);
//        return this;
//    }

    private void initView() {
        // TODO Auto-generated method stub
        LayoutInflater lay = LayoutInflater.from(mContext);
        mView = lay.inflate(R.layout.view_banner, this);
        mViewpagerBanner = (ViewPager) mView.findViewById(R.id.viewpager_banner);
        mPoints = (BannerSelectPointView) mView.findViewById(R.id.viewpage_banner_point);
        mViewpagerBanner.setOnPageChangeListener(this);
        mViewpagerBanner.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = Math.abs(event.getX());
                        startY = Math.abs(event.getY());
                        mIsBannerScroll = false;
                        cancelTimer();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        endX = Math.abs(event.getX());
                        endY = Math.abs(event.getY());
                        dx = Math.abs(startX - endX);
                        dy = Math.abs(startY - endY);
                        if (dx <= 8) { // 点击事件
                            if (mCurrBannerList != null && mCurrBannerList.size() > 0) {
                                if (mCurrBannerList != null && mCurrBannerList.size() > 0) {
                                    if (mCurrBannerList.get(mCurrPagerItemPosition).url.startsWith("http:") ||
                                            mCurrBannerList.get(mCurrPagerItemPosition).url.startsWith("https:")) {
                                        Intent intent = new Intent(mContext, WebActivity.class);
                                        intent.putExtra("url", mCurrBannerList.get(mCurrPagerItemPosition).url);
                                        mContext.startActivity(intent);
                                    }
                                }
                            }
                        }
                        mIsBannerScroll = true;
                        initTimer();
                        break;
                }
                return false;
            }
        });

    }

    /**
     * 禁止滚动
     */
    public void stopScroll() {
        mViewpagerBanner.setOnTouchListener(null);
        mIsBannerScroll = false;
    }

//	public void loadData() {
//		// TODO Auto-generated method stub
//		getBannerList();
//	}

    public void setList(ArrayList<BannerListData.Banner> list) {
        mCurrBannerList = list;
        ArrayList<String> piclist = new ArrayList<>();
        if (mCurrBannerList != null && mCurrBannerList.size() > 0) {
            for (int i = 0; i < mCurrBannerList.size(); i++) {
                piclist.add(mCurrBannerList.get(i).thumb);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            final ImageView image = new ImageView(mContext);
            LayoutParams lp = new LayoutParams(Global.screenWidth, LayoutParams.WRAP_CONTENT);
            image.setLayoutParams(lp);
            image.setScaleType(ScaleType.FIT_CENTER);
            image.setImageResource(R.mipmap.pic_default_icon);
//            ImageLoader.getInstance().displayImage(list.get(i), image, options);
            ImageLoader.getInstance().loadImage(piclist.get(i), options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    image.setImageBitmap(bitmap);
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    int viewHeight = (int) ((float) height / width * Global.screenWidth);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Global.screenWidth, viewHeight);
                    setLayoutParams(lp);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
            mPageViewList.add(image);
        }
        //
        mPagerAdapter = new MyViewPagerAdapter(mPageViewList);
        mViewpagerBanner.setAdapter(mPagerAdapter);
        //
        mPoints.setPointNum(mPageViewList.size());
        mPoints.setSelectIndex(mCurrPagerItemPosition);
        mIsBannerScroll = true;
        initTimer();
    }

    public void setResList(List<Integer> list) {
        for (int i = 0; i < list.size(); i++) {
            final ImageView image = new ImageView(mContext);
            LayoutParams lp = new LayoutParams(Global.screenWidth, LayoutParams.WRAP_CONTENT);
            image.setLayoutParams(lp);
            image.setScaleType(ScaleType.CENTER_CROP);
            image.setImageResource(R.mipmap.pic_default_icon);
            ImageLoader.getInstance().loadImage("drawable://" + list.get(i), options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    image.setImageBitmap(bitmap);
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    int viewHeight = (int) ((float) height / width * Global.screenWidth);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Global.screenWidth, viewHeight);
                    setLayoutParams(lp);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
//            ImageLoader.getInstance().displayImage("drawable://" + list.get(i), image, options);
            mPageViewList.add(image);
        }
        //
        mPagerAdapter = new MyViewPagerAdapter(mPageViewList);
        mViewpagerBanner.setAdapter(mPagerAdapter);
        //
        mPoints.setPointNum(mPageViewList.size());
        mPoints.setSelectIndex(mCurrPagerItemPosition);
        mIsBannerScroll = true;
        initTimer();
    }

    public int getListSize() {
        return mPageViewList.size();
    }

    public void clearList() {
        if (mPagerAdapter != null) {
            mPagerAdapter.clearList();
        }
        mIsBannerScroll = false;
        cancelTimer();
        mCurrPagerItemPosition = 0;
    }

    class MyViewPagerAdapter extends PagerAdapter {
        private List<ImageView> mListViews;

        public MyViewPagerAdapter(List<ImageView> mListViews) {
            this.mListViews = mListViews;
        }

        public void clearList() {
            if (mListViews != null) {
                mListViews.clear();
                notifyDataSetChanged();
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mListViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mListViews.get(position), 0);
            return mListViews.get(position);
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int position) {
        // TODO Auto-generated method stub
        mCurrPagerItemPosition = position;
        if (mPoints != null) {
            mPoints.setSelectIndex(mCurrPagerItemPosition);
            pageChange(mCurrPagerItemPosition);
        }
    }

    /**
     * 计时器--控制banner显示周期
     */
    private void initTimer() {
        mTimer = new Timer();
        mTask = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (mIsBannerScroll) {
                    mCurrPagerItemPosition++;
                    mCurrPagerItemPosition = mCurrPagerItemPosition % mPagerAdapter.getCount();
                    Message mess = new Message();
                    mess.what = MSG_PAGER_SCROLL_CONTROLL;
                    mHandlerMain.sendMessage(mess);
                } else {
                    return;
                }
            }
        };
        mTimer.schedule(mTask, mTimerDuration, mTimerDuration);
    }

    Handler mHandlerMain = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_PAGER_SCROLL_CONTROLL:
                    mViewpagerBanner.setCurrentItem(mCurrPagerItemPosition++);
                    break;
            }

        }

    };

    /**
     * 暂停banner滚动
     */
    public void cancelTimer() {
        if (mTimer != null && mTask != null) {
            mTimer.cancel();
            mTask.cancel();
            mTask = null;
            mTimer = null;
        }
    }

//	/**
//	 * 获取顶部banner数据
//	 *
//	 */
//	public void getBannerList() {
//		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("catid", "1"));
//		new DownLoadDataLib("post", new BannerListData()).setHandlerwhat(BannerRequestCode).setHandler(mHandler).getBannerList(params);
//	}
//
//	Handler mHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			// TODO Auto-generated method stub
//			super.handleMessage(msg);
//			if (msg.obj == null) {
//				// Global.showToast("数据返回错误，请检查网络重试...", mContext);
//				return;
//			}
//			switch (msg.what) {
//			case BannerRequestCode:
//				BannerListData data = (BannerListData) msg.obj;
//				int code = data.code;
//				// Global.showToast(data.msg, mContext);
//				if (code == 1) {
//					mCurrBannerList = data.data;
//					if (mCurrBannerList != null && mCurrBannerList.size() > 0) {
//						fillData(mCurrBannerList);
//					}
//					break;
//				}
//			}
//			// BannerListData data = (BannerListData) msg.obj;
//			// if (data != null) {
//			// int status = data.
//			// if (status == 1) {
//			// ArrayList<Banner> list = data.getInfo();
//			// if (list != null && list.size() > 0) {
//			// fillData(list);
//			// }
//			// }
//			// }
//		}
//
//	};

    private void fillData(ArrayList<BannerListData.Banner> list) {
        for (int i = 0; i < list.size(); i++) {
            ImageView image = new ImageView(mContext);
            LayoutParams lp = new LayoutParams(Global.screenWidth, LayoutParams.WRAP_CONTENT);
            image.setLayoutParams(lp);
            image.setScaleType(ScaleType.FIT_CENTER);
            // image.setBackgroundResource(R.drawable.pic_thumb_bg);
            image.setImageResource(R.mipmap.pic_default_icon);
            image.setTag(list.get(i).id);
            if (options == null) {
                options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.pic_default_icon).showImageForEmptyUri(R.mipmap.pic_default_icon)
                        .showImageOnFail(R.mipmap.pic_default_icon).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
                        .imageScaleType(ImageScaleType.IN_SAMPLE_INT).resetViewBeforeLoading(false).displayer(new FadeInBitmapDisplayer(500)).build();
            }
            ImageLoader.getInstance().displayImage(list.get(i).thumb, image, options);
            mPageViewList.add(image);
        }
        //
        mPagerAdapter = new MyViewPagerAdapter(mPageViewList);
        mViewpagerBanner.setAdapter(mPagerAdapter);
        //
        mPoints.setPointNum(mPageViewList.size());
        mPoints.setSelectIndex(mCurrPagerItemPosition);
        mIsBannerScroll = true;
        initTimer();
    }

    public void setOnPageChangeListener(PageChangeListener pagechangelistener) {
        this.pagechangelistener = pagechangelistener;
    }

    public interface PageChangeListener {
        public void pageChangeListener(int index);
    }

    private void pageChange(int index) {
        if (pagechangelistener != null) {
            pagechangelistener.pageChangeListener(index);
        }
    }

}
