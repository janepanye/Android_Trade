package com.coco3g.caopantx.data;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coco3g.caopantx.bean.JPushData;
import com.coco3g.caopantx.bean.UserData;
import com.coco3g.caopantx.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Global {
    public static final String APP_CACHE = "data"; // 用户信息
    public static final String LOGIN_INFO = "login"; // 用户登录信息
    public static final String rootPath = "com_app_weiyi";
    public static final String localThumbPath = "thumbnail"; // 应用的图片存放目录
    public static final String localVideoPath = "video"; // 应用的视频存放目录
    public static final String localAudioPath = "audio"; // 应用的音频存放目录
    public static final String login_user_phone = "login_user_phone"; // 登录用户的手机号

    public static final String login_user_pwd = "login_user_pwd"; // 登录用户的密码

    //关闭activity的广播的action
    public static final String FINISH_MAINACTIVITY_ACTION = "com.coco3g.zhong.utils.FinishMainActivityBroadcast";
    public static UserData.UserInfo USERINFO = null; // 用户信息
    public static String UPDATE_TIME = ""; // 新的派单-最新时间
    public static String LAT = "", LNG = "";

    public static ArrayList<Activity> REGISTERINFLIST = new ArrayList<>();//注册界面activity集合
    /**
     * height为高的一边，width为低的一边
     **/
    public static int screenHeight, screenWidth, topbarHeight;
    static FileOutputStream fos;
    static ObjectOutputStream oos;
    static FileInputStream fis = null;
    static ObjectInputStream ois = null;
    public static String MODEL = "";
    public static String IMEI = "";
    public static String MAC_ADDRESS = "";
    /**
     * SDK版本号
     **/
    public static int SDK_VERSION = 0;
    public static HashMap<String, String> LOGIN_INFO_MAP = null;
    //
    public final static int RESULT_CHECK_GALLERY_CODE = 14; // 系统相册中，选择的图片后，返回的code
    public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 10; //相册返回code
    public static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 11; // 相机拍照返回code
    public final static int RESULT_CHECK_VIDEO_CODE = 13; // 录制视频后，返回的code
    public static final int REQUEST_CODE_GETIMAGE_BYCROP = 12; //图片裁剪返回code
    public static final int REFRESH_DATA = 103; // 返回后数据刷新操作
    public static final int RETURN_PAY_CODE = 1000; // 银生宝支付返回code
    //
    public static JPushData NOTICE_DATA = null; // 通知下发，携带的数据

    /**
     * 获取当前SDK版本号
     *
     * @return
     */
    public static int getAndroidSDKVersion() {
        int version = 0;
        try {
            version = Build.VERSION.SDK_INT;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

    /**
     * 获取屏幕长宽
     *
     * @param context
     */
    public static void getScreenWH(Context context) {
        WindowManager w = ((Activity) context).getWindowManager();
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17) try {
            screenHeight = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        else if (Build.VERSION.SDK_INT >= 17) {
            try {
                android.graphics.Point realSize = new android.graphics.Point();
                Display.class.getMethod("getRealSize", android.graphics.Point.class).invoke(d, realSize);
                screenHeight = realSize.y;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.e("w-h", screenWidth + "----" + screenHeight);
    }

    /**
     * 获取IMEI号，IESI号，手机型号
     */
    /*public static void getInfo(Context context) {
        TelephonyManager mTm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = mTm.getDeviceId();
        String imsi = mTm.getSubscriberId();
        String mtype = Build.MODEL; // 手机型号
        String mtyb = Build.BRAND;// 手机品牌
        String numer = mTm.getLine1Number(); // 手机号码，有的可得，有的不可得
        IMEI = imei;
        Log.i("text", "手机IMEI号:" + imei + " 手机IESI号:" + imsi + " 手机型号:" + mtype + " 手机品牌:" + mtyb + " 手机号码:" + numer);
        MODEL = "手机IMEI号:" + imei + " 手机IESI号:" + imsi + " 手机型号:" + mtype + " 手机品牌:" + mtyb + " 手机号码:" + numer + " MAC:" +
                getMacAddress(context);
    }*/

    /**
     * 获取手机MAC地址 只有手机开启wifi才能获取到mac地址
     */
    /*public static String getMacAddress(Context context) {
        MAC_ADDRESS = "";
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            MAC_ADDRESS = wifiInfo.getMacAddress();
            Log.e("text", " MAC:" + MAC_ADDRESS);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return MAC_ADDRESS;
    }*/

    /**
     * 获取通知栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 获取当前屏幕的长宽比
     *
     * @param a
     * @param b
     * @return
     */
    public static String getScreenRatio(int a, int b) {
        int min;
        int temp = 1;
        min = (a < b) ? a : b;
        for (int i = 1; i <= min; i++) {
            if (a % i == 0 && b % i == 0) temp = i;
        }
        int a_ = a / temp;
        int b_ = b / temp;
        return a_ + ":" + b_;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int pxTodip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dipTopx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 获取应用根目录
     *
     * @param c
     * @return
     */
    public static String getPath(Context c) {
        String path = null;
        if (c == null) return null;
        File file = null;
        if (isSdcardInsert()) {
            File sdDir = Environment.getExternalStorageDirectory();
            path = sdDir.toString() + File.separator + rootPath;
        } else {
            path = c.getFilesDir().getPath() + File.separator + rootPath;
        }
        file = new File(path);
        if (!file.exists()) file.mkdirs();
        return path;
    }

    /**
     * 判断是否有SD卡
     *
     * @return
     */
    public static boolean isSdcardInsert() {
        boolean isInsert = false;
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            isInsert = false;
        } else isInsert = true;
        return isInsert;
    }

    /**
     * 获取应用版本号
     *
     * @return
     * @throws Exception
     */
    public static int getAppVersionCode(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (packInfo == null) {
            return Integer.MAX_VALUE;
        }
        return packInfo.versionCode;
    }

    /**
     * 获取应用版本号
     *
     * @return
     * @throws Exception
     */
    public static String getAppVersion(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (packInfo == null) {
            return "";
        }
        return "v" + packInfo.versionName;
    }

    /**
     * 从SD卡中删除文件
     *
     * @param path
     * @return
     */
    public static boolean deleteFileFromSD(String path) {
        if (path == null) {
            return false;
        }
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        return file.delete();
    }

    @SuppressLint("NewApi")
    public static int getRealScreenSize(boolean isWidth, Activity act) {
        final DisplayMetrics metrics = new DisplayMetrics();
        Display display = act.getWindowManager().getDefaultDisplay();
        if (hasPermanentMenuKey(act)) {
            act.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int width = metrics.widthPixels; // 屏幕宽度（像素）
            int height = metrics.heightPixels; // 屏幕高度（像素）

            if (isWidth) return width;
            else return height;
        } else {
            Method mGetRawH = null, mGetRawW = null;

            // Not real dimensions
            display.getMetrics(metrics);
            int width = metrics.heightPixels;
            int height = metrics.widthPixels;

            try {
                // For JellyBeans and onward
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    display.getRealMetrics(metrics);

                    // Real dimensions
                    width = metrics.heightPixels;
                    height = metrics.widthPixels;
                } else {
                    mGetRawH = Display.class.getMethod("getRawHeight");
                    mGetRawW = Display.class.getMethod("getRawWidth");

                    try {
                        width = (Integer) mGetRawW.invoke(display);
                        height = (Integer) mGetRawH.invoke(display);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            } catch (NoSuchMethodException e3) {
                e3.printStackTrace();
            }

            if (isWidth) {
                return width;
            } else {
                return height;
            }
        }
    }

    public static boolean hasPermanentMenuKey(Context con) {
        ViewConfiguration configuration = ViewConfiguration.get(con);
        try {
            Object result = configuration.getClass().getMethod("hasPermanentMenuKey", new Class[]{}).invoke(configuration, new
                    Object[]{});

            if (result != null && result instanceof Boolean)
                return Boolean.parseBoolean(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * 将数据序列化到本地
     *
     * @param context
     * @param hm
     */
    public static void serializeData(Context context, Object hm, String dir) {
        String path = context.getFilesDir().getPath() + File.separator + dir;
        File f = new File(path);
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            fos = new FileOutputStream(f);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(hm);
            oos.flush();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读取序列化数据
     *
     * @param context
     * @return
     */
    public static Object readSerializeData(Context context, String dir) {
        String path = context.getFilesDir().getPath() + File.separator + dir;
        Object o = new Object();
        File f = new File(path);
        if (f.exists()) {
            try {
                fis = new FileInputStream(f);
                ois = new ObjectInputStream(fis);
                o = ois.readObject();
                fis.close();
                ois.close();
                return o;
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (StreamCorruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }

    /**
     * 保存应用中的轻量级数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveTempData(Context context, String key, String value) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences("daling_temp_data", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 读取应用中的轻量级数据
     *
     * @param context
     * @param key
     * @return
     */
    public static String readTempData(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("daling_temp_data",
                Activity.MODE_PRIVATE);
        String value = sharedPreferences.getString(key, "");
        return value;
    }

    /**
     * 验证登录
     *
     * @param context
     */
    public static boolean verifyLogin(Context context) {
        if (Global.USERINFO == null) { // 还未登录
            return false;
        } else {
            return true;
        }
    }

    /**
     * 验证手机号的合法性
     *
     * @param phone
     * @return
     */
    public static boolean verifyPhone(String phone) {
        String rule = "^1(3|5|7|8|4)\\d{9}";
        Pattern p = Pattern.compile(rule);
        Matcher m = p.matcher(phone);
        if (!m.matches()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 保存登录信息
     *
     * @param context
     * @param username
     * @param password
     */
    public static void saveLoginInfo(Context context, String username, String password) {
        HashMap<String, String> loginmap = new HashMap<String, String>();
        loginmap.put("username", username);
        loginmap.put("password", password);
        serializeData(context, loginmap, LOGIN_INFO);
    }

    /**
     * 读取登录信息
     *
     * @param context
     * @return
     */
    @SuppressWarnings("unchecked")
    public static HashMap<String, String> readLoginInfo(Context context) {
        return (HashMap<String, String>) readSerializeData(context, LOGIN_INFO);
    }

    /**
     * 删除序列化
     *
     * @param context
     * @param dir
     */
    public static void deleteSerializeData(Context context, String dir) {
        if (context == null) {
            Log.e("路径", "context为空啊");
        }
        String path = context.getFilesDir().getPath() + File.separator + dir;
        File f = new File(path);
        Log.e("路径", path + "jkjdk");
        if (f.exists()) {
            f.delete();
        }
    }

    /**
     * 清空文件夹
     *
     * @param file
     */
    public static void deleteDir(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteDir(files[i]);
                }
            }
            file.delete();
        } else {

        }
    }

    /**
     * 获取文件夹大小
     *
     * @param f
     * @return
     */
    public static long getFileSize(File f) {
        if (!f.exists()) {
            return 0;
        }
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = 0 + "K";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 获取多媒体目录
     *
     * @param context
     * @param filedir
     * @param filename
     * @return
     */
    public static String getDirPath(Context context, String filedir, String filename) {
        File file1 = new File(getPath(context) + File.separator + filedir);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        File file2 = new File(file1.getAbsolutePath() + File.separator + filename);
        if (!file2.exists()) {
            try {
                file2.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file2.getAbsolutePath();
    }

    public static String durationToTime(long duration) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm′ss″");
        String hms = formatter.format(duration - 8 * 60 * 60 * 1000);
        return hms;
    }

    static Toast toast;

    public static void showToast(String content, Context con) {
        // Toast toast = getToast(con, content, -1);
        // toast.show();
        if (con == null) {
            return;
        }
        if (content == null) {
            content = "null";
        }
        if (toast == null) {
            toast = new Toast(con);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        RelativeLayout relative = new RelativeLayout(con);
        relative.setBackgroundResource(R.mipmap.pic_toast_bg);
        TextView textView = new TextView(con);
        textView.setText(content);
        textView.setTextSize(15f);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);
        relative.addView(textView, lp);

        toast.setView(relative);
        toast.show();
    }

    /**
     * 根据文件绝对路径获取文件名
     *
     * @param filePath
     * @return
     */
    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath)) return "";
        return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
    }

    /**
     * 获取文件扩展名
     *
     * @param fileName
     * @return
     */
    public static String getFileFormat(String fileName) {
        if (TextUtils.isEmpty(fileName)) return "";

        int point = fileName.lastIndexOf('.');
        return fileName.substring(point + 1);
    }

    public static String convertToThumb(String url) {
        if (url == null) {
            return "";
        }
        String newurl;
        if (url.contains(".")) {
            try {
                int index = url.lastIndexOf(".");
                newurl = url.substring(0, index) + "_1" + url.substring(index);
                return newurl;
            } catch (Exception e) {
                e.printStackTrace();
                return url;
            }

        }
        return url;
    }

    /**
     * 元转换成分
     *
     * @param source
     * @return
     */
    public static int currencyConvert(String source) {
        float sourceF = Float.parseFloat(source);
        DecimalFormat df = new DecimalFormat("#.00");
        float sourceS = Float.parseFloat(df.format(sourceF));
        int target = (int) (sourceS * 100);
        return target;
    }

    /**
     * MD5加密
     *
     * @param info
     * @return
     */
    public static String getMD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();

            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }

            return strBuf.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static HashMap<String, String> parseCustomUrl(String url) {
        HashMap<String, String> hashmap = new HashMap<>();
        if (!TextUtils.isEmpty(url)) {
            String[] str1 = url.split("&");
            if (str1 != null && str1.length > 0) {
                for (String keyvalue : str1) {
                    if (keyvalue.contains("=")) {
                        String[] str2 = keyvalue.split("=");
                        hashmap.put(str2[0], str2[1]);
                    }
                }
            }
        }
        return hashmap;
    }

    /**
     * 清除cookie
     *
     * @param context
     */
    public static void clearCookie(Context context) {
        CookieSyncManager.createInstance(context.getApplicationContext());
        CookieManager.getInstance().removeAllCookie();
    }

    /**
     * 拨打电话
     *
     * @param context
     * @param number
     */
    public static void callPhone(Context context, String number) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + number);
        intent.setData(data);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Global.showToast("没有相关权限", context);
            return;
        }
        context.startActivity(intent);
    }
}
