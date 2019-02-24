package com.wd.tech.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.bean.HomeListBean;
import com.wd.tech.util.DateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.MyViewHolder> {


    private List<HomeListBean> mList = new ArrayList<>();

    private Context mContext;

    public HomeListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addItem(List<HomeListBean> beanList) {
        if (beanList != null) {
            mList.addAll(beanList);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_home_list, viewGroup, false);
//        View view = View.inflate(mContext, R.layout.item_home_list, null);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        HomeListBean homeListBean = mList.get(i);
        myViewHolder.img.setImageURI((homeListBean.getThumbnail()));
        myViewHolder.title.setText(homeListBean.getTitle());
        myViewHolder.summary.setText(homeListBean.getSummary());
        myViewHolder.source.setText(homeListBean.getSource());
        try {
            myViewHolder.time.setText(DateUtils.dateFormat(new Date(homeListBean.getReleaseTime()), DateUtils.DATE_PATTERN));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        myViewHolder.collect.setText(homeListBean.getCollection() + "");
        myViewHolder.share.setText(homeListBean.getShare() + "");
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }


    //内部类
    class MyViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView img;
        private TextView title, summary, source, time, collect, share;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.item_home_list_img);
            title = itemView.findViewById(R.id.item_home_list_title);
            summary = itemView.findViewById(R.id.item_home_list_summary);
            source = itemView.findViewById(R.id.item_home_list_source);
            time = itemView.findViewById(R.id.item_home_list_time);
            collect = itemView.findViewById(R.id.item_home_list_txt_coll);
            share = itemView.findViewById(R.id.item_home_list_txt_share);
        }
    }


}