package com.smarter.onejoke.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.smarter.onejoke.R;
import com.umeng.analytics.MobclickAgent;

import net.youmi.android.spot.SpotManager;

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
//        LinearLayout welcomeView = (LinearLayout)findViewById(R.id.welcome_view);
//
//        SplashView splashView = new SplashView(WelcomeActivity.this,MainActivity.class);
//        welcomeView.addView(splashView.getSplashView());
//
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        },3000);
        SpotManager.getInstance(this).showSplashSpotAds(this, MainActivity.class);
    }



    @Override

    protected void onStop() {

        // 如果不调用此方法，则按home键的时候会出现图标无法显示的情况。
        SpotManager.getInstance(this).onStop();
        super.onStop();
    }
    // 请务必加上词句，否则进入网页广告后无法进去原sdk
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 10045) {
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        // 取消后退键
    }

    @Override
    protected void onDestroy() {

        SpotManager.getInstance(this).onDestroy();
        super.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
