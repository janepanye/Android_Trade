package com.coco3g.caopantx.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.coco3g.caopantx.R;
import com.coco3g.caopantx.activity.BaseFragment;
import com.coco3g.caopantx.activity.K_DetailActivity;
import com.coco3g.caopantx.adapter.TransacationAdapter;
import com.coco3g.caopantx.bean.BaseData;
import com.coco3g.caopantx.bean.TransListDataBean;
import com.coco3g.caopantx.listener.ITransListListener;
import com.coco3g.caopantx.presenter.SocketRequestPresenter;
import com.coco3g.caopantx.presenter.TransPresenter;
import com.coco3g.caopantx.utils.Coco3gBroadcastUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by MIN on 16/6/13.
 */
public class TransactionFragment extends BaseFragment {
    private View view;
    //    private ListView mGuoJiListView, mGuoNeiListView;
    private PullToRefreshListView mGuoJiListView;
    private PullToRefreshListView mGuoNeiListView;
    //    private PullToRefreshListView mGuoJiListView1, mGuoNeiListView1;
    private TransacationAdapter mGuoJiAdapter, mGuoNeiAdapter;
    private int mCurrType = 14;//当前的期货类别，15 国际期货，14 国内期货
    SocketRequestPresenter mSocketPresenter;
    public Coco3gBroadcastUtils mCurrBoardCast;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_transaction, null);
        mGuoJiAdapter = new TransacationAdapter(getActivity());
        mGuoNeiAdapter = new TransacationAdapter(getActivity());
        mGuoJiAdapter.setType(0);
        mGuoNeiAdapter.setType(0);
        initView();
        getTransList();
        //
        // 接收需要刷新当前界面的广播
        mCurrBoardCast = new Coco3gBroadcastUtils(getActivity());
        mCurrBoardCast.receiveBroadcast(Coco3gBroadcastUtils.RETURN_UPDATE_TRANSLIST_FLAG)
                .setOnReceivebroadcastListener(new Coco3gBroadcastUtils.OnReceiveBroadcastListener() {
                    @Override
                    public void receiveReturn(Intent intent) {
//                        getTransList();
                        refreshData();
                    }
                });
        return view;
    }


    /**
     * 初始化
     */
    private void initView() {
        mGuoJiListView = (PullToRefreshListView) view.findViewById(R.id.transaction_listview_guoji);
        mGuoNeiListView = (PullToRefreshListView) view.findViewById(R.id.transaction_listview_guonei);

        mGuoJiListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mGuoJiListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getTransList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
//
        mGuoNeiListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mGuoNeiListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getTransList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

        //点击进入国际下单详情
        mGuoJiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), K_DetailActivity.class);
                intent.putExtra("id", mGuoJiAdapter.getList().get(position - 1).tid);
                intent.putExtra("device", "Android");
                intent.putExtra("moni", "0");
                startActivity(intent);
            }
        });
        //点击进入国内下单详情
        mGuoNeiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), K_DetailActivity.class);
                intent.putExtra("id", mGuoNeiAdapter.getList().get(position - 1).tid);
                intent.putExtra("device", "Android");
                intent.putExtra("moni", "0");
                startActivity(intent);
            }
        });
        mGuoNeiListView.setVisibility(View.VISIBLE);
        mGuoJiListView.setVisibility(View.GONE);
    }

    public void switchTrans(int index) {
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
     * 获取期货列表
     */
    private void getTransList() {
        new TransPresenter(getActivity()).getTransList(mCurrType + "", "Android", "0", new ITransListListener() {
            @Override
            public void onSuccess(TransListDataBean data) {
                mGuoJiAdapter.clearList();
                mGuoNeiAdapter.clearList();

                if (mCurrType == 15) {
                    mGuoJiAdapter.setList(data.response);
                    mGuoJiListView.setAdapter(mGuoJiAdapter);
                    mGuoJiListView.onRefreshComplete();
                } else if (mCurrType == 14) {
                    mGuoNeiAdapter.setList(data.response);
                    mGuoNeiListView.setAdapter(mGuoNeiAdapter);
                    mGuoNeiListView.onRefreshComplete();
                }
                //
                if (mSocketPresenter != null) {
                    mSocketPresenter.closeSocket();
                    mSocketPresenter = null;
                }
                //
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
     * 通过socket获取实时行情数据
     */
    private void getTransListSocketData() {
        mSocketPresenter.transList("{\"command\":\"binduid\",\"uid\":\"get_hangqing\"}", new ITransListListener() {
            @Override
            public void onSuccess(TransListDataBean data) {
//                ArrayList<TransListDataBean.TransData> list = new ArrayList<TransListDataBean.TransData>(data.data);

                ArrayList<TransListDataBean.TransData> list = data.data;
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

    //当返回MainActivity的时候,在onResume里面(刷新国内期货或国内期货的数据)
    public void refreshData() {
        if (mGuoNeiListView != null && mGuoNeiListView.getVisibility() == View.VISIBLE) {
            mGuoNeiListView.setRefreshing(true);
        }
        if (mGuoJiListView != null && mGuoJiListView.getVisibility() == View.VISIBLE) {
            mGuoJiListView.setRefreshing(true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mSocketPresenter != null) {
            mSocketPresenter.closeSocket();
            mSocketPresenter = null;
        }
    }
}
