package com.smarter.onejoke.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.smarter.onejoke.R;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class AboutActivity extends BaseActivity {

    private TextView versionText;
    private TextView aboutAuthor;
    private TextView checkUpgrade;
    private TextView markOnPlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        versionText = (TextView)findViewById(R.id.version_text);
        aboutAuthor = (TextView)findViewById(R.id.about_author);
        checkUpgrade = (TextView)findViewById(R.id.check_update);
        markOnPlay = (TextView)findViewById(R.id.mark_on_play);


        versionText.setText(getVersion());
        aboutAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this,AuthorActivity.class);
                startActivity(intent);

            }
        });
        checkUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutActivity.this, "正在检查。。。", Toast.LENGTH_SHORT).show();
                UmengUpdateAgent.setUpdateAutoPopup(false);
                UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
                    @Override
                    public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
                        switch (updateStatus) {
                            case UpdateStatus.Yes: // has update
                                UmengUpdateAgent.showUpdateDialog(AboutActivity.this, updateInfo);
                                break;
                            case UpdateStatus.No: // has no update
                                Toast.makeText(AboutActivity.this, "没有发现新版本", Toast.LENGTH_SHORT).show();
                                break;
                            case UpdateStatus.NoneWifi: // none wifi
                                Toast.makeText(AboutActivity.this, " 没有wifi连接， 只在wif检查更新 ", Toast.LENGTH_SHORT).show();
                                break;
                            case UpdateStatus.Timeout: // time out
                                Toast.makeText(AboutActivity.this, "超时", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
                UmengUpdateAgent.update(AboutActivity.this);
            }
        });
        markOnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);


    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_about, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String getVersion(){
        String versionName = "1.0.1";
        PackageManager manager = this.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(getPackageName(),0);
            versionName = info.versionName;
            return "Version:"+versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "Version:"+versionName;

        }
    }
}
