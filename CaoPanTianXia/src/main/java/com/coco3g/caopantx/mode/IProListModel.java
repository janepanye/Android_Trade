package com.coco3g.caopantx.mode;

import com.coco3g.caopantx.listener.IProListListener;

/**
 * Created by lisen on 16/9/5.
 */
public interface IProListModel {

    void getProList(String catid, String device, IProListListener listener);

}
