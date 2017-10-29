package com.coco3g.caopantx.listener;


import com.coco3g.caopantx.bean.TransListDataBean;

/**
 * Created by lisen on 16/9/4.
 */
public interface ITransListListener extends IBaseListener{

    void onSuccess(TransListDataBean data);
}
