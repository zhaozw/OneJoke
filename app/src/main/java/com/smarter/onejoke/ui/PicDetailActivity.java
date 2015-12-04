package com.smarter.onejoke.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.kyview.AdViewStream;
import com.kyview.AdViewTargeting;
import com.kyview.interfaces.AdViewInterface;
import com.smarter.onejoke.R;
import com.smarter.onejoke.adapter.PicPageAdapter;
import com.smarter.onejoke.model.PicInfo;
import com.smarter.onejoke.utils.ShareUtils;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;

import java.util.ArrayList;
import java.util.List;

public class PicDetailActivity extends BaseActivity implements AdViewInterface {

    private ViewPager viewPager;
    private List<PicInfo> picInfoList = new ArrayList<>();
    private PicPageAdapter picPageAdapter;
    private int position;
    private LinearLayout layout;
    private AdViewStream viewStream;
    private int count = 0;

    private final static String weixinAppID = "wx006e00e8f84351fb";
    private final static String weixinAppSecret = "1f115f46a8e3fc77a3a0d844d717caaf";

    final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");


    @Override
    protected void onStart() {
        super.onStart();
        AdViewTargeting.setAdSize(AdViewTargeting.AdSize.BANNER_SMART);
        AdViewTargeting.setBannerSwitcherMode(AdViewTargeting.BannerSwitcher.CANCLOSED);
        layout = (LinearLayout) findViewById(R.id.ad_banner);
        getBannerAd();
    }

    private void getBannerAd() {
        if (layout == null)
            return;
        layout.removeAllViews();
        viewStream = new AdViewStream(this, "SDK20151024100234gyduoom2dq8xm63");
        viewStream.setAdViewInterface(this);
        layout.addView(viewStream);
        layout.invalidate();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_detail);


        viewPager = (ViewPager) findViewById(R.id.pic_detail_pager);

        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        picInfoList = (List) intent.getCharSequenceArrayListExtra("picInfoList");
        if (picInfoList.size() > 0) {

            picPageAdapter = new PicPageAdapter(picInfoList, this);
            viewPager.setAdapter(picPageAdapter);
            viewPager.setCurrentItem(position);
            getSupportActionBar().setTitle((position + 1) + "/" + picInfoList.size());
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int i) {
                getSupportActionBar().setTitle((i + 1) + "/" + picInfoList.size());
                position = i;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pic_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            ShareUtils.shareToOther(this, picInfoList.get(position).getDescription(), picInfoList.get(position).getPicUrl());
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
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


    @Override
    public void onClosedAd() {
        count++;
        if (count < 3) {
            getBannerAd();
        } else {
            viewStream.setClosed(true);

        }
    }

    @Override
    public void onDisplayAd() {

    }

    @Override
    public void onClickAd() {

    }
}
