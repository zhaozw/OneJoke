package com.smarter.onejoke.ui;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.smarter.onejoke.R;
import com.smarter.onejoke.adapter.PicAdapter;
import com.smarter.onejoke.model.PicInfo;
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
public class PictureFragment extends Fragment implements JokeClient.DataReceivedListener {

    private static final String BASE_URL = "http://japi.juhe.cn/joke/img/list.from";
    @Bind(R.id.recycler_pic)
    RecyclerView recyclerPic;
    @Bind(R.id.refresh_pic)
    SwipeRefreshLayout refreshPic;
    private int picFlag = 0;

    private long currentTime;

    private List<PicInfo> picInfoList;
    private RecyclerView.LayoutManager layoutManager;
    private PicAdapter picAdapter;

    public PictureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View picView = inflater.inflate(R.layout.fragment_picture, container, false);
        ButterKnife.bind(this, picView);
        initView();
        return picView;
    }

    private void initView() {
        picInfoList = SPUtils.getFirstPagePictures(getContext());
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerPic.setLayoutManager(layoutManager);

        refreshPic.setColorSchemeResources(android.R.color.holo_red_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_purple);

        refreshPic.post(new Runnable() {
            @Override
            public void run() {
                refreshPic.setRefreshing(true);
                getPicData();
            }
        });


        refreshPic.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPicData();
            }
        });

        recyclerPic.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                if (lastVisibleItem == totalItemCount - 1 && dy > 0) {
                    refreshPic.setRefreshing(true);
                    getMoreData();
                }
            }
        });

        picAdapter = new PicAdapter(picInfoList, getActivity());
        recyclerPic.setAdapter(picAdapter);
    }

    private void getPicData() {
        picFlag = 0;
        currentTime = System.currentTimeMillis() / 1000;
        Log.i("currentTime--->", currentTime + "");
        Parameters parameters = new Parameters();
        parameters.add("sort", "desc");
        parameters.add("page", 1);
        parameters.add("pagesize", 20);
        parameters.add("time", currentTime);
        JokeClient.fetchData(getActivity(), BASE_URL, parameters, this);
    }

    private void getMoreData() {
        picFlag = 1;
        if (currentTime != picInfoList.get(picInfoList.size() - 1).getUnixtime()) {
            currentTime = picInfoList.get(picInfoList.size() - 1).getUnixtime();
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
        if (refreshPic != null)
            refreshPic.setRefreshing(false);
        if (picFlag == 0) {
            picInfoList.clear();
            try {
                picInfoList.addAll(parseJson(json));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SPUtils.saveFirstPagePictures(getContext(), picInfoList.subList(0, 20));
            picAdapter.notifyDataSetChanged();
        } else {
            try {
                picInfoList.addAll(parseJson(json));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            picAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDataFiled(String error) {
        if (refreshPic != null)
            refreshPic.setRefreshing(false);
        Snackbar.make(recyclerPic, error, Snackbar.LENGTH_SHORT).show();
    }

    private List<PicInfo> parseJson(String json) throws org.json.JSONException {
        String result = new JSONObject(json).getString("result");
        String data = new JSONObject(result).getString("data");
        List<PicInfo> infos = JSON.parseArray(data, PicInfo.class);
        if (infos == null) return new ArrayList<>();
        return infos;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
