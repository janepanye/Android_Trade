package com.coco3g.caopantx.listener;

import com.coco3g.caopantx.bean.TransDataListDataBean;

/**
 * Created by lisen on 16/8/31.
 */
public interface ISocketTransListListener extends IBaseListener {

    void onSuccess(TransDataListDataBean data);

}
