package com.coco3g.caopantx.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coco3g.caopantx.R;
import com.coco3g.caopantx.activity.BaseFragment;
import com.coco3g.caopantx.activity.WebActivity;
import com.coco3g.caopantx.data.DataUrl;
import com.coco3g.caopantx.utils.Coco3gBroadcastUtils;

import org.w3c.dom.Text;

/**
 * Created by MIN on 16/6/13.
 */
//public class FindFragment extends BaseFragment implements View.OnClickListener {
//    private View view;
//    private LinearLayout mRlTGZQ;
//    private RelativeLayout mRlXXZX, mRlRWZX, mRlJSSJ;
//    private ImageView mIvMNDS, mIvXinYongHu;
//    //刷新整个webview的广播
//    public Coco3gBroadcastUtils mCurrBoardCast;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment_find_new, null);
//        initView();
////        initData();
////        setWebView();
//        // 接收需要 刷新当前界面的广播
//        mCurrBoardCast = new Coco3gBroadcastUtils(getActivity());
//        mCurrBoardCast.receiveBroadcast(Coco3gBroadcastUtils.RETURN_UPDATE_FIND_FLAG)
//                .setOnReceivebroadcastListener(new Coco3gBroadcastUtils.OnReceiveBroadcastListener() {
//                    @Override
//                    public void receiveReturn(Intent intent) {
//                    }
//                });
//
//        return view;
//    }
//
//    //    @Override
////    public void onStart() {
////        super.onStart();
////        // 验证用户是否登录
////        if (Global.USERINFO == null && Global.LOGIN_INFO_MAP == null) {
////            isLogin = false;
////        } else {
////            isLogin = true;
////        }
////    }
////
//    private void initView() {
//        mIvMNDS = (ImageView) view.findViewById(R.id.find_iv_mnds);
//        mIvXinYongHu = (ImageView) view.findViewById(R.id.find_iv_xinyonghu);
//        mRlTGZQ = (LinearLayout) view.findViewById(R.id.find_rl_tgzq);
//        mRlXXZX = (RelativeLayout) view.findViewById(R.id.find_rl_xxzx);
//        mRlRWZX = (RelativeLayout) view.findViewById(R.id.find_rl_rwzx);
////        mRlJFSC = (RelativeLayout) view.findViewById(R.id.find_rl_jfsc);
//        mRlJSSJ = (RelativeLayout) view.findViewById(R.id.find_rl_jssj);
//        mIvMNDS.setOnClickListener(this);
//        mIvXinYongHu.setOnClickListener(this);
//        mRlTGZQ.setOnClickListener(this);
//        mRlXXZX.setOnClickListener(this);
//        mRlRWZX.setOnClickListener(this);
////        mRlJFSC.setOnClickListener(this);
//        mRlJSSJ.setOnClickListener(this);
//    }
//
//    //
////    /**
////     * 点击事件
////     * @param v
////     */
////    @Override
//    public void onClick(View v) {
//        Intent intent = null;
//        switch (v.getId()) {
//            case R.id.find_iv_mnds://模拟大赛
//                intent = new Intent(getActivity(), WebActivity.class);
//                intent.putExtra("url", DataUrl.MONIDASAI);
//                startActivity(intent);
//                break;
//            case R.id.find_iv_xinyonghu://新用户
//                intent = new Intent(getActivity(), WebActivity.class);
//                intent.putExtra("url", DataUrl.BASE_URL + "/content/index/peixun");   //ZHUANXIANG
//                startActivity(intent);
//                break;
//            case R.id.find_rl_tgzq://推广赚钱
//                intent = new Intent(getActivity(), WebActivity.class);
//                intent.putExtra("url", DataUrl.BASE_URL + "/content/index/lists/catid/3");  //DataUrl.TUIGUANGZHUANQIAN
//                startActivity(intent);
//                break;
//            case R.id.find_rl_xxzx://信息中心
//                intent = new Intent(getActivity(), WebActivity.class);
//                intent.putExtra("url", DataUrl.XINXIZHONGXIN);
//                startActivity(intent);
//                break;
//            case R.id.find_rl_rwzx://任务中心
//                intent = new Intent(getActivity(), WebActivity.class);
//                intent.putExtra("url", DataUrl.RENWUZHONGXIN);
//                startActivity(intent);
//                break;
//    //            case R.id.find_rl_jfsc://积分商城
//    //                intent = new Intent(getActivity(), WebActivity.class);
//    //                intent.putExtra("url", DataUrl.JIFENSHANGCHENG);
//    //                startActivity(intent);
//    //                break;
//            case R.id.find_rl_jssj: // 金十数据
//                intent = new Intent(getActivity(), WebActivity.class);
//                intent.putExtra("url", DataUrl.JINSHISHUJU);
//                startActivity(intent);
//                break;
//        }
//    }
////
////    private Handler mHandler = new Handler() {
////        @Override
////        public void handleMessage(Message msg) {
////            super.handleMessage(msg);
////            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
////            mHandler.sendEmptyMessageDelayed(0, 3000);
////        }
////    };
////    /**
////     * 测试数据
////     */
////
////    private ArrayList<TestPicInfo> picList = new ArrayList<TestPicInfo>();
////
////    private void initData() {
////        picList.add(new TestPicInfo(R.mipmap.pic_test_home));
////        picList.add(new TestPicInfo(R.mipmap.pic_test_home));
////        picList.add(new TestPicInfo(R.mipmap.pic_test_home));
////        picList.add(new TestPicInfo(R.mipmap.pic_test_home));
////        HomeViewPagerAdapter viewPagerAdapter = new HomeViewPagerAdapter(getActivity(), picList);
////        mViewPager.setAdapter(viewPagerAdapter);
////        int midCount = 1000 / 2;
////        int indexCount = midCount % picList.size();
////        mViewPager.setCurrentItem(midCount - indexCount);
////        //让viewpager自动播放
////        mHandler.sendEmptyMessageDelayed(0, 3000);
////        initDot();
////        updateViewPagerInfo();
////    }
////
////    //-------------------一道华丽的分割线--------------------------
////
////    private void initDot() {
////        for (int i = 0; i < picList.size(); i++) {
////            View viewDot = new View(getActivity());
////            viewDot.setBackgroundResource(R.drawable.selector_viewpager_dot);
////            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(Global.dipTopx(getActivity(), 8), Global.dipTopx(getActivity(), 8));
////            int padding = Global.dipTopx(getActivity(), 5);
////            param.setMargins(padding, padding, padding, padding);
////            viewDot.setLayoutParams(param);
////            mLLDot.addView(viewDot);
////        }
////    }
////
////    private void updateViewPagerInfo() {
////        int currentPage = mViewPager.getCurrentItem() % picList.size();
////        for (int i = 0; i < mLLDot.getChildCount(); i++) {
////            mLLDot.getChildAt(i).setEnabled(i == currentPage);
////        }
////    }
//
//
////    public void setWebView() {
////        mWebView.loadUrl(DataUrl.BASE_URL + DataUrl.FIND_URL);
////        mWebView.setOnTitleListener(new MyScrollWebView.SetTitleListener() {
////            @Override
////            public void setTitle(String title) {
////                setTitleFromHtml(title);
////            }
////        });
//
////        WebSettings wSet = webView.getSettings();
////        wSet.setJavaScriptEnabled(true);
////        webView.setWebViewClient(new WebViewClient() {
////            @Override
////            public boolean shouldOverrideUrlLoading(WebView view, String url) {
////                webView.getSettings().setJavaScriptEnabled(true);
////                webView.loadUrl(url);
////                return true;
////            }
////
////            @Override
////            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
////                super.onReceivedError(view, errorCode, description, failingUrl);
////                Global.showToast(getResources().getString(R.string.connection_error),getActivity());
////            }
////        });
//
////    }
//
//}




