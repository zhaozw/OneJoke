package com.smarter.onejoke.utils;

import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Fresco工具类
 * Created by panl on 15/12/7.
 */
public class FrescoUtils {
    public static void displayImage(String url, SimpleDraweeView draweeView) {
        Uri uri = Uri.parse(url);
        draweeView.setImageURI(uri);
    }

    public static void displayGifView(String url, SimpleDraweeView draweeView){
        Uri uri = Uri.parse(url);
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri).build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setAutoPlayAnimations(true)
                .build();
        draweeView.setController(controller);
    }
}
