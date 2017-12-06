package com.coco3g.caopantx.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.coco3g.caopantx.activity.BaseFragment;
import com.coco3g.caopantx.activity.GuoJiActivity;
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
import com.coco3g.caopantx.view.BannerView;
import com.coco3g.caopantx.view.ExpandableGridView;
import com.coco3g.caopantx.view.MyTemplate;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
// * Created by MIN on 16/6/13.
// */
//public class HomeFragment extends BaseFragment implements View.OnClickListener {
//    View view;
//    View mHeaderView;
//    LinearLayout mLinearHeaderMenu;
//    BannerView mHeaderBanner;
//    TextView mTxtMoni, mTxtNews, mTxtFinance, mTxtHelp, mTxtRadio, mTxtGuarantee, mGuijiSection,mGuijiOne, mGujiTwo,mGuojiThree;
//    PullToRefreshExpandableListView mListView;
//    ExpandableListView mBaseListView;
//    HomeAdapter mAdapter;
//
//
//    //
//    ArrayList<ProGroupListDataBean> mCurrAllData = new ArrayList<>();
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment_home, null);
//        initView();
//        mAdapter = new HomeAdapter(getActivity());
//        getBnnerList();
//        getGuoNeiProList();
//        return view;
//    }
//
//    private void initView() {
//        LayoutInflater lay = LayoutInflater.from(getActivity());
////        xml加载头部视图
//        mHeaderView = lay. inflate(R.layout.a_home_head_view, null);
//
//        mHeaderBanner = (BannerView) mHeaderView.findViewById(R.id.view_banner_home_header);
//
//        mLinearHeaderMenu = (LinearLayout) mHeaderView.findViewById(R.id.linear_home_header_menu);
//
//        mTxtMoni = (TextView) mHeaderView.findViewById(R.id.tv_home_moni);
//        mTxtNews = (TextView) mHeaderView.findViewById(R.id.tv_home_news);
//        mTxtFinance = (TextView) mHeaderView.findViewById(R.id.tv_home_finance);
//        mTxtHelp = (TextView) mHeaderView.findViewById(R.id.tv_home_help);
//        mTxtRadio = (TextView) mHeaderView.findViewById(R.id.tv_home_radio);
//        mTxtGuarantee = (TextView) mHeaderView.findViewById(R.id.tv_home_guarantee);
//
//        mGuijiSection = (TextView) mHeaderView.findViewById(R.id.list_item_all_guoji);
//
//
//        mListView = (PullToRefreshExpandableListView) view.findViewById(R.id.listview_home);
//        mBaseListView = mListView.getRefreshableView();
//
//        mBaseListView.addHeaderView(mHeaderView);
//        mBaseListView.setGroupIndicator(null);
//        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
//        //
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
//        mTxtMoni.setOnClickListener(this);
//        mTxtNews.setOnClickListener(this);
//        mTxtFinance.setOnClickListener(this);
//        mTxtHelp.setOnClickListener(this);
//        mTxtRadio.setOnClickListener(this);
//        mTxtGuarantee.setOnClickListener(this);
//
//        mGuijiSection.setOnClickListener(this);
//    }
//
//    /**
//     * 获取banner
//     */
//    private void getBnnerList() {
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
//    /**
//     * 获取外盘产品列表数据
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
//
//
//    /**
//     * 菜单点击事件
//     */
//    @Override
//    public void onClick(View v) {
//        Intent intent;
//        switch (v.getId()) {
//
//            case R.id.tv_home_moni:
//                intent = new Intent(getActivity(), MoNiTransActivity.class);
//                startActivity(intent);
//                break;
//
//            case R.id.tv_home_news:
//                intent = new Intent(getActivity(), WebActivity.class);
//                intent.putExtra("url", DataUrl.BASE_URL + "/content/index/news");
//                startActivity(intent);
//                break;
//
//            case R.id.tv_home_finance:
//                intent = new Intent(getActivity(), WebActivity.class);
//                intent.putExtra("url", DataUrl.JINSHISHUJU );    //DataUrl.BASE_URL + "/Member/index/tuiguang.html"
//                startActivity(intent);
//                break;
//
//            case R.id.tv_home_help:
//                intent = new Intent(getActivity(), WebActivity.class);
//                intent.putExtra("url", DataUrl.BASE_URL + "/content/index/lists/catid/3");
//                startActivity(intent);
//                break;
//
//            case R.id.tv_home_radio:
//                intent = new Intent(getActivity(), WebActivity.class);
//                intent.putExtra("url", DataUrl.BASE_URL + "/Member/index/zhibo_list");
//                startActivity(intent);
//                break;
//
//            case R.id.tv_home_guarantee:
//                intent = new Intent(getActivity(), WebActivity.class);
//                intent.putExtra("url", DataUrl.BASE_URL + "/Home/index/guarantee");
//                startActivity(intent);
//                break;
//
//
//            case R.id.list_item_all_guoji:
//                intent = new Intent(getActivity(), GuoJiActivity.class);
//                startActivity(intent);
//                break;
//
//        }
//    }
//}



