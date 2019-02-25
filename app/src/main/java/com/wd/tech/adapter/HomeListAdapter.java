package com.wd.tech.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.bean.HomeListBean;
import com.wd.tech.bean.details.InforDetailsBean;
import com.wd.tech.util.DateUtils;
import com.wd.tech.view.InforDetailsActivity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<HomeListBean> mList = new ArrayList<>();

    private Context mContext;

    public HomeListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addItem(List<HomeListBean> beanList) {
        if (beanList != null) {
            mList.addAll(beanList);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
        if (type == 1) {
            View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_home_list2, viewGroup, false);
            return new MyViewHolder2(inflate);
        } else if (type == 2) {
            View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_home_list, viewGroup, false);
            return new MyViewHolder(inflate);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof MyViewHolder2) {
            ((MyViewHolder2) viewHolder).content.setText(mList.get(i).getInfoAdvertisingVo().getContent());
            ((MyViewHolder2) viewHolder).img.setImageURI(Uri.parse(mList.get(i).getInfoAdvertisingVo().getPic()));
        } else if (viewHolder instanceof MyViewHolder) {
            final HomeListBean homeListBean = mList.get(i);
            ((MyViewHolder) viewHolder).img.setImageURI((homeListBean.getThumbnail()));
            ((MyViewHolder) viewHolder).title.setText(homeListBean.getTitle());
            ((MyViewHolder) viewHolder).summary.setText(homeListBean.getSummary());
            ((MyViewHolder) viewHolder).source.setText(homeListBean.getSource());
            try {
                ((MyViewHolder) viewHolder).time.setText(DateUtils.dateFormat(new Date(homeListBean.getReleaseTime()), DateUtils.DATE_PATTERN));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int isPay = homeListBean.getWhetherPay();
            if (isPay == 1) {
                ((MyViewHolder) viewHolder).pay.setBackgroundResource(R.drawable.money_bag_icon);
            } else {
                ((MyViewHolder) viewHolder).pay.setBackgroundColor(0x00FFFFFF);
            }


            ((MyViewHolder) viewHolder).collect.setText(homeListBean.getCollection() + "");
            ((MyViewHolder) viewHolder).share.setText(homeListBean.getShare() + "");
            ((MyViewHolder) viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, InforDetailsActivity.class);
                    intent.putExtra("homeListId", homeListBean.getId() + "");
                    mContext.startActivity(intent);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position).getWhetherAdvertising() == 1) {
            return 1;
        } else if (mList.get(position).getWhetherAdvertising() == 2) {
            return 2;
        }
        return 2;
    }


    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }


    //内部类
    class MyViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView img;
        private ImageView pay;
        private TextView title, summary, source, time, collect, share;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.item_home_list_img);
            title = itemView.findViewById(R.id.item_home_list_title);
            summary = itemView.findViewById(R.id.item_home_list_summary);
            source = itemView.findViewById(R.id.item_home_list_source);
            time = itemView.findViewById(R.id.item_home_list_time);
            pay = itemView.findViewById(R.id.item_home_list_img_pay);
            collect = itemView.findViewById(R.id.item_home_list_txt_coll);
            share = itemView.findViewById(R.id.item_home_list_txt_share);
        }
    }

    class MyViewHolder2 extends RecyclerView.ViewHolder {

        private TextView content;
        private SimpleDraweeView img;

        public MyViewHolder2(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.item_home_list2_content);
            img = itemView.findViewById(R.id.item_home_list2_img);
        }
    }


}
