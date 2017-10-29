package com.coco3g.caopantx.data;

/**
 * Created by lisen on 16/7/25.
 */
public class Constants {
    // socket请求地址
    public final static String SOCKET_BASE_URL = "ws://121.196.199.38:";
    // socket连接端口
    public final static int SOCKET_PORT = 8282;
    // 请求超时时间
    public final static int SOCKET_TIME_OUT = 10000;
    // 获取手机验证码
    public final static String GET_VERIFY_COMMAND = "GetMsg_Reg";
    // 注册
    public final static String REGISTER_COMMAND = "Register";
    // 认证/登录
    public final static String AUTH_LOGIN_COMMAND = "LoginAuth";
    // 签名
    public final static String SIGN_COMMAND = "Sign";
    // 签名时获取手机验证码
    public final static String SIGN_GET_VERIFY_COMMAND = "GetMsg";
    // 微信
    public static final String WEIXIN_APP_ID = "wx0c9622ef8dd200aa";
    // QQ
    public static final String QQ_APP_ID = "1105555593";
    // 极光推送
    public static String JPUSH_REGISTERID = "";
}
