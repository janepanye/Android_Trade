package com.coco3g.caopantx.listener;

import com.coco3g.caopantx.bean.UserData;
import com.coco3g.caopantx.bean.UserInfoDataBean;

/**
 * Created by MIN on 16/6/16.
 */
public interface IUserInfoListenter extends IBaseListener {
    void onSuccess(UserInfoDataBean data);
}
