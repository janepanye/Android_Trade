package com.coco3g.caopantx.mode;

import com.coco3g.caopantx.listener.IVersionUpdateListener;

/**
 * Created by lisen on 16/4/13.
 */
public interface IVersionManageModel {
    public void checkNewVersion(boolean showLoading, String loadingMsg, IVersionUpdateListener listener);
}