public class HomeFragment extends BaseFragment implements View.OnClickListener {
    View view;
    View mHeaderView;
    LinearLayout mLinearHeaderMenu;
    BannerView mHeaderBanner;
    MyTemplate mOutsideExit,mInsideExit;

    TextView  mGuijiOne, mGujiTwo,mGuojiThree;
    PullToRefreshExpandableListView mListView;
    ExpandableListView mBaseListView;
    HomeAdapter mAdapter;


    SocketRequestPresenter mSocketPresenter;
    private TransacationAdapter mGuoJiAdapter, mGuoNeiAdapter;


    private ExpandableGridView mAppGridView = null;
    private List<Map<String, Object>> dataList;
    private SimpleAdapter simpAdapter;

    private int[] mAppIcons = {R.mipmap.pic_home_micp_1, R.mipmap.pic_home_news_1,
                                 R.mipmap.pic_find_jfsc, R.mipmap.pic_home_xszy_1,
                                 R.mipmap.pic_home_radio,R.mipmap.pic_home_garuatee
    };
    private String[] mAppNames = {"模拟练习场", "财经资讯","今日头条","新手指导","财经直播","安全保障"};



    //
    ArrayList<ProGroupListDataBean> mCurrAllData = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, null);

        mGuoJiAdapter = new TransacationAdapter(getActivity());
        mGuoNeiAdapter = new TransacationAdapter(getActivity());
        mGuoJiAdapter.setType(0);
        mGuoNeiAdapter.setType(0);
        mAdapter = new HomeAdapter(getActivity());

        initView();

        getBnnerList();
        getGuoNeiProList();

        //获取socket数据
        mSocketPresenter = new SocketRequestPresenter(getActivity());

        getTransListSocketData();
        return view;
    }


    private void initView() {
        LayoutInflater lay = LayoutInflater.from(getActivity());
//        xml加载头部视图
        mHeaderView = lay. inflate(R.layout.a_home_head_view_new, null);

        mHeaderBanner = (BannerView) mHeaderView.findViewById(R.id.view_banner_home_header);



//        mLinearHeaderMenu = (LinearLayout) mHeaderView.findViewById(R.id.linear_home_header_menu1);

        //获取menu菜单项
        mAppGridView = (ExpandableGridView) mHeaderView.findViewById(R.id.menu_gridView); // step1

        mInsideExit = (MyTemplate) mHeaderView.findViewById(R.id.menu_list_guonei);
        mOutsideExit = (MyTemplate) mHeaderView.findViewById(R.id.menu_list_guoji);


        mListView = (PullToRefreshExpandableListView) view.findViewById(R.id.listview_home);

        mBaseListView = mListView.getRefreshableView();

        mBaseListView.addHeaderView(mHeaderView);
        mBaseListView.setGroupIndicator(null);
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);






        //初始化数据
        List<Map<String,Object>> listItems = new ArrayList<>();

        for (int i=0; i<mAppIcons.length; i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("icon", mAppIcons[i]);
            listItem.put("name", mAppNames[i]);
            listItems.add(listItem);
        }

        //创建simpleAdpater

        simpAdapter = new SimpleAdapter(getActivity(),
                listItems,
                R.layout.menu_gridview_item,
                new String[]{"icon", "name"},
                new int[]{R.id.icon_img, R.id.name_tv});

        mAppGridView.setAdapter(simpAdapter);

        //添加列表项监听器
        mAppGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                        intent.putExtra("url", DataUrl.JINSHISHUJU );    //DataUrl.BASE_URL + "/Member/index/tuiguang.html"
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


                    case 6:
                        intent = new Intent(getActivity(), GuoJiActivity.class);
                        startActivity(intent);
                        break;

                }
            }
        });




        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ExpandableListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                mAdapter.clearList();
                getGuoNeiProList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {

            }
        });
        //
        mBaseListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        mBaseListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(getActivity(), K_DetailActivity.class);
                intent.putExtra("id", mCurrAllData.get(groupPosition).groupList.get(childPosition).tid);
                intent.putExtra("device", "Android");
                intent.putExtra("moni", "0");
                startActivity(intent);
                return true;
            }
        });

