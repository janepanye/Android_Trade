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
 */
public class GuideListViewAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> data;

    public GuideListViewAdapter(Context mContext, ArrayList<String> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
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
            convertView = View.inflate(mContext, R.layout.a_guide_listview_item, null);
            holder.tvGuiZe = (TextView) convertView.findViewById(R.id.item_tv_guize);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvGuiZe.setText(data.get(position).toString());
        return convertView;
    }

    private class ViewHolder {
        TextView tvGuiZe;
    }
}
