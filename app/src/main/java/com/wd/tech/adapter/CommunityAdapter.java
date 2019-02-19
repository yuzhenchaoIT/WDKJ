package com.wd.tech.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.view.MyGridView;

public class CommunityAdapter extends  RecyclerView.Adapter {

    private Context context;

    public CommunityAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.com_item,viewGroup,false);
        return  new MyHodler(view);
    }
    @Override
    public  void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        MyHodler myHodler = new MyHodler(viewHolder.itemView);
    }

    @Override
    public int getItemCount() {
        return 0;
    }
    class MyHodler extends RecyclerView.ViewHolder {
        SimpleDraweeView avatar;
        TextView nickname;
        TextView time;
        TextView text;
        MyGridView gridView;
        ImageAdapter imageAdapter;
        CheckBox checkBox;
        TextView text_sum;
        public MyHodler(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.add_zan);
            text_sum = itemView.findViewById(R.id.circle_sum);
            avatar = itemView.findViewById(R.id.com_item_image);
            text = itemView.findViewById(R.id.com_item_text);
            nickname = itemView.findViewById(R.id.nickname);
            time = itemView.findViewById(R.id.com_item_time);
            gridView = itemView.findViewById(R.id.grid_view);
            imageAdapter = new ImageAdapter();
        }
    }
}
