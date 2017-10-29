package com.coco3g.caopantx.net.utls;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.coco3g.caopantx.R;
import com.coco3g.caopantx.bean.BaseData;
import com.coco3g.caopantx.bean.VersionUpdateDataBean;
import com.coco3g.caopantx.data.Global;
import com.coco3g.caopantx.listener.IVersionUpdateListener;
import com.coco3g.caopantx.presenter.VersionManagePresenter;

public class VersionUpdateUtils {
    Context mContext;

    public VersionUpdateUtils(Context context) {
        mContext = context;
    }

    /**
     * 新版本检测
     *
     * @param showLoading
     * @param loadingMsg
     */
    public void checkUpdate(final boolean showLoading, String loadingMsg) {
        new VersionManagePresenter(mContext).checkNewVersion(showLoading, loadingMsg, new IVersionUpdateListener() {
            @Override
            public void onSuccess(VersionUpdateDataBean data) {
                if (data.response == null && showLoading) {
                    AlertDialog dialog = new AlertDialog.Builder(mContext,
                            AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setTitle(mContext.getString(R.string.alert_hint)).setMessage("已是最新版本")
                            .setPositiveButton(mContext.getString(R.string.close), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).create();
                    dialog.show();
                    return;
                }
                final VersionUpdateDataBean.ReturnData v_info = data.response;
                int code = Integer.valueOf(v_info.code);
                if (code > Global.getAppVersionCode(mContext)) {
                    AlertDialog dialog = new AlertDialog.Builder(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setCancelable(false).setTitle(mContext.getString(R.string.alert_hint))
                            .setMessage("发现新版本，点击下载").setPositiveButton("下载", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Global.getAndroidSDKVersion() < 11) {
                                        Uri uri = Uri.parse(v_info.appurl);
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        mContext.startActivity(intent);
                                    } else {
                                        DownLoadFileUtil dlf = new DownLoadFileUtil(mContext);
                                        dlf.addDownLoadUrl(v_info.appurl);
                                    }
                                }
                            }).setNegativeButton(mContext.getString(R.string.close), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    ((Activity)mContext).finish();
                                }
                            }).create();

                    dialog.show();
                }
            }

            @Override
            public void onFailure(BaseData data) {

            }

            @Override
            public void onError() {

            }

        });
    }

//    Handler mHandlerUpdate = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            // TODO Auto-generated method stub
//            super.handleMessage(msg);
//            final VersionUpdateData info = (VersionUpdateData) msg.obj;
//
//        }
//    };
}
