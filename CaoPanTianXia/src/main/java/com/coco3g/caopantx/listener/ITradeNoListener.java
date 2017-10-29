package com.coco3g.caopantx.listener;

import com.coco3g.caopantx.bean.TradeNoDataBean;

/**
 * Created by lisen on 16/9/6.
 */
public interface ITradeNoListener extends IBaseListener {
    void onSuccess(TradeNoDataBean data);
}
