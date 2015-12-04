package com.smarter.onejoke.adapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.smarter.onejoke.R;
import com.smarter.onejoke.ui.PicDetailActivity;
import com.smarter.onejoke.model.PicInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by panl on 15/2/10.
 */
public class PicAdapter extends RecyclerView.Adapter<PicAdapter.ViewHolder> {

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
            .cacheInMemory(true)
            .cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();

    private List<PicInfo> picInfoList = new ArrayList<>();
    private Activity context;
    private int lastPosition;

    public PicAdapter(List<PicInfo> picInfoList, Activity context) {
        this.picInfoList = picInfoList;
        this.context = context;

    }

    @Override
    public PicAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View picView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture, parent, false);
        ViewHolder viewHolder = new ViewHolder(picView);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return picInfoList.size();
    }

    @Override
    public void onBindViewHolder(PicAdapter.ViewHolder holder, final int position) {
        holder.descriptionText.setText(picInfoList.get(position).getDescription());
        holder.picImage.setImageResource(R.mipmap.pic_default);
        imageLoader.displayImage(picInfoList.get(position).getPicUrl(), holder.picImage, options);
        holder.picImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PicDetailActivity.class);
                intent.putCharSequenceArrayListExtra("picInfoList", (ArrayList) picInfoList);
                intent.putExtra("position", position);
                context.startActivity(intent);

            }
        });

        if (lastPosition < position) {
            ObjectAnimator.ofFloat(holder.itemView, "translationY",
                    holder.itemView.getHeight() / 3, 0.0f).setDuration(250).start();
            lastPosition = position;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView picImage;
        public TextView descriptionText;

        public ViewHolder(View itemView) {
            super(itemView);
            picImage = (ImageView) itemView.findViewById(R.id.pic_image);
            descriptionText = (TextView) itemView.findViewById(R.id.desc_text);
        }
    }
}
