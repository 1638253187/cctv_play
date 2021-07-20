package com.zkzj.rtmp_terminal.main;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zkzj.rtmp_terminal.R;
import com.zkzj.rtmp_terminal.adapter.RecyFlAdapter;
import com.zkzj.rtmp_terminal.adapter.RecyliAdapter;
import com.zkzj.rtmp_terminal.bean.TvBean;
import com.zkzj.rtmp_terminal.util.BackMain;
import com.zkzj.rtmp_terminal.util.CenterLayoutManager;
import com.zkzj.rtmp_terminal.util.Constant;
import com.zkzj.rtmp_terminal.view.LoadingDialog;

import org.easydarwin.video.EasyPlayerClient;

import java.util.ArrayList;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private TextureView mSvSurfaceview;
    private EasyPlayerClient client;
    public static ImageView mBtnSwitchOrientation;
    public static RelativeLayout res_container;
    public static int NotTouch = 0;
    private BackMain backMain;
    public static String tv_address;
    private String tv_name;
    private TextView mTv_name;
    private ImageView mIv_back;
    private RecyclerView mRecyLoad;
    private CenterLayoutManager layoutManager;
    public static String PlayAddress;
    private RecyliAdapter adapter;
    private ArrayList<TvBean> tv_list;
    private LoadingDialog mLoadingDialog;
    private ConnectivityManager manager;
    private PopupWindow popupWindow3;
    private boolean wifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);//切换正常主题
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(getSystemUiVisibility());
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        tv_name = intent.getStringExtra("tv_name");
        tv_list = (ArrayList<TvBean>) intent.getSerializableExtra("list");
        tv_address = intent.getStringExtra("tv_address");
        PlayAddress = tv_address;
        initView();
        showLoading();
    }

    private void initView() {
        mSvSurfaceview = (TextureView) findViewById(R.id.sv_surfaceview);
        mBtnSwitchOrientation = (ImageView) findViewById(R.id.btn_switch_orientation);
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        res_container = (RelativeLayout) findViewById(R.id.res_container);
        mTv_name = (TextView) findViewById(R.id.tv_Name);
        mTv_name.setText(tv_name);
        mRecyLoad = (RecyclerView) findViewById(R.id.recy_load);
        mLoadingDialog = new LoadingDialog(this, getString(R.string.loading), false);
        mLoadingDialog.getWindow().setGravity(Gravity.TOP);
        initAdapter();
        mBtnSwitchOrientation.setOnClickListener(this);
        mIv_back.setOnClickListener(this);
        mSvSurfaceview.setOnTouchListener(this);
        backMain = new BackMain(1000 * 3, 1000, MainActivity.this);
        client = new EasyPlayerClient(MainActivity.this, Constant.KEY, mSvSurfaceview, null, null);
    }
    int IsWindow = 0;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            boolean isWifi = checkNetworkState();
            IsWindow++;
            if (IsWindow == 1) {
                if (isWifi) {
                    if (wifi) {
                        client.play(tv_address);
                        Log.e("tag", "IsWifi");
                    } else {
                        initPlay();
                        Log.e("tag", "wifi");
                    }
                }
            }
        }
    }

    private void initPlay() {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.5f;
        window.setAttributes(lp);
        View inflate3 = LayoutInflater.from(MainActivity.this).inflate(R.layout.edit_dialog, null);
        TextView sure = inflate3.findViewById(R.id.dialog_confirm_sure);
        TextView cancel = inflate3.findViewById(R.id.dialog_confirm_cancel);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client.play(tv_address);
                popupWindow3.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                popupWindow3.dismiss();
            }
        });

        popupWindow3 = new PopupWindow(inflate3, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow3.setBackgroundDrawable(new BitmapDrawable());
        popupWindow3.setOutsideTouchable(false);
        popupWindow3.setFocusable(true);
         /*
             2.先创建动画的style 样式,去使用进出场动画
          */
        popupWindow3.setAnimationStyle(R.style.popAnimation);
        popupWindow3.showAtLocation(mSvSurfaceview, Gravity.CENTER, 0, 0);
        popupWindow3.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Window window = getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.alpha = 1.0f;
                window.setAttributes(lp);
            }
        });
    }

    /**
     * 检测网络是否连接
     *
     * @return
     */
    private boolean checkNetworkState() {
        boolean flag = false;
        //得到网络连接信息
        manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //去进行判断网络是否连接
        if (manager.getActiveNetworkInfo() != null) {
            flag = manager.getActiveNetworkInfo().isAvailable();
        }
        if (!flag) {
            setNetwork();
        } else {
            wifi = isWifi(this);
        }
        return flag;
    }

    /**
     * 网络未连接时，调用设置方法
     */
    private void setNetwork() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("网络提示信息");
        builder.setMessage("网络不可用，如果继续，请先设置网络！");
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = null;
                /**
                 * 判断手机系统的版本！如果API大于10 就是3.0+
                 * 因为3.0以上的版本的设置和3.0以下的设置不一样，调用的方法不同
                 */
                if (android.os.Build.VERSION.SDK_INT > 10) {
                    intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
                } else {
                    intent = new Intent();
                    ComponentName component = new ComponentName(
                            "com.android.settings",
                            "com.android.settings.WirelessSettings");
                    intent.setComponent(component);
                    intent.setAction("android.intent.action.VIEW");
                }
                startActivity(intent);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create();
        builder.show();
    }


    private static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }


    /**
     * 网络已经连接，然后去判断是wifi连接还是GPRS连接
     * 设置一些自己的逻辑调用
     */
    private void isNetworkAvailable() {

        NetworkInfo.State gprs = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        NetworkInfo.State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (gprs == NetworkInfo.State.CONNECTED || gprs == NetworkInfo.State.CONNECTING) {
            Toast.makeText(this, "您是数据网络连接", Toast.LENGTH_SHORT).show();
        }
        //判断为wifi状态下才加载广告，如果是GPRS手机网络则不加载！
        if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
            Toast.makeText(this, "你是wife连接", Toast.LENGTH_SHORT).show();
//                loadAdmob();
        }

        Toast.makeText(this, "到了这步", Toast.LENGTH_SHORT).show();
    }

    private void initAdapter() {
        ((DefaultItemAnimator) mRecyLoad.getItemAnimator()).setSupportsChangeAnimations(false);
        layoutManager = new CenterLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyLoad.setLayoutManager(layoutManager);
        adapter = new RecyliAdapter(this, tv_list);
        mRecyLoad.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setOnItemCliclListener(new RecyliAdapter.OnItemCliclListener() {
            @Override
            public void onItemClick(int position) {
                TvBean tvBean = tv_list.get(position);
                if (tvBean.getTv_address().equals(PlayAddress)) {
                } else {
                    Show();
                    client.stop();
                    client.play(tvBean.getTv_address());
//                    initData(tvBean.getTv_address());
                    PlayAddress = tvBean.getTv_address();
                    mTv_name.setText(tvBean.getTv_name());
                    hideLoading();
                }
            }
        });
        for (int i = 0; i < tv_list.size(); i++) {
            if (tv_list.get(i).getTv_address().equals(tv_address)) {
                if (i != 0) {
                    MoveToPosition(layoutManager, i - 1);
                } else {
                    MoveToPosition(layoutManager, i);
                }
//                layoutManager.smoothScrollToPosition(mRecyLoad, new RecyclerView.State(), i);
            }
        }
    }

    private void initData(String url) {
        for (int i = 0; i < tv_list.size(); i++) {
            if (tv_list.get(i).getTv_address().equals(url)) {
//                MoveToPosition(layoutManager,i-1);
                layoutManager.smoothScrollToPosition(mRecyLoad, new RecyclerView.State(), i);
            }
        }
    }

    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this, getString(R.string.loading), false);
            mLoadingDialog.getWindow().setGravity(Gravity.TOP);
        } else {

        }

    }

    public void Show(){
        mLoadingDialog.show();
    }

    public void hideLoading() {
        if (mLoadingDialog != null) {
            new CountDownTimer(2000, 100) {
                @Override
                public void onTick(long l) {
                }

                @Override
                public void onFinish() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLoadingDialog.hide();
                        }
                    });
                }
            }.start();

        }
    }

    private void Gong() {
        mBtnSwitchOrientation.setVisibility(View.GONE);
    }

    private void orientation() {
        int orientation = getRequestedOrientation();
        if (orientation == SCREEN_ORIENTATION_UNSPECIFIED || orientation ==
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            ViewGroup.LayoutParams layoutParams = mSvSurfaceview.getLayoutParams();
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            mSvSurfaceview.setLayoutParams(layoutParams);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            ViewGroup.LayoutParams layoutParams = mSvSurfaceview.getLayoutParams();
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = 800;
            mSvSurfaceview.setLayoutParams(layoutParams);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    /*跳转到正在播放的项目*/
    public static void MoveToPosition(LinearLayoutManager manager, int n) {
        manager.scrollToPositionWithOffset(n, 0);
        manager.setStackFromEnd(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_switch_orientation:
                orientation();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (client != null) {
            client.stop();
            backMain.cancel();
            orientation();
        }
    }

    @Override
    protected void onResume() {
        timeStart();
        super.onResume();
    }

    //region 无操作 返回主页
    private void timeStart() {
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                backMain.start();
            }
        });
    }

    /*全屏显示*/
    @TargetApi(19)
    private static int getSystemUiVisibility() {
        int flags = View.SYSTEM_UI_FLAG_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            flags |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        return flags;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.sv_surfaceview:
                switch (event.getAction()) {
                    //获取触摸动作，如果ACTION_UP，计时开始。
                    case MotionEvent.ACTION_DOWN:
                        res_container.setVisibility(View.VISIBLE);
                        backMain.start();
                        break;
                    //否则其他动作计时取消
                    default:
                        backMain.cancel();
                        break;
                }
        }
        return false;
    }
}