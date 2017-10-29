package com.coco3g.caopantx.mode;

import com.coco3g.caopantx.listener.IBaseDataListener;
import com.coco3g.caopantx.listener.IUserInfoListenter;
import com.coco3g.caopantx.listener.IUserListenter;

/**
 * Created by MIN on 16/6/16.
 */
public interface IPublicMode {
    //注册
    void register(String username, String password, String code, String recommendCode, IUserListenter listener);

    //登录
    void login(String username, String password, IUserListenter listener);

    void forgetPwd(String password, String phone, String phone_code, IBaseDataListener listener);

    //发送验证码
    void sendSms(String phone, String codetype, IBaseDataListener listener);

    //退出登录
    void logout(IBaseDataListener listener);

    // 风险告知书确认
    void agree(String proid, String typeid, IBaseDataListener listener);

    void getUserInfo(IUserInfoListenter listener);


}
