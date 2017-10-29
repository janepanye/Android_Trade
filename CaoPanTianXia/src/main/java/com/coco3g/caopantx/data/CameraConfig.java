package com.coco3g.caopantx.data;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.OrientationEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("ALL")
public class CameraConfig {
    Context mContext = null;
    private Size mBestPreviewSizeBack; // 后置摄像头所设置的预览尺寸
    private Size mBestPictureSizeBack; // 后置摄像头所设置的照片尺寸
    private Size mBestVideoSizeBack; // 后置摄像头所设置的视频尺寸

    private Size mBestPreviewSizeFront; // 前置摄像头所设置的预览尺寸
    private Size mBestPictureSizeFront; // 前置摄像头所设置的照片尺寸
    private Size mBestVideoSizeFront; // 前置摄像头所设置的视频尺寸

    private boolean supportContinueFocus = false; // 后置摄像头是否支持连续对焦
    private boolean supportAutoFocus = false; // 后置摄像头是否支持自动对焦
    private int[] mBestPreviewFps = new int[2]; // 摄像头所支持的最佳预览帧数
    // 展示的屏幕尺寸
    public ArrayList<String> mSupportSizeList = new ArrayList<String>(); // 摄像头支持的屏幕尺寸
    private HashMap<String, ArrayList<Size>> mHashPreRatio = new HashMap<String, ArrayList<Size>>(); // 摄像头支持的屏幕比例及该比例下支持的预览分辨率
    private HashMap<String, ArrayList<Size>> mHashPicRatio = new HashMap<String, ArrayList<Size>>(); // 摄像头支持的屏幕比例及该比例下支持的照片分辨率
    private HashMap<String, ArrayList<Size>> mHashVideoRatio = new HashMap<String, ArrayList<Size>>(); // 摄像头支持的视频分辨率
    OrientationEventListener mOrientationEventListener = null;

    public CameraConfig(Context context) {
        mContext = context;
    }

