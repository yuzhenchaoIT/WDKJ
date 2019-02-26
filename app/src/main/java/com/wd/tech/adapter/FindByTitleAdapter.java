package com.wd.tech.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wd.tech.R;
import com.wd.tech.bean.FindTitleBean;
import com.wd.tech.util.DateUtils;
import com.wd.tech.view.InforDetailsActivity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lmx
 * @date 2019/2/26
 */
public class FindByTitleAdapter extends RecyclerView.Adapter<FindByTitleAdapter.MyHolder> {

    private List<FindTitleBean> mList = new ArrayList<>();
    private Context mContext;

    public FindByTitleAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addList(List<FindTitleBean> beanList) {
        if (beanList != null) {
            mList.addAll(beanList);
        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_by_title_layout, viewGroup, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        final FindTitleBean titleBean = mList.get(i);
        myHolder.title.setText(titleBean.getTitle());
        myHolder.source.setText(titleBean.getSource());
        try {
            myHolder.time.setText(DateUtils.dateFormat(new Date(titleBean.getReleaseTime()), DateUtils.MINUTE_PATTERN));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, InforDetailsActivity.class);
                intent.putExtra("homeListId", titleBean.getId() + "");
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void clearData() {
        mList.clear();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private TextView title, source, time;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_by_title_one);
            source = itemView.findViewById(R.id.item_by_title_source);
            time = itemView.findViewById(R.id.item_by_title_time);
        }
    }
}
