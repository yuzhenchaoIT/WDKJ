package com.wd.tech.adapter;

import android.content.Context;
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
import com.wd.tech.bean.AllInfoPlateBean;
import com.wd.tech.view.SortListActivity;

import java.util.ArrayList;
import java.util.List;

public class PlateAdapter extends RecyclerView.Adapter<PlateAdapter.MyViewHolder> {

    private List<AllInfoPlateBean> mList = new ArrayList<>();

    private Context mContext;

    public PlateAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addItem(List<AllInfoPlateBean> beanList) {
        if (beanList != null) {
            mList.addAll(beanList);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_plate, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final AllInfoPlateBean plateBean = mList.get(i);
        myViewHolder.img.setImageURI(Uri.parse(plateBean.getPic()));
        myViewHolder.title.setText(plateBean.getName());
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SortListActivity.class);
                intent.putExtra("id", plateBean.getId() + "");
                intent.putExtra("title", plateBean.getName() + "");
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView img;
        private TextView title;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.item_plate_img);
            title = itemView.findViewById(R.id.item_plate_title);
        }
    }


}
