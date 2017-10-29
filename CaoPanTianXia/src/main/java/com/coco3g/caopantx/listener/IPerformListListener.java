package com.coco3g.caopantx.listener;

import com.coco3g.caopantx.bean.PerformListData;

/**
 * Created by lisen on 16/6/1.
 */
public interface IPerformListListener extends IBaseListener{
    void onSuccess(PerformListData data);
}
