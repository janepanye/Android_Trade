package com.coco3g.caopantx.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import com.coco3g.caopantx.bean.ProGroupListDataBean;
import com.coco3g.caopantx.R;
import com.coco3g.caopantx.bean.ProChildListDataBean;

import java.util.ArrayList;

import java.net.MalformedURLException;
import java.net.UnknownServiceException;
import org.apache.http.conn.ConnectTimeoutException;
import android.widget.Toast;





public class HomeAdapter extends BaseExpandableListAdapter {

    Context mContext;
    ArrayList<ProGroupListDataBean> mList = new ArrayList<>();

    public HomeAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setList(ArrayList<ProGroupListDataBean> list) {
        mList = list;
    }

    public void clearList() {
        if (mList != null) {
            mList.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getGroupCount() {
        return mList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mList.get(groupPosition).groupList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mList.get(groupPosition).groupList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    ViewHolderGroup viewHolderGroup;

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (Build.VERSION.SDK_INT >= 11) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        }
        if (convertView == null) {
            viewHolderGroup = new ViewHolderGroup();
            convertView = View.inflate(mContext, R.layout.a_home_group_item, null);
            viewHolderGroup.mTxtName = (TextView) convertView.findViewById(R.id.tv_home_item_group_name);
            viewHolderGroup.mTxtChange = (TextView) convertView.findViewById(R.id.tv_home_item_group_change);
            convertView.setTag(viewHolderGroup);
        } else {
            viewHolderGroup = (ViewHolderGroup) convertView.getTag();
        }
        viewHolderGroup.mTxtName.setText(mList.get(groupPosition).name);
        return convertView;
    }

    private class ViewHolderGroup {
        TextView mTxtName, mTxtChange;
    }

    ViewHolderChild viewHolderChild;

    @Override
//    PYTODO 布局
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolderChild = new ViewHolderChild();
            convertView = View.inflate(mContext, R.layout.a_home_child_item, null);
            viewHolderChild.tvChildImg = (ImageView) convertView.findViewById(R.id.item_child_iv);
            viewHolderChild.tvChildName = (TextView) convertView.findViewById(R.id.item_child_tv_name);
//            viewHolderChild.tvChildIcon = (TextView) convertView.findViewById(R.id.item_child_iv);
            viewHolderChild.mTxtState = (TextView) convertView.findViewById(R.id.tv_home_item_state);
            viewHolderChild.tvChildDec = (TextView) convertView.findViewById(R.id.item_child_tv_dec);
            viewHolderChild.tvChildTime = (TextView) convertView.findViewById(R.id.item_child_tv_time);
            viewHolderChild.tvChildMoney = (TextView) convertView.findViewById(R.id.item_child_tv_money);
            convertView.setTag(viewHolderChild);
        } else {
            viewHolderChild = (ViewHolderChild) convertView.getTag();
        }

        ArrayList<ProChildListDataBean.ProBaseData> childitem = mList.get(groupPosition).groupList;

//        String imag=childitem.get(childPosition).img;
//        String img="http://visit.panshou.cn/Upload/"+imag;
//        Bitmap bitmap = getHttpBitmaps(img);
//        // 图片
//        viewHolderChild.tvChildImg.setImageBitmap(bitmap);

//        //图片资源
//        String url = "http://visit.panshou.cn/Upload/"+ childitem.get(childPosition).img;
//        //得到可用的图片
//        Bitmap bitmap = getHttpBitmap(url);
//        // 图片
//        viewHolderChild.tvChildImg.setImageBitmap(bitmap);



