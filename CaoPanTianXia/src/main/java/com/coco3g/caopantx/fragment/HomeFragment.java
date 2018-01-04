package com.coco3g.caopantx.fragment;



import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;

import com.coco3g.caopantx.activity.BaseFragment;
import com.coco3g.caopantx.activity.GuoJiActivity;
import com.coco3g.caopantx.activity.GuoNeiActivity;
import com.coco3g.caopantx.activity.MoNiTransActivity;
import com.coco3g.caopantx.activity.WebActivity;
import com.coco3g.caopantx.adapter.HomeAdapter;
import com.coco3g.caopantx.adapter.TransacationAdapter;
import com.coco3g.caopantx.bean.BannerListData;
import com.coco3g.caopantx.bean.BaseData;
import com.coco3g.caopantx.bean.ProGroupListDataBean;
import com.coco3g.caopantx.bean.TransListDataBean;
import com.coco3g.caopantx.data.DataUrl;
import com.coco3g.caopantx.listener.IProListListener;
import com.coco3g.caopantx.listener.ITransListListener;
import com.coco3g.caopantx.presenter.BannerListPresenter;
import com.coco3g.caopantx.presenter.ProListPresenter;
import com.coco3g.caopantx.R;
import com.coco3g.caopantx.activity.K_DetailActivity;
import com.coco3g.caopantx.bean.ProChildListDataBean;
import com.coco3g.caopantx.listener.IBannerListener;
import com.coco3g.caopantx.presenter.SocketRequestPresenter;
import com.coco3g.caopantx.presenter.TransPresenter;
import com.coco3g.caopantx.view.BannerView;
import com.coco3g.caopantx.view.ExpandableGridView;
import com.coco3g.caopantx.view.KViewWebView;
import com.coco3g.caopantx.view.MyTemplate;
import com.coco3g.caopantx.view.PagerSlidingTabStrip;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class HomeFragment extends BaseFragment{
    View view;
    LinearLayout mHeaderView;
    BannerView mHeaderBanner;
    MyTemplate mOutsideExit,mInsideExit;
    private int mCurrType = 15;//当前的期货类别， 15 国际期货  14 国内期货

    PullToRefreshScrollView mPullRefreshScrollView;
    //  ExpandableListView mBaseListView;

    HomeAdapter mAdapter;



    SocketRequestPresenter mSocketPresenter;
    private TransacationAdapter mGuoJiAdapter, mGuoNeiAdapter;


    private ExpandableGridView mAppGridView;
    private SimpleAdapter simpAdapter;

    private int[] mAppIcons = {R.mipmap.pic_home_micp_1, R.mipmap.pic_home_news_1,
            R.mipmap.pic_find_jfsc, R.mipmap.pic_home_xszy_1,
            R.mipmap.pic_home_radio,R.mipmap.pic_home_garuatee
    };
    private String[] mAppNames = {"模拟练习场", "财经资讯","今日头条","新手指导","财经直播","安全保障"};
    private String[] webView_url = new String[]{DataUrl.JINSHISHUJU,DataUrl.BASE_URL + "/content/index/news",DataUrl.JINSHISHUJU};


    //  PYDODO
    private PagerSlidingTabStrip tabPager;
    private ViewPager viewPager;
    private HomePagerAdapter mPagerAdapter;
    //  "业绩排名"
    private String[] title = new String[]{"财经资讯","今日头条"};
    private List<View> views = new ArrayList();

    private ArrayList<KViewWebView> mViewList = new ArrayList<>();



    //
    ArrayList<ProGroupListDataBean> mCurrAllData = new ArrayList<>();

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, null);

        mGuoJiAdapter = new TransacationAdapter(getActivity());
        mGuoNeiAdapter = new TransacationAdapter(getActivity());
        mGuoJiAdapter.setType(0);
        mGuoNeiAdapter.setType(0);

        //   mAdapter = new HomeAdapter(getActivity());

        //初始化之前就给发Socket数据


        initView();

        getBannerList();

        getGuoNeiProList();

        // 国际入口
        getGuojiTransList();
        // 国内入口
        getGuoneiTransList();
        return view;
    }


    private void initView() {
        LayoutInflater lay = LayoutInflater.from(getActivity());
//        xml加载头部视图
        //   mHeaderView = lay.inflate(R.layout.a_home_head_view_new, null);
        mHeaderView = (LinearLayout) view.findViewById(R.id.home_main);


        mHeaderBanner = (BannerView) mHeaderView.findViewById(R.id.view_banner_home_header);


        //获取menu菜单项
        mAppGridView = (ExpandableGridView) mHeaderView.findViewById(R.id.menu_gridView); // step1

//        mOutsideExit = (MyTemplate) mHeaderView.findViewById(R.id.menu_list_guoji);
        mInsideExit = (MyTemplate) mHeaderView.findViewById(R.id.menu_list_guonei);
        tabPager = (PagerSlidingTabStrip) mHeaderView.findViewById(R.id.home_tabPager);
        viewPager = (ViewPager) mHeaderView.findViewById(R.id.home_webViewPager);

        // PYTODO   下面的View点击
        mPullRefreshScrollView = (PullToRefreshScrollView) view.findViewById(R.id.home_refresh);


        for (int i = 0;i<title.length;i++) {
            KViewWebView  webView = new KViewWebView(getActivity());
            webView.loadUrl(webView_url[i]);
            mViewList.add(webView);
        }

        mPagerAdapter = new HomePagerAdapter();


        viewPager.setAdapter(mPagerAdapter);
        tabPager.setViewPager(viewPager);




        //初始化数据 step2
        List<Map<String,Object>> listItems = new ArrayList<>();

        for (int i=0; i<mAppIcons.length; i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("icon", mAppIcons[i]);
            listItem.put("name", mAppNames[i]);
            listItems.add(listItem);
        }

        //创建simpleAdpater  step3

        simpAdapter = new SimpleAdapter(getActivity(),
                listItems,
                R.layout.menu_gridview_item,
                new String[]{"icon", "name"},
                new int[]{R.id.icon_img, R.id.name_tv});

        mAppGridView.setAdapter(simpAdapter);

        //添加列表项监听器
        mAppGridView.setOnItemClickListener(menuClickListenr);


        mInsideExit.setOnClickListener(new MyTemplate.OnClickListener() {
            @Override
            public void onMoreClick(View view) {

                Intent intent = new Intent(getActivity(), GuoNeiActivity.class);
                startActivity(intent);

            }

            @Override
            public void onOneClick(View view) {
                Intent intent = new Intent(getActivity(), K_DetailActivity.class);
                intent.putExtra("id", mCurrAllData.get(0).groupList.get(0).tid);
                intent.putExtra("device", "Android");
                intent.putExtra("moni", "0");
                startActivity(intent);

            }

            @Override
            public void onTwoClick(View view) {
                Intent intent = new Intent(getActivity(), K_DetailActivity.class);
                intent.putExtra("id", mCurrAllData.get(0).groupList.get(1).tid);
                intent.putExtra("device", "Android");
                intent.putExtra("moni", "0");
                startActivity(intent);

            }

            @Override
            public void onThreeClick(View view) {
                Intent intent = new Intent(getActivity(), K_DetailActivity.class);
                intent.putExtra("id", mCurrAllData.get(0).groupList.get(2).tid);
                intent.putExtra("device", "Android");
                intent.putExtra("moni", "0");
                startActivity(intent);

            }
        });

//        mOutsideExit.setOnClickListener(new MyTemplate.OnClickListener() {
//            @Override
//            public void onMoreClick(View view) {
//
//                Intent intent = new Intent(getActivity(), GuoJiActivity.class);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onOneClick(View view) {
//                Intent intent = new Intent(getActivity(), K_DetailActivity.class);
//                intent.putExtra("id", mCurrAllData.get(1).groupList.get(0).tid);
//                intent.putExtra("device", "Android");
//                intent.putExtra("moni", "0");
//                startActivity(intent);
//
//            }
//
//            @Override
//            public void onTwoClick(View view) {
//                Intent intent = new Intent(getActivity(), K_DetailActivity.class);
//                intent.putExtra("id", mCurrAllData.get(1).groupList.get(1).tid);
//                intent.putExtra("device", "Android");
//                intent.putExtra("moni", "0");
//                startActivity(intent);
//
//            }
//
//            @Override
//            public void onThreeClick(View view) {
//                Intent intent = new Intent(getActivity(), K_DetailActivity.class);
//                intent.putExtra("id", mCurrAllData.get(1).groupList.get(2).tid);
//                intent.putExtra("device", "Android");
//                intent.putExtra("moni", "0");
//                startActivity(intent);
//
//            }
//        });


        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                // 刷新异步处理
                new GetDataTask().execute();
            }
        });
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(3000);

                if (mSocketPresenter != null) {
                    mSocketPresenter.closeSocket();
                    mSocketPresenter = null;
                }
                if (mSocketPresenter == null) {
                    // 获取交易列表实时数据
                    mSocketPresenter = new SocketRequestPresenter(getActivity());
                    getTransListSocketData();
                }

            } catch (InterruptedException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            // Do some stuff here

            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshScrollView.onRefreshComplete();

            super.onPostExecute(result);
        }
    }

    public class HomePagerAdapter extends PagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

        private final int[] ICONS = {R.mipmap.icon_list_01, R.mipmap.icon_list_02,
                R.mipmap.icon_list_03};


        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }


        @Override
        public int getPageIconResId(int position) {
            return ICONS[position];
        }


        @Override
        public int getCount() {
            return mViewList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));
            return mViewList.get(position) != null ? mViewList.get(position) : null;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;

        }
    }

    /**
     * 头部菜单点击事件
     */
    private AdapterView.OnItemClickListener menuClickListenr =new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent;
            switch (i) {

                case 0:
                    intent = new Intent(getActivity(), MoNiTransActivity.class);
                    startActivity(intent);
                    break;

                case 1:
                    intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("url", DataUrl.BASE_URL + "/content/index/news");
                    startActivity(intent);
                    break;

                case 2:
                    intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("url", DataUrl.JINSHISHUJU );
                    startActivity(intent);
                    break;

                case 3:
                    intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("url", DataUrl.BASE_URL + "/content/index/lists/catid/3");
                    startActivity(intent);
                    break;

                case 4:
                    intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("url", DataUrl.BASE_URL + "/Member/index/zhibo_list");
                    startActivity(intent);
                    break;

                case 5:
                    intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("url", DataUrl.BASE_URL + "/Home/index/guarantee");
                    startActivity(intent);
                    break;

            }
        }

    };

    /**
     * 获取banner
     */
    private void getBannerList() {
        new BannerListPresenter(getActivity()).getBannerList("16", "Android", new IBannerListener() {
            @Override
            public void onSuccess(BannerListData data) {
                ArrayList<BannerListData.Banner> piclist = data.response;
                if (piclist != null && piclist.size() > 0) {
                    mHeaderBanner.setList(piclist);
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
     * 获取国际期货列表
     */
    private void getGuojiTransList() {
        new TransPresenter(getActivity()).getTransList( "15", "Android", "0", new ITransListListener() {
            @Override
            public void onSuccess(TransListDataBean data) {
                mGuoJiAdapter.clearList();

                mGuoJiAdapter.setList(data.response);

                if (mSocketPresenter != null) {
                    mSocketPresenter.closeSocket();
                    mSocketPresenter = null;
                }
                if (mSocketPresenter == null) {
                    // 获取交易列表实时数据
                    mSocketPresenter = new SocketRequestPresenter(getActivity());
                    getTransListSocketData();
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


    private void getGuoneiTransList() {
        new TransPresenter(getActivity()).getTransList( "14", "Android", "0", new ITransListListener() {
            @Override
            public void onSuccess(TransListDataBean data) {

                mGuoNeiAdapter.clearList();


//                if (mCurrType == 15) {
//                    mGuoJiAdapter.setList(data.response);
//
//                } else if (mCurrType == 14) {
                mGuoNeiAdapter.setList(data.response);

//                }

                if (mSocketPresenter != null) {
                    mSocketPresenter.closeSocket();
                    mSocketPresenter = null;
                }
                if (mSocketPresenter == null) {
                    // 获取交易列表实时数据
                    mSocketPresenter = new SocketRequestPresenter(getActivity());
                    getTransListSocketData();
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
     * Socket数据获取
     */
    private void getTransListSocketData() {
        mSocketPresenter.transList("{\"command\":\"binduid\",\"uid\":\"get_hangqing\"}", new ITransListListener() {
            @Override
            public void onSuccess(TransListDataBean data) {
//                ArrayList<TransListDataBean.TransData> list = new ArrayList<TransListDataBean.TransData>(data.data);
                ArrayList<TransListDataBean.TransData> list = data.data;


//                if (mGuoJiAdapter != null && mGuoJiAdapter.getList() != null && mGuoJiAdapter.getList().size() > 0) {
//                    ArrayList<TransListDataBean.TransData> oldlist = mGuoJiAdapter.getList();
//                    ArrayList<TransListDataBean.TransData> newlist = new ArrayList<>();
//                    for (int i = oldlist.size()-1; i >= 0; i--) {
//                        TransListDataBean.TransData olditemdata = oldlist.get(i);
//                        String prono = olditemdata.prono;
//                        for (int j = 0; j < list.size(); j++) {
//                            TransListDataBean.TransData itemdata = list.get(j);
//                            if (prono.equalsIgnoreCase(itemdata.prono)) {
//                                itemdata.title = olditemdata.title;
//                                itemdata.tid = olditemdata.tid;
//                                itemdata.nums = olditemdata.nums;
//                                itemdata.zhang = olditemdata.zhang;
//                                itemdata.die = olditemdata.die;
//                                itemdata.order_nums = olditemdata.order_nums;
//                                newlist.add(itemdata);
//                                break;
//                            }
//                        }
//
//                        Message message = new Message();
//                        message.obj = newlist;
//                        mHandlerRefreshView.sendMessage(message);
//                    }
//                }

                if (mGuoNeiAdapter != null && mGuoNeiAdapter.getList() != null && mGuoNeiAdapter.getList().size() > 0) {
                    ArrayList<TransListDataBean.TransData> oldlist = mGuoNeiAdapter.getList();
                    ArrayList<TransListDataBean.TransData> newlist = new ArrayList<>();
                    for (int i = oldlist.size()-1; i >= 0; i--) {
                        TransListDataBean.TransData olditemdata = oldlist.get(i);
                        String prono = olditemdata.prono;
                        for (int j = 0; j < list.size(); j++) {
                            TransListDataBean.TransData itemdata = list.get(j);
                            if (prono.equalsIgnoreCase(itemdata.prono)) {
                                itemdata.title = olditemdata.title;
                                itemdata.tid = olditemdata.tid;
                                itemdata.nums = olditemdata.nums;
                                itemdata.zhang = olditemdata.zhang;
                                itemdata.die = olditemdata.die;
                                itemdata.order_nums = olditemdata.order_nums;
                                newlist.add(itemdata);
                                break;
                            }
                        }
//                        }
                        Message message = new Message();
                        message.obj = newlist;
                        mHandlerRefreshView1.sendMessage(message);
                    }
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




//    /**
//     * 获取实时行情后，刷新相关view
//     */
//
//    Handler mHandlerRefreshView = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//
//            ArrayList<TransListDataBean.TransData> m = (ArrayList<TransListDataBean.TransData>) msg.obj;
//
//
//
//            if (m != null && m.size() >= 3) {
//                TransListDataBean.TransData itemdata = m.get(0);
//
//
//
//                mOutsideExit.setOneHeadText(itemdata.title);
//                mOutsideExit.setOneCenterText(itemdata.lastprice);
//                mOutsideExit.setOneFootText(itemdata.perprice);
//
//                float rate = Float.parseFloat(itemdata.perprice);
//                if (rate < 0){
//                    mOutsideExit.setOneCenterColor(R.color.green);
//                    mOutsideExit.setOneFootColor(R.color.green);
//                }else {
//                    mOutsideExit.setOneCenterColor(R.color.red);
//                    mOutsideExit.setOneFootColor(R.color.red);
//                }
//
//
//                TransListDataBean.TransData itemdataTwo = m.get(1);
//                mOutsideExit.setTwoHeadText(itemdataTwo.title);
//                mOutsideExit.setTwoCenterText(itemdataTwo.lastprice);
//                mOutsideExit.setTwoFootText(itemdataTwo.perprice);
//
//                float rate1 = Float.parseFloat(itemdataTwo.perprice);
//                if (rate1 < 0){
//                    mOutsideExit.setTwoCenterColor(R.color.green);
//                    mOutsideExit.setTwoFootColor(R.color.green);
//                }else {
//                    mOutsideExit.setTwoCenterColor(R.color.red);
//                    mOutsideExit.setTwoFootColor(R.color.red);
//                }
//
//
//                TransListDataBean.TransData itemdataThr = m.get(2);
//                mOutsideExit.setThreeHeadText(itemdataThr.title);
//                mOutsideExit.setThreeCenterText(itemdataThr.lastprice);
//                mOutsideExit.setThreeFootText(itemdataThr.perprice);
//
//                float rate2 = Float.parseFloat(itemdataThr.perprice);
//                if (rate2 < 0){
//                    mOutsideExit.setThreeCenterColor(R.color.green);
//                    mOutsideExit.setThreeFootColor(R.color.green);
//                }else {
//                    mOutsideExit.setThreeCenterColor(R.color.red);
//                    mOutsideExit.setThreeFootColor(R.color.red);
//                }
//
//            }
//
//        }
//    };
//

    Handler mHandlerRefreshView1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ArrayList<TransListDataBean.TransData> m = (ArrayList<TransListDataBean.TransData>) msg.obj;

            if (m != null && m.size() >= 3){
                TransListDataBean.TransData itemdata =  m.get(0);
                mInsideExit.setOneHeadText(itemdata.title);
                mInsideExit.setOneCenterText(itemdata.lastprice);
                mInsideExit.setOneFootText(itemdata.perprice);
                float rate = Float.parseFloat(itemdata.perprice);
                if (rate < 0){
                    mInsideExit.setOneCenterColor(R.color.green);
                    mInsideExit.setOneFootColor(R.color.green);
                }else {
                    mInsideExit.setOneCenterColor(R.color.red);
                    mInsideExit.setOneFootColor(R.color.red);
                }


                TransListDataBean.TransData itemdataTwo =  m.get(1);
                mInsideExit.setTwoHeadText(itemdataTwo.title);
                mInsideExit.setTwoCenterText(itemdataTwo.lastprice);
                mInsideExit.setTwoFootText(itemdataTwo.perprice);

                float rate1 = Float.parseFloat(itemdataTwo.perprice);
                if (rate1 < 0){
                    mInsideExit.setTwoCenterColor(R.color.green);
                    mInsideExit.setTwoFootColor(R.color.green);
                }else {
                    mInsideExit.setTwoCenterColor(R.color.red);
                    mInsideExit.setTwoFootColor(R.color.red);
                }

                TransListDataBean.TransData itemdataThr =  m.get(2);
                mInsideExit.setThreeHeadText(itemdataThr.title);
                mInsideExit.setThreeCenterText(itemdataThr.lastprice);
                mInsideExit.setThreeFootText(itemdataThr.perprice);

                float rate2 = Float.parseFloat(itemdataThr.perprice);
                if (rate2 < 0){
                    mInsideExit.setThreeCenterColor(R.color.green);
                    mInsideExit.setThreeFootColor(R.color.green);
                }else {
                    mInsideExit.setThreeCenterColor(R.color.red);
                    mInsideExit.setThreeFootColor(R.color.red);
                }
            }
        }
    };



    //    /**
//     * 获取内盘产品列表数据
//     */
    private void getGuoNeiProList() {
        new ProListPresenter(getActivity()).getProList("14", "Android", new IProListListener() {
            @Override
            public void onSuccess(ProChildListDataBean data) {

                ProGroupListDataBean guoneiItemdata = new ProGroupListDataBean();
                guoneiItemdata.groupList = data.response;
                mCurrAllData.add(guoneiItemdata);
                getGuoJiProList();

            }

            @Override
            public void onFailure(BaseData data) {

            }

            @Override
            public void onError() {

            }
        });
    }

    private void getGuoJiProList() {
        new ProListPresenter(getActivity()).getProList("15", "Android", new IProListListener() {
            @Override
            public void onSuccess(ProChildListDataBean data) {

                ProGroupListDataBean guoneiItemdata = new ProGroupListDataBean();
                guoneiItemdata.groupList = data.response;
                mCurrAllData.add(guoneiItemdata);

            }

            @Override
            public void onFailure(BaseData data) {

            }

            @Override
            public void onError() {

            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSocketPresenter != null) {
            mSocketPresenter.closeSocket();
        }
    }
}




/*Last Version*/

//public class HomeFragment extends BaseFragment{
//    View view;
//    View mHeaderView;
//    BannerView mHeaderBanner;
//    MyTemplate mOutsideExit,mInsideExit;
//    private int mCurrType = 15;//当前的期货类别， 15 国际期货  14 国内期货
//
//    PullToRefreshExpandableListView mListView;
//    ExpandableListView mBaseListView;
//    HomeAdapter mAdapter;
//
//
//    SocketRequestPresenter mSocketPresenter;
//    private TransacationAdapter mGuoJiAdapter, mGuoNeiAdapter;
//
//
//    private ExpandableGridView mAppGridView;
//    private SimpleAdapter simpAdapter;
//
//    private int[] mAppIcons = {R.mipmap.pic_home_micp_1, R.mipmap.pic_home_news_1,
//                                 R.mipmap.pic_find_jfsc, R.mipmap.pic_home_xszy_1,
//                                 R.mipmap.pic_home_radio,R.mipmap.pic_home_garuatee
//    };
//    private String[] mAppNames = {"模拟练习场", "财经资讯","今日头条","新手指导","财经直播","安全保障"};
//
//    //
//    ArrayList<ProGroupListDataBean> mCurrAllData = new ArrayList<>();
//
//    /**
//     * @param inflater
//     * @param container
//     * @param savedInstanceState
//     * @return
//     */
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment_home, null);
//
//        mGuoJiAdapter = new TransacationAdapter(getActivity());
//        mGuoNeiAdapter = new TransacationAdapter(getActivity());
//        mGuoJiAdapter.setType(0);
//        mGuoNeiAdapter.setType(0);
//
//        mAdapter = new HomeAdapter(getActivity());
//
//        initView();
//
//        getBannerList();
//
//        getGuoNeiProList();
//
//        getGuojiTransList();
//
//        getGuoneiTransList();
//
//        return view;
//    }
//
//
//    private void initView() {
//        LayoutInflater lay = LayoutInflater.from(getActivity());
////        xml加载头部视图
//        mHeaderView = lay. inflate(R.layout.a_home_head_view_new, null);
//
//        mHeaderBanner = (BannerView) mHeaderView.findViewById(R.id.view_banner_home_header);
//
//
//        //获取menu菜单项
//        mAppGridView = (ExpandableGridView) mHeaderView.findViewById(R.id.menu_gridView); // step1
//
//        //初始化数据 step2
//        List<Map<String,Object>> listItems = new ArrayList<>();
//
//        for (int i=0; i<mAppIcons.length; i++) {
//            Map<String, Object> listItem = new HashMap<String, Object>();
//            listItem.put("icon", mAppIcons[i]);
//            listItem.put("name", mAppNames[i]);
//            listItems.add(listItem);
//        }
//
//        //创建simpleAdpater  step3
//
//        simpAdapter = new SimpleAdapter(getActivity(),
//                listItems,
//                R.layout.menu_gridview_item,
//                new String[]{"icon", "name"},
//                new int[]{R.id.icon_img, R.id.name_tv});
//
//        mAppGridView.setAdapter(simpAdapter);
//
//        //添加列表项监听器
//        mAppGridView.setOnItemClickListener(menuClickListenr);
//
//
//        mOutsideExit = (MyTemplate) mHeaderView.findViewById(R.id.menu_list_guoji);
//        mInsideExit = (MyTemplate) mHeaderView.findViewById(R.id.menu_list_guonei);
//
//
//        // PYTODO   下面的View点击
//        mListView = (PullToRefreshExpandableListView) view.findViewById(R.id.listview_home);
//
//        mBaseListView = mListView.getRefreshableView();
//
//        mBaseListView.addHeaderView(mHeaderView);
//
//        mBaseListView.setGroupIndicator(null);
//
//        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
//
//
//
//        mInsideExit.setOnClickListener(new MyTemplate.OnClickListener() {
//            @Override
//            public void onMoreClick(View view) {
//
//                Intent intent = new Intent(getActivity(), GuoNeiActivity.class);
//                startActivity(intent);
//
//            }
//
//            @Override
//            public void onOneClick(View view) {
//                Intent intent = new Intent(getActivity(), K_DetailActivity.class);
//                intent.putExtra("id", mCurrAllData.get(0).groupList.get(0).tid);
//                intent.putExtra("device", "Android");
//                intent.putExtra("moni", "0");
//                startActivity(intent);
//
//            }
//
//            @Override
//            public void onTwoClick(View view) {
//                Intent intent = new Intent(getActivity(), K_DetailActivity.class);
//                intent.putExtra("id", mCurrAllData.get(0).groupList.get(1).tid);
//                intent.putExtra("device", "Android");
//                intent.putExtra("moni", "0");
//                startActivity(intent);
//
//            }
//
//            @Override
//            public void onThreeClick(View view) {
//                Intent intent = new Intent(getActivity(), K_DetailActivity.class);
//                intent.putExtra("id", mCurrAllData.get(0).groupList.get(2).tid);
//                intent.putExtra("device", "Android");
//                intent.putExtra("moni", "0");
//                startActivity(intent);
//
//            }
//        });
//
//        mOutsideExit.setOnClickListener(new MyTemplate.OnClickListener() {
//            @Override
//            public void onMoreClick(View view) {
//
//                Intent intent = new Intent(getActivity(), GuoJiActivity.class);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onOneClick(View view) {
//                Intent intent = new Intent(getActivity(), K_DetailActivity.class);
//                intent.putExtra("id", mCurrAllData.get(1).groupList.get(0).tid);
//                intent.putExtra("device", "Android");
//                intent.putExtra("moni", "0");
//                startActivity(intent);
//
//            }
//
//            @Override
//            public void onTwoClick(View view) {
//                Intent intent = new Intent(getActivity(), K_DetailActivity.class);
//                intent.putExtra("id", mCurrAllData.get(1).groupList.get(1).tid);
//                intent.putExtra("device", "Android");
//                intent.putExtra("moni", "0");
//                startActivity(intent);
//
//            }
//
//            @Override
//            public void onThreeClick(View view) {
//                Intent intent = new Intent(getActivity(), K_DetailActivity.class);
//                intent.putExtra("id", mCurrAllData.get(1).groupList.get(2).tid);
//                intent.putExtra("device", "Android");
//                intent.putExtra("moni", "0");
//                startActivity(intent);
//
//            }
//        });
//
//
//
//
//
//
//
//        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ExpandableListView>() {
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
//                mAdapter.clearList();
//                getGuoNeiProList();
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
//
//            }
//        });
//        //
//        mBaseListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                return true;
//            }
//        });
//        mBaseListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                Intent intent = new Intent(getActivity(), K_DetailActivity.class);
//                intent.putExtra("id", mCurrAllData.get(groupPosition).groupList.get(childPosition).tid);
//                intent.putExtra("device", "Android");
//                intent.putExtra("moni", "0");
//                startActivity(intent);
//                return true;
//            }
//        });
//
//
//
//    }
//
//
//    /**
//     * 头部菜单点击事件
//     */
//    private AdapterView.OnItemClickListener menuClickListenr =new AdapterView.OnItemClickListener() {
//        @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent;
//                switch (i) {
//
//                    case 0:
//                        intent = new Intent(getActivity(), MoNiTransActivity.class);
//                        startActivity(intent);
//                        break;
//
//                    case 1:
//                        intent = new Intent(getActivity(), WebActivity.class);
//                        intent.putExtra("url", DataUrl.BASE_URL + "/content/index/news");
//                        startActivity(intent);
//                        break;
//
//                    case 2:
//                        intent = new Intent(getActivity(), WebActivity.class);
//                        intent.putExtra("url", DataUrl.JINSHISHUJU );
//                        startActivity(intent);
//                        break;
//
//                    case 3:
//                        intent = new Intent(getActivity(), WebActivity.class);
//                        intent.putExtra("url", DataUrl.BASE_URL + "/content/index/lists/catid/3");
//                        startActivity(intent);
//                        break;
//
//                    case 4:
//                        intent = new Intent(getActivity(), WebActivity.class);
//                        intent.putExtra("url", DataUrl.BASE_URL + "/Member/index/zhibo_list");
//                        startActivity(intent);
//                        break;
//
//                    case 5:
//                        intent = new Intent(getActivity(), WebActivity.class);
//                        intent.putExtra("url", DataUrl.BASE_URL + "/Home/index/guarantee");
//                        startActivity(intent);
//                        break;
//
//                }
//            }
//
//    };
//
//    /**
//     * 获取banner
//     */
//    private void getBannerList() {
//        new BannerListPresenter(getActivity()).getBannerList("16", "Android", new IBannerListener() {
//            @Override
//            public void onSuccess(BannerListData data) {
//                ArrayList<BannerListData.Banner> piclist = data.response;
//                if (piclist != null && piclist.size() > 0) {
//                    mHeaderBanner.setList(piclist);
//                }
//            }
//
//            @Override
//            public void onFailure(BaseData data) {
//
//            }
//
//            @Override
//            public void onError() {
//
//            }
//        });
//    }
//
//
//
//
//    /**
//     * 获取国际期货列表
//     */
//    private void getGuojiTransList() {
//        new TransPresenter(getActivity()).getTransList( "15", "Android", "0", new ITransListListener() {
//            @Override
//            public void onSuccess(TransListDataBean data) {
//                mGuoJiAdapter.clearList();
//
//                mGuoJiAdapter.setList(data.response);
//
//                if (mSocketPresenter != null) {
//                    mSocketPresenter.closeSocket();
//                    mSocketPresenter = null;
//                }
//                if (mSocketPresenter == null) {
//                    // 获取交易列表实时数据
//                    mSocketPresenter = new SocketRequestPresenter(getActivity());
//                    getTransListSocketData();
//                }
//
//            }
//
//            @Override
//            public void onFailure(BaseData data) {
//
//            }
//
//            @Override
//            public void onError() {
//
//            }
//        });
//    }
//
//
//    private void getGuoneiTransList() {
//        new TransPresenter(getActivity()).getTransList( "14", "Android", "0", new ITransListListener() {
//            @Override
//            public void onSuccess(TransListDataBean data) {
//
//                mGuoNeiAdapter.clearList();
//
//
////                if (mCurrType == 15) {
////                    mGuoJiAdapter.setList(data.response);
////
////                } else if (mCurrType == 14) {
//                    mGuoNeiAdapter.setList(data.response);
//
////                }
//
//                if (mSocketPresenter != null) {
//                    mSocketPresenter.closeSocket();
//                    mSocketPresenter = null;
//                }
//                if (mSocketPresenter == null) {
//                    // 获取交易列表实时数据
//                    mSocketPresenter = new SocketRequestPresenter(getActivity());
//                    getTransListSocketData();
//                }
//
//            }
//
//            @Override
//            public void onFailure(BaseData data) {
//
//            }
//
//            @Override
//            public void onError() {
//
//            }
//        });
//    }
//
//
//
//    private void getTransListSocketData() {
//        mSocketPresenter.transList("{\"command\":\"binduid\",\"uid\":\"get_hangqing\"}", new ITransListListener() {
//            @Override
//            public void onSuccess(TransListDataBean data) {
////                ArrayList<TransListDataBean.TransData> list = new ArrayList<TransListDataBean.TransData>(data.data);
//                ArrayList<TransListDataBean.TransData> list = data.data;
//
////                if (mCurrType == 15) {
//                    if (mGuoJiAdapter != null && mGuoJiAdapter.getList() != null && mGuoJiAdapter.getList().size() > 0) {
//                        ArrayList<TransListDataBean.TransData> oldlist = mGuoJiAdapter.getList();
//                        ArrayList<TransListDataBean.TransData> newlist = new ArrayList<>();
//                        for (int i = oldlist.size()-1; i >= 0; i--) {
//                            TransListDataBean.TransData olditemdata = oldlist.get(i);
//                            String prono = olditemdata.prono;
//                            for (int j = 0; j < list.size(); j++) {
//                                TransListDataBean.TransData itemdata = list.get(j);
//                                if (prono.equalsIgnoreCase(itemdata.prono)) {
//                                    itemdata.title = olditemdata.title;
//                                    itemdata.tid = olditemdata.tid;
//                                    itemdata.nums = olditemdata.nums;
//                                    itemdata.zhang = olditemdata.zhang;
//                                    itemdata.die = olditemdata.die;
//                                    itemdata.order_nums = olditemdata.order_nums;
//                                    newlist.add(itemdata);
//                                    break;
//                                }
//                            }
////                        }
//                        Message message = new Message();
//                        message.obj = newlist;
//                        mHandlerRefreshView.sendMessage(message);
//                    }
//                }
////                else if (mCurrType == 14) {
//                    if (mGuoNeiAdapter != null && mGuoNeiAdapter.getList() != null && mGuoNeiAdapter.getList().size() > 0) {
//                        ArrayList<TransListDataBean.TransData> oldlist = mGuoNeiAdapter.getList();
//                        ArrayList<TransListDataBean.TransData> newlist = new ArrayList<>();
//                        for (int i = oldlist.size()-1; i >= 0; i--) {
//                            TransListDataBean.TransData olditemdata = oldlist.get(i);
//                            String prono = olditemdata.prono;
//                            for (int j = 0; j < list.size(); j++) {
//                                TransListDataBean.TransData itemdata = list.get(j);
//                                if (prono.equalsIgnoreCase(itemdata.prono)) {
//                                    itemdata.title = olditemdata.title;
//                                    itemdata.tid = olditemdata.tid;
//                                    itemdata.nums = olditemdata.nums;
//                                    itemdata.zhang = olditemdata.zhang;
//                                    itemdata.die = olditemdata.die;
//                                    itemdata.order_nums = olditemdata.order_nums;
//                                    newlist.add(itemdata);
//                                    break;
//                                }
//                            }
////                        }
//                        Message message = new Message();
//                        message.obj = newlist;
//                        mHandlerRefreshView1.sendMessage(message);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(BaseData data) {
//
//            }
//
//            @Override
//            public void onError() {
//
//            }
//        });
//    }
//
//
//
//
//    /**
//     * 获取实时行情后，刷新相关view
//     */
//
//    Handler mHandlerRefreshView = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//
//            ArrayList<TransListDataBean.TransData> m = (ArrayList<TransListDataBean.TransData>) msg.obj;
//
//
//
//            if (m != null && m.size() >= 3) {
//                TransListDataBean.TransData itemdata = m.get(0);
//
//
//
//                mOutsideExit.setOneHeadText(itemdata.title);
//                mOutsideExit.setOneCenterText(itemdata.lastprice);
//                mOutsideExit.setOneFootText(itemdata.perprice);
//
//                float rate = Float.parseFloat(itemdata.perprice);
//                if (rate < 0){
//                    mOutsideExit.setOneCenterColor(R.color.green);
//                    mOutsideExit.setOneFootColor(R.color.green);
//                }else {
//                    mOutsideExit.setOneCenterColor(R.color.red);
//                    mOutsideExit.setOneFootColor(R.color.red);
//                }
//
//
//                TransListDataBean.TransData itemdataTwo = m.get(1);
//                mOutsideExit.setTwoHeadText(itemdataTwo.title);
//                mOutsideExit.setTwoCenterText(itemdataTwo.lastprice);
//                mOutsideExit.setTwoFootText(itemdataTwo.perprice);
//
//                float rate1 = Float.parseFloat(itemdataTwo.perprice);
//                if (rate1 < 0){
//                    mOutsideExit.setTwoCenterColor(R.color.green);
//                    mOutsideExit.setTwoFootColor(R.color.green);
//                }else {
//                    mOutsideExit.setTwoCenterColor(R.color.red);
//                    mOutsideExit.setTwoFootColor(R.color.red);
//                }
//
//
//                TransListDataBean.TransData itemdataThr = m.get(2);
//                mOutsideExit.setThreeHeadText(itemdataThr.title);
//                mOutsideExit.setThreeCenterText(itemdataThr.lastprice);
//                mOutsideExit.setThreeFootText(itemdataThr.perprice);
//
//                float rate2 = Float.parseFloat(itemdataThr.perprice);
//                if (rate2 < 0){
//                    mOutsideExit.setThreeCenterColor(R.color.green);
//                    mOutsideExit.setThreeFootColor(R.color.green);
//                }else {
//                    mOutsideExit.setThreeCenterColor(R.color.red);
//                    mOutsideExit.setThreeFootColor(R.color.red);
//                }
//
//            }
//
//        }
//    };
//
//
//    Handler mHandlerRefreshView1 = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//
//            ArrayList<TransListDataBean.TransData> m = (ArrayList<TransListDataBean.TransData>) msg.obj;
//
//            if (m != null && m.size() >= 3){
//                TransListDataBean.TransData itemdata =  m.get(0);
//                mInsideExit.setOneHeadText(itemdata.title);
//                mInsideExit.setOneCenterText(itemdata.lastprice);
//                mInsideExit.setOneFootText(itemdata.perprice);
//                float rate = Float.parseFloat(itemdata.perprice);
//                if (rate < 0){
//                    mInsideExit.setOneCenterColor(R.color.green);
//                    mInsideExit.setOneFootColor(R.color.green);
//                }else {
//                    mInsideExit.setOneCenterColor(R.color.red);
//                    mInsideExit.setOneFootColor(R.color.red);
//                }
//
//
//                TransListDataBean.TransData itemdataTwo =  m.get(1);
//                mInsideExit.setTwoHeadText(itemdataTwo.title);
//                mInsideExit.setTwoCenterText(itemdataTwo.lastprice);
//                mInsideExit.setTwoFootText(itemdataTwo.perprice);
//
//                float rate1 = Float.parseFloat(itemdataTwo.perprice);
//                if (rate1 < 0){
//                    mInsideExit.setTwoCenterColor(R.color.green);
//                    mInsideExit.setTwoFootColor(R.color.green);
//                }else {
//                    mInsideExit.setTwoCenterColor(R.color.red);
//                    mInsideExit.setTwoFootColor(R.color.red);
//                }
//
//                TransListDataBean.TransData itemdataThr =  m.get(2);
//                mInsideExit.setThreeHeadText(itemdataThr.title);
//                mInsideExit.setThreeCenterText(itemdataThr.lastprice);
//                mInsideExit.setThreeFootText(itemdataThr.perprice);
//
//                float rate2 = Float.parseFloat(itemdataThr.perprice);
//                if (rate2 < 0){
//                    mInsideExit.setThreeCenterColor(R.color.green);
//                    mInsideExit.setThreeFootColor(R.color.green);
//                }else {
//                    mInsideExit.setThreeCenterColor(R.color.red);
//                    mInsideExit.setThreeFootColor(R.color.red);
//                }
//            }
//        }
//    };
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (mSocketPresenter != null) {
//            mSocketPresenter.closeSocket();
//        }
//    }
//
//
//    /**
//     * 获取内盘产品列表数据
//     */
//    private void getGuoNeiProList() {
//        new ProListPresenter(getActivity()).getProList("14", "Android", new IProListListener() {
//            @Override
//            public void onSuccess(ProChildListDataBean data) {
//                mListView.onRefreshComplete();
//                ProGroupListDataBean guoneiItemdata = new ProGroupListDataBean();
//                guoneiItemdata.name = "国内期货推荐";
//                guoneiItemdata.groupList = data.response;
//                mCurrAllData.add(guoneiItemdata);
////                //PYDO 添加
////                mAdapter.setList(mCurrAllData);
////                mBaseListView.setAdapter(mAdapter);
////                for (int i = 0; i < mCurrAllData.size(); i++) {
////                    mBaseListView.expandGroup(i);
////                }
//
//                getGuoJiProList();
//            }
//            @Override
//            public void onFailure(BaseData data) {
//                mListView.onRefreshComplete();
//            }
//
//            @Override
//            public void onError() {
//                mListView.onRefreshComplete();
//            }
//        });
//    }
//
//
//    /**
//     * 获取外盘产品列表数据
//     */
//    private void getGuoJiProList() {
//        new ProListPresenter(getActivity()).getProList("15", "Android", new IProListListener() {
//            @Override
//            public void onSuccess(ProChildListDataBean data) {
//                ProGroupListDataBean guojiItemdata = new ProGroupListDataBean();
//                guojiItemdata.name = "国际期货推荐";
//                guojiItemdata.groupList = data.response;
//                mCurrAllData.add(guojiItemdata);
//                mAdapter.setList(mCurrAllData);
//                mBaseListView.setAdapter(mAdapter);
//                //
//                for (int i = 0; i < mCurrAllData.size(); i++) {
//                    mBaseListView.expandGroup(i);
//                }
//            }
//
//            @Override
//            public void onFailure(BaseData data) {
//
//            }
//
//            @Override
//            public void onError() {
//
//            }
//        });
//    }
//}
