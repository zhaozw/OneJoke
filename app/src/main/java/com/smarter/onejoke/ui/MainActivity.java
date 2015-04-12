package com.smarter.onejoke.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.kyview.interfaces.AdInstlInterface;
import com.kyview.screen.interstitial.AdInstlManager;
import com.smarter.onejoke.R;
import com.smarter.onejoke.adapter.MyPageAdapter;
import com.umeng.fb.FeedbackAgent;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.update.UmengUpdateAgent;

import java.util.ArrayList;
import java.util.List;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import me.drakeet.materialdialog.MaterialDialog;


public class MainActivity extends BaseActivity implements MaterialTabListener{
    private MaterialTabHost tabHost;
    private ViewPager viewPager;
    private List<Fragment> fragments = new ArrayList<>();
    private MyPageAdapter pageAdapter;
    private String[] tabTitle = {"每日笑话","每日趣图"};

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
        mTintManager.setStatusBarTintEnabled(true);
        mTintManager.setStatusBarTintResource(R.color.light_blue);
        //友盟检查更新
        UmengUpdateAgent.update(this);
        //友盟用户反馈
        agent = new FeedbackAgent(this);
        agent.sync();


        tabHost = (MaterialTabHost)findViewById(R.id.materialTabHost);
        viewPager = (ViewPager)findViewById(R.id.view_pager);

        fragments.add(new JokeFragment());
        fragments.add(new PictureFragment());

        pageAdapter = new MyPageAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(pageAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
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
//                Toast.makeText(MainActivity.this, "ReceivedAd",
//                        Toast.LENGTH_SHORT).show();
                        adView = adInstlManager.getContentView();
                    }

                    @Override
                    public void onReceivedAdFailed(String s) {
//                Toast.makeText(MainActivity.this, "onReceiveAdFailed",
//                        Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        }else if (id == R.id.action_about){
            Intent intent = new Intent(MainActivity.this,AboutActivity.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.action_message){
            Intent intent = new Intent(MainActivity.this,MessageActivity.class);
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

            final MaterialDialog materialDialog = new MaterialDialog(this);
            if (adView != null) {
                if (adView.getParent() != null) {
                    ((ViewGroup) adView.getParent()).removeView(adView);
                }
                        materialDialog
                                .setView(adView)
                        .setNegativeButton("去看看", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                adInstlManager.clickAdReport();
                                adInstlManager.destroy();
                            }
                        })
                        .setPositiveButton("退出",new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        }).show();



            }else {
                        materialDialog
                                .setTitle("确定要退出吗?")
                                .setMessage("")
                        .setNegativeButton("再看一会", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                materialDialog.dismiss();
                            }
                        })
                        .setPositiveButton("退出",new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        }).show();


            }
        }

        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }


}
