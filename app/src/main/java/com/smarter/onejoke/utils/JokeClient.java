package com.smarter.onejoke.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;

/**
 * Created by panl on 15/2/8.
 */
public class JokeClient {
    public static void getJoke(String url,Parameters parameters,final Handler handler){
        JuheData.executeWithAPI(95,url,JuheData.GET,parameters,new DataCallBack() {
            @Override
            public void resultLoaded(int i, String s, String s2) {
                Message message = new Message();
                if (i == 0){
                    message.obj = s2;
                    message.what = 0;
                    handler.sendMessage(message);
                    Log.i("Joke",s2);
                }else {
                    message.obj = s;
                    message.what = 1;
                    handler.sendMessage(message);
                    Log.i("reson",s);
                }
            }
        });
        Log.i("Run","run");
    }

}
