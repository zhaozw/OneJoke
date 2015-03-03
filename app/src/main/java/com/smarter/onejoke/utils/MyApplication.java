package com.smarter.onejoke.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.thinkland.sdk.android.SDKInitializer;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

/**
 * Created by panl on 15/2/8.
 */
public class MyApplication extends Application {
    public static final String APP_ID = "2882303761517307330";
    public static final String APP_KEY = "5921730753330";
    public static final String TAG = "com.smarter.onejoke";

    @Override
    public void onCreate() {
        super.onCreate();
        //聚合数据sdk初始化
        SDKInitializer.initialize(getApplicationContext());
        //小米推送
        //初始化push推送服务
        if(shouldInit()) {
            MiPushClient.registerPush(this, APP_ID, APP_KEY);
        }
        //打开Log
        LoggerInterface newLogger = new LoggerInterface() {
            @Override
            public void setTag(String tag) {
                // ignore
            }
            @Override
            public void log(String content, Throwable t) {
                Log.d(TAG, content, t);
            }
            @Override
            public void log(String content) {
                Log.d(TAG, content);
            }
        };
        Logger.setLogger(this, newLogger);
    }
    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
