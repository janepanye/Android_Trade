package com.coco3g.caopantx.mode;

import com.coco3g.caopantx.listener.IBannerListener;

/**
 * Created by lisen on 16/9/5.
 */
public interface IBannerModel {

    void getBannerList(String catid, String device, IBannerListener listener);

}
