package com.coco3g.caopantx.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


import com.coco3g.caopantx.bean.BaseData;
import com.coco3g.caopantx.bean.HangQingBaseDataBean;
import com.coco3g.caopantx.bean.KViewUrlDataBean;
import com.coco3g.caopantx.bean.OrderDataBean;
import com.coco3g.caopantx.bean.ProDetailDataBean;
import com.coco3g.caopantx.data.DataUrl;
import com.coco3g.caopantx.data.Global;
import com.coco3g.caopantx.listener.IBaseDataListener;
import com.coco3g.caopantx.listener.IKViewUrlListener;
import com.coco3g.caopantx.listener.IProDetailListener;
import com.coco3g.caopantx.listener.ISocketHangQingListener;
import com.coco3g.caopantx.listener.ISocketOrderListener;
import com.coco3g.caopantx.presenter.KViewUrlPresenter;
import com.coco3g.caopantx.presenter.ProDetailPresenter;
import com.coco3g.caopantx.presenter.SocketRequestPresenter;
import com.coco3g.caopantx.utils.Coco3gBroadcastUtils;
import com.coco3g.caopantx.view.SlideSwitch;
import com.coco3g.caopantx.view.SlideSwitch.SlideListener;
import com.coco3g.caopantx.view.SpinerPopWindow;
import com.coco3g.caopantx.view.TopBarView;
import com.coco3g.caopantx.R;
import com.coco3g.caopantx.view.KViewWebView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lisen on 16/9/2.
 */
public class K_DetailActivity extends BaseActivity implements View.OnClickListener,SlideListener{
    TopBarView mTopBar;
    RelativeLayout mRelativeXiaDan,mRelativeQuickTrade;
    TabLayout mTabLayout;
    KViewWebView mWeb1, mWeb2, mWeb3, mWeb4, mWeb5;
    //    NotScrollViewPager mViewPager;
    TextView mTxtSalePrice, mTxtPerprice, mTxtZhangdie, mTxtBuynum, mTxtSalenum, mTxtShipan, mTxtShipanDs, mTxtFudong,
            mTxtMaizhang, mTxtMaidie, mTxtYiJianQP, mTxtFenShi, mTxtRiXian, mTxtPanKou, mTxtFenZhong, mTxtJieSuan, mTxtChiCangUnRead,
            mTxtnums,mTxtZhiSunNum,mTxtZhiYingNum;
    TextView[] textViews;  //三个webview
    ImageView mImageUnread;
    ProgressBar mProgress1, mProgress2;

    LinearLayout mLinearWeb, mLinearMaidie, mLinearMaZhang, mLinear;
    RelativeLayout mRelativeChiCangZB, mRelativeChiCang;
    CheckBox mSwitch;
    SlideSwitch mQuickSwitch;

    private SpinerPopWindow<String> mSpinerPopWindow;
    private List<String> minutelist;




    //
    RelativeLayout.LayoutParams mLpKview;
    String[] mTitlelist;
    ArrayList<KViewWebView> mViewList = new ArrayList<>();
    ArrayList<String> mVUrlList = new ArrayList<>();
    HashMap<Integer, Boolean> mCurrLoadMap = new HashMap<>();
    String mID = "", mDevice = "", mMoni = "";
    KViewUrlDataBean.Url mCurrUrl;
    ProDetailDataBean.Pro mCurrProInfo;
    SocketRequestPresenter mSocketPresenter, mSocketOrderPresenter;
    Coco3gBroadcastUtils mCurrBoardCast;
    private ImageView mImageRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kdetail);
        mID = getIntent().getStringExtra("id");
        mDevice = getIntent().getStringExtra("device");
        mMoni = getIntent().getStringExtra("moni");
        mTitlelist = new String[]{"分时", "日线", "盘口", "5分线","15分线"};

        initView();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int webHeight = mLinearWeb.getHeight() - mTabLayout.getHeight();
        mLpKview = new RelativeLayout.LayoutParams(Global.screenWidth, webHeight);
        mLpKview.topMargin = Global.dipTopx(this, 10f);
        mWeb1.setLayoutParams(mLpKview);
        mWeb2.setLayoutParams(mLpKview);
        mWeb3.setLayoutParams(mLpKview);
        mWeb4.setLayoutParams(mLpKview);
