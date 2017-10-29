package com.coco3g.caopantx.listener;

import com.coco3g.caopantx.bean.OrderDetailDataBean;

/**
 * Created by lisen on 16/9/6.
 */
public interface IOrderDetailListener extends IBaseListener {
    void onSuccess(OrderDetailDataBean data);
}
