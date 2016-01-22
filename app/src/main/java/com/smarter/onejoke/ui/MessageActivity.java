package com.smarter.onejoke.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.smarter.onejoke.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MessageActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.app_bar)
    AppBarLayout appBar;
    @Bind(R.id.message_list)
    ListView listView;
    private ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();
    private String jsonData;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        preferences = getSharedPreferences("MESSAGE", 0);
        editor = preferences.edit();

        Intent intent = getIntent();
        String pushMessage = intent.getStringExtra("PushMessage");

        jsonData = preferences.getString("Message", null);
        if (jsonData != null) {
            try {
                parserJson(jsonData);
                if (pushMessage != null) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                    String currentTime = formatter.format(curDate);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("MessageContent", pushMessage);
                    map.put("MessageTime", currentTime);
                    listItem.add(map);
                    buildJson(listItem);
                }

                //生成适配器的Item和动态数组对应的元素
                SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,//数据源
                        R.layout.item_message,//ListItem的XML实现
                        //动态数组与ImageItem对应的子项
                        new String[]{"MessageContent", "MessageTime"},
                        //ImageItem的XML文件里面的一个ImageView,两个TextView ID
                        new int[]{R.id.message_content, R.id.message_time});
                //添加并且显示
                listView.setAdapter(listItemAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            if (pushMessage != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                String currentTime = formatter.format(curDate);
                HashMap<String, Object> map = new HashMap<>();
                map.put("MessageContent", pushMessage);
                map.put("MessageTime", currentTime);
                listItem.add(map);
                try {
                    buildJson(listItem);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //生成适配器的Item和动态数组对应的元素
            SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,//数据源
                    R.layout.item_message,//ListItem的XML实现
                    //动态数组与ImageItem对应的子项
                    new String[]{"MessageContent", "MessageTime"},
                    //ImageItem的XML文件里面的一个ImageView,两个TextView ID
                    new int[]{R.id.message_content, R.id.message_time});

            //添加并且显示
            listView.setAdapter(listItemAdapter);

        }
    }

    public void buildJson(ArrayList<HashMap<String, Object>> listItem) throws JSONException {
        JSONArray json = new JSONArray();
        for (int i = 0; i < listItem.size(); i++) {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("MessageContent", listItem.get(i).get("MessageContent"));
            jsonObj.put("MessageTime", listItem.get(i).get("MessageTime"));
            //把每个数据当作一对象添加到数组里
            json.put(jsonObj);
            jsonData = json.toString();
            editor.putString("Message", jsonData);
            editor.commit();
        }
    }

    // 解析JSON字符串
    public void parserJson(String jsonData) throws JSONException {
        //构建JSON数组对象
        JSONArray json1 = new JSONArray(jsonData);
        for (int i = 0; i < json1.length(); i++) {
            JSONObject jsonObj2 = json1.optJSONObject(i);
            String messageContent = jsonObj2.getString("MessageContent");
            String messageTime = jsonObj2.getString("MessageTime");
            HashMap<String, Object> map = new HashMap<>();
            map.put("MessageContent", messageContent);
            map.put("MessageTime", messageTime);
            listItem.add(map);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
