package com.smarter.onejoke.ui;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.smarter.onejoke.R;
import com.smarter.onejoke.adapter.JokeAdapter;
import com.smarter.onejoke.model.JokeInfo;
import com.smarter.onejoke.utils.JokeClient;
import com.smarter.onejoke.utils.SPUtils;
import com.thinkland.sdk.android.Parameters;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class JokeFragment extends Fragment implements JokeClient.DataReceivedListener {

    private static final String BASE_URL = "http://japi.juhe.cn/joke/content/list.from";
    @Bind(R.id.recycler_joke)
    RecyclerView recyclerJoke;
    @Bind(R.id.refresh_joke)
    SwipeRefreshLayout refreshJoke;
    private int jokeFlag = 0;

    private RecyclerView.Adapter jokeAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<JokeInfo> jokeInfoList;
    private long currentTime;


    public JokeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View jokeView = inflater.inflate(R.layout.fragment_joke, container, false);
        ButterKnife.bind(this, jokeView);
        initRecyclerView();
        return jokeView;
    }

    private void initRecyclerView() {
        jokeInfoList = SPUtils.getFirstPageJoke(getContext());
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerJoke.setLayoutManager(layoutManager);

        refreshJoke.setColorSchemeResources(android.R.color.holo_red_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_purple);

        refreshJoke.post(new Runnable() {
            @Override
            public void run() {
                refreshJoke.setRefreshing(true);
                getJokeData();
            }
        });

        refreshJoke.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getJokeData();
            }
        });

        recyclerJoke.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();

                if (lastVisibleItem == totalItemCount - 1 && dy > 0) {
                    refreshJoke.setRefreshing(true);
                    getMoreData();
                }

            }
        });

        jokeAdapter = new JokeAdapter(jokeInfoList, getActivity());
        recyclerJoke.setAdapter(jokeAdapter);
    }

    private void getJokeData() {
        jokeFlag = 0;
        currentTime = System.currentTimeMillis() / 1000;
        Parameters parameters = new Parameters();
        parameters.add("sort", "desc");
        parameters.add("page", 1);
        parameters.add("pagesize", 20);
        parameters.add("time", currentTime);
        JokeClient.fetchData(getActivity(), BASE_URL, parameters, this);
    }

    private void getMoreData() {
        jokeFlag = 1;
        if (currentTime != jokeInfoList.get(jokeInfoList.size() - 1).getUnixtime()) {
            currentTime = jokeInfoList.get(jokeInfoList.size() - 1).getUnixtime();
            Parameters parameters = new Parameters();
            parameters.add("sort", "desc");
            parameters.add("page", 1);
            parameters.add("pagesize", 20);
            parameters.add("time", currentTime);
            JokeClient.fetchData(getActivity(), BASE_URL, parameters, this);
        }
    }

    @Override
    public void onDataReceived(String json) {
        if (refreshJoke != null)
            refreshJoke.setRefreshing(false);
        if (jokeFlag == 0) {
            jokeInfoList.clear();
            try {
                jokeInfoList.addAll(parseJson(json));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SPUtils.saveFirstPageJoke(getContext(), jokeInfoList.subList(0, 20));
            jokeAdapter.notifyDataSetChanged();
        } else {
            try {
                jokeInfoList.addAll(parseJson(json));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jokeAdapter.notifyDataSetChanged();
        }
    }

    private List<JokeInfo> parseJson(String json) throws JSONException {
        String result = new JSONObject(json).getString("result");
        String data = new JSONObject(result).getString("data");
        List<JokeInfo> models = JSON.parseArray(data, JokeInfo.class);
        if (models == null) return new ArrayList<>();
        return models;
    }

    @Override
    public void onDataFiled(String error) {
        if (refreshJoke != null)
            refreshJoke.setRefreshing(false);
        Snackbar.make(recyclerJoke, error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
