package com.wd.tech.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wd.tech.R;
import com.wd.tech.bean.NoticeList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<NoticeList> list = new ArrayList<>();
    private Activity activity;
    private LayoutInflater inflater;

    public NoticeAdapter(Activity activity) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.notice_item,viewGroup,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        MyHolder myHolder = (MyHolder) holder;
        long createTime = list.get(i).getCreateTime();
        Date date = new Date(createTime);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String s = format.format(date);
        myHolder.textView1.setText(s);
        myHolder.textView2.setText(list.get(i).getContent());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addAll(List<NoticeList> result) {
        if (result!=null){
            list.addAll(result);
        }
    }

    public void clear() {
        list.clear();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView textView1;
        TextView textView2;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.mtext_date);
            textView2 = itemView.findViewById(R.id.mtext_content);
        }
    }
}