        // 名称
        viewHolderChild.tvChildName.setText(childitem.get(childPosition).title);
        // 描述
        viewHolderChild.tvChildDec.setText(childitem.get(childPosition).description);
        // 时间段
        viewHolderChild.tvChildTime.setText(childitem.get(childPosition).timedesc);
        // 保证金
        viewHolderChild.tvChildMoney.setText("￥\t" + childitem.get(childPosition).baozhengjin);
        // 状态
        String status = childitem.get(childPosition).status;
        if ("0".equals(status)) {
            viewHolderChild.mTxtState.setText("休市中");
            viewHolderChild.mTxtState.setTextColor(mContext.getResources().getColor(R.color.text_color_2));
        } else if ("1".equals(status)) {
            viewHolderChild.mTxtState.setText("交易中");
            viewHolderChild.mTxtState.setTextColor(mContext.getResources().getColor(R.color.app_theme_color));
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    private class ViewHolderChild {
        TextView tvChildName, tvChildDec, tvChildTime, tvChildMoney, mTxtState;
        ImageView tvChildImg;
    }




    /**
     * 获取图片图片资源
     * @param url
     * @return
     */
    public static Bitmap getHttpBitmap(String url){
        URL myFileURL;
        Bitmap bitmap=null;
        try{
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }

}





/****Last Version***/


/**
 * Created by MIN on 16/6/14.
 */
//public class HomeAdapter extends BaseExpandableListAdapter {
//
//    Context mContext;
//    ArrayList<ProGroupListDataBean> mList = new ArrayList<>();
//
//    public HomeAdapter(Context mContext) {
//        this.mContext = mContext;
//    }
//
//    public void setList(ArrayList<ProGroupListDataBean> list) {
//        mList = list;
//    }
//
//    public void clearList() {
//        if (mList != null) {
//            mList.clear();
//            notifyDataSetChanged();
//        }
//    }
//
//    @Override
//    public int getGroupCount() {
//        return mList.size();
//    }
//
//    @Override
//    public int getChildrenCount(int groupPosition) {
//        return mList.get(groupPosition).groupList.size();
//    }
//
//    @Override
//    public Object getGroup(int groupPosition) {
//        return mList.get(groupPosition);
//    }
//
//    @Override
//    public Object getChild(int groupPosition, int childPosition) {
//        return mList.get(groupPosition).groupList.get(childPosition);
//    }
//
//    @Override
//    public long getGroupId(int groupPosition) {
//        return groupPosition;
//    }
//
//    @Override
//    public long getChildId(int groupPosition, int childPosition) {
//        return childPosition;
//    }
//
//    @Override
//    public boolean hasStableIds() {
//        return false;
//    }
//
//    ViewHolderGroup viewHolderGroup;
//
//    @Override
//    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
//
//        if (Build.VERSION.SDK_INT >= 11) {
//            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
//            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
//        }
//        if (convertView == null) {
//            viewHolderGroup = new ViewHolderGroup();
//            convertView = View.inflate(mContext, R.layout.a_home_group_item, null);
//            viewHolderGroup.mTxtName = (TextView) convertView.findViewById(R.id.tv_home_item_group_name);
//            viewHolderGroup.mTxtChange = (TextView) convertView.findViewById(R.id.tv_home_item_group_change);
//            convertView.setTag(viewHolderGroup);
//        } else {
//            viewHolderGroup = (ViewHolderGroup) convertView.getTag();
//        }
//        viewHolderGroup.mTxtName.setText(mList.get(groupPosition).name);
//        return convertView;
//    }
//
//    private class ViewHolderGroup {
//        TextView mTxtName, mTxtChange;
//    }
//
//    ViewHolderChild viewHolderChild;
//
//    @Override
////    PYTODO 布局
//    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//        if (convertView == null) {
//            viewHolderChild = new ViewHolderChild();
//            convertView = View.inflate(mContext, R.layout.a_home_child_item, null);
//            viewHolderChild.tvChildImg = (ImageView) convertView.findViewById(R.id.item_child_iv);
//            viewHolderChild.tvChildName = (TextView) convertView.findViewById(R.id.item_child_tv_name);
////            viewHolderChild.tvChildIcon = (TextView) convertView.findViewById(R.id.item_child_iv);
//            viewHolderChild.mTxtState = (TextView) convertView.findViewById(R.id.tv_home_item_state);
//            viewHolderChild.tvChildDec = (TextView) convertView.findViewById(R.id.item_child_tv_dec);
//            viewHolderChild.tvChildTime = (TextView) convertView.findViewById(R.id.item_child_tv_time);
//            viewHolderChild.tvChildMoney = (TextView) convertView.findViewById(R.id.item_child_tv_money);
//            convertView.setTag(viewHolderChild);
//        } else {
//            viewHolderChild = (ViewHolderChild) convertView.getTag();
//        }
//
//        ArrayList<ProChildListDataBean.ProBaseData> childitem = mList.get(groupPosition).groupList;
//
////        String imag=childitem.get(childPosition).img;
////        String img="http://visit.panshou.cn/Upload/"+imag;
////        Bitmap bitmap = getHttpBitmaps(img);
////        // 图片
////        viewHolderChild.tvChildImg.setImageBitmap(bitmap);
//
////        //图片资源
////        String url = "http://visit.panshou.cn/Upload/"+ childitem.get(childPosition).img;
////        //得到可用的图片
////        Bitmap bitmap = getHttpBitmap(url);
////        // 图片
////        viewHolderChild.tvChildImg.setImageBitmap(bitmap);
//
//
//
//        // 名称
//        viewHolderChild.tvChildName.setText(childitem.get(childPosition).title);
//        // 描述
//        viewHolderChild.tvChildDec.setText(childitem.get(childPosition).description);
//        // 时间段
//        viewHolderChild.tvChildTime.setText(childitem.get(childPosition).timedesc);
//        // 保证金
//        viewHolderChild.tvChildMoney.setText("￥\t" + childitem.get(childPosition).baozhengjin);
//        // 状态
//        String status = childitem.get(childPosition).status;
//        if ("0".equals(status)) {
//            viewHolderChild.mTxtState.setText("休市中");
//            viewHolderChild.mTxtState.setTextColor(mContext.getResources().getColor(R.color.text_color_2));
//        } else if ("1".equals(status)) {
//            viewHolderChild.mTxtState.setText("交易中");
//            viewHolderChild.mTxtState.setTextColor(mContext.getResources().getColor(R.color.app_theme_color));
//        }
//        return convertView;
//    }
//
//    @Override
//    public boolean isChildSelectable(int groupPosition, int childPosition) {
//        return true;
//    }
//    private class ViewHolderChild {
//        TextView tvChildName, tvChildDec, tvChildTime, tvChildMoney, mTxtState;
//        ImageView tvChildImg;
//    }
//
//
//
//
//    /**
//     * 获取网落图片资源
//     * @param url
//     * @return
//     */
//    public static Bitmap getHttpBitmap(String url){
//        URL myFileURL;
//        Bitmap bitmap=null;
//        try{
//            myFileURL = new URL(url);
//            //获得连接
//            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
//            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
//            conn.setConnectTimeout(6000);
//            //连接设置获得数据流
//            conn.setDoInput(true);
//            //不使用缓存
//            conn.setUseCaches(false);
//            //这句可有可无，没有影响
//            //conn.connect();
//            //得到数据流
//            InputStream is = conn.getInputStream();
//            //解析得到图片
//            bitmap = BitmapFactory.decodeStream(is);
//            //关闭数据流
//            is.close();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        return bitmap;
//    }
//
//}


