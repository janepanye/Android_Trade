package com.coco3g.caopantx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.coco3g.caopantx.adapter.TransacationAdapter;
import com.coco3g.caopantx.bean.BaseData;
import com.coco3g.caopantx.bean.TransListDataBean;
import com.coco3g.caopantx.presenter.SocketRequestPresenter;
import com.coco3g.caopantx.presenter.TransPresenter;
import com.coco3g.caopantx.view.TopBarView;
import com.coco3g.caopantx.R;
import com.coco3g.caopantx.listener.ITransListListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

/**
 * Created by lisen on 16/9/5.
 */
public class GuoNeiActivity extends BaseActivity {
    TopBarView mTopBar;
    PullToRefreshListView mGuoNeiListView;
    TransacationAdapter mGuoNeiAdapter;
    SocketRequestPresenter mSocketPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guoji);
        mGuoNeiAdapter = new TransacationAdapter(this);
        initView();
        getTransList();
        // 获取交易列表实时数据
        mSocketPresenter = new SocketRequestPresenter(this);
        getTransListSocketData();
    }

    private void initView() {
        mTopBar = (TopBarView) findViewById(R.id.topbar_guoji);
        mTopBar.setTitle("国内期货");
        mGuoNeiListView = (PullToRefreshListView) findViewById(R.id.listview_guoji);

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
        //
        mGuoNeiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GuoNeiActivity.this, K_DetailActivity.class);
                intent.putExtra("id", mGuoNeiAdapter.getList().get(position - 1).tid);
                intent.putExtra("device", "Android");
                intent.putExtra("moni", "0");
                startActivity(intent);
            }
        });
    }

    /**
     * 获取期货列表
     */
    private void getTransList() {
        new TransPresenter(this).getTransList("14", "Android", "0", new ITransListListener() {
            @Override
            public void onSuccess(TransListDataBean data) {
                mGuoNeiAdapter.setList(data.response);
                mGuoNeiListView.setAdapter(mGuoNeiAdapter);
                mGuoNeiListView.onRefreshComplete();
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
//                                itemdata.lastprice = olditemdata.lastprice;
                                newlist.add(itemdata);
                                break;
                            }
                        }
                    }
                    Message message = new Message();
                    message.obj = newlist;
                    mHandlerRefreshView.sendMessage(message);
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
            mGuoNeiAdapter.updateListData((ArrayList<TransListDataBean.TransData>) msg.obj);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSocketPresenter != null) {
            mSocketPresenter.closeSocket();
        }
    }
}
