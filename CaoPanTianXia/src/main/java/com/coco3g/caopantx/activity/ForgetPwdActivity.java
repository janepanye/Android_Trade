package com.coco3g.caopantx.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.coco3g.caopantx.view.TopBarView;
import com.coco3g.caopantx.R;
import com.coco3g.caopantx.bean.BaseData;
import com.coco3g.caopantx.data.Global;
import com.coco3g.caopantx.listener.IBaseDataListener;
import com.coco3g.caopantx.presenter.PublicPresenter;
import com.coco3g.caopantx.utils.CountDownTimerUtils;

/**
 * Created by MIN on 16/6/18.
 */
public class ForgetPwdActivity extends BaseActivity {
    private TopBarView mTopBar;
    private EditText mPhone, mCode, mPwd, mConfirmPwd;
    private Button mGetCode, mConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpwd);
        initView();
    }

    private void initView() {
        mTopBar = (TopBarView) findViewById(R.id.forgetpwd_topbar);
        mTopBar.setTitle(getResources().getString(R.string.text_login_forgetpwd));
        mPhone = (EditText) findViewById(R.id.forgetpwd_et_phone);
        mCode = (EditText) findViewById(R.id.forgetpwd_et_code);
        mPwd = (EditText) findViewById(R.id.forgetpwd_et_pwd);
        mConfirmPwd = (EditText) findViewById(R.id.forgetpwd_et_confirm_pwd);
        mGetCode = (Button) findViewById(R.id.forgetpwd_btn_getcode);
        mConfirm = (Button) findViewById(R.id.forgetpwd_btn_confirm);
        mGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mPhone.getText().toString().trim();
                if (!TextUtils.isEmpty(phone)) {
                    getVerifierCode(phone);
                }
            }
        });
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mPhone.getText().toString().trim();
                String code = mCode.getText().toString().trim();
                String pwd = mPwd.getText().toString().trim();
                String confirmPwd = mConfirmPwd.getText().toString().trim();
                if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(code) && !TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(confirmPwd)) {
                    if (!pwd.equals(confirmPwd)) {
                        Global.showToast("两次密码输入不一致", getApplication());
                        return;
                    }
                } else {
                    Global.showToast("请输入完整信息", getApplication());
                    return;
                }
                resetPwd(phone, pwd, code);
            }
        });
    }

    /**
     * 获取验证码
     *
     * @param phone
     */
    private void getVerifierCode(String phone) {
        new PublicPresenter(ForgetPwdActivity.this).sendSms(phone, "pwd", new IBaseDataListener() {
            @Override
            public void onSuccess(BaseData data) {
                CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(60000, 1000, mGetCode, getApplicationContext());
                countDownTimerUtils.start();
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
     * 重置密码
     *
     * @param phone
     * @param pwd
     * @param code
     */
    public void resetPwd(final String phone, final String pwd, String code) {
        new PublicPresenter(ForgetPwdActivity.this).forgetPwd(phone, pwd, code, new IBaseDataListener() {
            @Override
            public void onSuccess(BaseData data) {
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
}
