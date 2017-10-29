package com.coco3g.caopantx.mode;

import com.coco3g.caopantx.listener.ITransListListener;

/**
 * Created by lisen on 16/9/4.
 */
public interface ITransModel {

    void getTransList(String catid, String device, String moni, ITransListListener listener);

}
