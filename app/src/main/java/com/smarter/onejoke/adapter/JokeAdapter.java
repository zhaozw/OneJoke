package com.smarter.onejoke.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.smarter.onejoke.R;
import com.smarter.onejoke.model.JokeInfo;
import com.smarter.onejoke.utils.ShareUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by panl on 15/2/8.
 */
public class JokeAdapter extends RecyclerView.Adapter<JokeAdapter.ViewHolder> {
    private List<JokeInfo> jokeInfoList = new ArrayList<>();
    private int[] iconNumberBg = {R.drawable.bg_blue,
            R.drawable.bg_green,
            R.drawable.bg_grey,
            R.drawable.bg_orange};
    private Activity context;
    private final static String weixinAppID = "wx006e00e8f84351fb";
    private final static String weixinAppSecret = "1f115f46a8e3fc77a3a0d844d717caaf";

    @Override
    public int getItemCount() {
        return jokeInfoList.size();
    }

    @Override
    public JokeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View jokeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_joke, parent, false);
        ViewHolder viewHolder = new ViewHolder(jokeView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(JokeAdapter.ViewHolder holder, final int position) {
        holder.jokeContent.setText(jokeInfoList.get(position).getContents());
        holder.iconNumber.setText("" + (position + 1));
        holder.iconNumber.setBackgroundResource(iconNumberBg[(int) (Math.random() * 10) % 4]);
        holder.updateTime.setText(jokeInfoList.get(position).getUpdateTime());
        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.shareToOther(context, jokeInfoList.get(position).getContents(), null);
            }
        });

    }

    public JokeAdapter(List<JokeInfo> jokeInfoList, Activity context) {
        this.jokeInfoList = jokeInfoList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView jokeContent;
        public TextView iconNumber;
        public TextView updateTime;
        public ImageButton shareButton;

        public ViewHolder(View itemView) {
            super(itemView);
            jokeContent = (TextView) itemView.findViewById(R.id.content_joke);
            iconNumber = (TextView) itemView.findViewById(R.id.icon_number);
            updateTime = (TextView) itemView.findViewById(R.id.time_text);
            shareButton = (ImageButton) itemView.findViewById(R.id.share_button);
        }
    }
}
