package com.coco3g.caopantx.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.coco3g.caopantx.bean.BaseData;
import com.coco3g.caopantx.bean.UserData;
import com.coco3g.caopantx.bean.UserInfoDataBean;
import com.coco3g.caopantx.data.Constants;
import com.coco3g.caopantx.data.DataUrl;
import com.coco3g.caopantx.listener.IBaseDataListener;
import com.coco3g.caopantx.listener.IUserInfoListenter;
import com.coco3g.caopantx.mode.IPublicMode;
import com.coco3g.caopantx.net.utls.DownLoadDataThread;
import com.coco3g.caopantx.R;
import com.coco3g.caopantx.listener.IUserListenter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by MIN on 16/6/16.
 */
public class PublicPresenter implements IPublicMode {
    private Context mContext;

    public PublicPresenter(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 注册
     */
    @Override
    public void register(String username, String password, String code, String recommendCode, final IUserListenter listener) {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("code", code));
        params.add(new BasicNameValuePair("regno", recommendCode));
        params.add(new BasicNameValuePair("device", "Android"));
        params.add(new BasicNameValuePair("uuid", Constants.JPUSH_REGISTERID));

        new DownLoadDataThread(mContext, true, mContext.getResources().getString(R.string.loading), "post", new UserData())
                .setUrl(DataUrl.REGISTER).addParams(params).setHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case DownLoadDataThread.NET_RESULT_CODE_SUCCESS:
                        listener.onSuccess((UserData) msg.obj);
                        break;
                    case DownLoadDataThread.NET_RESULT_CODE_FAILURE:
                        listener.onFailure((BaseData) msg.obj);
                        break;
                    case DownLoadDataThread.NET_RESULT_CODE_ERROR:
                        listener.onError();
                        break;
                }
            }
        }).start();
    }

    /**
     * 登录
     */
    @Override
    public void login(String username, String password, final IUserListenter listener) {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("device", "Android"));
        params.add(new BasicNameValuePair("uuid", Constants.JPUSH_REGISTERID));

        new DownLoadDataThread(mContext, true, mContext.getResources().getString(R.string.loading), "post", new UserData())
                .setUrl(DataUrl.LOGIN).addParams(params).setHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case DownLoadDataThread.NET_RESULT_CODE_SUCCESS:
                        listener.onSuccess((UserData) msg.obj);
                        break;
                    case DownLoadDataThread.NET_RESULT_CODE_FAILURE:
                        listener.onFailure((BaseData) msg.obj);
                        break;
                    case DownLoadDataThread.NET_RESULT_CODE_ERROR:
                        listener.onError();
                        break;
                }
            }
        }).start();
    }

    /**
     * 忘记密码
     *
     * @param password
     * @param phone
     * @param phone_code
     * @param listener
     */
    @Override
    public void forgetPwd(String phone, String password, String phone_code, final IBaseDataListener listener) {
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("phone_code", phone_code));
        params.add(new BasicNameValuePair("device", "Android"));
        new DownLoadDataThread(mContext, true, mContext.getResources().getString(R.string.loading), "post", new BaseData())
                .setUrl(DataUrl.FORGET_PWD).addParams(params).setHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case DownLoadDataThread.NET_RESULT_CODE_SUCCESS:
                        listener.onSuccess((BaseData) msg.obj);
                        break;
                    case DownLoadDataThread.NET_RESULT_CODE_FAILURE:
                        listener.onFailure((BaseData) msg.obj);
                        break;
                    case DownLoadDataThread.NET_RESULT_CODE_ERROR:
                        listener.onError();
                        break;
                }
            }
        }).start();
    }

    /**
     * 发送验证码
     *
     * @param phone
     * @param codetype
     * @param listener
     */
    @Override
    public void sendSms(String phone, String codetype, final IBaseDataListener listener) {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("device", "Android"));
        params.add(new BasicNameValuePair("codetype", codetype));

        new DownLoadDataThread(mContext, false, "", "post", new BaseData())
                .setUrl(DataUrl.SENDSMS).addParams(params).setHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case DownLoadDataThread.NET_RESULT_CODE_SUCCESS:
                        listener.onSuccess((BaseData) msg.obj);
                        break;
                    case DownLoadDataThread.NET_RESULT_CODE_FAILURE:
                        listener.onFailure((BaseData) msg.obj);
                        break;
                    case DownLoadDataThread.NET_RESULT_CODE_ERROR:
                        listener.onError();
                        break;
                }
            }
        }).start();
    }


    //退出登录
    @Override
    public void logout(final IBaseDataListener listener) {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("device", "Android"));

        new DownLoadDataThread(mContext, false, "", "post", new BaseData())
                .setUrl(DataUrl.LOGOUT).addParams(params).setHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case DownLoadDataThread.NET_RESULT_CODE_SUCCESS:
                        listener.onSuccess((BaseData) msg.obj);
                        break;
                    case DownLoadDataThread.NET_RESULT_CODE_FAILURE:
                        listener.onFailure((BaseData) msg.obj);
                        break;
                    case DownLoadDataThread.NET_RESULT_CODE_ERROR:
                        listener.onError();
                        break;
                }
            }
        }).start();

    }

    /**
     * 风险告知书和服务协议确认
     *
     * @param proid
     * @param listener
     */
    @Override
    public void agree(String proid, String typeid, final IBaseDataListener listener) {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("proid", proid));
        params.add(new BasicNameValuePair("typeid", typeid));
        params.add(new BasicNameValuePair("device", "Android"));
        new DownLoadDataThread(mContext, false, "", "post", new BaseData())
                .setUrl(DataUrl.AGREE_URL).addParams(params).setHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case DownLoadDataThread.NET_RESULT_CODE_SUCCESS:
                        listener.onSuccess((BaseData) msg.obj);
                        break;
                    case DownLoadDataThread.NET_RESULT_CODE_FAILURE:
                        listener.onFailure((BaseData) msg.obj);
                        break;
                    case DownLoadDataThread.NET_RESULT_CODE_ERROR:
                        listener.onError();
                        break;
                }
            }
        }).start();
    }

    /**
     * 个人资料
     * @param listener
     */
    @Override
    public void getUserInfo(final IUserInfoListenter listener) {
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("device", "Android"));
        new DownLoadDataThread(mContext, false, "", "post", new UserInfoDataBean())
                .setUrl(DataUrl.USERINFO_URL).addParams(params).setHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case DownLoadDataThread.NET_RESULT_CODE_SUCCESS:
                        listener.onSuccess((UserInfoDataBean) msg.obj);
                        break;
                    case DownLoadDataThread.NET_RESULT_CODE_FAILURE:
                        listener.onFailure((BaseData) msg.obj);
                        break;
                    case DownLoadDataThread.NET_RESULT_CODE_ERROR:
                        listener.onError();
                        break;
                }
            }
        }).start();
    }
}
