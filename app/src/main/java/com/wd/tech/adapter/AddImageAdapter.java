package com.wd.tech.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;

import java.util.List;


/***
 * 佛曰： 永无BUG 盘他！
 */
public class AddImageAdapter extends RecyclerView.Adapter<AddImageAdapter.MyViewHolder> {
    private List<Object> mDatas;
    private Context mContext;
    private LayoutInflater inflater;
    Dakai dakai;

    public AddImageAdapter(Context context, List<Object> datas, Dakai dakai){
        this.mContext=context;
        this.mDatas=datas;
        this.dakai=dakai;
        inflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    //填充onCreateViewHolder方法返回的holder中的控件
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        if(mDatas.get(position) instanceof String ){
            String imageUrl = (String) mDatas.get(position);
            if (imageUrl.contains("http:")) {//加载http
                holder.iv15.setImageURI(Uri.parse(imageUrl));
            } else {//加载sd卡
                Uri uri = Uri.parse("file://" + imageUrl);
                holder.iv15.setImageURI(uri);
            }
        }else {
            int id = (int) mDatas.get(position);
            Uri uri = Uri.parse("res:///" + id);
            holder.iv15.setImageURI(uri);
        }

        holder.iv15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position==0){
                    dakai.onDakaiXiangCe();
                }else {
                    Toast.makeText(mContext, ""+position, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.add_image_item,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    class MyViewHolder extends RecyclerView.ViewHolder { //承载Item视图的子布局
        SimpleDraweeView iv15;

        public MyViewHolder(View view) {
            super(view);
           iv15 = view.findViewById(R.id.iv15);

        }
    }

   public interface Dakai{
        void onDakaiXiangCe();
   }
}