package com.wd.tech.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;

import java.util.List;


public class PostAdapter2 extends RecyclerView.Adapter<PostAdapter2.MyViewHolder>  {
    private List<String> mDatas;
    private Context mContext;
    private LayoutInflater inflater;

    public PostAdapter2(Context context, List<String> datas){
        this.mContext=context;
        this.mDatas=datas;
        inflater=LayoutInflater.from(mContext);
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    //填充onCreateViewHolder方法返回的holder中的控件
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.simple.setImageURI(mDatas.get(position));
    }

    //重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.mypost_item2,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    class MyViewHolder extends RecyclerView.ViewHolder { //承载Item视图的子布局
        SimpleDraweeView simple;

        public MyViewHolder(View view) {
            super(view);
            simple = view.findViewById(R.id.simple);
        }
    }



}