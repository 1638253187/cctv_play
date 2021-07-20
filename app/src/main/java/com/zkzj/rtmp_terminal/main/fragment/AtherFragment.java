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
import com.zkzj.rtmp_terminal.R;
import com.zkzj.rtmp_terminal.adapter.RecyFlAdapter;
import com.zkzj.rtmp_terminal.bean.TvBean;
import com.zkzj.rtmp_terminal.main.MainActivity;

import java.util.ArrayList;


public class AtherFragment extends Fragment implements  OnRefreshLoadMoreListener {

    private View view;
    private RecyclerView mRecyTv;
    private SmartRefreshLayout mSmart;
    private LinearLayoutManager layoutManager;
    private ArrayList<TvBean> list;
    private RecyFlAdapter adapter;

    public AtherFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_ather, container, false);
        initData();
        initView(inflate);
        return inflate;
    }

    private void initData() {
        list = new ArrayList<>();
        list.add(new TvBean(R.mipmap.cctv, "韩国GoodTV", "rtmp://mobliestream.c3tv.com:554/live/goodtv.sdp", ""));
        list.add(new TvBean(R.mipmap.cctv, "CHC动作电影", "rtmp://58.200.131.2:1935/livetv/chcatv", ""));
        list.add(new TvBean(R.mipmap.cctv, "凤凰卫视中文台", "rtmp://58.200.131.2:1935/livetv/fhzw", ""));
        list.add(new TvBean(R.mipmap.cctv, "凤凰卫视资讯台", "rtmp://58.200.131.2:1935/livetv/fhzx", ""));
        list.add(new TvBean(R.mipmap.cctv, "凤凰卫视电影台", "rtmp://58.200.131.2:1935/livetv/fhdy", ""));
        list.add(new TvBean(R.mipmap.cctv, "Star Sports", "rtmp://58.200.131.2:1935/livetv/starsports", ""));
        list.add(new TvBean(R.mipmap.cctv, "Channel[V]", "rtmp://58.200.131.2:1935/livetv/channelv", ""));

    }

    private void initView(View inflate) {
        mRecyTv = (RecyclerView) inflate.findViewById(R.id.recy_tv);
        mSmart = (SmartRefreshLayout) inflate.findViewById(R.id.smart);
        mSmart.setOnRefreshLoadMoreListener(this);
        ((DefaultItemAnimator) mRecyTv.getItemAnimator()).setSupportsChangeAnimations(false);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyTv.setLayoutManager(layoutManager);
        adapter = new RecyFlAdapter(getActivity(), list);
        adapter.setHasStableIds(true);
        mRecyTv.setAdapter(adapter);
        mRecyTv.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyDataSetChanged();
        adapter.setOnItemCliclListener(new RecyFlAdapter.OnItemCliclListener() {
            @Override
            public void onItemClick(int position) {
                TvBean tvBean = list.get(position);
                String tv_name = tvBean.getTv_name();
                String tv_address = tvBean.getTv_address();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("list",list);
                intent.putExtra("tv_name", tv_name);
                intent.putExtra("tv_address", tv_address);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mSmart.finishRefresh();
    }



}