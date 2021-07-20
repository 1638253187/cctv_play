package com.zkzj.rtmp_terminal.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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


public class WeiShiFragment extends Fragment implements OnRefreshLoadMoreListener {

    private View view;
    private RecyclerView mRecyTv;
    private SmartRefreshLayout mSmart;
    private ArrayList<TvBean> list;
    private LinearLayoutManager layoutManager;
    private RecyFlAdapter adapter;

    public WeiShiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_wei_shi, container, false);
        initData();
        initView(inflate);
        return inflate;
    }

    private void initData() {
        list = new ArrayList<>();
        list.add(new TvBean(R.mipmap.cctv, "河南卫视", "rtmp://58.200.131.2:1935/livetv/hntv", "电影频道"));
        list.add(new TvBean(R.mipmap.cctv, "江苏卫视", "rtmp://58.200.131.2:1935/livetv/jstv", "电影频道"));
        list.add(new TvBean(R.mipmap.cctv, "安徽卫视", "rtmp://58.200.131.2:1935/livetv/ahtv", "电影频道"));
        list.add(new TvBean(R.mipmap.cctv, "辽宁卫视", "rtmp://58.200.131.2:1935/livetv/lntv", "电影频道"));
        list.add(new TvBean(R.mipmap.cctv, "山西卫视", "rtmp://58.200.131.2:1935/livetv/sxrtv", "电影频道"));
        list.add(new TvBean(R.mipmap.cctv, "兵团卫视", "rtmp://58.200.131.2:1935/livetv/bttv", "电影频道"));
        list.add(new TvBean(R.mipmap.cctv, "黑龙江卫视", "rtmp://58.200.131.2:1935/livetv/hljtv", "芒果台"));
        list.add(new TvBean(R.mipmap.cctv, "江西卫视", "rtmp://58.200.131.2:1935/livetv/jxtv", "芒果台"));
        list.add(new TvBean(R.mipmap.cctv, "广东卫视", "rtmp://58.200.131.2:1935/livetv/gdtv", "芒果台"));
        list.add(new TvBean(R.mipmap.cctv, "贵州卫视", "rtmp://58.200.131.2:1935/livetv/gztv", "芒果台"));
        list.add(new TvBean(R.mipmap.cctv, "湖北卫视", "rtmp://58.200.131.2:1935/livetv/hbtv", "芒果台"));
        list.add(new TvBean(R.mipmap.cctv, "湖南卫视", "rtmp://58.200.131.2:1935/livetv/hunantv", "芒果台"));
        list.add(new TvBean(R.mipmap.cctv, "四川卫视", "rtmp://58.200.131.2:1935/livetv/sctv", "芒果台"));
        list.add(new TvBean(R.mipmap.cctv, "CCTVNEWS", "中央六套电视直播频道", ""));
        list.add(new TvBean(R.mipmap.cctv, "重庆卫视", "rtmp://58.200.131.2:1935/livetv/cqtv", ""));
        list.add(new TvBean(R.mipmap.cctv, "东方卫视", "rtmp://58.200.131.2:1935/livetv/dftv", ""));
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
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mSmart.finishRefresh();
    }



}