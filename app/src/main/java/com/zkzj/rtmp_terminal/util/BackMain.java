package com.zkzj.rtmp_terminal.util;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;

import com.zkzj.rtmp_terminal.main.MainActivity;
public class BackMain extends CountDownTimer {

    private Context context;

    /**
     * 参数 millisInFuture       倒计时总时间（如60S，120s等）
     * 参数 countDownInterval    渐变时间（每次倒计1s）
     */
    public BackMain(long millisInFuture, long countDownInterval, Context context) {
        super(millisInFuture, countDownInterval);
        this.context = context;
    }

    // 计时完毕时触发
    @Override
    public void onFinish() {
        MainActivity.res_container.setVisibility(View.GONE);
        MainActivity.NotTouch = 1;
    }

    // 计时过程显示
    @Override
    public void onTick(long millisUntilFinished) {
    }


}