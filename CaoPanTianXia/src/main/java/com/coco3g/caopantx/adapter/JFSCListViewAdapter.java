package com.coco3g.caopantx.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.coco3g.caopantx.data.Global;
import com.coco3g.caopantx.R;

import java.util.ArrayList;

/**
 * Created by MIN on 16/6/17.
 */
public class JFSCListViewAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> mData;

    public JFSCListViewAdapter(Context mContext, ArrayList<String> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private ViewHolder holder;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.a_jfsc_listview_item, null);
            holder.mTvContent = (TextView) convertView.findViewById(R.id.item_tv_duihuanquan);
            holder.mBtnDuiHuan = (Button) convertView.findViewById(R.id.item_btn_duihuan);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTvContent.setText(mData.get(position).toString());
        holder.mBtnDuiHuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.showToast("兑换", mContext);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView mTvContent;
        Button mBtnDuiHuan;
    }
}
