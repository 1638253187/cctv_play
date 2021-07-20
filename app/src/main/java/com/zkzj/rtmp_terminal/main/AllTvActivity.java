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
        setTheme(R.style.AppTheme);//切换正常主题
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_all_tv);
        initView();
        SetBackUi();
        boolean isUpdate = parseJson(jsonurl);
    }


    private boolean parseJson(String str) {
        //第一步获取okHttpClient对象
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        //第二步构建Request对象
        Request request = new Request.Builder()
                .url(str)
                .get()
                .build();
        //第三步构建Call对象
        final Call call = client.newCall(request);
        //第四步:同步get请求
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Response response = call.execute();//必须子线程执行
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        Log.e("tag", "数据为:" + jsonObject);
                        type = jsonObject.getString("appname");
                        verName = jsonObject.getString("verName");
                        verCode = jsonObject.getInt("verCode");
                        Title = jsonObject.getString("Title");
                        Log.e("tag", "更新版本为:" + verCode);
                        PackageManager packageManager = getPackageManager();
                        int version = -1;
                        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
                        version = packInfo.versionCode;//获取版本
                        Log.e("tag","当前版本:"+version);
                        if (verCode != 0) {
                            if (verCode > version) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Update_Apk();
//                                        Update_Apk_two();
                                    }
                                });
                                Log.e("tag", "走了 升级");
                            } else {
                                Log.e("tag", "无需升级");
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
        manager.setApkName("卫星电视.apk")
                .setApkUrl(url)
                .setSmallIcon(R.mipmap.ic_launcher)
                .download();
    }

    private void Update_Apk() {
        UpdateConfiguration configuration = new UpdateConfiguration()
                //输出错误日志
                .setEnableLog(true)
                //设置自定义的下载🚀
                //.setHttpManager()
                //下载完成自动跳动安装页面
                .setJumpInstallPage(true)
                //设置对话框背景图片 (图片规范参照demo中的示例图)
//                .setDialogImage(R.drawable.icon)
                //设置按钮的颜色
                .setDialogButtonColor(Color.parseColor("#E743DA"))
                //设置对话框强制更新时进度条和文字的颜色
                .setDialogProgressBarColor(Color.parseColor("#E743DA"))
                //设置按钮的文字颜色
                .setDialogButtonTextColor(Color.WHITE)
                //设置是否显示通知栏进度
                .setShowNotification(true)
                //设置是否提示后台下载toast
                .setShowBgdToast(false)
                //设置强制更新
                .setForcedUpgrade(true)
                //设置对话框按钮的点击监听
                .setButtonClickListener(new OnButtonClickListener() {
                    @Override
                    public void onButtonClick(int id) {
                        Log.e("TAG", String.valueOf(id));
                    }
                })
                //设置下载过程的监听
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

        //设置下载过程的监听
        manager = DownloadManager.getInstance(this);
        manager.setApkName("卫星电视.apk")
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
        mTab.getTabAt(1).setText("卫视");
        mTab.getTabAt(2).setText("少儿");
        mTab.getTabAt(3).setText("其他");

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
        //设置渐变色状态栏
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
     * 连续按两次返回键，退出应用
     */
    private void doubleBackQuit() {
        Snackbar.make(mVp, "再按一次退出程序！(๑ت๑)", Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.parseColor("#009688"))
                .setAction("关于作者", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(AllTvActivity.this, About.class));
                    }
                })
                .show();
        if (System.currentTimeMillis() - exitTime > 3000) {
//            Toast.makeText (getApplicationContext (), "再按一次退出", Toast.LENGTH_SHORT).show ();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }


}