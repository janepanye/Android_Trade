package com.coco3g.caopantx.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.coco3g.caopantx.bean.TransListDataBean;
import com.coco3g.caopantx.R;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by MIN on 16/6/15.
 */
/**
 * Adpaters实现getCount，getItem，getItemId，getView四个方法
 */

public class TransacationAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<TransListDataBean.TransData> mList = new ArrayList<>();
    private int mMoNi = 0;

    public TransacationAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setList(ArrayList<TransListDataBean.TransData> list) {
        if (list != null) {
            mList.clear();
            mList = list;
        }
    }

    public ArrayList<TransListDataBean.TransData> getList() {
        return mList;
    }

    public void updateListData(ArrayList<TransListDataBean.TransData> list) {
//        if (mList == null) {
//            mList = new ArrayList<>();
//        } else {
//            mList.clear();
//        }
        mList = list;
        notifyDataSetChanged();
    }

    public void clearList() {
        if (mList != null) {
            mList.clear();
        }
    }

    /**
     * @param type 提供接口判断是模拟还是是实盘
     *
     */
    public void setType(int type) {
        this.mMoNi = type;
    }


    /**
     * 要绑定的条目的数目
     */
    @Override
    public int getCount() {
        return mList.size();
    }

    /**
     * 根据一个索引（位置）获得该位置的对象
     */
    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    /**
     * 获取条目的id
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    private ViewHolder holder;

    /**
     * 获取该条目要显示的界面
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.a_transaction_listview_item, null);
            holder.tvQiHuoName = (TextView) convertView.findViewById(R.id.list_item_tv_qihuoname);
            holder.tvQHNumber = (TextView) convertView.findViewById(R.id.list_item_tv_qihuonumber);
            holder.tvMoney = (TextView) convertView.findViewById(R.id.list_item_tv_money);
            holder.tvType = (TextView) convertView.findViewById(R.id.list_item_tv_type);
            holder.tvZuiXinJia = (TextView) convertView.findViewById(R.id.list_item_tv_zuixinjia);
            holder.tvZhangdiefu = (TextView) convertView.findViewById(R.id.list_item_tv_zhangdiefu);
            holder.tvChiCang = (TextView) convertView.findViewById(R.id.list_item_tv_chicang_num);
            holder.tvYingKui = (TextView) convertView.findViewById(R.id.list_item_tv_yingkui_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvQiHuoName.setText(mList.get(position).title);
        holder.tvQHNumber.setText(mList.get(position).prono);
        if (mMoNi == 0) {
            holder.tvType.setVisibility(View.GONE);
        } else if (mMoNi == 1) {
            holder.tvType.setText("模拟");
        }
        holder.tvMoney.setText(mList.get(position).tprice + "元一手");
        // 最新价
        if (!TextUtils.isEmpty(mList.get(position).lastprice)) {
            holder.tvZuiXinJia.setText(mList.get(position).lastprice);
        } else {
            holder.tvZuiXinJia.setText("0.00");
        }
        // 涨跌幅
        if (!TextUtils.isEmpty(mList.get(position).perprice)) {
            float rate = Float.parseFloat(mList.get(position).perprice);
            if (rate < 0) {
                holder.tvZhangdiefu.setTextColor(mContext.getResources().getColor(R.color.green));
                holder.tvZuiXinJia.setTextColor(mContext.getResources().getColor(R.color.green));
            } else {
                holder.tvZhangdiefu.setTextColor(mContext.getResources().getColor(R.color.red));
                holder.tvZuiXinJia.setTextColor(mContext.getResources().getColor(R.color.red));
            }
            holder.tvZhangdiefu.setText(rate + "%");
        }
        // 持仓
        holder.tvChiCang.setText( mList.get(position).order_nums);
        //
        if (mList.get(position).buyPrice == null && mList.get(position).salePrice == null) {
            mList.get(position).buyPrice = "0";
            mList.get(position).salePrice = "0";
        }
        if (!TextUtils.isEmpty(mList.get(position).lastprice)) {
            float lastPrice = Float.parseFloat(mList.get(position).lastprice);
            float zhangPrice = 0;
            if (mList.get(position).zhang != null && mList.get(position).zhang.size() > 0) {
                for (int i = 0; i < mList.get(position).zhang.size(); i++) {
                    float price = Float.parseFloat(mList.get(position).zhang.get(i).price);
                    //
                    BigDecimal bd1 = new BigDecimal(Float.toString(lastPrice));
                    BigDecimal bd2 = new BigDecimal(Float.toString(price));
                    float subPrice = bd1.subtract(bd2).floatValue();
                    //
                    float f = subPrice * Integer.parseInt(mList.get(position).zhang.get(i).nums) * Integer.parseInt(mList.get(position).nums);
//                    f = Float.parseFloat(String.format("%.0f", f));
//                    zhangPrice = zhangPrice + f;
                    float rate = 1f;
                    if (!TextUtils.isEmpty(mList.get(position).zhang.get(0).rate)) {
                        rate = Float.parseFloat(mList.get(position).zhang.get(0).rate);
                    }
                    f = f * rate;
                    f = Float.parseFloat(String.format("%.2f", f));
                    zhangPrice = zhangPrice + f;
                }
//                float rate = 1f;
//                if (!TextUtils.isEmpty(mList.get(position).zhang.get(0).rate)) {
//                    rate = Float.parseFloat(mList.get(position).zhang.get(0).rate);
//                }
//                zhangPrice = zhangPrice * rate;
            }
            //
            float diePrice = 0;
            if (mList.get(position).die != null && mList.get(position).die.size() > 0) {
                for (int i = 0; i < mList.get(position).die.size(); i++) {
                    float price = Float.parseFloat(mList.get(position).die.get(i).price);
                    //
                    BigDecimal bd1 = new BigDecimal(Float.toString(price));
                    BigDecimal bd2 = new BigDecimal(Float.toString(lastPrice));
                    float subPrice = bd1.subtract(bd2).floatValue();
                    //
                    float f = subPrice * Integer.parseInt(mList.get(position).die.get(i).nums) * Integer.parseInt(mList.get(position).nums);
//                    f = Float.parseFloat(String.format("%.0f", f));
//                    diePrice = diePrice + f;
                    float rate = 1f;
                    if (!TextUtils.isEmpty(mList.get(position).die.get(0).rate)) {
                        rate = Float.parseFloat(mList.get(position).die.get(0).rate);
                    }
                    f = f * rate;
                    f = Float.parseFloat(String.format("%.2f", f));
                    diePrice = diePrice + f;
                }
//                float rate = 1f;
//                if (!TextUtils.isEmpty(mList.get(position).die.get(0).rate)) {
//                    rate = Float.parseFloat(mList.get(position).die.get(0).rate);
//                }
//                diePrice = diePrice * rate;
            }
            float totalP = zhangPrice + diePrice;
//            holder.tvYingKui.setText("盈亏：" + String.format("%.2f", (float) Math.round(totalP)));
//            holder.tvYingKui.setText("盈亏：" + String.format("%.2f", Double.parseDouble(totalP + "")));

            if (totalP >= 0){
                holder.tvYingKui.setTextColor(mContext.getResources().getColor(R.color.red));
            }else {
                holder.tvYingKui.setTextColor(mContext.getResources().getColor(R.color.green));
            }
            holder.tvYingKui.setText(String.format("%.2f", totalP));
        }
//        holder.tvYingKui.setText("盈亏：" + String.format("%.2f", totalP));
        return convertView;
    }

    private class ViewHolder {
        TextView tvQiHuoName, tvQHNumber, tvMoney, tvType, tvZuiXinJia, tvZhangdiefu, tvChiCang, tvYingKui;
    }
}
