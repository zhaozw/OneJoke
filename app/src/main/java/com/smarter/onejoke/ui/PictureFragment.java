package com.smarter.onejoke.ui;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.smarter.onejoke.R;
import com.smarter.onejoke.adapter.DividerItemDecoration;
import com.smarter.onejoke.adapter.PicAdapter;
import com.smarter.onejoke.utils.JokeClient;
import com.smarter.onejoke.model.PicInfo;
import com.thinkland.sdk.android.Parameters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PictureFragment extends Fragment {

    private static final String BASE_URL = "http://japi.juhe.cn/joke/img/list.from";
    private int picFlag = 0;

    private long currentTime;

    private List<PicInfo> picInfoList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FloatingActionButton fabPic;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView.LayoutManager layoutManager;
    private PicAdapter picAdapter;




    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            refreshLayout.setRefreshing(false);

            if (msg.what == 0){
                String picResult = (String)msg.obj;
                parseJsonAndShowList(picResult);
            }else if (msg.what == 1){
                String reason = (String)msg.obj;
                Toast.makeText(getActivity(),reason,Toast.LENGTH_SHORT).show();
            }

        }
    };


    public PictureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View picView = inflater.inflate(R.layout.fragment_picture, container, false);
        recyclerView = (RecyclerView)picView.findViewById(R.id.recycler_pic);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);

        fabPic = (FloatingActionButton)picView.findViewById(R.id.fab_picture);
        if (PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getBoolean("pref_dark_theme", false)){
            fabPic.setColorNormal(getResources().getColor(R.color.colorPrimaryInverse));
        }else {
            fabPic.setColorNormal(getResources().getColor(R.color.colorPrimary));
        }
        fabPic.hide(false);
        refreshLayout = (SwipeRefreshLayout)picView.findViewById(R.id.refresh_pic);
        refreshLayout.setColorSchemeResources(android.R.color.holo_red_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_purple);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        }, 250);

        fabPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshLayout.setRefreshing(true);
                getPicData();
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPicData();
            }
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem =  layoutManager.findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                if (lastVisibleItem == totalItemCount-1 && dy > 0) {
                    refreshLayout.setRefreshing(true);
                    getMoreData();
                }
                if (dy>0){
                    fabPic.hide();
                }else {
                    fabPic.show();
                }
            }
        });


        getPicData();
        return picView;
    }

    private void parseJsonAndShowList(String picResult){
        try {
            JSONObject object_pic = new JSONObject(picResult);
            long resultCode = object_pic.getLong("error_code");
            if (resultCode == 0) {
                JSONObject object = object_pic.getJSONObject("result");
                if (picFlag == 0) {
                    picInfoList.clear();
                    JSONArray jsonArray = object.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        PicInfo picInfo = new PicInfo();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String picUrl = jsonObject.getString("url");
                                /*if (!picUrl.endsWith(".gif")) {
                                    picInfo.setPicUrl(picUrl);
                                    String description = jsonObject.getString("content");
                                    picInfo.setDescription(description);
                                    long unixTime = jsonObject.getLong("unixtime");
                                    Log.i("UnixTime",unixTime+"");
                                    picInfo.setUnixTime(unixTime);
                                    picInfoList.add(picInfo);
                                }*/
                        picInfo.setPicUrl(picUrl);
                        String description = jsonObject.getString("content");
                        picInfo.setDescription(description);
                        long unixTime = jsonObject.getLong("unixtime");
                        picInfo.setUnixTime(unixTime);
                        picInfoList.add(picInfo);
                    }
                    picAdapter = new PicAdapter(picInfoList, getActivity());
                    recyclerView.setAdapter(picAdapter);
                    fabPic.show();
                }else if (picFlag == 1){
                    JSONArray jsonArray = object.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        PicInfo picInfo = new PicInfo();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String picUrl = jsonObject.getString("url");
                        if (!picUrl.endsWith(".gif")) {
                            picInfo.setPicUrl(picUrl);
                            String description = jsonObject.getString("content");
                            picInfo.setDescription(description);
                            long unixTime = jsonObject.getLong("unixtime");
                            Log.i("UnixTime",unixTime+"");
                            picInfo.setUnixTime(unixTime);
                            picInfoList.add(picInfo);
                        }
                    }

                    picAdapter.notifyDataSetChanged();
                }
            }else {
                Toast.makeText(getActivity(),object_pic.getString("reason"),Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getPicData(){
        picFlag = 0;
        currentTime = System.currentTimeMillis()/1000;
        Log.i("currentTime--->", currentTime + "");
        Parameters parameters = new Parameters();
        parameters.add("sort","desc");
        parameters.add("page",1);
        parameters.add("pagesize",20);
        parameters.add("time",currentTime);
        JokeClient.getJoke(BASE_URL, parameters, handler);
    }
    private void getMoreData(){
        picFlag = 1;
        if (currentTime != picInfoList.get(picInfoList.size()-1).getUnixTime()) {
            currentTime = picInfoList.get(picInfoList.size()-1).getUnixTime();
            Parameters parameters = new Parameters();
            parameters.add("sort", "desc");
            parameters.add("page", 1);
            parameters.add("pagesize", 20);
            parameters.add("time", currentTime);
            JokeClient.getJoke(BASE_URL, parameters, handler);
        }
    }


}
