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
import com.wd.tech.bean.CommentList;
import java.util.ArrayList;
import java.util.List;

public class DePlAdapter extends  RecyclerView.Adapter {

    private Context context;

    private List<CommentList> list = new ArrayList<>();



    public DePlAdapter(Context context) {
        this.context = context;
    }
    public void addList(List<CommentList> list){
        this.list.addAll(list);
    }
    public void clearlist(){
        list.clear();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment,viewGroup,false);
        return  new MyHodler(view);
    }
    @Override
    public  void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        MyHodler myHodler = new MyHodler(viewHolder.itemView);
        myHodler.name.setText(list.get(i).getNickName());
        myHodler.imagehead.setImageURI(Uri.parse(list.get(i).getHeadPic()));
        myHodler.nr.setText(list.get(i).getContent());
        myHodler.time.setText(list.get(i).getCommentTime()+"");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class MyHodler extends RecyclerView.ViewHolder {


      TextView name,time,nr;
        SimpleDraweeView imagehead;
        public MyHodler(@NonNull View itemView) {
            super(itemView);
             imagehead = itemView.findViewById(R.id.ic_head);
            name = itemView.findViewById(R.id.ic_name);
            time = itemView.findViewById(R.id.ic_time);
            nr = itemView.findViewById(R.id.ic_nr);
        }
    }

}
