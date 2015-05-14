package com.smarter.onejoke.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.smarter.onejoke.R;
import com.smarter.onejoke.customview.ExpandableTextView;
import com.smarter.onejoke.utils.PicInfo;

import java.util.List;

/**
 * Created by panl on 15/2/11.
 */
public class PicPageAdapter extends PagerAdapter{
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
            .cacheInMemory(true)
            .cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
    private List<PicInfo> picInfoList;
    private Context context;
    private ImageView picDetailImage;
    private ExpandableTextView contentPicText;




    public PicPageAdapter(List<PicInfo> picInfoList,Context context) {
        this.picInfoList = picInfoList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return picInfoList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View picDetailView = LayoutInflater.from(context).inflate(R.layout.item_pic_detail,container,false);
//        if (picInfoList.get(position).getPicUrl().endsWith(".gif")){
//            picDetailImage = (ImageView)picDetailView.findViewById(R.id.gif_detail_image);
//        }else {
//            picDetailImage = (ImageView) picDetailView.findViewById(R.id.pic_detail_image);
//        }
        picDetailImage = (ImageView) picDetailView.findViewById(R.id.pic_detail_image);
        contentPicText = (ExpandableTextView)picDetailView.findViewById(R.id.content_pic_text);
        contentPicText.setText(picInfoList.get(position).getDescription());
        contentPicText.setBackgroundColor(Color.argb(60, 33, 150, 243));
        imageLoader.displayImage(picInfoList.get(position).getPicUrl(), picDetailImage, options);

        container.addView(picDetailView,0);
        return picDetailView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }


}


