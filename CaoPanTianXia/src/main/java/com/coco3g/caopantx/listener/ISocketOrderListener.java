package com.coco3g.caopantx.listener;

import com.coco3g.caopantx.bean.OrderDataBean;

/**
 * Created by lisen on 2016/10/25.
 */

public interface ISocketOrderListener extends IBaseListener {

    void onSuccess(OrderDataBean data);

}
