package com.wd.tech.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.bean.CommunityListBean;
import com.wd.tech.bean.CommunityUserPostVoListBean;
import com.wd.tech.bean.CommunityUserVoBean;
import com.wd.tech.bean.UserPost;
import com.wd.tech.util.StringUtils;
import com.wd.tech.view.SpaceActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpaceAdapter extends  RecyclerView.Adapter {

    private Context context;

    private List<CommunityUserPostVoListBean> postVoList = new ArrayList<>();



    public SpaceAdapter(Context context) {
        this.context = context;
    }
    public void addList(List<CommunityUserPostVoListBean> list){
        postVoList.addAll(list);
    }
    public void clearlist(){
        postVoList.clear();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_space,viewGroup,false);
        return  new MyHodler(view);
    }
    @Override
    public  void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        MyHodler myHodler = new MyHodler(viewHolder.itemView);
        myHodler.text.setText(postVoList.get(i).getContent());
        myHodler.text_sum.setText(""+postVoList.get(i).getWhetherGreat());
        myHodler.plsum.setText(postVoList.get(i).getComment()+"");
        if(StringUtils.isEmpty(postVoList.get(i).getFile())){
            myHodler.gridView.setVisibility(View.GONE);
        } else {
            myHodler.gridView.setVisibility(View.VISIBLE);
            String[] images = postVoList.get(i).getFile().split(",");
//            int imageCount = (int) (Math.random() * 9) + 1;
            int imageCount = images.length;
            int colNum;//列数
            if (imageCount == 1) {
                colNum = 1;
            } else if (imageCount == 2 || imageCount == 4) {
                colNum = 2;
            } else {
                colNum = 3;
            }
            myHodler.imageAdapter.clear();
             myHodler.imageAdapter.addAll(Arrays.asList(images));
             myHodler.gridLayoutManager.setSpanCount(colNum);
             myHodler.imageAdapter.notifyDataSetChanged();
        }
        myHodler.imageAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return postVoList.size();
    }
    class MyHodler extends RecyclerView.ViewHolder {
        TextView text;
        RecyclerView gridView;
        CheckBox checkBox;
        TextView text_sum;
        TextView plsum;
        ImageAdapter imageAdapter;
        GridLayoutManager gridLayoutManager;
        ImageView iamgepl;

        public MyHodler(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.spaceadd_zan);
            text_sum = itemView.findViewById(R.id.space_sum);
            text = itemView.findViewById(R.id.space_item_text);
            plsum = itemView.findViewById(R.id.space_com_sum);
            gridView = itemView.findViewById(R.id.spacegrid_view);
            iamgepl =itemView.findViewById(R.id.space_item_pl);
             gridLayoutManager = new GridLayoutManager(context,3);
            imageAdapter = new ImageAdapter();
            gridView.setLayoutManager(gridLayoutManager);
            gridView.setAdapter(imageAdapter);
        }
    }

}
