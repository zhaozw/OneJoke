package com.smarter.onejoke.utils;

import android.app.Activity;

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
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.WeiXinShareContent;


public class ShareUtils {
    public final static UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
    private final static String weixinAppID = "wx006e00e8f84351fb";
    private final static String weixinAppSecret = "1f115f46a8e3fc77a3a0d844d717caaf";

    public static void initController(Activity context) {
        mController.getConfig().setPlatformOrder(SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT,
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ,
                SHARE_MEDIA.QZONE, SHARE_MEDIA.RENREN);


        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(context, weixinAppID, weixinAppSecret);
        wxHandler.addToSocialSDK();
        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(context, weixinAppID, weixinAppSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(context, "100424468",
                "c7394704798a158208a74ab60104f0ba");
        qqSsoHandler.addToSocialSDK();

        //参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(context, "100424468",
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
        RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler(context,
                "201874", "28401c0964f04a72a14c812d6132fcef",
                "3bf66e42db1e4fa9829b955cc300b737");
        renrenSsoHandler.addToSocialSDK();
        mController.getConfig().setSsoHandler(renrenSsoHandler);
    }

    public static void shareToOther(Activity context, String content, String imgUrl) {
        initController(context);
        WeiXinShareContent weiXinShareContent = new WeiXinShareContent();
        weiXinShareContent.setShareContent(content
                + "\n\n" + "开心段子:下载地址:" + "http://www.wandoujia.com/apps/com.smarter.onejoke");
        weiXinShareContent.setTargetUrl("http://www.wandoujia.com/apps/com.smarter.onejoke");
        mController.setShareMedia(weiXinShareContent);


        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(content
                + "\n\n" + "开心段子:下载地址:" + "http://www.wandoujia.com/apps/com.smarter.onejoke");
        qqShareContent.setTargetUrl("http://www.wandoujia.com/apps/com.smarter.onejoke");
        mController.setShareMedia(qqShareContent);

        QZoneShareContent qZoneShareContent = new QZoneShareContent();
        qZoneShareContent.setShareContent(content
                + "\n\n" + "开心段子:下载地址:" + "http://www.wandoujia.com/apps/com.smarter.onejoke");
        qZoneShareContent.setTargetUrl("http://www.wandoujia.com/apps/com.smarter.onejoke");
        mController.setShareMedia(qZoneShareContent);
        // 设置分享内容
        mController.setShareContent(content
                + "\n\n" + "开心段子:下载地址:" + "http://www.wandoujia.com/apps/com.smarter.onejoke");

        if (imgUrl != null)
            mController.setShareMedia(new UMImage(context, imgUrl));

        mController.openShare(context, false);
    }
}
