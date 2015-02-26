package com.smarter.onejoke.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.smarter.onejoke.R;
import com.smarter.onejoke.adapter.MyPageAdapter;
import com.umeng.fb.FeedbackAgent;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.update.UmengUpdateAgent;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;

import java.util.ArrayList;
import java.util.List;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;


public class MainActivity extends BaseActivity implements MaterialTabListener{
    private MaterialTabHost tabHost;
    private ViewPager viewPager;
    private List<Fragment> fragments = new ArrayList<>();
    private MyPageAdapter pageAdapter;
    private String[] tabTitle = {"每日笑话","每日趣图"};
    //UmengSDK内容
    private FeedbackAgent agent;
    //有米广告
    private AdView adView;
    private View view;
    private final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

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


        adView = new AdView(this, AdSize.SIZE_300x250);
        view = getLayoutInflater().inflate(R.layout.view_dialog,null);
        LinearLayout layout = (LinearLayout)view.findViewById(R.id.view_dialog);
        layout.addView(adView);


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
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
            new MaterialDialog.Builder(this)
                    .customView(view, false)
                    .title("确定要退出吗？")
                    .positiveText("退出")
                    .negativeText("取消")
                    .negativeColor(R.color.light_blue)
                    .positiveColor(R.color.light_blue)
                    .dividerColor(R.color.light_blue)
                    .callback(new MaterialDialog.ButtonCallback() {

                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            finish();
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            dialog.dismiss();
                        }
                    })
                    .build()
                    .show();

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
