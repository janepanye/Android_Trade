package com.coco3g.caopantx.listener;

import com.coco3g.caopantx.bean.UserData;

/**
 * Created by MIN on 16/6/16.
 */
public interface IUserListenter extends IBaseListener {
    void onSuccess(UserData data);
}
