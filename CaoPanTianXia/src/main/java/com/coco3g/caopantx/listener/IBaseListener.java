package com.coco3g.caopantx.listener;

import com.coco3g.caopantx.bean.BaseData;

/**
 * Created by lisen on 16/2/25 00:14.
 */
public interface IBaseListener {

    void onFailure(BaseData data);

    void onError();

}
