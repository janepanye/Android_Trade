package com.coco3g.caopantx.listener;

import com.coco3g.caopantx.bean.BaseData;

/**
 * Created by lisen on 16/4/5.
 */
public interface IBaseDataListener extends IBaseListener {
    void onSuccess(BaseData data);
}
