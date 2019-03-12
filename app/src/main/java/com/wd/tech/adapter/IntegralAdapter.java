package com.wd.tech.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wd.tech.R;
import com.wd.tech.bean.UserInRecord;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IntegralAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<UserInRecord> list = new ArrayList<>();
    private Activity activity;
    private LayoutInflater inflater;

    public IntegralAdapter(Activity activity) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.integral_item,viewGroup,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        MyHolder myHolder = (MyHolder) holder;
        int type = list.get(i).getType();
        if (type==1){
            myHolder.textView1.setText("签到");
        }else if (type==2){
            myHolder.textView1.setText("评论");
        }else if (type==3){
            myHolder.textView1.setText("分享");
        }else if (type==4){
            myHolder.textView1.setText("发帖");
        }else if (type==5){
            myHolder.textView1.setText("抽奖收入");
        }else if (type==6){
            myHolder.textView1.setText("收费资讯");
        }else if (type==7){
            myHolder.textView1.setText("抽奖支出");
        }else if (type==8){
            myHolder.textView1.setText("完善个人信息(单次奖励)");
        }else if (type==9){
            myHolder.textView1.setText("查看广告");
        }else if (type==10){
            myHolder.textView1.setText("绑定第三方");
        }
        long createTime = list.get(i).getCreateTime();
        Date date = new Date(createTime);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String s = format.format(date);
        myHolder.textView2.setText(s);
        int direction = list.get(i).getDirection();
        if (direction == 1){
            myHolder.textView3.setText("+"+list.get(i).getAmount());
        }else {
            myHolder.textView3.setText("-"+list.get(i).getAmount());
            myHolder.textView3.setTextColor(Color.BLUE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addAll(List<UserInRecord> result) {
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
        TextView textView3;
        TextView textView4;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.mtext_content_int);
            textView2 = itemView.findViewById(R.id.mtext_date_int);
            textView3 = itemView.findViewById(R.id.mtext_num_int);
        }
    }
}
