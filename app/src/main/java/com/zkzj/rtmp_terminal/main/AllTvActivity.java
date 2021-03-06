package com.zkzj.rtmp_terminal.main;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.azhon.appupdate.config.UpdateConfiguration;
import com.azhon.appupdate.listener.OnButtonClickListener;
import com.azhon.appupdate.listener.OnDownloadListener;
import com.azhon.appupdate.manager.DownloadManager;
import com.zkzj.rtmp_terminal.R;
import com.zkzj.rtmp_terminal.main.fragment.AtherFragment;
import com.zkzj.rtmp_terminal.main.fragment.CCtv_fragment;
import com.zkzj.rtmp_terminal.main.fragment.ShaoerFragment;
import com.zkzj.rtmp_terminal.main.fragment.WeiShiFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AllTvActivity extends AppCompatActivity {

    private TabLayout mTab;
    private ViewPager mVp;
    private RelativeLayout mRelay;
    private int mStartColor;
    private int newVersion = 2;
    private DownloadManager manager;
    private String url = "https://www.guanzhu.mobi/apk/RmGG6.apk";
    private String jsonurl = "http://39.106.108.12/ver.json";
    private int newVerCode;
    private String newVerName;
    private String Title;
    private String type;
    private int verCode;
    private String verName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);//??????????????????
        //???????????????
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //???????????????
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_all_tv);
        initView();
        SetBackUi();
        boolean isUpdate = parseJson(jsonurl);
    }


    private boolean parseJson(String str) {
        //???????????????okHttpClient??????
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        //???????????????Request??????
        Request request = new Request.Builder()
                .url(str)
                .get()
                .build();
        //???????????????Call??????
        final Call call = client.newCall(request);
        //?????????:??????get??????
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Response response = call.execute();//?????????????????????
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        Log.e("tag", "?????????:" + jsonObject);
                        type = jsonObject.getString("appname");
                        verName = jsonObject.getString("verName");
                        verCode = jsonObject.getInt("verCode");
                        Title = jsonObject.getString("Title");
                        Log.e("tag", "???????????????:" + verCode);
                        PackageManager packageManager = getPackageManager();
                        int version = -1;
                        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
                        version = packInfo.versionCode;//????????????
                        Log.e("tag","????????????:"+version);
                        if (verCode != 0) {
                            if (verCode > version) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Update_Apk();
//                                        Update_Apk_two();
                                    }
                                });
                                Log.e("tag", "?????? ??????");
                            } else {
                                Log.e("tag", "????????????");
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void Update_Apk_two() {
        DownloadManager manager = DownloadManager.getInstance(this);
        manager.setApkName("????????????.apk")
                .setApkUrl(url)
                .setSmallIcon(R.mipmap.ic_launcher)
                .download();
    }

    private void Update_Apk() {
        UpdateConfiguration configuration = new UpdateConfiguration()
                //??????????????????
                .setEnableLog(true)
                //????????????????????????????
                //.setHttpManager()
                //????????????????????????????????????
                .setJumpInstallPage(true)
                //??????????????????????????? (??????????????????demo???????????????)
//                .setDialogImage(R.drawable.icon)
                //?????????????????????
                .setDialogButtonColor(Color.parseColor("#E743DA"))
                //?????????????????????????????????????????????????????????
                .setDialogProgressBarColor(Color.parseColor("#E743DA"))
                //???????????????????????????
                .setDialogButtonTextColor(Color.WHITE)
                //?????????????????????????????????
                .setShowNotification(true)
                //??????????????????????????????toast
                .setShowBgdToast(false)
                //??????????????????
                .setForcedUpgrade(true)
                //????????????????????????????????????
                .setButtonClickListener(new OnButtonClickListener() {
                    @Override
                    public void onButtonClick(int id) {
                        Log.e("TAG", String.valueOf(id));
                    }
                })
                //???????????????????????????
                .setOnDownloadListener(new OnDownloadListener() {
                    @Override
                    public void start() {

                    }

                    @Override
                    public void downloading(int max, int progress) {
                        int curr = (int) (progress / (double) max * 100.0);
//                        mNumberProgressBar.setMax(100);
//                        mNumberProgressBar.setProgress(curr);
                    }

                    @Override
                    public void done(File apk) {
                    }

                    @Override
                    public void cancel() {
                    }

                    @Override
                    public void error(Exception e) {
                    }
                });

        //???????????????????????????
        manager = DownloadManager.getInstance(this);
        manager.setApkName("????????????.apk")
                .setApkUrl(url)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setShowNewerToast(true)
                .setConfiguration(configuration)
                .setApkVersionCode(2)
                .setApkVersionName(verName)
                .setApkSize("7.3")
                .setApkDescription(Title)
//                .setApkMD5("DC501F04BBAA458C9DC33008EFED5E7F")
                .download();
    }


    private void initView() {
        mTab = (TabLayout) findViewById(R.id.tab);
        mVp = (ViewPager) findViewById(R.id.vp);
        mRelay = (RelativeLayout) findViewById(R.id.relay);
        final ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new CCtv_fragment());
        fragments.add(new WeiShiFragment());
        fragments.add(new ShaoerFragment());
        fragments.add(new AtherFragment());
        mVp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {

                return fragments.get(i);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        mTab.setupWithViewPager(mVp);
        mTab.getTabAt(0).setText("CCTV");
        mTab.getTabAt(1).setText("??????");
        mTab.getTabAt(2).setText("??????");
        mTab.getTabAt(3).setText("??????");

        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        SetBackUi();
                        break;
                    case 1:
                        SetBackUi();
                        break;
                    case 2:
                        SetBackUi();
                        break;
                    case 3:
                        SetBackUi();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }


    private void SetBackUi() {
        //????????????????????????
        Random random = new Random();
        mStartColor = 0xff000000 | random.nextInt(0xffffff);
        int mEndColor = 0xff000000 | random.nextInt(0xffffff);
        int[] colors = {mStartColor, mEndColor};
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
        mRelay.setBackground(gradientDrawable);
    }

    private long exitTime = 0;

    public void onBackPressed() {
        doubleBackQuit();
    }

    /**
     * ???????????????????????????????????????
     */
    private void doubleBackQuit() {
        Snackbar.make(mVp, "???????????????????????????(????????)", Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.parseColor("#009688"))
                .setAction("????????????", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(AllTvActivity.this, About.class));
                    }
                })
                .show();
        if (System.currentTimeMillis() - exitTime > 3000) {
//            Toast.makeText (getApplicationContext (), "??????????????????", Toast.LENGTH_SHORT).show ();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }


}