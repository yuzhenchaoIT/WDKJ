package com.wd.tech.adapter;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.bean.AllInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AllInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private List<AllInfo> list = new ArrayList<>();
    private Activity activity;
    private LayoutInflater inflater;

    public AllInfoAdapter(Activity activity) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.allinfo_item,viewGroup,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.draweeView.setImageURI(list.get(i).getThumbnail());
        myHolder.textView1.setText(list.get(i).getTitle());
        long createTime = list.get(i).getCreateTime();
        Date date = new Date(createTime);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String s = format.format(date);
        myHolder.textView2.setText(s);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addAll(List<AllInfo> result) {
        if (result!=null){
            list.addAll(result);
        }
    }

    public void clear() {
        list.clear();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        SimpleDraweeView draweeView;
        TextView textView1,textView2;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            draweeView = itemView.findViewById(R.id.mSimpleAll);
            textView1 = itemView.findViewById(R.id.mTextNameAll);
            textView2 = itemView.findViewById(R.id.mTextAll);
        }
    }

}