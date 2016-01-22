package com.smarter.onejoke.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.kyview.AdViewStream;
import com.kyview.AdViewTargeting;
import com.kyview.interfaces.AdViewInterface;
import com.smarter.onejoke.R;
import com.smarter.onejoke.adapter.PicPageAdapter;
import com.smarter.onejoke.model.PicInfo;
import com.smarter.onejoke.utils.FileUtil;
import com.smarter.onejoke.utils.ShareUtils;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PicDetailActivity extends BaseActivity implements AdViewInterface {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.app_bar)
    AppBarLayout appBar;
    @Bind(R.id.pic_detail_pager)
    ViewPager viewPager;
    @Bind(R.id.ad_banner)
    LinearLayout layout;
    private List<PicInfo> picInfoList = new ArrayList<>();
    private PicPageAdapter picPageAdapter;
    private int position;
    private AdViewStream viewStream;
    private int count = 0;

    final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");


    @Override
    protected void onStart() {
        super.onStart();
        AdViewTargeting.setAdSize(AdViewTargeting.AdSize.BANNER_SMART);
        AdViewTargeting.setBannerSwitcherMode(AdViewTargeting.BannerSwitcher.CANCLOSED);
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
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);


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
        getMenuInflater().inflate(R.menu.menu_pic_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share) {
            ShareUtils.shareToOther(this, picInfoList.get(position).getContent(), picInfoList.get(position).getUrl());
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.action_save) {
            if (FileUtil.isSDCardEnable()) {
                Snackbar.make(viewPager, "正在下载", Snackbar.LENGTH_SHORT).show();
                saveGif();
            } else {
                Snackbar.make(viewPager, "SD卡不存在", Snackbar.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }


    private void saveGif() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = picInfoList.get(position).getUrl();
                try {
                    final Uri uri = FileUtil.saveFileToSDCard(path, path.substring(path.length() - 10, path.length()));
                    if (uri != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                                PicDetailActivity.this.sendBroadcast(scannerIntent);
                                Snackbar.make(viewPager, "保存成功", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

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
