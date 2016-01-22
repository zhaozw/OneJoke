package com.smarter.onejoke.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;

/**
 * juheClient
 * Created by panl on 15/2/8.
 */
public class JokeClient {

    public static void getJoke(Context context, String url, Parameters parameters, final Handler handler) {
        JuheData.executeWithAPI(context, 95, url, JuheData.GET, parameters, new DataCallBack() {
            @Override
            public void onSuccess(int i, String s) {
                Message message = new Message();
                message.obj = s;
                message.what = 0;
                handler.sendMessage(message);
            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onFailure(int i, String s, Throwable throwable) {
                Message message = new Message();
                message.obj = "服务器异常";
                message.what = 1;
                handler.sendMessage(message);

            }
        });
    }

    public static void fetchData(Context context, String url, Parameters parameters, final DataReceivedListener listener) {
        JuheData.executeWithAPI(context, 95, url, JuheData.GET, parameters, new DataCallBack() {
            @Override
            public void onSuccess(int i, String s) {
               listener.onDataReceived(s);
            }
            @Override
            public void onFinish() {

            }
            @Override
            public void onFailure(int i, String s, Throwable throwable) {
                listener.onDataFiled(s);
            }
        });
    }

    public interface DataReceivedListener{
        void onDataReceived(String json);
        void onDataFiled(String error);
    }

}
