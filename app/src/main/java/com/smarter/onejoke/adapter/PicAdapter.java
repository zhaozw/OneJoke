package com.smarter.onejoke.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smarter.onejoke.R;
import com.smarter.onejoke.ui.PicDetailActivity;
import com.smarter.onejoke.utils.PicInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by panl on 15/2/10.
 */
public class PicAdapter extends RecyclerView.Adapter<PicAdapter.ViewHolder> {

    private List<PicInfo> picInfoList = new ArrayList<>();
    private Activity context;

    public PicAdapter(List<PicInfo> picInfoList,Activity context) {
        this.picInfoList = picInfoList;
        this.context = context;

    }

    @Override
    public PicAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View picView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture,parent,false);
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
        Picasso.with(context).load(picInfoList
                .get(position)
                .getPicUrl())
                .resize(200,200)
                .centerCrop()
                .into(holder.picImage);
        holder.picImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PicDetailActivity.class);
                intent.putCharSequenceArrayListExtra("picInfoList",(ArrayList)picInfoList);
                intent.putExtra("position",position);
                context.startActivity(intent);

            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView picImage;
        public TextView descriptionText;
        public ViewHolder(View itemView) {
            super(itemView);
            picImage = (ImageView)itemView.findViewById(R.id.pic_image);
            descriptionText = (TextView)itemView.findViewById(R.id.desc_text);
        }
    }
}
