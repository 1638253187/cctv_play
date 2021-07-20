package com.zkzj.rtmp_terminal.app;

import android.app.Application;
import android.util.Log;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

public class TvApp extends Application {
    private TvApp instances;

    @Override
    public void onCreate() {
        super.onCreate();
        instances = this;
/**
 * 初始化common库
 * 参数1:上下文，不能为空
 * 参数2:友盟 app key
 * 参数3:友盟 channel，channel参数是为了统计我们的渠道，当没有渠道时我默认的是三星渠道。
 * 参数4:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
 * 参数5:Push推送业务的secret，没有集成推送业务写空就可以
 */
        String channelName = AnalyticsConfig.getChannel(this);
        if (channelName == null || "".equals(channelName)) {
            channelName = "APP-AN-HUAWEI";
        }
        UMConfigure.init(this, "609b3c4a53b6726499f90c30", channelName, UMConfigure.DEVICE_TYPE_PHONE, "");
        //设置是否打印日志 true和false
        UMConfigure.setLogEnabled(true);
        // 我选用的是手动采集模式MANUAL，下面会讲解一下页面采集模式。
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);

    }


}