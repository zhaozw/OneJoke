package com.smarter.onejoke.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.kyview.AdViewTargeting;
import com.kyview.interfaces.AdSpreadInterface;
import com.kyview.screen.spreadscreen.AdSpreadManager;
import com.smarter.onejoke.R;

public class WelcomeActivity extends BaseActivity implements AdSpreadInterface{

    private AdSpreadManager adSpreadManager = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mTintManager.setStatusBarDarkMode(true,this);

        AdViewTargeting.setBannerSwitcherMode(AdViewTargeting.BannerSwitcher.CANCLOSED);
        // 初始化开屏广告 ,需要传入布局用来加载开屏页面
        adSpreadManager = new AdSpreadManager(this,
                "SDK20151024100234gyduoom2dq8xm63",
                (RelativeLayout) findViewById(R.id.spread_layout));
        // 设置开屏回调接口
        adSpreadManager.setAdSpreadInterface(this);
        // 设置开屏下方LOGO，必须调用该方法
//        adSpreadManager.setLogo(R.drawable.onejoke_logo);
        // 设置开屏背景颜色，可不设置
        // adSpreadManager.setBackgroundColor(Color.WHITE);
        // 设置开屏倒计时通知方式
        adSpreadManager.setSpreadNotifyType(this, AdSpreadManager.NOTIFY_CUSTOM);
        // 请求开屏广告
        adSpreadManager.requestAd();


    }
    @Override
    public void onAdSpreadPrepareClosed() {
        jumpToMain();
    }


    @Override
    public void onBackPressed() {
        // 取消后退键
    }


    @Override
    public void onAdClosedAd() {

    }

    @Override
    public void onAdReceived(View view) {
//        Toast.makeText(this, "onAdRecieved", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onAdDisplayAd() {

    }

    @Override
    public void onAdReceiveFailed(String s) {
//        Toast.makeText(this, "onAdReceiveFailed " + s,
//                Toast.LENGTH_SHORT).show();
        jumpToMain();
    }

    @Override
    public void onAdClosedByUser() {
        jumpToMain();
    }

    /**
     * 用户必须设置 adSpreadManager.setSpreadNotifyType(this,
     * AdSpreadManager.NOTIFY_CUSTOM); 方可回调该方法，否则不调用
     *
     * @param view
     *            返回顶部自定义布局（RelativeLayout）
     * @param ruleTime
     *            规定必须展示时间 适用于cpm&cpc 在规定时间内不可关闭，否则不计入数据
     * @param delayTime
     *            在延时时间内可以自由处理，延时时间到达后自动调用 onAdSpreadPrepareClosed 接口
     */
    @Override
    public void onAdNotifyCustomCallback(final ViewGroup view,
                                         final int ruleTime, final int delayTime) {
       adSpreadManager.notifySpreadAdStop();
    }

    private void jumpToMain(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                if (null != adSpreadManager)
                    adSpreadManager.setAdSpreadInterface(null);
               finish();
            }
        }, 2000);
    }
}
