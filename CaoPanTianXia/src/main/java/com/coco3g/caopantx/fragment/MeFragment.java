package com.coco3g.caopantx.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coco3g.caopantx.R;
import com.coco3g.caopantx.activity.BaseFragment;
import com.coco3g.caopantx.activity.StartActivity;
import com.coco3g.caopantx.activity.WebActivity;
import com.coco3g.caopantx.bean.BaseData;
import com.coco3g.caopantx.bean.UserData;
import com.coco3g.caopantx.bean.UserInfoDataBean;
import com.coco3g.caopantx.data.DataUrl;
import com.coco3g.caopantx.data.Global;
import com.coco3g.caopantx.listener.IBaseDataListener;
import com.coco3g.caopantx.listener.IUserInfoListenter;
import com.coco3g.caopantx.listener.IUserListenter;
import com.coco3g.caopantx.presenter.PublicPresenter;
import com.coco3g.caopantx.utils.Coco3gBroadcastUtils;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by MIN on 16/6/13.
 */



public class MeFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private ImageView mImgIcon;
    private TextView mTxtPhone, mTxtMoney, mTvTiXian, mTvChongZhi, mTvFinance, mTvLack, mTvTrade, mTvAward, mTvPersonal, mTvContact, mTvAbout;
    private LinearLayout mHasLogin;
    private boolean isLogin;
    public Coco3gBroadcastUtils mCurrBoardCast;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_me_new, null);

        initView();
        // 接收需要 刷新当前界面的广播
        mCurrBoardCast = new Coco3gBroadcastUtils(getActivity());
        mCurrBoardCast.receiveBroadcast(Coco3gBroadcastUtils.RETURN_UPDATE_ME_FLAG)
                .setOnReceivebroadcastListener(new Coco3gBroadcastUtils.OnReceiveBroadcastListener() {
                    @Override
                    public void receiveReturn(Intent intent) {
//                        if (Global.USERINFO != null) {
//                            mTxtMoney.setText(Global.USERINFO.amount + "元");
//                        }
                        getUserInfo();
                    }
                });
        if (Global.USERINFO != null) {
            mTxtPhone.setText(Global.USERINFO.nickname);
            mTxtMoney.setText(Global.USERINFO.amount);
            //图片资源
//            String url = "http://visit.panshou.cn"+Global.USERINFO.head;
//            //得到可用的图片
//            Bitmap bitmap = getHttpBitmap(url);
//            // 图片
//            mImgIcon.setImageBitmap(bitmap);

        }
        return view;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (Global.USERINFO != null) {

                mTxtPhone.setText(Global.USERINFO.nickname);
                mTxtMoney.setText(Global.USERINFO.amount);
//                //图片资源
//                String url = "http://visit.panshou.cn"+Global.USERINFO.head;
//                //得到可用的图片
//                Bitmap bitmap = getHttpBitmap(url);
//                // 图片
//                mImgIcon.setImageBitmap(bitmap);
            }
        }
    }

    private void initView() {
        mHasLogin = (LinearLayout) view.findViewById(R.id.mine_ll_has_login);
        mImgIcon = (ImageView) view.findViewById(R.id.mine_icon);
        mTxtPhone = (TextView) view.findViewById(R.id.mine_tv_phone);
        mTxtMoney = (TextView) view.findViewById(R.id.mine_tv_money);
        mTvTiXian = (TextView) view.findViewById(R.id.mine_tv_tixian);
        mTvChongZhi = (TextView) view.findViewById(R.id.mine_tv_chongzhi);
        mTvFinance = (TextView) view.findViewById(R.id.tv_mine_finance);
        mTvLack = (TextView) view.findViewById(R.id.tv_mine_lack);
        mTvTrade = (TextView) view.findViewById(R.id.tv_mine_trade);
        mTvAward = (TextView) view.findViewById(R.id.tv_mine_youhui);
        mTvPersonal = (TextView) view.findViewById(R.id.tv_mine_personal);
        mTvContact = (TextView) view.findViewById(R.id.tv_mine_contact);
        mTvAbout = (TextView) view.findViewById(R.id.tv_mine_about);

//        mTxtPhone.setOnClickListener(this);
        mTvTiXian.setOnClickListener(this);
        mTvChongZhi.setOnClickListener(this);
        mTvFinance.setOnClickListener(this);
        mTvLack.setOnClickListener(this);
        mTvTrade.setOnClickListener(this);
        mTvAward.setOnClickListener(this);
        mTvPersonal.setOnClickListener(this);
        mTvContact.setOnClickListener(this);
        mTvAbout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {

            case R.id.mine_tv_tixian://提现
                intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", DataUrl.TIXIAN);
                startActivity(intent);
                break;
            case R.id.mine_tv_chongzhi://充值
                intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", DataUrl.CHONGZHI);
                startActivity(intent);
                break;
            case R.id.tv_mine_finance://资金明细
                intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", DataUrl.ZIJINMINGXI);
                startActivity(intent);
                break;
            case R.id.tv_mine_lack://盈亏明细
                intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", DataUrl.EARN_OR_LACK);
                startActivity(intent);
                break;
            case R.id.tv_mine_trade://交易记录
                intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", DataUrl.JIAOYIJILU);
                startActivity(intent);
                break;
            case R.id.tv_mine_youhui://我的优惠
                intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", DataUrl.WODEYOUHUI);
                startActivity(intent);
                break;
            case R.id.tv_mine_personal://个人资料
                intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", DataUrl.GERENZILIAO);
                startActivity(intent);
                break;
            case R.id.tv_mine_contact://联系我们
                intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", DataUrl.CONTACTUS);
                startActivity(intent);
                break;
            case R.id.tv_mine_about://关于我们
                intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", DataUrl.SHEZHI);
                startActivity(intent);
                break;
        }
    }

    /**
     * 获取个人资料
     */
    public void getUserInfo() {
        new PublicPresenter(getActivity()).getUserInfo(new IUserInfoListenter() {
            @Override
            public void onSuccess(UserInfoDataBean data) {
                try {
                    Global.USERINFO = data.response.userinfo;
                    if (Global.USERINFO != null) {
                        mTxtMoney.setText(Global.USERINFO.amount);
                        mTxtPhone.setText(Global.USERINFO.nickname);  //手机号

//                        //图片资源
                        String url = "http://visit.panshou.cn"+Global.USERINFO.head;
                        //得到可用的图片
                        Bitmap bitmap = getHttpBitmap(url);
                        // 图片
                        mImgIcon.setImageBitmap(bitmap);


                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
     * 获取网落图片资源
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