    public void initConfig(int id) {
        try {
            Camera camera = Camera.open(id);
            Camera.Parameters parameters = camera.getParameters();
            // 获取预览尺寸
            List<Size> supportPreview = parameters.getSupportedPreviewSizes();
            if (supportPreview == null) {
                supportPreview = parameters.getSupportedPreviewSizes();
            }
            // 获取照片分辨率
            List<Size> supportPicture = parameters.getSupportedPictureSizes();
            if (supportPicture == null) {
                supportPicture = parameters.getSupportedPictureSizes();
            }
            // 获取视频支持分辨率
            String supportVideoStr = parameters.get("video-size-values");
            List<Size> supportVideo = null;
            if (supportVideoStr == null) {
                supportVideo = parameters.getSupportedPreviewSizes();
            } else {
                supportVideo = videoStringToList(camera, supportVideoStr);
            }
            // 预览帧
            List<int[]> range = parameters.getSupportedPreviewFpsRange();
            List<Integer> previewFps = new ArrayList<Integer>();
            for (int j = 0; j < range.size(); j++) {
                int[] r = range.get(j);
                for (int k = 0; k < r.length; k++) {
                    previewFps.add(r[k]);
                }
            }
            if (id == Camera.CameraInfo.CAMERA_FACING_BACK) {
                ArrayList<String> list = (ArrayList<String>) parameters.getSupportedFocusModes();
                if (list != null) {
                    for (String mode : list) {
                        if (Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE.equalsIgnoreCase(mode)) {
                            setSupportContinueFocus(true); // 支持连续对焦
                            break;
                        } else {
                            if (Camera.Parameters.FOCUS_MODE_AUTO.equalsIgnoreCase(mode)) {
                                setSupportAutoFocus(true); // 支持自动对焦
                            }
                        }

                    }
                }
            }
            closeCamera(camera); // 关闭相机，释放相机资源
            initCameraData(id, supportPreview, supportPicture, supportVideo, previewFps);
        } catch (Exception e) {
            // Toast.makeText(mContext, "相机连接失败，请重启手机...",
            // Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * 初始化摄像头相关数据
     *
     * @param cameraid
     * @param supportPreview
     * @param supportPicture
     * @param previewFps
     * @param supportVideo
     */
    private void initCameraData(int cameraid, List<Size> supportPreview, List<Size> supportPicture, List<Size> supportVideo, List<Integer> previewFps) {
        /* 对两个list进行排序 */
        Collections.sort(supportPreview, new ComparatorValues());
        Collections.sort(supportPicture, new ComparatorValues());
        Collections.sort(supportVideo, new ComparatorValues());
        /* 获取最佳预览帧 */
        if (previewFps != null && previewFps.size() > 0) {
            Collections.sort(previewFps, new ComparatorFpsValues());
            if (previewFps != null && previewFps.size() >= 2) {
                setmBestPreviewFps(previewFps.get(0), previewFps.get(1));
            } else if (previewFps != null && previewFps.size() == 1) {
                setmBestPreviewFps(previewFps.get(0), previewFps.get(0));
            } else {
                setmBestPreviewFps(0, 0);
            }

        }
        mSupportSizeList.clear();
        for (int i = 0; i < supportPreview.size(); i++) {
            Size preview = supportPreview.get(i);
            int width = preview.width;
            int height = preview.height;
            if (!mSupportSizeList.contains(Global.getScreenRatio(width, height))) {
                mSupportSizeList.add(Global.getScreenRatio(width, height));
            }
        }
        for (int i = 0; i < mSupportSizeList.size(); i++) { // 获取最佳预览分辨率
            String str_ = mSupportSizeList.get(i);
            String[] str = str_.split(":");
            int width = Integer.parseInt(str[0]);
            int height = Integer.parseInt(str[1]);
            double d1 = (double) width / height;
            String str1 = String.format("%.4f", d1);
            ArrayList<Size> al = new ArrayList<Size>();
            for (int j = 0; j < supportPreview.size(); j++) {
                Size preview = supportPreview.get(j);
                int width_ = preview.width;
                int height_ = preview.height;
                double d2 = (double) width_ / height_;
                String str2 = String.format("%.4f", d2);
                if (str1.equals(str2)) {
                    al.add(preview);
                }
            }
            mHashPreRatio.put(str_, al);
        }
        ArrayList<Size> list = null;
        for (int i = 0; i < mSupportSizeList.size(); i++) { // 获取最佳照片分辨率
            String[] temp = mSupportSizeList.get(i).split(":");
            if (temp != null && temp.length == 2) {
                int a = Integer.parseInt(temp[0].trim());
                int b = Integer.parseInt(temp[1].trim());
                double d = (double) a / b;
                String str = String.format("%.4f", d);
                list = new ArrayList<Size>();
                for (int j = 0; j < supportPicture.size(); j++) {
                    Size picture = supportPicture.get(j);
                    int a1 = picture.width;
                    int b1 = picture.height;
                    double d1 = (double) a1 / b1;
                    String str1 = String.format("%.4f", d1);
                    if (str.equals(str1)) {
                        list.add(picture);
                        continue;
                    }
                }
            }
            mHashPicRatio.put(mSupportSizeList.get(i), list);
        }
        for (int i = 0; i < mSupportSizeList.size(); i++) { // 获取最佳视频分辨率
            String str_ = mSupportSizeList.get(i);
            String[] str = str_.split(":");
            int width = Integer.parseInt(str[0]);
            int height = Integer.parseInt(str[1]);
            double d1 = (double) width / height;
            String str1 = String.format("%.4f", d1);
            ArrayList<Size> al = new ArrayList<Size>();
            for (int j = 0; j < supportVideo.size(); j++) {
                Size preview = supportVideo.get(j);
                int width_ = preview.width;
                int height_ = preview.height;
                double d3 = (double) width_ / height_;
                String str3 = String.format("%.4f", d3);
                if (str1.equals(str3)) {
                    al.add(preview);
                }
            }
            mHashVideoRatio.put(str_, al);
        }
        /* 先获取全屏预览尺寸，如果摄像头不支持，则采用标准照片尺寸比例（4：3） */
//        String ratio = Global.getScreenRatio(Global.screenHeight, Global.screenWidth);
        ArrayList<Size> al_pre_ = mHashPreRatio.get("4:3");
        ArrayList<Size> al_pic_ = mHashPicRatio.get("4:3");
        ArrayList<Size> al_video_ = mHashVideoRatio.get("4:3");
        if (cameraid == 0) {
            Size size = getPicSize(al_pic_);
            if (size != null) {
                Log.e("size", size.width + "--" + size.height);
                setmBestPictureSizeBack(size);
            } else {
                if (al_pic_.size() >= 1) {
                    if (al_pic_.size() >= 2) {
                        if (al_pic_.size() >= 3) {
                            setmBestPictureSizeBack(al_pic_.get(2));
                        } else {
                            setmBestPictureSizeBack(al_pic_.get(1));
                        }
                    } else {
                        setmBestPictureSizeBack(al_pic_.get(1));
                    }
                }
            }
            setmBestPreviewSizeBack(al_pre_.get(0));
            if (al_video_.size() > 0) {
                setmBestVideoSizeBack(al_video_.get(0));
            }
        } else {
            if (al_pic_.size() >= 1) {
                if (al_pic_.size() >= 2) {
                    setmBestPictureSizeFront(al_pic_.get(1));
                } else {
                    setmBestPictureSizeFront(al_pic_.get(0));
                }
            }
            setmBestPreviewSizeFront(al_pre_.get(0));
            if (al_video_.size() > 0) {
                setmBestVideoSizeFront(al_video_.get(0));
            }
        }
    }

    public List<Size> videoStringToList(Camera camera, String supportVideo) {
        String[] strVideo = supportVideo.split(",");
        List<Size> list = new ArrayList<Size>();
        for (int i = 0; i < strVideo.length; i++) {
            String temp[] = strVideo[i].split("x");
            Size size = camera.new Size(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
            list.add(size);
        }
        return list;
    }

    /**
     * 关闭摄像头，回收资源
     */
    public void closeCamera(Camera camera) {
        if (camera == null) {
            return;
        }
        try {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        } catch (Exception e) {
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
            e.printStackTrace();
        }

    }

    public HashMap<String, ArrayList<Size>> getmHashMapRatio() {
        return mHashPicRatio;
    }

    public void setmHashMapRatio(HashMap<String, ArrayList<Size>> mHashMapRatio) {
        this.mHashPicRatio = mHashMapRatio;
    }

    public Size getmBestPreviewSizeBack() {
        return mBestPreviewSizeBack;
    }

    public void setmBestPreviewSizeBack(Size mBestPreviewSizeBack) {
        this.mBestPreviewSizeBack = mBestPreviewSizeBack;
    }

    public Size getmBestVideoSizeBack() {
        return mBestVideoSizeBack;
    }

    public void setmBestVideoSizeBack(Size mBestVideoSizeBack) {
        this.mBestVideoSizeBack = mBestVideoSizeBack;
    }

    public Size getmBestVideoSizeFront() {
        return mBestVideoSizeFront;
    }

    public void setmBestVideoSizeFront(Size mBestVideoSizeFront) {
        this.mBestVideoSizeFront = mBestVideoSizeFront;
    }

    public Size getmBestPictureSizeBack() {
        return mBestPictureSizeBack;
    }

    public void setmBestPictureSizeBack(Size mBestPictureSizeBack) {
        this.mBestPictureSizeBack = mBestPictureSizeBack;
    }

    public Size getmBestPreviewSizeFront() {
        return mBestPreviewSizeFront;
    }

    public void setmBestPreviewSizeFront(Size mBestPreviewSizeFront) {
        this.mBestPreviewSizeFront = mBestPreviewSizeFront;
    }

    public Size getmBestPictureSizeFront() {
        return mBestPictureSizeFront;
    }

    public void setmBestPictureSizeFront(Size mBestPictureSizeFront) {
        this.mBestPictureSizeFront = mBestPictureSizeFront;
    }

    public boolean isSupportContinueFocus() {
        return supportContinueFocus;
    }

    public void setSupportContinueFocus(boolean supportContinueFocus) {
        this.supportContinueFocus = supportContinueFocus;
    }

    public boolean isSupportAutoFocus() {
        return supportAutoFocus;
    }

    public void setSupportAutoFocus(boolean supportAutoFocus) {
        this.supportAutoFocus = supportAutoFocus;
    }

    public int[] getmBestPreviewFps() {
        return mBestPreviewFps;
    }

    public void setmBestPreviewFps(int mMaxBestPreviewFps, int mSecBestPreviewFps) {
        this.mBestPreviewFps[0] = mSecBestPreviewFps;
        this.mBestPreviewFps[1] = mMaxBestPreviewFps;
    }

    class ComparatorValues implements Comparator<Size> {
        @Override
        public int compare(Size object1, Size object2) {
            long m1 = object1.width * object1.height;
            long m2 = object2.width * object2.height;
            int result = 0;
            if (m1 > m2) {
                result = -1;
            }
            if (m1 < m2) {
                result = 1;
            }
            return result;
        }
    }

    class ComparatorFpsValues implements Comparator<Integer> {
        @Override
        public int compare(Integer object1, Integer object2) {
            int m1 = object1;
            int m2 = object2;
            int result = 0;
            if (m1 > m2) {
                result = -1;
            }
            if (m1 < m2) {
                result = 1;
            }
            return result;
        }
    }

    private Size getPicSize(ArrayList<Size> list) {
        if (list != null && list.size() > 0) {
            int num = list.size();
            int index = 0;
            // if (list != null && list.size() > 2) {
            // return list.get(1);
            // } else if (list.size() > 0 && list.size() <= 1) {
            // return list.get(0);
            // }
            for (int i = 0; i < num; i++) {
                int height = list.get(i).height;
                int width = list.get(i).width;
                long result = height * width;
                String size = String.format("%.2f", (double) result / 1024 / 1024);
                float f = Float.parseFloat(size);
                if (f > 2.0f) {
                    continue;
                } else {
                    index = i;
                    break;
                }
            }
            return list.get(index);
        }
        return null;
    }

    // /**
    // * 检查Camera是否有效
    // *
    // * @param context
    // * @return
    // */
    // public static boolean checkCameraHardware(Context context) {
    // if
    // (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
    // {
    // return true;
    // } else {
    // return false;
    // }
    //
    // }

}
