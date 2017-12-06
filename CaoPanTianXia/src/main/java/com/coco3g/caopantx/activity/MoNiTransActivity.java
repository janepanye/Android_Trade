package com.coco3g.caopantx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coco3g.caopantx.data.DataUrl;
import com.coco3g.caopantx.data.Global;
import com.coco3g.caopantx.view.TopBarView;
import com.coco3g.caopantx.R;
import com.coco3g.caopantx.adapter.TransacationAdapter;
import com.coco3g.caopantx.bean.BaseData;
import com.coco3g.caopantx.bean.TransListDataBean;
import com.coco3g.caopantx.listener.ITransListListener;
import com.coco3g.caopantx.presenter.SocketRequestPresenter;
import com.coco3g.caopantx.presenter.TransPresenter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by lisen on 16/8/22.
 */
public class MoNiTransActivity extends BaseActivity {
    TopBarView mTopBar;
    private PullToRefreshListView mGuoJiListView, mGuoNeiListView;
    private TransacationAdapter mGuoJiAdapter, mGuoNeiAdapter;
    private int mCurrType = 14;//当前的期货类别，15 国际期货，14 国内期货
    SocketRequestPresenter mSocketPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moni_trans);
        mGuoJiAdapter = new TransacationAdapter(MoNiTransActivity.this);
        mGuoNeiAdapter = new TransacationAdapter(MoNiTransActivity.this);
        mGuoJiAdapter.setType(1);
        mGuoNeiAdapter.setType(1);
        getTransList();
        // 获取交易列表实时数据
        mSocketPresenter = new SocketRequestPresenter(MoNiTransActivity.this);
        getTransListSocketData();
        initView();
    }

    private void initView() {
        mTopBar = (TopBarView) findViewById(R.id.topbar_moni_trans);
        mTopBar.showMiddleMenu();
        mTopBar.setOnClickMiddleMenuListener(new TopBarView.OnClickMiddleMenu() {
            @Override
            public void onClickMiddleMenu(int resID) {
                switch (resID) {
                    case R.id.tv_topbar_middle_left:
                        switchTrans(14);
                        break;
                    case R.id.tv_topbar_middle_right:
                        switchTrans(15);
                        break;
                }
            }
        });
//          点击持仓明细
        mTopBar.setOnClickTopRight(new TopBarView.OnClickTopRight(){
            @Override
                public void onClickTopRight(){
                Intent intent = new Intent(MoNiTransActivity.this, WebActivity.class);
                intent.putExtra("title", "");
                intent.putExtra("moni", "1");
//                intent.putExtra("catid", "resID");
                intent.putExtra("url", DataUrl.CHICANGMN);
                startActivity(intent);
            }
        });

        mGuoJiListView = (PullToRefreshListView) findViewById(R.id.listview_moni_transaction_guoji);
        mGuoNeiListView = (PullToRefreshListView) findViewById(R.id.listview_moni_transaction_guonei);

        mGuoJiListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mGuoNeiListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

        mGuoJiListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getTransList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

        mGuoNeiListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getTransList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });



        mGuoJiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MoNiTransActivity.this, K_DetailActivity.class);
                intent.putExtra("id", mGuoJiAdapter.getList().get(position - 1).tid);
                intent.putExtra("device", "Android");
                intent.putExtra("moni", "1");
                startActivity(intent);
            }
        });
        mGuoNeiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MoNiTransActivity.this, K_DetailActivity.class);
                intent.putExtra("id", mGuoNeiAdapter.getList().get(position - 1).tid);
                intent.putExtra("device", "Android");
                intent.putExtra("moni", "1");
                startActivity(intent);
            }
        });
        mGuoNeiListView.setVisibility(View.VISIBLE);
        mGuoJiListView.setVisibility(View.GONE);
    }

    /**
     * 获取期货列表
     */
    private void getTransList() {
        new TransPresenter(MoNiTransActivity.this).getTransList(mCurrType + "", "Android", "1", new ITransListListener() {
            @Override
            public void onSuccess(TransListDataBean data) {
                if (mCurrType == 15) {
                    mGuoJiAdapter.setList(data.response);
                    mGuoJiListView.setAdapter(mGuoJiAdapter);
                    mGuoJiListView.onRefreshComplete();
                } else if (mCurrType == 14) {
                    mGuoNeiAdapter.setList(data.response);
                    mGuoNeiListView.setAdapter(mGuoNeiAdapter);
                    mGuoNeiListView.onRefreshComplete();
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
    private void getTransListSocketData() {
        mSocketPresenter.transList("{\"command\":\"binduid\",\"uid\":\"get_hangqing\"}", new ITransListListener() {
            @Override
            public void onSuccess(TransListDataBean data) {
                ArrayList<TransListDataBean.TransData> list = data.data;
//                Log.d("Socket数据", data.data.toString());
                if (mCurrType == 15) {
                    if (mGuoJiAdapter != null && mGuoJiAdapter.getList() != null && mGuoJiAdapter.getList().size() > 0) {
                        ArrayList<TransListDataBean.TransData> oldlist = mGuoJiAdapter.getList();
                        ArrayList<TransListDataBean.TransData> newlist = new ArrayList<>();
                        for (int i = 0; i < oldlist.size(); i++) {
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
//                                    itemdata.lastprice = olditemdata.lastprice;
                                    newlist.add(itemdata);
                                    break;
                                }
                            }
                        }
                        Message message = new Message();
                        message.obj = newlist;
                        mHandlerRefreshView.sendMessage(message);
                    }
                } else if (mCurrType == 14) {
                    if (mGuoNeiAdapter != null && mGuoNeiAdapter.getList() != null && mGuoNeiAdapter.getList().size() > 0) {
                        ArrayList<TransListDataBean.TransData> oldlist = mGuoNeiAdapter.getList();
                        ArrayList<TransListDataBean.TransData> newlist = new ArrayList<>();
                        for (int i = 0; i < oldlist.size(); i++) {
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
//                                    itemdata.lastprice = olditemdata.lastprice;
                                    newlist.add(itemdata);
                                    break;
                                }
                            }
                        }
                        Message message = new Message();
                        message.obj = newlist;
                        mHandlerRefreshView.sendMessage(message);
//                        mGuoNeiAdapter.updateListData(newlist);
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

    /**
     * 获取实时行情后，刷新相关view
     */
    Handler mHandlerRefreshView = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mCurrType == 15) {
                mGuoJiAdapter.updateListData((ArrayList<TransListDataBean.TransData>) msg.obj);
            } else if (mCurrType == 14) {
                mGuoNeiAdapter.updateListData((ArrayList<TransListDataBean.TransData>) msg.obj);
            }
        }
    };

    public void switchTrans(int index) {
//        if (index == 0) {
//            mWebView1.setVisibility(View.VISIBLE);
//            mWebView2.setVisibility(View.GONE);
//        } else  if (index == 1) {
//            mWebView1.setVisibility(View.GONE);
//            mWebView2.setVisibility(View.VISIBLE);
//        }
        mCurrType = index;
        if (index == 15) {
            mGuoJiListView.setVisibility(View.VISIBLE);
            mGuoNeiListView.setVisibility(View.GONE);
            if (mGuoJiAdapter.getList() == null || mGuoJiAdapter.getList().size() == 0) {
                getTransList();
            }
        } else if (index == 14) {
            mGuoJiListView.setVisibility(View.GONE);
            mGuoNeiListView.setVisibility(View.VISIBLE);
            if (mGuoNeiAdapter.getList() == null || mGuoNeiAdapter.getList().size() == 0) {
                getTransList();
            }
        }
    }

    /**
     * onDestroy关闭心跳
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSocketPresenter != null) {
            mSocketPresenter.closeSocket();
        }
    }
}
