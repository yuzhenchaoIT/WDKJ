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
import com.wd.tech.bean.details.FindAllCommentListBean;
import com.wd.tech.util.DateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailAllCommentAdapter extends RecyclerView.Adapter<DetailAllCommentAdapter.MyViewHolder> {

    private List<FindAllCommentListBean> mList = new ArrayList<>();

    private Context mContext;

    public DetailAllCommentAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addItem(List<FindAllCommentListBean> beanList) {
        if (beanList != null) {
            mList.addAll(beanList);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_detail_all_comment, viewGroup, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        FindAllCommentListBean listBean = mList.get(i);
        myViewHolder.headPic.setImageURI(Uri.parse(listBean.getHeadPic()));
        myViewHolder.nickName.setText(listBean.getNickName());
        try {
            myViewHolder.time.setText(DateUtils.dateFormat(new Date(listBean.getCommentTime()), DateUtils.MINUTE_PATTERN));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        myViewHolder.context.setText(listBean.getContent());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void clear() {
        mList.clear();
    }

    //内部类
    class MyViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView headPic;
        private TextView nickName, time, context;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            headPic = itemView.findViewById(R.id.item_details_all_comment_head_img);
            nickName = itemView.findViewById(R.id.item_details_all_comment_nick_name);
            time = itemView.findViewById(R.id.item_details_all_comment_time);
            context = itemView.findViewById(R.id.item_details_all_comment_txt);
        }
    }


}
