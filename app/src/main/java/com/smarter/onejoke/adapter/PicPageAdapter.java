package com.smarter.onejoke.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.felipecsl.gifimageview.library.GifImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.smarter.onejoke.R;
import com.smarter.onejoke.customview.ExpandableTextView;
import com.smarter.onejoke.model.PicInfo;
import com.smarter.onejoke.utils.GifDataLoader;

import java.util.List;

/**
 * Created by panl on 15/2/11.
 */
public class PicPageAdapter extends PagerAdapter {
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
            .cacheInMemory(true)
            .cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
    private List<PicInfo> picInfoList;
    private Context context;


    public PicPageAdapter(List<PicInfo> picInfoList, Context context) {
        this.picInfoList = picInfoList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return picInfoList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View picDetailView = LayoutInflater.from(context).inflate(R.layout.item_pic_detail, container, false);
        ImageView picDetailImage = (ImageView) picDetailView.findViewById(R.id.pic_detail_image);
        final GifImageView gifImageView = (GifImageView) picDetailView.findViewById(R.id.gif_picture_detail);
        ExpandableTextView contentPicText = (ExpandableTextView) picDetailView.findViewById(R.id.content_pic_text);
        contentPicText.setText(picInfoList.get(position).getDescription());
        contentPicText.setBackgroundColor(Color.argb(60, 33, 150, 243));
        if (picInfoList.get(position).getPicUrl().endsWith(".gif")) {
            picDetailImage.setVisibility(View.GONE);
            gifImageView.setVisibility(View.VISIBLE);
            new GifDataLoader() {
                @Override
                protected void onPostExecute(byte[] bytes) {
                    gifImageView.setBytes(bytes);
                    gifImageView.startAnimation();
                }
            }.execute(picInfoList.get(position).getPicUrl());
        } else {
            picDetailImage.setVisibility(View.VISIBLE);
            gifImageView.setVisibility(View.GONE);
            imageLoader.displayImage(picInfoList.get(position).getPicUrl(), picDetailImage, options);
        }
        container.addView(picDetailView, 0);
        return picDetailView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }


}


