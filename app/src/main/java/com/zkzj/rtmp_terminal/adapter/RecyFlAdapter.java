package com.zkzj.rtmp_terminal.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zkzj.rtmp_terminal.R;
import com.zkzj.rtmp_terminal.bean.TvBean;

import java.util.ArrayList;

public class RecyFlAdapter extends RecyclerView.Adapter<RecyFlAdapter.ViewHolder> {
    private Context context;
    private ArrayList<TvBean> list;
    private OnItemCliclListener onItemCliclListener;
    private View inflate;

    public void setOnItemCliclListener(OnItemCliclListener onItemCliclListener) {
        this.onItemCliclListener = onItemCliclListener;
    }

    public RecyFlAdapter(Context context, ArrayList<TvBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        inflate = LayoutInflater.from(context).inflate(R.layout.tv_item, viewGroup, false);
        return new ViewHolder (inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final TvBean tvBean = list.get(i);
        viewHolder.tv_name.setText(tvBean.getTv_name());
        viewHolder.view.setOnClickListener (new View.OnClickListener () {
        @Override
        public void onClick(View v) {
            if (onItemCliclListener!=null){
                onItemCliclListener.onItemClick (i);
            }
        }
    });
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_cctv;
        private TextView tv_name;
        private View view;

        public ViewHolder(@NonNull View itemView) {
            super (itemView);
            iv_cctv = itemView.findViewById (R.id.iv_cctv);
            tv_name = itemView.findViewById (R.id.tv_name);
            view = itemView;
        }
    }

    public interface OnItemCliclListener {
        void onItemClick(int position);
    }
}
