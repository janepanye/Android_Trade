package com.coco3g.caopantx.net.utls;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.coco3g.caopantx.utils.ImageUtils;
import com.coco3g.caopantx.R;
import com.coco3g.caopantx.data.Constants;
import com.coco3g.caopantx.data.Global;
import com.coco3g.caopantx.wxapi.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.File;
import java.io.IOException;

public class ShareAppUtils {
    Context mContext;
    String mShareUrl, mShareTitle, mShareDesc, mShareImage;
    Bitmap shareBitmap;
    /* 微信相关 */
    private IWXAPI api;
    Tencent mTencent = null;
    int supportApiLevel, mShareFlag;
    DisplayImageOptions options;

    public ShareAppUtils(Context context, int shareFlag, String url, String title, String desc, String image) {
        mContext = context;
        mShareUrl = url;
        mShareTitle = title;
        mShareDesc = desc;
        mShareImage = image;
        mShareFlag = shareFlag;
        //
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.pic_share_default_bg)
                .showImageOnFail(R.mipmap.pic_share_default_bg)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
        /* QQ相关 */
//        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, mContext.getApplicationContext());

        // boolean isInstalledWeibo = mWeiboShareAPI.isWeiboAppInstalled();
//        supportApiLevel = mWeiboShareAPI.getWeiboAppSupportAPI();
//        if (savedInstanceState != null) {
//            mWeiboShareAPI.handleWeiboResponse(((Activity) mContext).getIntent(), this);
//        }
        ImageLoader.getInstance().loadImage(mShareImage, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                shareBitmap = bitmap;
                switch (mShareFlag) {
                    case 1:
                        shareToWeiXin();
                        break;
                    case 2:
                        shareToMoment();
                        break;
                    case 3:
                        shareToQQ();
                        break;

                }
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
    }

    /**
     * 分享到微信
     */
    private void shareToWeiXin() {
        api = WXAPIFactory.createWXAPI(mContext, Constants.WEIXIN_APP_ID);
        api.registerApp(Constants.WEIXIN_APP_ID);
        //
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = mShareUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = mShareTitle;
        msg.description = mShareDesc;
        if (shareBitmap == null) {
            shareBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.pic_share_default_bg);
        }
        msg.thumbData = Util.bmpToByteArray(shareBitmap, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "webpage";
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }

    /**
     * 分享到微信朋友圈
     */
    private void shareToMoment() {
        api = WXAPIFactory.createWXAPI(mContext, Constants.WEIXIN_APP_ID);
        api.registerApp(Constants.WEIXIN_APP_ID);
        //
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = mShareUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = mShareTitle;
        msg.description = mShareDesc;
        if (shareBitmap == null) {
            shareBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.pic_share_default_bg);
        }
        msg.thumbData = Util.bmpToByteArray(shareBitmap, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "webpage";
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }

    /************************ 分享到QQ ************************/
    public void shareToQQ() {
        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, mContext.getApplicationContext());
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, mShareTitle);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, mShareDesc);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, mShareUrl);
        if (!TextUtils.isEmpty(mShareImage) && (mShareImage.startsWith("http") || mShareImage.startsWith("https"))) {
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, mShareImage);
        } else {
            String filepath = Global.getPath(mContext) + File.separator + Global.localThumbPath + File.separator + "sharepic.png";
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.pic_share_default_bg);
            try {
                ImageUtils.saveImageToSD(mContext, filepath, bitmap, Bitmap.CompressFormat.PNG, 70);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, filepath);
        }
        // params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, mShareImage);
        // params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
        // "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, mContext.getResources().getString(R.string.app_name));
        // params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, "其他附加功能");
        mTencent.shareToQQ(((Activity) mContext), params, new IUiListener() {

            @Override
            public void onError(UiError e) {
                // TODO Auto-generated method stub
                Global.showToast(e.errorMessage + "-" + e.errorCode, mContext);
            }

            @Override
            public void onComplete(Object arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                Global.showToast("用户取消", mContext);
            }
        });
    }

//    /**
//     * 分享到QQ
//     */
//    public void shareToQQ() {
//        final Bundle params = new Bundle();
//        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
//        params.putString(QQShare.SHARE_TO_QQ_TITLE, mShareTitle);
//        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, mShareDesc);
//        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, mShareUrl);
//        if (!TextUtils.isEmpty(mShareImage) && (mShareImage.startsWith("http") || mShareImage.startsWith("https"))) {
//            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, mShareImage);
//        } else {
//            String filepath = Global.getPath(mContext) + File.separator + Global.localThumbPath + File.separator + "sharepic.png";
//            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.pic_share_default_bg);
//            try {
//                ImageUtils.saveImageToSD(mContext, filepath, bitmap, CompressFormat.PNG, 70);
//            } catch (IOException e1) {
//                // TODO Auto-generated catch block
//                e1.printStackTrace();
//            }
//            params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, filepath);
//        }
//        // params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, mShareImage);
//        // params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
//        // "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
//        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, mContext.getResources().getString(R.string.app_name));
//        // params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, "其他附加功能");
//        mTencent.shareToQQ(((Activity) mContext), params, new IUiListener() {
//
//            @Override
//            public void onError(UiError e) {
//                // TODO Auto-generated method stub
//                Global.showToast(e.errorMessage + "-" + e.errorCode, mContext);
//            }
//
//            @Override
//            public void onComplete(Object arg0) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void onCancel() {
//                // TODO Auto-generated method stub
//                Global.showToast("用户取消", mContext);
//            }
//        });
//    }

}
