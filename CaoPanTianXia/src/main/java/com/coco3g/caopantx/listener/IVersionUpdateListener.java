package com.coco3g.caopantx.listener;

import com.coco3g.caopantx.bean.VersionUpdateDataBean;

/**
 * Created by lisen on 16/4/13.
 */
public interface IVersionUpdateListener extends IBaseListener {
    public void onSuccess(VersionUpdateDataBean data);
}
