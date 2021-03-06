package com.wd.tech.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.wd.tech.R;
import com.wd.tech.bean.MyPost;
import com.wd.tech.core.FolderTextView;
import com.wd.tech.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<MyPost> list = new ArrayList<>();
    private Activity activity;
    private LayoutInflater inflater;
    private Shan shan;


    public PostAdapter(Activity activity) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
    }
    public void getShan(Shan shan){
        this.shan=shan;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.mypost_item,viewGroup,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.folderTextView.setText(list.get(i).getContent()+"");
        myHolder.folderTextView.setTailColor(0xff20affa);
        myHolder.textView3.setText(list.get(i).getPraise()+"");
        myHolder.textView4.setText(list.get(i).getComment()+"");
        if(StringUtils.isEmpty(list.get(i).getFile())){
            myHolder.recyclerView.setVisibility(View.GONE);
        } else {
            myHolder.recyclerView.setVisibility(View.VISIBLE);
            String[] images = list.get(i).getFile().split(",");
//            int imageCount = (int) (Math.random() * 9) + 1;
            int imageCount = images.length;
            int colNum;//列数
            if (imageCount == 1) {
                colNum = 1;
            } else if (imageCount == 2 || imageCount == 4) {
                colNum = 2;
            } else {
                colNum = 3;
            }
            myHolder.adapter2.clear();
            myHolder.adapter2.addAll(Arrays.asList(images));
            myHolder.gridLayoutManager.setSpanCount(colNum);
            myHolder.adapter2.notifyDataSetChanged();
        }
        long publishTime = list.get(i).getPublishTime();
        Date date = new Date(publishTime);
        SimpleDateFormat format = new SimpleDateFormat("MM-dd  HH:mm:ss");
        String s = format.format(date);
        myHolder.textView1.setText(s);
        myHolder.textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shan.onShan(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addAll(List<MyPost> result) {
        if (result!=null){
            list.addAll(result);
        }
    }

    public void clear() {
        list.clear();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        FolderTextView folderTextView;
        RecyclerView recyclerView;
        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        PostAdapter2 adapter2;
        GridLayoutManager gridLayoutManager;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            folderTextView = itemView.findViewById(R.id.mfolder_post);
            recyclerView = itemView.findViewById(R.id.mrecycler_post);
            gridLayoutManager = new GridLayoutManager(activity,3);
            adapter2 = new PostAdapter2(activity);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(adapter2);
            textView1 = itemView.findViewById(R.id.mdate_post);
            textView2 = itemView.findViewById(R.id.mdelete_post);
            textView3 = itemView.findViewById(R.id.mzanshu_post);
            textView4 = itemView.findViewById(R.id.mping_post);
        }
    }
    public interface Shan{
        void onShan(int i);
    }

}
