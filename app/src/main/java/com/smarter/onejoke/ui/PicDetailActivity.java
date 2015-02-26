package com.smarter.onejoke.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.smarter.onejoke.R;
import com.smarter.onejoke.adapter.PicPageAdapter;
import com.smarter.onejoke.utils.PicInfo;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.RenrenSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import java.util.ArrayList;
import java.util.List;

public class PicDetailActivity extends BaseActivity {

    private ViewPager viewPager;
    private List<PicInfo> picInfoList = new ArrayList<>();
    private PicPageAdapter picPageAdapter;
    private int position ;

    private final static String weixinAppID = "wx006e00e8f84351fb";
    private final static String weixinAppSecret = "1f115f46a8e3fc77a3a0d844d717caaf";

    final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_detail);

        mTintManager.setStatusBarTintEnabled(true);
        mTintManager.setStatusBarTintResource(R.color.light_blue);

        viewPager = (ViewPager) findViewById(R.id.pic_detail_pager);

        Intent intent = getIntent();
        position = intent.getIntExtra("position",0);
        picInfoList = (List)intent.getCharSequenceArrayListExtra("picInfoList");
        if (picInfoList.size()>0) {

            picPageAdapter = new PicPageAdapter(picInfoList, this);
            viewPager.setAdapter(picPageAdapter);
            viewPager.setCurrentItem(position);
            getSupportActionBar().setTitle((position+1) + "/" + picInfoList.size());
        }
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int i) {
                getSupportActionBar().setTitle((i+1)+"/"+picInfoList.size());
                position = i;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setShareConfig(){
        mController.getConfig().setPlatformOrder(SHARE_MEDIA.SINA,SHARE_MEDIA.TENCENT,
                SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.QQ,
                SHARE_MEDIA.QZONE,SHARE_MEDIA.RENREN);


        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(this,weixinAppID,weixinAppSecret);
        wxHandler.addToSocialSDK();
        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(this,weixinAppID,weixinAppSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "100424468",
                "c7394704798a158208a74ab60104f0ba");
        qqSsoHandler.addToSocialSDK();

        //参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, "100424468",
                "c7394704798a158208a74ab60104f0ba");
        qZoneSsoHandler.addToSocialSDK();

        //设置新浪SSO handler
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        //设置腾讯微博SSO handler
        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());


        //添加人人网SSO授权功能
        //APPID:201874
        //API Key:28401c0964f04a72a14c812d6132fcef
        //Secret:3bf66e42db1e4fa9829b955cc300b737
        RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler(this,
                "201874", "28401c0964f04a72a14c812d6132fcef",
                "3bf66e42db1e4fa9829b955cc300b737");
        renrenSsoHandler.addToSocialSDK();
        mController.getConfig().setSsoHandler(renrenSsoHandler);


        WeiXinShareContent weiXinShareContent = new WeiXinShareContent();
        weiXinShareContent.setShareContent(picInfoList.get(position).getDescription()
                +"\n"+"下载地址:"+"http://www.wandoujia.com/apps/com.smarter.onejoke");
        weiXinShareContent.setTargetUrl("http://www.wandoujia.com/apps/com.smarter.onejoke");
        mController.setShareMedia(weiXinShareContent);


        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(picInfoList.get(position).getDescription()
                +"\n"+"下载地址:"+"http://www.wandoujia.com/apps/com.smarter.onejoke");
        qqShareContent.setTargetUrl("http://www.wandoujia.com/apps/com.smarter.onejoke");
        mController.setShareMedia(qqShareContent);

        QZoneShareContent qZoneShareContent = new QZoneShareContent();
        qZoneShareContent.setShareContent(picInfoList.get(position).getDescription()
                +"\n"+"下载地址:"+"http://www.wandoujia.com/apps/com.smarter.onejoke");
        qZoneShareContent.setTargetUrl("http://www.wandoujia.com/apps/com.smarter.onejoke");
        mController.setShareMedia(qZoneShareContent);
        // 设置分享内容
        mController.setShareContent(picInfoList.get(position).getDescription()
                +"\n"+"下载地址:"+"http://www.wandoujia.com/apps/com.smarter.onejoke");
        mController.setShareMedia(new UMImage(this,
                picInfoList.get(position).getPicUrl()));
        mController.openShare(this,false);

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
            setShareConfig();

            return true;
        }else if (id == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