//        PYTODO
        mWeb5.setLayoutParams(mLpKview);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSocketPresenter != null) {
            mSocketPresenter.closeSocket();
        }
        if (mSocketOrderPresenter != null) {
            mSocketOrderPresenter.closeSocket();
        }
        getProDetail();
    }

    /**
     *  初始化UI
     */
    private void initView() {
        mTopBar = (TopBarView) findViewById(R.id.topbar_kdetail);
        TextView tv = new TextView(this);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(14f);
        int padding = Global.dipTopx(this, 10);
        tv.setTextColor(getResources().getColor(R.color.white));
        tv.setPadding(padding, padding, padding, padding);
        tv.setText("规则说明");
        mTopBar.setRightView(tv);





        mTopBar.setOnClickRightListener(new TopBarView.OnClickRightView() {
            @Override
            public void onClickTopbarView() {
                if (mCurrProInfo != null && !TextUtils.isEmpty(mCurrProInfo.detail_url)) {
                    Intent intent = new Intent(K_DetailActivity.this, WebActivity.class);
                    intent.putExtra("url", mCurrProInfo.detail_url);
                    startActivity(intent);
                }
            }
        });
        mLinearWeb = (LinearLayout) findViewById(R.id.linear_kdetail_web);
        mTabLayout = (TabLayout) findViewById(R.id.tab_kdetail);
        mWeb1 = (KViewWebView) findViewById(R.id.web_kdetail_1);
        mWeb2 = (KViewWebView) findViewById(R.id.web_kdetail_2);
        mWeb3 = (KViewWebView) findViewById(R.id.web_kdetail_3);
        mWeb4 = (KViewWebView) findViewById(R.id.web_kdetail_4);
//        PYTODO
        mWeb5 = (KViewWebView) findViewById(R.id.web_kdetail_5);


        mRelativeXiaDan = (RelativeLayout) findViewById(R.id.relative_kdetail_jisu_xiadan);
        mTxtSalePrice = (TextView) findViewById(R.id.tv_kdetail_saleprice);
        mTxtPerprice = (TextView) findViewById(R.id.tv_kdetail_perprice);
        mTxtZhangdie = (TextView) findViewById(R.id.tv_kdetail_zhangdie);
        mTxtBuynum = (TextView) findViewById(R.id.tv_kdetail_buynum_num);
        mTxtSalenum = (TextView) findViewById(R.id.tv_kdetail_salenum_num);
        mTxtShipan = (TextView) findViewById(R.id.tv_kdetail_shipan);
        mTxtShipanDs = (TextView) findViewById(R.id.tv_kdetail_shipan_description);
        mTxtFudong = (TextView) findViewById(R.id.tv_kdetail_fudong);
        mQuickSwitch = (SlideSwitch) findViewById(R.id.quickSwitch);


        mImageUnread = (ImageView) findViewById(R.id.tv_kdetail_unread);
        mTxtJieSuan = (TextView) findViewById(R.id.tv_kdetail_jiesuan);
        mTxtMaizhang = (TextView) findViewById(R.id.tv_kdetail_maizhang);
        mTxtMaidie = (TextView) findViewById(R.id.tv_kdetail_maidie);
        mTxtChiCangUnRead = (TextView) findViewById(R.id.tv_kdetail_chicang_unread_num);
        mProgress1 = (ProgressBar) findViewById(R.id.progress_kdetail_1);
        mProgress2 = (ProgressBar) findViewById(R.id.progress_kdetail_2);
        mLinearMaidie = (LinearLayout) findViewById(R.id.linear_kdetail_maidie);
        mLinearMaZhang = (LinearLayout) findViewById(R.id.linear_kdetail_maizhang);

        mRelativeChiCangZB = (RelativeLayout) findViewById(R.id.relative_kdetail_chicang_zhibo);

        mRelativeChiCang = (RelativeLayout) findViewById(R.id.relative_kdetail_chicang);

        mRelativeQuickTrade = (RelativeLayout) findViewById(R.id.relative_kdetail_quick_trade);
        mTxtnums = (TextView) findViewById(R.id.tv_kdetail_nums);
        mTxtZhiSunNum = (TextView) findViewById(R.id.tv_kdetail_zhisun_num);
        mTxtZhiYingNum = (TextView) findViewById(R.id.tv_kdetail_zhiying_num);
//        mTxtZhiSun = (TextView) findViewById(R.id.tv_kdetail_zhisun);
//        mTxtZhiYing = (TextView) findViewById(R.id.tv_kdetail_zhiying);
//        mSwitch = (CheckBox) findViewById(R.id.switch_kdetail);
        mImageRefresh = (ImageView) findViewById(R.id.image_kdetail_refresh_data);
//        mTxtUnLogin = (TextView) findViewById(R.id.tv_kdetail_unlogin);
        mTxtYiJianQP = (TextView) findViewById(R.id.tv_kdetail_yijian_quanping);
        mTxtFenShi = (TextView) findViewById(R.id.tv_kdetail_fenshi);
        mTxtRiXian = (TextView) findViewById(R.id.tv_kdetail_rixian);
        mTxtPanKou = (TextView) findViewById(R.id.tv_kdetail_pankou);

        // 初始化下拉框数据
        initData();

        mTxtFenZhong = (TextView) findViewById(R.id.tv_kdetail_min);
        mSpinerPopWindow = new SpinerPopWindow<String>(this, minutelist,itemClickListener);
        mSpinerPopWindow.setOnDismissListener(dismissListener);

        //
        textViews = new TextView[]{mTxtFenShi, mTxtRiXian, mTxtPanKou, mTxtFenZhong};
        textViews[0].setSelected(true);
        for (int i = 0; i < mTitlelist.length; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitlelist[i]));
        }
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                PYTODO -2
                for (int i = 0; i < mViewList.size()-2; i++) {
                    if (i == tab.getPosition()) {
                        mViewList.get(i).setVisibility(View.VISIBLE);

                        if (!mCurrLoadMap.get(i)) {
//
                            mViewList.get(i).loadUrl(mVUrlList.get(i));
                            mCurrLoadMap.put(i, true);
                        }
                    } else {
                        mViewList.get(i).setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });



        // 接收需要刷新当前界面的广播
        mCurrBoardCast = new Coco3gBroadcastUtils(this);
        mCurrBoardCast.receiveBroadcast(Coco3gBroadcastUtils.RETURN_UPDATE_FLAG)
                .setOnReceivebroadcastListener(new Coco3gBroadcastUtils.OnReceiveBroadcastListener() {
                    @Override
                    public void receiveReturn(Intent intent) {
                        refreshCurrAllData(); // 刷新全部数据
                    }
                });
        //

        mImageRefresh.setOnClickListener(this);
        mRelativeXiaDan.setOnClickListener(this);
        mLinearMaidie.setOnClickListener(this);
        mLinearMaZhang.setOnClickListener(this);
        mTxtYiJianQP.setOnClickListener(this);
        mRelativeChiCangZB.setOnClickListener(this);
        mRelativeChiCang.setOnClickListener(this);
        mTxtFenShi.setOnClickListener(this);
        mTxtRiXian.setOnClickListener(this);
        mTxtPanKou.setOnClickListener(this);
        mTxtFenZhong.setOnClickListener(this);
        mTxtJieSuan.setOnClickListener(this);


        mQuickSwitch.setSlideListener(this);

        //
        mWeb2.setVisibility(View.GONE);
        mWeb3.setVisibility(View.GONE);
        mWeb4.setVisibility(View.GONE);
