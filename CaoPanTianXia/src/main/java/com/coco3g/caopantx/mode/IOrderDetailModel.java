package com.coco3g.caopantx.mode;

import com.coco3g.caopantx.listener.IOrderDetailListener;
import com.coco3g.caopantx.listener.ITradeNoListener;

/**
 * Created by lisen on 16/9/6.
 */
public interface IOrderDetailModel {

    void getOrderDetail(String orderid, IOrderDetailListener listener);

//    void getTradeNo(String orderid, ITradeNoListener listener);

    void getOrderInfo(String orderid, ITradeNoListener listener);

}
