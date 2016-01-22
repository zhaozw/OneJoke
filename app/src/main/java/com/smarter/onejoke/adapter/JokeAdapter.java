package com.smarter.onejoke.adapter;

import android.app.Activity;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.smarter.onejoke.R;
import com.smarter.onejoke.model.JokeInfo;
import com.smarter.onejoke.utils.AndroidUtils;
import com.smarter.onejoke.utils.ShareUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by panl on 15/2/8.
 */
public class JokeAdapter extends RecyclerView.Adapter<JokeAdapter.ViewHolder> {


    private List<JokeInfo> jokeInfoList = new ArrayList<>();
    private Activity context;

    @Override
    public int getItemCount() {
        return jokeInfoList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View jokeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_joke, parent, false);
        return new ViewHolder(jokeView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.contentJoke.setText(jokeInfoList.get(position).getContent());
        holder.timeText.setText(jokeInfoList.get(position).getUpdatetime());
        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.shareToOther(context, jokeInfoList.get(holder.getAdapterPosition()).getContent(), null);
            }
        });
        holder.copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidUtils.CopyText(context, jokeInfoList.get(holder.getAdapterPosition()).getContent());
            }
        });

    }

    public JokeAdapter(List<JokeInfo> jokeInfoList, Activity context) {
        this.jokeInfoList = jokeInfoList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.time_text)
        TextView timeText;
        @Bind(R.id.share_button)
        ImageButton shareButton;
        @Bind(R.id.copy_button)
        ImageButton copyButton;
        @Bind(R.id.content_joke)
        TextView contentJoke;
        @Bind(R.id.joke_bg)
        View jokeBg;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (PreferenceManager.getDefaultSharedPreferences(context)
                    .getBoolean("pref_dark_theme", false)) {
                jokeBg.setBackgroundColor(context.getResources().getColor(R.color.night_bg));
            }
        }
    }
}
