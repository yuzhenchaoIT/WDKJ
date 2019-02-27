package com.wd.tech.adapter;

import android.content.Intent;
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
import com.wd.tech.bean.PlList;
import com.wd.tech.view.DetailActivity;

import java.util.ArrayList;
import java.util.List;

public class PlImageAdapter extends RecyclerView.Adapter<PlImageAdapter.ViewHolder> {
    private ArrayList<PlList> mList = new ArrayList<>();

    public void addAll(List<PlList> list) {
        if (mList!=null){
            mList.addAll(list);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.com_pl_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i){
        viewHolder.name.setText(mList.get(i).getNickName()+":");
        viewHolder.pl.setText(mList.get(i).getContent());
//        viewHolder.image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(viewHolder.itemView.getContext(),DetailActivity.class);
//                viewHolder.itemView.getContext().startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {

            return mList.size();


    }

    public void clear() {
        mList.clear();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,pl;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             name = itemView.findViewById(R.id.pl_name);
             pl = itemView.findViewById(R.id.pl_nr);
        }
    }
}
