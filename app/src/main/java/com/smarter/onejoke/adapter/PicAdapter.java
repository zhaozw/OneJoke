package com.smarter.onejoke.adapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.smarter.onejoke.R;
import com.smarter.onejoke.model.PicInfo;
import com.smarter.onejoke.ui.PicDetailActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 显示图片
 * Created by panl on 15/2/10.
 */
public class PicAdapter extends RecyclerView.Adapter<PicAdapter.ViewHolder> {

    private List<PicInfo> picInfoList = new ArrayList<>();
    private Activity context;
    private int lastPosition;

    public PicAdapter(List<PicInfo> picInfoList, Activity context) {
        this.picInfoList = picInfoList;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View picView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture, parent, false);
        return new ViewHolder(picView);
    }

    @Override
    public int getItemCount() {
        return picInfoList.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.descText.setText(picInfoList.get(position).getContent());
        holder.timeText.setText(picInfoList.get(position).getUpdatetime());
        if (picInfoList.get(position).getUrl().endsWith(".gif")) {
            Glide.with(context).load(picInfoList.get(position).getUrl()).asGif()
                    .placeholder(R.mipmap.pic_default)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.picImage);
        } else {
            Glide.with(context).load(picInfoList.get(position).getUrl())
                    .placeholder(R.mipmap.pic_default).into(holder.picImage);
        }

        holder.picImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PicDetailActivity.class);
                intent.putExtra("picInfoList", (Serializable) picInfoList);
                intent.putExtra("position", holder.getAdapterPosition());
                context.startActivity(intent);

            }
        });

        if (lastPosition < position) {
            ObjectAnimator.ofFloat(holder.itemView, "translationY",
                    holder.itemView.getHeight() / 2, 0.0f).setDuration(400).start();
            lastPosition = position;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.pic_image)
        ImageView picImage;
        @Bind(R.id.desc_text)
        TextView descText;
        @Bind(R.id.pic_bg)
        RelativeLayout picBg;
        @Bind(R.id.time_text)
        TextView timeText;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (PreferenceManager.getDefaultSharedPreferences(context)
                    .getBoolean("pref_dark_theme", false)) {
                picBg.setBackgroundColor(context.getResources().getColor(R.color.night_bg));
            }
        }
    }
}
