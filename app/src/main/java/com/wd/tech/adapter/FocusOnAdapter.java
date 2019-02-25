package com.wd.tech.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.wd.tech.R;
import com.wd.tech.bean.FollowUser;
import com.wd.tech.core.SideslipView;


import java.util.ArrayList;
import java.util.List;


public class FocusOnAdapter extends RecyclerView.Adapter<FocusOnAdapter.MyViewHolder>  {
    private List<FollowUser> mDatas = new ArrayList<>();
    private Activity activity;
    private LayoutInflater inflater;
    Shan shan;

    public FocusOnAdapter(Activity activity) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
    }

    public void getShan(Shan shan){
        this.shan=shan;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    //填充onCreateViewHolder方法返回的holder中的控件
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Glide.with(activity).load(mDatas.get(position).getHeadPic())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(holder.head);
        holder.name.setText(""+mDatas.get(position).getNickName());
        holder.sideslipView.close();
        holder.shanchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shan.onshan(position);
            }
        });
    }

    //重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.focuson_item,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    public void addAll(List<FollowUser> result) {
        if (result!=null){
            mDatas.addAll(result);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder { //承载Item视图的子布局
        ImageView  head;
        TextView  name;
        TextView qian;
        TextView shanchu;
        SideslipView sideslipView;

        public MyViewHolder(View view) {
            super(view);
            head = view.findViewById(R.id.mImageFoc);
            name = view.findViewById(R.id.mTextNameFoc);
            qian = view.findViewById(R.id.mTextsigFoc);
            shanchu = view.findViewById(R.id.mTextDelFoc);
            sideslipView = view.findViewById(R.id.mSides);
        }
    }

    public interface Shan{
        void onshan(int i);
    }


}