package com.smarter.onejoke.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.smarter.onejoke.R;
import com.smarter.onejoke.utils.JokeInfo;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.RenrenSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by panl on 15/2/8.
 */
public class JokeAdapter extends RecyclerView.Adapter<JokeAdapter.ViewHolder> {
    private List<JokeInfo> jokeInfoList = new ArrayList<>();
    private int[] iconNumberBg = {R.drawable.bg_blue,
            R.drawable.bg_green,
            R.drawable.bg_grey,
            R.drawable.bg_orange};
    private final UMSocialService mController;
    private Activity context;
    private final static String weixinAppID = "wx006e00e8f84351fb";
    private final static String weixinAppSecret = "1f115f46a8e3fc77a3a0d844d717caaf";

    @Override
    public int getItemCount() {
        return jokeInfoList.size();
    }

    @Override
    public JokeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View jokeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_joke,parent,false);
        ViewHolder viewHolder = new ViewHolder(jokeView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(JokeAdapter.ViewHolder holder, final int position) {
        holder.jokeContent.setText(jokeInfoList.get(position).getContents());
        holder.iconNumber.setText(""+(position+1));
        holder.iconNumber.setBackgroundResource(iconNumberBg[(int)(Math.random()*10)%4]);
        holder.updateTime.setText(jokeInfoList.get(position).getUpdateTime());
        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeiXinShareContent weiXinShareContent = new WeiXinShareContent();
                weiXinShareContent.setShareContent(jokeInfoList.get(position).getContents()
                +"\n\n"+"开心段子:下载地址:"+"http://www.wandoujia.com/apps/com.smarter.onejoke");
                weiXinShareContent.setTargetUrl("http://www.wandoujia.com/apps/com.smarter.onejoke");
                mController.setShareMedia(weiXinShareContent);


                QQShareContent qqShareContent = new QQShareContent();
                qqShareContent.setShareContent(jokeInfoList.get(position).getContents()
                        +"\n\n"+"开心段子:下载地址:"+"http://www.wandoujia.com/apps/com.smarter.onejoke");
                qqShareContent.setTargetUrl("http://www.wandoujia.com/apps/com.smarter.onejoke");
                mController.setShareMedia(qqShareContent);

                QZoneShareContent qZoneShareContent = new QZoneShareContent();
                qZoneShareContent.setShareContent(jokeInfoList.get(position).getContents()
                        +"\n\n"+"开心段子:下载地址:"+"http://www.wandoujia.com/apps/com.smarter.onejoke");
                qZoneShareContent.setTargetUrl("http://www.wandoujia.com/apps/com.smarter.onejoke");
                mController.setShareMedia(qZoneShareContent);
                // 设置分享内容
                mController.setShareContent(jokeInfoList.get(position).getContents()
                        +"\n\n"+"开心段子:下载地址:"+"http://www.wandoujia.com/apps/com.smarter.onejoke");
                mController.openShare(context, false);
            }
        });

    }

    public JokeAdapter(List<JokeInfo> jokeInfoList,UMSocialService mController,Activity context) {
        this.jokeInfoList = jokeInfoList;
        this.mController = UMServiceFactory.getUMSocialService("com.umeng.share");
        this.context = context;

        mController.getConfig().setPlatformOrder(SHARE_MEDIA.SINA,SHARE_MEDIA.TENCENT,
                SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.QQ,
                SHARE_MEDIA.QZONE,SHARE_MEDIA.RENREN);


        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(context,weixinAppID,weixinAppSecret);
        wxHandler.addToSocialSDK();
        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(context,weixinAppID,weixinAppSecret);
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

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView jokeContent;
        public TextView iconNumber;
        public TextView updateTime;
        public ImageButton shareButton;
        public ViewHolder(View itemView) {
            super(itemView);
            jokeContent = (TextView)itemView.findViewById(R.id.content_joke);
            iconNumber = (TextView)itemView.findViewById(R.id.icon_number);
            updateTime = (TextView)itemView.findViewById(R.id.time_text);
            shareButton = (ImageButton)itemView.findViewById(R.id.share_button);
        }
    }
}
