package com.coco3g.caopantx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.coco3g.caopantx.bean.BaseData;
import com.coco3g.caopantx.bean.UserData;
import com.coco3g.caopantx.utils.Coco3gBroadcastUtils;
import com.coco3g.caopantx.view.MyScrollWebView;
import com.coco3g.caopantx.R;
import com.coco3g.caopantx.data.Global;
import com.coco3g.caopantx.listener.IUserListenter;
import com.coco3g.caopantx.presenter.PublicPresenter;

import java.util.HashMap;

/**
 * Created by MIN on 16/6/12.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    /**** 手机号、密码 ***/
    private EditText mEtPhone, mEtPwd;
    /**** 忘记密码、注册 ***/
    private TextView mTvForgetPwd, mBtnRegister;
    /**** 登陆按钮 ***/
    private Button mBtnLogin;

    private MyScrollWebView mWebView;
    //判断登录页面是否由锁屏超时而来
    public static boolean fromLockScreenBroadcast = false;
    //登录人的登录信息
    HashMap<String, String> userInfo;
    String hideStr = "";
    private boolean finishFromAfterLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

    }

    private void initView() {
        mEtPhone = (EditText) findViewById(R.id.login_et_phone);
        mEtPwd = (EditText) findViewById(R.id.login_et_password);
        mTvForgetPwd = (TextView) findViewById(R.id.login_tv_forgetpwd);
        mBtnLogin = (Button) findViewById(R.id.login_btn_login);
        mBtnRegister = (TextView) findViewById(R.id.login_btn_register);
        mBtnLogin.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
        mTvForgetPwd.setOnClickListener(this);
//       readSerializeData读取本地序列化数据
        if (Global.readSerializeData(LoginActivity.this, Global.login_user_phone) != null && Global.readSerializeData(LoginActivity.this, Global.login_user_pwd) != null) {
            String realPhone = (String) Global.readSerializeData(LoginActivity.this, Global.login_user_phone);
            String password = (String) Global.readSerializeData(LoginActivity.this, Global.login_user_pwd);
            if (!TextUtils.isEmpty(realPhone)) {
                if (realPhone.length() == 11) {
                    String showPhone1 = realPhone.substring(0, 3);
                    hideStr = realPhone.substring(3, 7);
                    String showphone2 = realPhone.substring(7);
                    mEtPhone.setText(showPhone1 + "****" + showphone2);
                } else {
                    mEtPhone.setText(realPhone);
                }
            }

            if (!TextUtils.isEmpty(password)){
                mEtPwd.setText(password);

            }

        }
//        mEtPhone.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Global.serializeData(LoginActivity.this, s, Global.login_user_phone);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        //这是从锁屏过来的
//        if (fromLockScreenBroadcast) {
//            String username = getIntent().getStringExtra("username");
//            mEtPhone.setText(username);
//            //删除保存的登录用户的信息
//            Global.deleteSerializeData(this, Global.LOGIN_INFO);
//            Global.USERINFO = null;
//        }

    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login_btn_login:
                String phone = mEtPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Global.showToast("请输入手机号", getApplication());
                    return;
                } else {
                    if (phone.contains("****") && phone.length() == 11) {
                        phone = phone.replace("****", hideStr);
                    }
                }
                String pwd = mEtPwd.getText().toString().trim();
                if (TextUtils.isEmpty(pwd)) {
                    Global.showToast("请输入密码", getApplication());
                    return;
                }
                login(phone, pwd);
                break;
            case R.id.login_btn_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                Global.REGISTERINFLIST.add(LoginActivity.this);
                startActivity(intent);
                break;
            case R.id.login_tv_forgetpwd:
                Intent intent1 = new Intent(LoginActivity.this, ForgetPwdActivity.class);
                startActivity(intent1);
                break;
        }
    }

    /**
     * 登录
     */
    public void login(final String phone, final String pwd) {
        new PublicPresenter(LoginActivity.this).login(phone, pwd, new IUserListenter() {
            @Override
            public void onSuccess(UserData data) {
//                if (MainActivity.MAIN_CONTEXT != null) {
//                    ((Activity) MainActivity.MAIN_CONTEXT).finish();
//
//                }
                Global.USERINFO = data.response;

//                Global.saveLoginInfo(LoginActivity.this, phone, pwd);//  PYTODO
                finishFromAfterLogin = true;

                //   将数据序列化到本地
                Global.serializeData(LoginActivity.this, phone, Global.login_user_phone);
                Global.serializeData(LoginActivity.this, pwd, Global.login_user_pwd);
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(BaseData data) {

            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!finishFromAfterLogin) {
            new Coco3gBroadcastUtils(LoginActivity.this).sendBroadcast(Coco3gBroadcastUtils.RETURN_HOME_FLAG, null);
        }
    }

}
