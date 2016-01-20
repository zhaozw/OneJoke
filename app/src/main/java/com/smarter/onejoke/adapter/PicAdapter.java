package com.smarter.onejoke.adapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.smarter.onejoke.R;
import com.smarter.onejoke.model.PicInfo;
import com.smarter.onejoke.ui.PicDetailActivity;
import com.smarter.onejoke.utils.FrescoUtils;

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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.descText.setText(picInfoList.get(position).getDescription());
        FrescoUtils.displayImage(picInfoList.get(position).getPicUrl(), holder.picImage);
        holder.picImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PicDetailActivity.class);
                intent.putExtra("picInfoList", (Serializable) picInfoList);
                intent.putExtra("position", position);
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
        SimpleDraweeView picImage;
        @Bind(R.id.desc_text)
        TextView descText;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