//        监听事件
//        mGuijiSection.setOnClickListener(this);

    }


    /**
     * 获取banner
     */
    private void getBnnerList() {
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
     * 获取外盘产品列表数据
     */
    private void getGuoNeiProList() {
        new ProListPresenter(getActivity()).getProList("14", "Android", new IProListListener() {
            @Override
            public void onSuccess(ProChildListDataBean data) {
                mListView.onRefreshComplete();
                ProGroupListDataBean guoneiItemdata = new ProGroupListDataBean();
                guoneiItemdata.name = "国内期货推荐";
                guoneiItemdata.groupList = data.response;
                mCurrAllData.add(guoneiItemdata);
                getGuoJiProList();
            }
            @Override
            public void onFailure(BaseData data) {
                mListView.onRefreshComplete();
            }

            @Override
            public void onError() {
                mListView.onRefreshComplete();
            }
        });
    }


    /**
     * 获取外盘产品列表数据
     */
    private void getGuoJiProList() {
        new ProListPresenter(getActivity()).getProList("15", "Android", new IProListListener() {
            @Override
            public void onSuccess(ProChildListDataBean data) {
                ProGroupListDataBean guojiItemdata = new ProGroupListDataBean();
                guojiItemdata.name = "国际期货推荐";
                guojiItemdata.groupList = data.response;
                mCurrAllData.add(guojiItemdata);
                mAdapter.setList(mCurrAllData);
                mBaseListView.setAdapter(mAdapter);
                //
                for (int i = 0; i < mCurrAllData.size(); i++) {
                    mBaseListView.expandGroup(i);
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
     * 菜单点击事件
     */
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {

            case R.id.list_item_all_guoji:
                intent = new Intent(getActivity(), GuoJiActivity.class);
                startActivity(intent);
                break;

        }
    }


    /**
     * 通过socket获取实时行情数据
     */
    private void getTransListSocketData() {
        mSocketPresenter.transList("{\"command\":\"binduid\",\"uid\":\"get_hangqing\"}", new ITransListListener() {
            @Override
            public void onSuccess(TransListDataBean data) {

                ArrayList<TransListDataBean.TransData> arrayList = data.data;



//                        ArrayList<TransListDataBean.TransData> oldlist = mGuoJiAdapter.getList();
//                        ArrayList<TransListDataBean.TransData> newlist = new ArrayList<>();
//                        for (int i = 0; i < oldlist.size(); i++) {
//                            TransListDataBean.TransData olditemdata = oldlist.get(i);
//                            String prono = olditemdata.prono;
//                            for (int j = 0; j < arrayList.size(); j++) {
//                                TransListDataBean.TransData itemdata = arrayList.get(j);
//                                if (prono.equalsIgnoreCase(itemdata.prono)) {
//                                    itemdata.title = olditemdata.title;
//                                    itemdata.tid = olditemdata.tid;
//                                    itemdata.nums = olditemdata.nums;
//                                    itemdata.zhang = olditemdata.zhang;
//                                    itemdata.die = olditemdata.die;
//                                    itemdata.perprice = olditemdata.perprice;
//                                    itemdata.lastprice = olditemdata.lastprice;
//                                    itemdata.order_nums = olditemdata.order_nums;

//                                newlist.add(itemdata);
//                                break;

//                            }
//                        }
                            Message message = new Message();
                            message.obj = arrayList;
                            mHandlerRefreshView.sendMessage(message);
//                        }

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

            ArrayList<TransListDataBean.TransData> m = (ArrayList<TransListDataBean.TransData>) msg.obj;

            if (m != null && m.size() > 3){
                TransListDataBean.TransData itemdata =  m.get(0);
                mInsideExit.setOneHeadText(itemdata.prono);
                mInsideExit.setOneCenterText(itemdata.lastprice);
                mInsideExit.setOneFootText(itemdata.perprice);

                TransListDataBean.TransData itemdataTwo =  m.get(1);
                mInsideExit.setTwoHeadText(itemdataTwo.prono);
                mInsideExit.setTwoCenterText(itemdataTwo.lastprice);
                mInsideExit.setTwoFootText(itemdataTwo.perprice);

                TransListDataBean.TransData itemdataThr =  m.get(2);
                mInsideExit.setThreeHeadText(itemdataThr.title);
                mInsideExit.setThreeCenterText(itemdataThr.lastprice);
                mInsideExit.setThreeFootText(itemdataThr.perprice);
            }

            mGuoJiAdapter.updateListData((ArrayList<TransListDataBean.TransData>) msg.obj);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSocketPresenter != null) {
            mSocketPresenter.closeSocket();
        }
    }


}
