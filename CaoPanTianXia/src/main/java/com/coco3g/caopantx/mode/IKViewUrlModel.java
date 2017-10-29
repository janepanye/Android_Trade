package com.coco3g.caopantx.mode;

import com.coco3g.caopantx.listener.IKViewUrlListener;

/**
 * Created by lisen on 16/9/2.
 */
public interface IKViewUrlModel {

    void getKViewUrl(String prono, String id, String catid, String nums, String device, String moni, IKViewUrlListener listener);

}
