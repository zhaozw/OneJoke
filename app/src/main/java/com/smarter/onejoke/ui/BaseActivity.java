package com.smarter.onejoke.ui;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.smarter.onejoke.R;
import com.smarter.onejoke.utils.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends AppCompatActivity {

    protected SystemBarTintManager mTintManager;

    protected void onCreate(Bundle savedInstanceState) {
        if (PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("pref_dark_theme", false)) {
            setTheme(R.style.AppTheme_Dark);
        }
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        mTintManager = new SystemBarTintManager(this);
        mTintManager.setStatusBarTintEnabled(true);
        if (PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("pref_dark_theme", false)){
            mTintManager.setStatusBarTintResource(R.color.colorPrimaryDarkInverse);
        }else {
            mTintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        }
    }

    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
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
