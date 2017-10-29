package com.coco3g.caopantx.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.coco3g.caopantx.R;

import java.util.ArrayList;

/**
 * Created by MIN on 16/6/17.
 * 信息中心界面中的adapter
 */
public class XXZXListViewAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> mData;

    public XXZXListViewAdapter(Context mContext) {
        this.mContext = mContext;
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
            convertView = View.inflate(mContext, R.layout.a_xxzx_listview_item, null);
            holder.tvContent = (TextView) convertView.findViewById(R.id.item_tv_content);
            holder.tvTime = (TextView) convertView.findViewById(R.id.item_tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvContent.setText(mData.get(position).toString());
        return convertView;
    }

    private class ViewHolder {
        TextView tvContent, tvTime;
    }

    public void setList(ArrayList<String> list){
        mData=list;
    }
}
