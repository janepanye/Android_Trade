package com.coco3g.caopantx.mode;

import com.coco3g.caopantx.listener.IBaseDataListener;
import com.coco3g.caopantx.listener.IProDetailListener;

/**
 * Created by lisen on 16/9/2.
 */
public interface IProDetailMode {

    //获取产品详情接口
    void getProDetail(String id, String device, String moni, IProDetailListener listener);

    //极速下单关闭接口
    void closeOrderSet(String prono, String id, String moni, String device, IBaseDataListener listener);

    // 一键平仓接口
    void pingCangAll(String wpno, String prono, String catid, String moni, String device, IBaseDataListener listener);

    // 极速下单接口
    void quickOrder(String setid, String prono, String tid, String moni, String ordertype, String shoushu, String type, String device, IBaseDataListener listener);
}