//        PYTODO
        mWeb5.setVisibility(View.GONE);
        //
        mViewList.add(mWeb1);
        mViewList.add(mWeb2);
        mViewList.add(mWeb3);
        mViewList.add(mWeb4);
//        PYTODO
        mViewList.add(mWeb5);
    }

    /**
     * 监听popupwindow取消
     */
    private OnDismissListener dismissListener=new OnDismissListener() {
        @Override
        public void onDismiss() {
            setTextImage(R.mipmap.icon_down);
        }
    };

    /**
     * popupwindow显示的ListView的item点击事件
     */
    private OnItemClickListener itemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mSpinerPopWindow.dismiss();
            mTxtFenZhong.setText(minutelist.get(position));
//        //    PYTODO

            switch (position) {

                case 0:
                    setWebviewVisible(3);
                    break;

                case 1:
                    setPopupWebviewVisible(4);
                    break;
            }
//            Toast.makeText(K_DetailActivity.this, "点击了:" + minutelist.get(position) + position,Toast.LENGTH_LONG).show();
        }
    };

    /**
     * 显示PopupWindow
     */
    private OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_kdetail_min:
                    mSpinerPopWindow.setWidth(mTxtFenZhong.getWidth());
                    mSpinerPopWindow.showAsDropDown(mTxtFenZhong);
                    setTextImage(R.mipmap.icon_up);
                    break;
            }
        }
    };

    /**
     * 初始化数据
     */
    private void initData() {
        minutelist = new ArrayList<String>();
        for (int i = 3; i < mTitlelist.length; i++) {
            minutelist.add(mTitlelist[i]);
        }
    }

    /**
     * 给TextView右边设置图片
     * @param resId
     */
    private void setTextImage(int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());// 必须设置图片大小，否则不显示
        mTxtFenZhong.setCompoundDrawables(null, null, drawable, null);
    }


    @Override
        // PYDO
        public void open() {
            // TODO Auto-generated method stub

//            Toast.makeText(this,
//                    "silde open",Toast.LENGTH_SHORT).show();

            if (Global.USERINFO == null) {
                    Intent intent = new Intent(K_DetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }

                    String url = DataUrl.START_JISU_ORDER_URL + mID + "/prono/" + mCurrProInfo.prono + "/moni/" + mMoni;
                    Intent intent = new Intent(K_DetailActivity.this, WebActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);


            mRelativeQuickTrade.setVisibility(View.VISIBLE);

        }

        @Override
        public void close() {
            // TODO Auto-generated method stub

//            Toast.makeText(this,
//                    "silde close",Toast.LENGTH_SHORT).show();

//            mQuickSwitch.setSlideable(false);

            mRelativeQuickTrade.setVisibility(View.GONE);
            closeOrderSet();

        }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {

            case R.id.tv_kdetail_fenshi:  //分时
                setWebviewVisible(0);
                break;
            case R.id.tv_kdetail_rixian:  //日线
                setWebviewVisible(1);
                break;
            case R.id.tv_kdetail_pankou:  //盘口
                setWebviewVisible(2);
                break;
//            case R.id.tv_kdetail_min5:  //5分线
//                setWebviewVisible(3);
//                break;

            case R.id.tv_kdetail_min:
                mSpinerPopWindow.setWidth(mTxtFenZhong.getWidth());
                mSpinerPopWindow.showAsDropDown(mTxtFenZhong);
                setTextImage(R.mipmap.icon_up);
                break;

            case R.id.tv_kdetail_yijian_quanping: // 一键全平
                if (alreadyLogin()) {
                    pingCangAll();
                }
                break;
            case R.id.relative_kdetail_chicang_zhibo: // 持仓直播
//                if (alreadyLogin()) {
//                    intent = new Intent(K_DetailActivity.this, WebActivity.class);
//                    intent.putExtra("url", mCurrUrl.zhibo);
//                    startActivity(intent);
//                }
                break;
            case R.id.relative_kdetail_chicang: // 持仓
                if (alreadyLogin()) {
                    if (mCurrUrl != null && !TextUtils.isEmpty(mCurrUrl.chicang)) {
                        intent = new Intent(K_DetailActivity.this, WebActivity.class);
                        intent.putExtra("url", mCurrUrl.chicang);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.tv_kdetail_jiesuan: // 结算
                if (alreadyLogin()) {
                    intent = new Intent(K_DetailActivity.this, WebActivity.class);
                    intent.putExtra("url", mCurrUrl.jiesuan);
                    startActivity(intent);
                }
                break;
            case R.id.linear_kdetail_maizhang: // 买涨
                if (alreadyLogin()) {
                    if ("0".equals(mCurrProInfo.fx_agree)) {
                        intent = new Intent(K_DetailActivity.this, AgreeActivity.class);
                        intent.putExtra("url", DataUrl.FENGXIAN_URL);
                        intent.putExtra("proid", mCurrProInfo.tid);
                        startActivity(intent);
                    } else if ("1".equals(mCurrProInfo.fx_agree)) {
                        if ("1".equals(mCurrProInfo.orderset.status)) {
                            quickOrder("0");
                        } else if (mCurrUrl != null && !TextUtils.isEmpty(mCurrUrl.buyzhang)) {
                            intent = new Intent(K_DetailActivity.this, WebActivity.class);
                            intent.putExtra("url", mCurrUrl.buyzhang);
                            startActivity(intent);
                        }
                    }
                }
                break;
            case R.id.linear_kdetail_maidie: // 买跌
                if (alreadyLogin()) {
                    if ("0".equals(mCurrProInfo.fx_agree)) {
                        intent = new Intent(K_DetailActivity.this, AgreeActivity.class);
                        intent.putExtra("url", DataUrl.FENGXIAN_URL);
                        intent.putExtra("proid", mCurrProInfo.tid);
                        startActivity(intent);
                    } else if ("1".equals(mCurrProInfo.fx_agree)) {
                        if ("1".equals(mCurrProInfo.orderset.status)) {
                            quickOrder("1");
                        } else {
                            intent = new Intent(K_DetailActivity.this, WebActivity.class);
                            intent.putExtra("url", mCurrUrl.buydie);
                            startActivity(intent);
                        }
                    }
                }
                break;
            case R.id.image_kdetail_refresh_data:
                // 刷新全部数据
                if (mSocketPresenter != null) {
                    mSocketPresenter.closeSocket(); // 先关闭socket通道
                }
                if (mSocketOrderPresenter != null) {
                    mSocketOrderPresenter.closeSocket(); // 先关闭socket通道
                }
                getProDetail();
                break;
            case R.id.relative_kdetail_jisu_xiadan:  //快速下单
                if (alreadyLogin()) {
                    if ("1".equals(mCurrProInfo.orderset.status)) {
                        closeOrderSet();

                    } else {
                        String url = DataUrl.START_JISU_ORDER_URL + mID + "/prono/" + mCurrProInfo.prono + "/moni/" + mMoni;
                        Intent intent3 = new Intent(K_DetailActivity.this, WebActivity.class);
                        intent3.putExtra("url", url);
                        startActivity(intent3);

                    }
                } else {

                }
                break;
            // PYDO
            case R.id.quickSwitch:
                if (alreadyLogin()) {
                    if ("1".equals(mCurrProInfo.orderset.status)) {
                        closeOrderSet();

                    } else {
                        String url = DataUrl.START_JISU_ORDER_URL + mID + "/prono/" + mCurrProInfo.prono + "/moni/" + mMoni;
                        Intent intent3 = new Intent(K_DetailActivity.this, WebActivity.class);
                        intent3.putExtra("url", url);
                        startActivity(intent3);

                    }
                } else {

                }
                break;

        }
    }
  //PYTODO -1
    //设置webview的显示与隐藏
    public void setWebviewVisible(int position) {

        for (int i = 0; i < mViewList.size()-1; i++) {

            mViewList.get(mViewList.size()-1).setVisibility(View.GONE);

            if (i == position) {
                textViews[i].setSelected(true);
                mViewList.get(i).setVisibility(View.VISIBLE);

                if (!mCurrLoadMap.get(i)) {

                    mViewList.get(i).loadUrl(mVUrlList.get(i));
                    mCurrLoadMap.put(i, true);
                }
            } else {
                textViews[i].setSelected(false);
                mViewList.get(i).setVisibility(View.GONE);
            }
        }
    }

    //PYTODO
    public void setPopupWebviewVisible(int position) {

        for (int i = 0; i < mViewList.size(); i++) {
            if (i == position){
                textViews[mViewList.size()-2].setSelected(true);
                mViewList.get(i).setVisibility(View.VISIBLE);
                if (!mCurrLoadMap.get(i)) {

                    mViewList.get(i).loadUrl(mVUrlList.get(i));
                    mCurrLoadMap.put(i, true);
                }
            }else {
                textViews[i].setSelected(false);
                mViewList.get(i).setVisibility(View.GONE);
            }


        }



//        for (int y = 0; y < minutelist.size(); y++){
//            if (y == position){
//                mViewList.get(y+3).setVisibility(View.VISIBLE);
//                mViewList.get(y+3).loadUrl(mVUrlList.get(y+3));
//                mCurrLoadMap.put(y+3, true);
//            }
//            textViews[mViewList.size()-1].setSelected(true);
//        }

    }



    /**
     * 获取K线图url
     */
    private void getKViewUrl() {
        new KViewUrlPresenter(this).getKViewUrl(mCurrProInfo.prono, mID, mCurrProInfo.catid, mCurrProInfo.nums, mDevice, mMoni, new IKViewUrlListener() {
            @Override
            public void onSuccess(KViewUrlDataBean data) {
                if (data.response != null) {
                    mCurrUrl = data.response; // 各种url
                    mVUrlList.add(data.response.minute);
                    mVUrlList.add(data.response.kline);
                    mVUrlList.add(data.response.pankou);
                    mVUrlList.add(data.response.minute_kline5);
                    mVUrlList.add(data.response.minute_kline15);
                    //
                    mWeb1.loadUrl(mVUrlList.get(0));
                    mCurrLoadMap.put(0, true);
                    mCurrLoadMap.put(1, false);
                    mCurrLoadMap.put(2, false);
                    mCurrLoadMap.put(3, false);
                    mCurrLoadMap.put(4, false);
                }

            }

            @Override
            public void onFailure(BaseData data) {

            }

            @Override
            public void onError() {

            }
        });
    }

    //判断是否是登录状态
    public boolean alreadyLogin() {
        if (Global.USERINFO != null) {
            return true;
        } else {
            Intent intent = new Intent(K_DetailActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        return false;
    }

    /**
     * 获取产品详情
     */
    private void getProDetail() {
        new ProDetailPresenter(this).getProDetail(mID, mDevice, mMoni, new IProDetailListener() {
            @Override
            public void onSuccess(ProDetailDataBean data) {
                mCurrProInfo = data.response;

                if (mCurrProInfo != null) {
                    fillData();

                    // 获取行情实时数据
                    mSocketPresenter = new SocketRequestPresenter(K_DetailActivity.this);
                    getHangQingSocketData();
                    if (Global.verifyLogin(K_DetailActivity.this)) {
                        if (mSocketOrderPresenter != null && !mSocketOrderPresenter.isClose()) {
                            mSocketOrderPresenter.closeSocket();
                            mSocketOrderPresenter = null;
                        }
                        mSocketOrderPresenter = new SocketRequestPresenter(K_DetailActivity.this);

                        // 获取实时订单数据
                        getOrderData();
                    }
                    // 获取K线图url
                    getKViewUrl();
                }
            }

            @Override
            public void onFailure(BaseData data) {

            }

            @Override
            public void onError() {

            }
        });
    }

    /**
     * 通过socket获取实时行情数据
     */
    private void getHangQingSocketData() {
        mSocketPresenter.hangqingData("{\"command\":\"binduid\",\"uid\":\"hangqing_one-" + mID + "\"}", new ISocketHangQingListener() {
            @Override
            public void onSuccess(HangQingBaseDataBean data) {
                HangQingBaseDataBean.HangQing hangqing = data.data;
                Message message = new Message();
                message.obj = hangqing;
                mHandlerRefreshView.sendMessage(message);
            }

            @Override
            public void onFailure(BaseData data) {

            }

            @Override
            public void onError() {

            }
        });
    }

    /**
     * 通过socket获取实时订单数据
     */
    private void getOrderData() {
        mSocketOrderPresenter.orderData("{\"command\":\"bindorder\",\"groupname\":\"order-" + Global.USERINFO.userid + "-" + mCurrProInfo.prono + "-" + mMoni + "\"}",
                new ISocketOrderListener() {
                    @Override
                    public void onSuccess(OrderDataBean data) {
//                        PYDO 添加try catch 判断
                        try {
                            OrderDataBean.OrderData orderdata = data.data;
                            Message message = new Message();
                            message.obj = orderdata;

                            mHandlerRefreshChicang.sendMessage(message);
                            mCurrProInfo.zhang = data.data.zhang;
                            mCurrProInfo.die = data.data.die;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(BaseData data) {

                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    /**
     * 获取实时行情后，刷新相关view
     */
    Handler mHandlerRefreshView = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                HangQingBaseDataBean.HangQing hangqing = (HangQingBaseDataBean.HangQing) msg.obj;
                mTxtSalePrice.setText(hangqing.buyPrice + "");
                mTxtMaizhang.setText(hangqing.buyPrice);
                mTxtMaidie.setText(hangqing.salePrice);
                float zhangdie = Float.parseFloat((hangqing.zhangdie).replace(",", ""));
                if (zhangdie < 0) {
                    mTxtPerprice.setTextColor(getResources().getColor(R.color.green));
                    mTxtSalePrice.setTextColor(getResources().getColor(R.color.green));
                    mTxtPerprice.setText("" + zhangdie);
                } else {
                    mTxtPerprice.setTextColor(getResources().getColor(R.color.yellow));
                    mTxtSalePrice.setTextColor(getResources().getColor(R.color.yellow));
                    mTxtPerprice.setText("+" + zhangdie);
                }

                float perprice = Float.parseFloat(hangqing.perprice);
                if (perprice < 0) {
                    mTxtZhangdie.setTextColor(getResources().getColor(R.color.green));
                    mTxtZhangdie.setText(perprice + "%");
                } else {
                    mTxtZhangdie.setTextColor(getResources().getColor(R.color.yellow));
                    mTxtZhangdie.setText("+" + perprice + "%");
                }

                //
                String buynum = hangqing.buyNums;
                String salenum = hangqing.saleNums;
                mTxtBuynum.setText(buynum);
                mTxtSalenum.setText(salenum);
                //
                float total = Float.parseFloat(buynum) + Float.parseFloat(salenum);
                int buyrate = (int) (Float.parseFloat(buynum) / total * 100);
                int salerate = (int) (Float.parseFloat(salenum) / total * 100);
                mProgress1.setProgress(100 - buyrate);
                mProgress2.setProgress(salerate);
                //
//            float lastPrice = Float.parseFloat(String.format("%.0f", Float.parseFloat(hangqing.lastprice)));
                float lastPrice = Float.parseFloat(hangqing.lastprice);
                float zhangPrice = 0;
                if (mCurrProInfo.zhang != null) {
                    for (int i = 0; i < mCurrProInfo.zhang.size(); i++) {
//                    float price = Float.parseFloat(String.format("%.0f", Float.parseFloat(mCurrProInfo.zhang.get(i).price)));
                        float price = Float.parseFloat(mCurrProInfo.zhang.get(i).price);
                        //
                        BigDecimal bd1 = new BigDecimal(Float.toString(lastPrice));
                        BigDecimal bd2 = new BigDecimal(Float.toString(price));
                        float subPrice = bd1.subtract(bd2).floatValue();
                        //
                        float f = subPrice * Integer.parseInt(mCurrProInfo.zhang.get(i).nums) *
                                Integer.parseInt(mCurrProInfo.nums);
//                    f = Float.parseFloat(String.format("%.0f", f));
//                    zhangPrice = zhangPrice + f;
                        float rate = 1f;
                        if (!TextUtils.isEmpty(mCurrProInfo.rate)) {
                            rate = Float.parseFloat(mCurrProInfo.rate);
                        }
                        f = f * rate;
                        f = Float.parseFloat(String.format("%.2f", f));
                        zhangPrice = zhangPrice + f;
                    }
//                float rate = 1f;
//                if (!TextUtils.isEmpty(mCurrProInfo.rate)) {
//                    rate = Float.parseFloat(mCurrProInfo.rate);
//                }
//                zhangPrice = zhangPrice * rate;
                }
                //
                float diePrice = 0;
                if (mCurrProInfo.die != null) {
                    for (int i = 0; i < mCurrProInfo.die.size(); i++) {
                        float price = Float.parseFloat(mCurrProInfo.die.get(i).price);
                        BigDecimal bd1 = new BigDecimal(Float.toString(price));
                        BigDecimal bd2 = new BigDecimal(Float.toString(lastPrice));
                        float subPrice = bd1.subtract(bd2).floatValue();
                        float f = subPrice * Integer.parseInt(mCurrProInfo.die.get(i).nums) *
                                Integer.parseInt(mCurrProInfo.nums);
//                    f = Float.parseFloat(String.format("%.0f", f));
                        float rate = 1f;
                        if (!TextUtils.isEmpty(mCurrProInfo.rate)) {
                            rate = Float.parseFloat(mCurrProInfo.rate);
                        }
                        f = f * rate;
                        f = Float.parseFloat(String.format("%.2f", f));
                        diePrice = diePrice + f;
                    }
//                float rate = 1f;
//                if (!TextUtils.isEmpty(mCurrProInfo.rate)) {
//                    rate = Float.parseFloat(mCurrProInfo.rate);
//                }
//                diePrice = diePrice * rate;
                }
                float totalP = zhangPrice + diePrice;
//            int totalP = (int) (zhangPrice + diePrice);
//            mTxtFudong.setText(String.format("%.2f", (float) Math.round(totalP)));
//            mTxtFudong.setText(String.format("%.2f", Double.parseDouble(totalP + "")));
                mTxtFudong.setText("" + String.format("%.2f", totalP));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 刷新持仓量
     */
    Handler mHandlerRefreshChicang = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                OrderDataBean.OrderData data = (OrderDataBean.OrderData) msg.obj;
                if (data != null) {
                    if (!TextUtils.isEmpty(data.order_nums) && !"0".equals(data.order_nums)) {
                        mTxtChiCangUnRead.setVisibility(View.VISIBLE);
                        mTxtChiCangUnRead.setText(data.order_nums);
                    } else {
                        mTxtChiCangUnRead.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(data.amount)) {
                        mTxtShipan.setText(data.amount + "元");
                    } else {
                        mTxtShipan.setText("0.00元");
                    }
                }
            }catch (Exception e) {
              e.printStackTrace();
            }
        }
    };



    /**
     * 填充详情相关数据
     */
    private void fillData() {
        mTopBar.setTitle(mCurrProInfo.title + mCurrProInfo.prono);
        if (Global.verifyLogin(K_DetailActivity.this)) {
//            mRelativeOrder.setVisibility(View.VISIBLE);
//            mLinearOrder.setVisibility(View.VISIBLE);
            if ("1".equals(mMoni)) {
                mTxtShipanDs.setText("模拟账户资金");
                mTxtShipan.setText(mCurrProInfo.amount + "元");
                mTxtShipan.setTextColor(getResources().getColor(R.color.white));
            } else {
                mTxtShipanDs.setText("账户实盘资金");
                mTxtShipan.setText(mCurrProInfo.amount + "元");
                mTxtShipan.setTextColor(getResources().getColor(R.color.white));
            }
        } else {
//            mLinearOrder.setVisibility(View.GONE);
//            mRelativeOrder.setVisibility(View.GONE);
            if ("1".equals(mMoni)) {
                mTxtShipanDs.setText("模拟账户资金");
            } else {
                mTxtShipanDs.setText("账户实盘资金");
            }
            if ("1".equals(mMoni)) {
                mTxtShipan.setText("未登录");
                mTxtShipan.setTextColor(getResources().getColor(R.color.transparent2));
            } else {
                mTxtShipan.setText("未登录");
                mTxtShipan.setTextColor(getResources().getColor(R.color.transparent2));
            }
        }
        //
        if ("1".equals(mCurrProInfo.zhibo)) {
//            mTxtChicangzhibo.setVisibility(View.VISIBLE);
        } else {
//            mTxtChicangzhibo.setVisibility(View.GONE);
        }

        if (Global.USERINFO == null) {
//            mSwitch.setChecked(false);
//            mTxtUnLogin.setVisibility(View.VISIBLE);
//            mTxtnums.setVisibility(View.GONE);
//            mRelativeZhiSun.setVisibility(View.GONE);

//            PYDO
//            mQuickSwitch.setState(false);
            mRelativeQuickTrade.setVisibility(View.GONE);



        } else {
//            mTxtUnLogin.setVisibility(View.GONE);
            // 根据orderset.status，设置极速下单开关的状态
            if ("1".equals(mCurrProInfo.orderset.status)) {
//                mSwitch.setChecked(true);
//                mTxtnums.setVisibility(View.VISIBLE);
//                mRelativeZhiSun.setVisibility(View.VISIBLE);
//            PYDO
                mQuickSwitch.setState(true);

                mRelativeQuickTrade.setVisibility(View.VISIBLE);

            } else {
//                mSwitch.setChecked(false);
//                mTxtnums.setVisibility(View.GONE);
//                mRelativeZhiSun.setVisibility(View.GONE);
//            PYDO
                mQuickSwitch.setState(false);

                mRelativeQuickTrade.setVisibility(View.GONE);
            }
        }
//            PYDO
        mTxtnums.setText(mCurrProInfo.orderset.nums);
        mTxtZhiSunNum.setText(mCurrProInfo.orderset.zhisun);
        mTxtZhiYingNum.setText(mCurrProInfo.orderset.zhiying);
    }

    /**
     * 关闭极速下单开关
     */
    private void closeOrderSet() {
        new ProDetailPresenter(this).closeOrderSet(mCurrProInfo.prono, mID, mMoni, "Android", new IBaseDataListener() {
            @Override
            public void onSuccess(BaseData data) {
//                mSwitch.setChecked(false);
//                mTxtnums.setVisibility(View.GONE);
//                mRelativeZhiSun.setVisibility(View.GONE);


                mRelativeQuickTrade.setVisibility(View.GONE);


                if (mSocketPresenter != null) {
                    mSocketPresenter.closeSocket();
                }
                if (mSocketOrderPresenter != null) {
                    mSocketOrderPresenter.closeSocket();
                }
                getProDetail();
            }

            @Override
            public void onFailure(BaseData data) {
//                mSwitch.setChecked(true);
 //                PYDO
//                mQuickSwitch.setSlideable(true);

            }

            @Override
            public void onError() {
//                mSwitch.setChecked(true);
//                PYDO
//                mQuickSwitch.setSlideable(true);

            }

        });
    }

    /**
     * 一键平仓
     */
    private void pingCangAll() {
        new ProDetailPresenter(this).pingCangAll(mCurrProInfo.wpno, mCurrProInfo.prono, mCurrProInfo.catid, mMoni, "Android", new IBaseDataListener() {
            @Override
            public void onSuccess(BaseData data) {
                new Coco3gBroadcastUtils(K_DetailActivity.this).sendBroadcast(Coco3gBroadcastUtils.RETURN_UPDATE_TRANSLIST_FLAG, null);
            }

            @Override
            public void onFailure(BaseData data) {

            }

            @Override
            public void onError() {

            }
        });
    }

    /**
     * 极速下单
     *
     * @param ordertype 0：买涨；1：买跌
     */
    private void quickOrder(String ordertype) {
        new ProDetailPresenter(this).quickOrder(mCurrProInfo.orderset.setid, mCurrProInfo.prono, mID, mMoni, ordertype, mCurrProInfo.nums,
                mCurrProInfo.type, "Android", new IBaseDataListener() {
                    @Override
                    public void onSuccess(BaseData data) {
//                        refreshCurrAllData();
                    }

                    @Override
                    public void onFailure(BaseData data) {

                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    /**
     * 刷新当前页，重新加载所有数据
     */
    private void refreshCurrAllData() {
        try {
            mSocketPresenter.closeSocket(); // 先关闭socket通道
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mSocketOrderPresenter.closeSocket(); // 先关闭socket通道
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getProDetail();
            }
        }, 1000);
    }

    @Override
    protected void onDestroy() {

        if (mSocketPresenter != null) {
            mSocketPresenter.closeSocket();
        }
        if (mSocketOrderPresenter != null) {
            mSocketOrderPresenter.closeSocket();
        }
        if (mCurrBoardCast != null) {
            mCurrBoardCast.unregisterBroadcast();
        }
        mWeb1.clearMemory();
        mWeb2.clearMemory();
        mWeb3.clearMemory();
        mWeb4.clearMemory();
        mWeb5.clearMemory();

        super.onDestroy();
    }
}
