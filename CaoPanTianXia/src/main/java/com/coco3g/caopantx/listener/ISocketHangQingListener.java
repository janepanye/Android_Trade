package com.coco3g.caopantx.listener;

import com.coco3g.caopantx.bean.HangQingBaseDataBean;

/**
 * Created by lisen on 16/9/2.
 */
public interface ISocketHangQingListener extends IBaseListener {

    void onSuccess(HangQingBaseDataBean data);

}
