package com.smarter.onejoke.utils;

import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Fresco工具类
 * Created by panl on 15/12/7.
 */
public class FrescoUtils {
    public static void displayImage(String url, SimpleDraweeView draweeView) {
        Uri uri = Uri.parse(url);
        draweeView.setImageURI(uri);
    }
}
