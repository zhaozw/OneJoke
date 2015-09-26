package com.smarter.onejoke.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.kyview.interfaces.AdInstlInterface;
import com.kyview.screen.interstitial.AdInstlManager;
import com.smarter.onejoke.R;
import com.smarter.onejoke.adapter.MyPageAdapter;
import com.umeng.fb.FeedbackAgent;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.update.UmengUpdateAgent;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;


public class MainActivity extends BaseActivity implements MaterialTabListener {
    private MaterialTabHost tabHost;
    private ViewPager viewPager;
    private MyPageAdapter pageAdapter;
    private String[] tabTitle = {"每日笑话", "每日趣图"};

    //UmengSDK内容
    private FeedbackAgent agent;
    private final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

    //AdView广告
    private View adView = null;
    private AdInstlManager adInstlManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initViewPager();
    }


    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setElevation(0);
        }
        //友盟检查更新
        UmengUpdateAgent.update(this);
        agent = new FeedbackAgent(this);
        agent.sync();
    }

    private void initViewPager() {
        tabHost = (MaterialTabHost) findViewById(R.id.materialTabHost);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        if (PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("pref_dark_theme", false)) {
            tabHost.setPrimaryColor(getResources().getColor(R.color.colorPrimaryInverse));
        } else {
            tabHost.setPrimaryColor(getResources().getColor(R.color.colorPrimary));
        }

        pageAdapter = new MyPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tabHost.setSelectedNavigationItem(position);
            }
        });
        for (int i = 0; i < pageAdapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setText(tabTitle[i])
                            .setTabListener(MainActivity.this)
            );
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        //AdView广告平台
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                adInstlManager = new AdInstlManager(MainActivity.this,
                        "SDK20151024100234gyduoom2dq8xm63");
                adInstlManager.requestAd();
                adInstlManager.setAdViewInterface(new AdInstlInterface() {
                    @Override
                    public void onClickAd() {

                    }

                    @Override
                    public void onDisplayAd() {

                    }

                    @Override
                    public void onAdDismiss() {
                    }

                    @Override
                    public void onReceivedAd(int i, View view) {
                        adView = adInstlManager.getContentView();
                    }

                    @Override
                    public void onReceivedAdFailed(String s) {
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem switchItem = menu.findItem(R.id.action_switch);
        MenuItemCompat.setActionView(switchItem, R.layout.view_switch_compact);
        final SwitchCompat switchCompat = (SwitchCompat) switchItem.getActionView()
                .findViewById(R.id.switchCompat);
        switchCompat.setChecked(PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("pref_dark_theme", false));
        switchCompat.setThumbResource(R.drawable.switch_thumb);
        switchCompat.setTrackResource(R.mipmap.switch_bg);
        switchCompat.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putBoolean("pref_dark_theme", isChecked).commit();
                        MainActivity.this.recreate();
                    }
                }
        );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_feedback) {
            agent.startFeedbackActivity();
            return true;
        } else if (id == R.id.action_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_message) {
            Intent intent = new Intent(MainActivity.this, MessageActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabSelected(MaterialTab materialTab) {
        viewPager.setCurrentItem(materialTab.getPosition());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 实例化广告条
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showDialog();
        }
        return super.onKeyDown(keyCode, event);
    }


    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (adView != null) {
            if (adView.getParent() != null) {
                ((ViewGroup) adView.getParent()).removeView(adView);
            }
            builder.setView(adView);
            builder.setNegativeButton("去看看", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adInstlManager.clickAdReport();
                    adInstlManager.destroy();
                }
            });
            builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            builder.create().show();
        } else {
            builder.setTitle("确定要退出吗？");
            builder.setNegativeButton("再看看", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            builder.create().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }


}
