package com.wd.tech.adapter;

import android.animation.ObjectAnimator;
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
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.wd.tech.R;
import com.wd.tech.bean.HomeListBean;
import com.wd.tech.bean.details.InforDetailsBean;
import com.wd.tech.util.DateUtils;
import com.wd.tech.view.AdvertWebActivity;
import com.wd.tech.view.InforDetailsActivity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.wd.tech.util.UIUtils.getResources;

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
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
        if (viewHolder instanceof MyViewHolder2) {
            ((MyViewHolder2) viewHolder).content.setText(mList.get(i).getInfoAdvertisingVo().getContent());
            ((MyViewHolder2) viewHolder).img.setImageURI(Uri.parse(mList.get(i).getInfoAdvertisingVo().getPic()));
            ((MyViewHolder2) viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, AdvertWebActivity.class);
                    intent.putExtra("AdvertUrl", mList.get(i).getInfoAdvertisingVo().getUrl() + "");
                    mContext.startActivity(intent);
                }
            });
        } else if (viewHolder instanceof MyViewHolder) {
            final HomeListBean homeListBean = mList.get(i);
            //图片
            ((MyViewHolder) viewHolder).img.setImageURI((homeListBean.getThumbnail()));
            //标题
            ((MyViewHolder) viewHolder).title.setText(homeListBean.getTitle());
            //简介
            ((MyViewHolder) viewHolder).summary.setText(homeListBean.getSummary());
            //来源地
            ((MyViewHolder) viewHolder).source.setText(homeListBean.getSource());
            try {
                //时间格式化
                ((MyViewHolder) viewHolder).time.setText(DateUtils.dateFormat(new Date(homeListBean.getReleaseTime()), DateUtils.DATE_PATTERN));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //是否付费
            int isPay = homeListBean.getWhetherPay();
            if (isPay == 1) {
                //赋值收费标识
                ((MyViewHolder) viewHolder).pay.setBackgroundResource(R.drawable.money_bag_icon);
            } else {
                //赋值空白
                ((MyViewHolder) viewHolder).pay.setBackgroundColor(0x00FFFFFF);
            }
            ((MyViewHolder) viewHolder).collect.setText(String.valueOf(homeListBean.getCollection()));
            //当前用户是否收藏 1=是，2=否
            final int whetherGreat = homeListBean.getWhetherCollection();
            if (whetherGreat == 1) {
                ((MyViewHolder) viewHolder).img_collect.setLiked(true);
            } else {
                ((MyViewHolder) viewHolder).img_collect.setLiked(false);
            }
            //分享值
            ((MyViewHolder) viewHolder).share.setText(homeListBean.getShare() + "");
            ((MyViewHolder) viewHolder).img_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(homeListBean.getTitle(), homeListBean.getSummary());
                    Toast.makeText(mContext, "点击了分享" + homeListBean.getId(), Toast.LENGTH_SHORT).show();
                }
            });
            //点击资讯条目进入详情页面
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
        private ImageView pay, img_share;
        private TextView title, summary, source, time, collect, share;
        private LikeButton img_collect;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.item_home_list_img);
            title = itemView.findViewById(R.id.item_home_list_title);
            summary = itemView.findViewById(R.id.item_home_list_summary);
            source = itemView.findViewById(R.id.item_home_list_source);
            time = itemView.findViewById(R.id.item_home_list_time);
            pay = itemView.findViewById(R.id.item_home_list_img_pay);
            img_collect = itemView.findViewById(R.id.item_home_list_img_coll);
            collect = itemView.findViewById(R.id.item_home_list_txt_coll);
            img_share = itemView.findViewById(R.id.item_home_list_img_share);
            share = itemView.findViewById(R.id.item_home_list_txt_share);

            img_collect.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    img_collect.setEnabled(true);
                    img_collect.setLikeDrawableRes(R.drawable.common_icon_collect_s);
                    HomeListBean homeListBean = mList.get(getAdapterPosition());
                    //是否收藏
                    int whetherCollection = homeListBean.getWhetherCollection();
                    if (whetherCollection == 2) {
                        homeListBean.setWhetherCollection(1);
                        homeListBean.setCollection(homeListBean.getCollection() + 1);
                    }
                    if (homeListBean != null) {
                        int id = homeListBean.getId();
                        mCommPriceListener.onPriceSuccessLitener(id);
                    }
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    img_collect.setEnabled(true);
                    img_collect.setLikeDrawableRes(R.drawable.common_icon_collect_n);
                    HomeListBean homeListBean = mList.get(getAdapterPosition());
                    //是否收藏
                    int whetherCollection = homeListBean.getWhetherCollection();
                    if (whetherCollection == 1) {
                        homeListBean.setWhetherCollection(2);
                        homeListBean.setCollection(homeListBean.getCollection() - 1);
                    }
                    mCommPriceListener.onPriceFiureLitener(homeListBean.getId());
                }
            });
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


    /**
     * 条目点击进入详情页面 接口回调
     */

    //定义接口
    public interface OnItemClickListener {
        void onItemClick(String title, String summary);
    }

    //方法名
    private OnItemClickListener onItemClickListener;

    //set方法      设置点击方法
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    //收藏的接口
    public interface CommPriceListener {
        void onPriceSuccessLitener(int uid);

        void onPriceFiureLitener(int uid);
    }

    //收藏的接口
    private CommPriceListener mCommPriceListener;

    //收藏的接口
    public void setCommPriceListener(CommPriceListener mCommPriceListener) {
        this.mCommPriceListener = mCommPriceListener;
    }
}