public class FindFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private ImageView mBanner;
    private TextView mTask, mMessage, mGuide, mFinance, mRadio;
//    private ImageView mIvMNDS, mIvXinYongHu;
    //刷新整个webview的广播
    public Coco3gBroadcastUtils mCurrBoardCast;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_find_new, null);
        initView();
//        initData();
//        setWebView();
        // 接收需要 刷新当前界面的广播
        mCurrBoardCast = new Coco3gBroadcastUtils(getActivity());
        mCurrBoardCast.receiveBroadcast(Coco3gBroadcastUtils.RETURN_UPDATE_FIND_FLAG)
                .setOnReceivebroadcastListener(new Coco3gBroadcastUtils.OnReceiveBroadcastListener() {
                    @Override
                    public void receiveReturn(Intent intent) {
                    }
                });

        return view;
    }

    //    @Override
//    public void onStart() {
//        super.onStart();
//        // 验证用户是否登录
//        if (Global.USERINFO == null && Global.LOGIN_INFO_MAP == null) {
//            isLogin = false;
//        } else {
//            isLogin = true;
//        }
//    }
//
    private void initView() {
        mBanner = (ImageView) view.findViewById(R.id.find_iv_banner);
        mTask = (TextView) view.findViewById(R.id.find_tv_task);
        mMessage = (TextView) view.findViewById(R.id.find_tv_msg);
        mGuide = (TextView) view.findViewById(R.id.tv_find_guide);
        mFinance = (TextView) view.findViewById(R.id.tv_find_finance);
        mRadio = (TextView) view.findViewById(R.id.tv_find_radio);


        mTask.setOnClickListener(this);
        mMessage.setOnClickListener(this);
        mGuide.setOnClickListener(this);
        mFinance.setOnClickListener(this);
        mRadio.setOnClickListener(this);

    }

    //
//    /**
//     * 点击事件
//     * @param v
//     */
//    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.find_tv_task://任务中心
                intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", DataUrl.RENWUZHONGXIN);
                startActivity(intent);
                break;
            case R.id.find_tv_msg://消息中心
                intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", DataUrl.XINXIZHONGXIN);
                startActivity(intent);
                break;
            case R.id.tv_find_guide://新手指导
                intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", DataUrl.BASE_URL + "/content/index/lists/catid/3");
                startActivity(intent);
                break;
            case R.id.tv_find_finance://财经资讯
                intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", DataUrl.JINSHISHUJU );
                startActivity(intent);
                break;
            case R.id.tv_find_radio://财经直播
                intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", DataUrl.BASE_URL + "/content/index/lists/catid/3");
                startActivity(intent);
                break;

//            case R.id.find_rl_jssj: // 金十数据
//                intent = new Intent(getActivity(), WebActivity.class);
//                intent.putExtra("url", DataUrl.JINSHISHUJU);
//                startActivity(intent);
//                break;
        }
    }
}