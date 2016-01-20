package com.smarter.onejoke.ui;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.smarter.onejoke.R;
import com.smarter.onejoke.adapter.JokeAdapter;
import com.smarter.onejoke.model.JokeInfo;
import com.smarter.onejoke.utils.JokeClient;
import com.thinkland.sdk.android.Parameters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class JokeFragment extends Fragment {

    private static final String BASE_URL = "http://japi.juhe.cn/joke/content/list.from";
    private int jokeFlag = 0;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter jokeAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout refreshLayout;
    private List<JokeInfo> jokeInfoList = new ArrayList<>();
    private long currentTime;


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            refreshLayout.setRefreshing(false);
            if (msg.what == 0) {
                refreshLayout.setRefreshing(false);
                String jokeResult = (String) msg.obj;
                paseJsonAndShowList(jokeResult);
            } else if (msg.what == 1) {
                String reason = (String) msg.obj;
                Toast.makeText(getActivity(), reason, Toast.LENGTH_SHORT).show();
            }
        }
    };

    public JokeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View jokeView = inflater.inflate(R.layout.fragment_joke, container, false);
        recyclerView = (RecyclerView) jokeView.findViewById(R.id.recycler_joke);

        refreshLayout = (SwipeRefreshLayout) jokeView.findViewById(R.id.refresh_joke);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        refreshLayout.setColorSchemeResources(android.R.color.holo_red_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_purple);

        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                getJokeData();
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getJokeData();
            }
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();

                if (lastVisibleItem == totalItemCount - 1 && dy > 0) {
                    refreshLayout.setRefreshing(true);
                    getMoreData();
                }

            }
        });

        jokeAdapter = new JokeAdapter(jokeInfoList, getActivity());
        recyclerView.setAdapter(jokeAdapter);

        return jokeView;
    }

    private void paseJsonAndShowList(String jokeResult) {
        try {
            JSONObject object_joke = new JSONObject(jokeResult);
            Log.i("object_joke", object_joke.toString());
            long resultCode = object_joke.getLong("error_code");
            Log.i("redultCode", resultCode + "");
            if (resultCode == 0) {
                JSONObject object = object_joke.getJSONObject("result");
                if (jokeFlag == 0) {
                    jokeInfoList.clear();

                    Log.i("object", object.toString());
                    JSONArray jsonArray = object.getJSONArray("data");
                    parseJson(jsonArray);
                    jokeAdapter.notifyDataSetChanged();

                } else if (jokeFlag == 1) {
                    JSONArray jsonArray = object.getJSONArray("data");
                    parseJson(jsonArray);
                    jokeAdapter.notifyDataSetChanged();
                    Log.i("JokeSize", jokeInfoList.size() + "");
                }

            } else {
                Toast.makeText(getActivity(), object_joke.getString("reason"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseJson(JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            String content = jsonObject.getString("content");
            String updateTime = jsonObject.getString("updatetime");
            long unixTime = jsonObject.getLong("unixtime");
            JokeInfo jokeInfo = new JokeInfo();
            jokeInfo.setContents(content);
            jokeInfo.setUpdateTime(updateTime);
            jokeInfo.setUnixTime(unixTime);
            jokeInfoList.add(jokeInfo);
            Log.i("data", content);

        }
    }

    private void getJokeData() {
        jokeFlag = 0;
        currentTime = System.currentTimeMillis() / 1000;
        Log.i("currentTime--->", currentTime + "");
        Parameters parameters = new Parameters();
        parameters.add("sort", "desc");
        parameters.add("page", 1);
        parameters.add("pagesize", 20);
        parameters.add("time", currentTime);
        JokeClient.getJoke(getActivity(),BASE_URL, parameters, handler);
    }

    private void getMoreData() {
        jokeFlag = 1;
        if (currentTime != jokeInfoList.get(jokeInfoList.size() - 1).getUnixTime()) {
            currentTime = jokeInfoList.get(jokeInfoList.size() - 1).getUnixTime();
            Parameters parameters = new Parameters();
            parameters.add("sort", "desc");
            parameters.add("page", 1);
            parameters.add("pagesize", 20);
            parameters.add("time", currentTime);
            JokeClient.getJoke(getActivity(),BASE_URL, parameters, handler);
        }
    }

}
