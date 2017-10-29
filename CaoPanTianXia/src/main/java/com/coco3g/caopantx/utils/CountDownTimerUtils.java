package com.coco3g.caopantx.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Button;

import com.coco3g.caopantx.R;

/**
 * Created by MIN on 16/5/9.
 * 发送验证码按钮倒计时
 */
public class CountDownTimerUtils extends CountDownTimer {
    private Button mCode;
    private Context mContext;

    public CountDownTimerUtils(long millisInFuture, long countDownInterval, Button mCode, Context context) {
        super(millisInFuture, countDownInterval);
        this.mCode = mCode;
        this.mContext = context;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mCode.setClickable(false);
        mCode.setText(millisUntilFinished / 1000 + "s后重新发送");
        mCode.setTextColor(mContext.getResources().getColor(R.color.color_line_bg));
    }

    @Override
    public void onFinish() {
        mCode.setClickable(true);
        mCode.setText("发送验证码");
        mCode.setTextColor(mContext.getResources().getColor(R.color.color_line_bg));
    }
}
