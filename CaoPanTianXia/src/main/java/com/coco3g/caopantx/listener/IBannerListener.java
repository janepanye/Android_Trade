package com.coco3g.caopantx.listener;

import com.coco3g.caopantx.bean.BannerListData;

/**
 * Created by lisen on 16/9/5.
 */
public interface IBannerListener extends IBaseListener {

    void onSuccess(BannerListData data);

}
