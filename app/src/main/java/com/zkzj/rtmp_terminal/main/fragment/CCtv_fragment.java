package com.zkzj.rtmp_terminal.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.umeng.analytics.MobclickAgent;
import com.zkzj.rtmp_terminal.R;
import com.zkzj.rtmp_terminal.adapter.RecyFlAdapter;
import com.zkzj.rtmp_terminal.bean.TvBean;
import com.zkzj.rtmp_terminal.main.MainActivity;

import java.util.ArrayList;


public class CCtv_fragment extends Fragment implements OnRefreshLoadMoreListener {


    private View view;
    public static RecyclerView mRecyTv;
    private SmartRefreshLayout mSmart;
    private ArrayList<TvBean> list;
    private LinearLayoutManager layoutManager;
    private RecyFlAdapter adapter;

    public CCtv_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_c_ctv_fragment, container, false);
        initData();
        initView(inflate);
        return inflate;
    }

    private void initData() {
        list = new ArrayList<>();
        list.add(new TvBean(R.mipmap.cctv,"CCTV1-综合","rtmp://58.200.131.2:1935/livetv/cctv1","新闻联播"));
        list.add(new TvBean(R.mipmap.cctv,"CCTV2-财经","rtmp://58.200.131.2:1935/livetv/cctv2","财经"));
        list.add(new TvBean(R.mipmap.cctv,"CCTV3-综艺","rtmp://58.200.131.2:1935/livetv/cctv3","财经"));
        list.add(new TvBean(R.mipmap.cctv,"CCTV4-中文国际(亚洲)","rtmp://58.200.131.2:1935/livetv/cctv4","亚洲"));
        list.add(new TvBean(R.mipmap.cctv,"CCTV5-体育","rtmp://58.200.131.2:1935/livetv/cctv5","体育看球"));
        list.add(new TvBean(R.mipmap.cctv,"CCTV6-电影","rtmp://58.200.131.2:1935/livetv/cctv6","电影频道"));
        list.add(new TvBean(R.mipmap.cctv,"CCTV-7军事农业","rtmp://58.200.131.2:1935/livetv/cctv7","电影频道"));
        list.add(new TvBean(R.mipmap.cctv,"CCTV-8电视剧","rtmp://58.200.131.2:1935/livetv/cctv8","电影频道"));
        list.add(new TvBean(R.mipmap.cctv,"CCTV-9记录","rtmp://58.200.131.2:1935/livetv/cctv9","电影频道"));
        list.add(new TvBean(R.mipmap.cctv,"CCTV-10科教","rtmp://58.200.131.2:1935/livetv/cctv10","电影频道"));
        list.add(new TvBean(R.mipmap.cctv,"CCTV-11戏曲","rtmp://58.200.131.2:1935/livetv/cctv11","电影频道"));
        list.add(new TvBean(R.mipmap.cctv,"CCTV-12社会与法","rtmp://58.200.131.2:1935/livetv/cctv12","电影频道"));
        list.add(new TvBean(R.mipmap.cctv,"CCTV-13新闻","rtmp://58.200.131.2:1935/livetv/cctv13","电影频道"));
        list.add(new TvBean(R.mipmap.cctv,"CCTV-14少儿","rtmp://58.200.131.2:1935/livetv/cctv14","电影频道"));
        list.add(new TvBean(R.mipmap.cctv,"CCTV-15音乐","rtmp://58.200.131.2:1935/livetv/cctv15","电影频道"));
        list.add(new TvBean(R.mipmap.cctv,"CGTN-16新闻","rtmp://58.200.131.2:1935/livetv/cctv16","电影频道"));
    }


    private void initView(View inflate) {
        mRecyTv = (RecyclerView) inflate.findViewById(R.id.recy_tv);
        mSmart = (SmartRefreshLayout) inflate.findViewById(R.id.smart);
        mSmart.setOnRefreshLoadMoreListener(this);
        ((DefaultItemAnimator) mRecyTv.getItemAnimator ()).setSupportsChangeAnimations (false);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyTv.setLayoutManager (layoutManager);
        adapter = new RecyFlAdapter(getActivity(), list);
        adapter.setHasStableIds (true);
        mRecyTv.setAdapter (adapter);
        mRecyTv.setItemAnimator (new DefaultItemAnimator());
        adapter.notifyDataSetChanged ();
        adapter.setOnItemCliclListener(new RecyFlAdapter.OnItemCliclListener() {
            @Override
            public void onItemClick(int position) {
                TvBean tvBean = list.get(position);
                String tv_name = tvBean.getTv_name();
                String tv_address = tvBean.getTv_address();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("list",list);
                intent.putExtra ("tv_name", tv_name);
                intent.putExtra ("tv_address", tv_address);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("CCtv_fragment"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(getActivity());//统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("CCtv_fragment"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(getActivity());

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mSmart.finishRefresh();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}