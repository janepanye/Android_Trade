package com.coco3g.caopantx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.coco3g.caopantx.bean.BaseData;
import com.coco3g.caopantx.bean.UserData;
import com.coco3g.caopantx.data.Global;
import com.coco3g.caopantx.listener.IBaseDataListener;
import com.coco3g.caopantx.presenter.PublicPresenter;
import com.coco3g.caopantx.view.TimingView;
import com.coco3g.caopantx.R;
import com.coco3g.caopantx.listener.IUserListenter;

/**
 * Created by MIN on 16/6/12.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private EditText mEtPhone, mEtCode, mEtPwd, mEtRecommendCode;
    private Button mBtnRegister;
    TimingView mTxtGetVerify;
    private CheckBox mCbxProtocal;
    private TextView mHaveAccount, mRegisterXieYi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();

    }

    private void initView() {
        mEtPhone = (EditText) findViewById(R.id.register_et_phone);
        mEtCode = (EditText) findViewById(R.id.register_et_code);
        mEtPwd = (EditText) findViewById(R.id.register_et_pwd);
        mEtRecommendCode = (EditText) findViewById(R.id.register_et_recommend_code);
        mTxtGetVerify = (TimingView) findViewById(R.id.register_btn_getcode);
        mTxtGetVerify.setTimeInterval(1000);
        mTxtGetVerify.setMaxSecond(60);
        mTxtGetVerify.setOnClickListener(this);
        mBtnRegister = (Button) findViewById(R.id.register_btn_register);
        mCbxProtocal = (CheckBox) findViewById(R.id.register_cbx_protocal);
        mHaveAccount = (TextView) findViewById(R.id.register_tv_have_account);
        mRegisterXieYi = (TextView) findViewById(R.id.register_tv_xieyi);
        mBtnRegister.setOnClickListener(this);
        mHaveAccount.setOnClickListener(this);
        mRegisterXieYi.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_btn_getcode:
                if (TextUtils.isEmpty(mEtPhone.getText().toString().trim())) {
                    Global.showToast("请先输入手机号", getApplication());
                    return;
                }
                getVerifierCode(mEtPhone.getText().toString().trim());
                break;
            case R.id.register_btn_register:

                String phone = mEtPhone.getText().toString().trim();
                String code = mEtCode.getText().toString().trim();
                //号码为空时点击注册
                if (TextUtils.isEmpty(phone)) {
                    Global.showToast("请输入手机号", this);
                    return;
                } else if (phone.length() != 11) {
                    Global.showToast("手机号码不正确", this);
                    return;
                }
                //手机号正确,验证码为空
                if (TextUtils.isEmpty(code)) {
                    Global.showToast("请获取验证码", this);
                    return;
                }

                String pwd = mEtPwd.getText().toString().trim();
                String recommendCode = mEtRecommendCode.getText().toString().trim();
                //密码检测
                if (TextUtils.isEmpty(pwd)) {
                    Global.showToast("请设置登录密码", this);
                    return;
                }
                if (!mCbxProtocal.isChecked()) {
                    Global.showToast("请先阅读协议", this);
                    return;
                }
                register(phone, pwd, code, recommendCode);

//                Intent intent = new Intent(RegisterActivity.this, RegisterNextActivity.class);
//                intent.putExtra("phone", phone);
//                intent.putExtra("code", code);
//                Global.REGISTERINFLIST.add(RegisterActivity.this);
//                startActivity(intent);
                break;
            case R.id.register_tv_have_account:
//                Intent intent1 = new Intent(RegisterActivity.this, LoginActivity.class);
//                startActivity(intent1);
                finish();
//                if (Global.REGISTERINFLIST != null) {
//                    for (Activity activity : Global.REGISTERINFLIST) {
//                        activity.finish();
//                    }
//                }
                Global.REGISTERINFLIST.clear();
                break;
            case R.id.register_tv_xieyi:
                Intent intent = new Intent(RegisterActivity.this, XieYiActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 获取验证码
     *
     * @param phone
     */
    private void getVerifierCode(String phone) {
        new PublicPresenter(RegisterActivity.this).sendSms(phone, "reg", new IBaseDataListener() {
            @Override
            public void onSuccess(BaseData data) {
                mTxtGetVerify.startTiming();
            }

            @Override
            public void onFailure(BaseData data) {

            }

            @Override
            public void onError() {

            }
        });
    }

    /**
     * 注册
     */
    public void register(final String username, final String pwd, final String code, final String recommendCode) {
        new PublicPresenter(RegisterActivity.this).register(username, pwd, code, recommendCode, new IUserListenter() {
            @Override
            public void onSuccess(UserData data) {
//                if (MainActivity.MAIN_CONTEXT != null) {
//                    ((Activity) MainActivity.MAIN_CONTEXT).finish();
//
//                }
                Global.USERINFO = data.response;
                Global.serializeData(RegisterActivity.this, username, Global.login_user_phone);
//                Global.saveLoginInfo(RegisterActivity.this, username, pwd);
//                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                Global.REGISTERINFLIST.add(RegisterActivity.this);
//                startActivity(intent);
                finish();
                for (Activity activity : Global.REGISTERINFLIST) {
                    activity.finish();
                }
                Global.REGISTERINFLIST.clear();
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
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mTxtGetVerify.stopTiming();
    }


}
