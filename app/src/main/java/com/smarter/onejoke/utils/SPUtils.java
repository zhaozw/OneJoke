package com.smarter.onejoke.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.smarter.onejoke.model.JokeInfo;
import com.smarter.onejoke.model.PicInfo;

import java.util.List;

/**
 * Created by panl on 16/1/22.
 */
public class SPUtils {
    private static final String JOKE = "joke";
    private static final String PICTURES = "pictures";
    private static final String ONE_JOKE = "onejoke";

    public static boolean saveFirstPageJoke(Context context, List<JokeInfo> jokeInfos) {
        SharedPreferences preferences = context.getSharedPreferences(ONE_JOKE, Context.MODE_PRIVATE);
        return preferences.edit().putString(JOKE, JSON.toJSONString(jokeInfos)).commit();
    }

    public static List<JokeInfo> getFirstPageJoke(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(ONE_JOKE, Context.MODE_PRIVATE);
        return JSON.parseArray(preferences.getString(JOKE,"[]"),JokeInfo.class);
    }

    public static boolean saveFirstPagePictures(Context context, List<PicInfo> picInfos) {
        SharedPreferences preferences = context.getSharedPreferences(ONE_JOKE, Context.MODE_PRIVATE);
        return preferences.edit().putString(PICTURES, JSON.toJSONString(picInfos)).commit();
    }

    public static List<PicInfo> getFirstPagePictures(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(ONE_JOKE, Context.MODE_PRIVATE);
        return JSON.parseArray(preferences.getString(PICTURES,"[]"),PicInfo.class);
    }
}
