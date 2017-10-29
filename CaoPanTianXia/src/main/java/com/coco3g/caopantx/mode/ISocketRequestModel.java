package com.coco3g.caopantx.mode;

import com.coco3g.caopantx.listener.ISocketOrderListener;
import com.coco3g.caopantx.listener.ISocketHangQingListener;
import com.coco3g.caopantx.listener.ITransListListener;

/**
 * Created by lisen on 16/7/25.
 */
public interface ISocketRequestModel {

    void transList(String param, ITransListListener listener);

    void hangqingData(String param, ISocketHangQingListener listener);

    void orderData(String param, ISocketOrderListener listener);
}