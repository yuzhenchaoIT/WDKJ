package com.wd.tech.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.bean.CommunityListBean;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.AddGreatPresenter;
import com.wd.tech.presenter.CancelGreatPresenter;
import com.wd.tech.util.DateUtils;
import com.wd.tech.util.StringUtils;
import com.wd.tech.view.CommentActivity;
import com.wd.tech.view.LoginActivity;
import com.wd.tech.view.SpaceActivity;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CommunityAdapter extends  RecyclerView.Adapter {

    private Context context;

    private List<CommunityListBean> mlist = new ArrayList<>();
    private Onclick onclick;
    public void  OnclickPl(Onclick onclick) {
        this.onclick = onclick;
    }
    public CommunityAdapter(Context context) {
        this.context = context;
    }
    public void addList(List<CommunityListBean> list){
        mlist.addAll(list);
    }
    public void clearlist(){
        mlist.clear();
    }
    public int getSize(){
       return mlist.size();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.com_item,viewGroup,false);
        return  new MyHodler(view);
    }
    @Override
    public  void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        final MyHodler myHodler = new MyHodler(viewHolder.itemView);
        myHodler.text.setText(mlist.get(i).getContent());
        myHodler.avatar.setImageURI(Uri.parse(mlist.get(i).getHeadPic()));
        myHodler.nickname.setText(mlist.get(i).getNickName());
        String s = toTime(mlist.get(i).getPublishTime());
        myHodler.time.setText(s);
        myHodler.gq.setText(mlist.get(i).getSignature());
        myHodler.text_sum.setText(""+mlist.get(i).getPraise());
        myHodler.plsum.setText(mlist.get(i).getComment()+"");
        //点赞
        if (mlist.get(i).getWhetherGreat()==1){
            myHodler.imageView.setImageResource(R.drawable.common_icon_praise);
        }else if (mlist.get(i).getWhetherGreat()==2){
            myHodler.imageView.setImageResource(R.drawable.common_icon_prise);
        }
        myHodler.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = WDActivity.getUser(context);
                if (mlist.get(i).getWhetherGreat()==1){
                    if (user!=null) {
                        CancelGreatPresenter cancelGreatPresenter = new CancelGreatPresenter(new CancelData());
                        cancelGreatPresenter.request(user.getUserId(), user.getSessionId(), mlist.get(i).getId());
                        myHodler.imageView.setImageResource(R.drawable.common_icon_prise);
                        mlist.get(i).setWhetherGreat(2);
                        mlist.get(i).setPraise(mlist.get(i).getPraise() - 1);
                        myHodler.text_sum.setText("" + mlist.get(i).getPraise());
                    }else {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }
                }else {
                    if (user!=null){
                        AddGreatPresenter addGreatPresenter = new AddGreatPresenter(new GreatData());
                        addGreatPresenter.request(user.getUserId(),user.getSessionId(),mlist.get(i).getId());
                        myHodler.imageView.setImageResource(R.drawable.common_icon_praise);
                        mlist.get(i).setWhetherGreat(1);
                        mlist.get(i).setPraise(mlist.get(i).getPraise()+1);
                        myHodler.text_sum.setText(""+mlist.get(i).getPraise());
                    }else {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
   ;
                    }

                }
            }
        });

        myHodler.imageAdapter1.clear();
        myHodler.imageAdapter1.addAll(mlist.get(i).getCommunityCommentVoList());
        myHodler.imageAdapter1.notifyDataSetChanged();
        if (mlist.get(i).getComment()>=3){
            myHodler.textView.setVisibility(View.VISIBLE);
        }else {
            myHodler.textView.setVisibility(View.GONE);
        }
        myHodler.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,CommentActivity.class);
                int userId = mlist.get(i).getId();
                intent.putExtra("id",userId);
                intent.putExtra("headpic",mlist.get(i).getHeadPic());
                intent.putExtra("name",mlist.get(i).getNickName());
                context.startActivity(intent);
            }
        });

        if(StringUtils.isEmpty(mlist.get(i).getFile())){
            myHodler.gridView.setVisibility(View.GONE);
        } else {
            myHodler.gridView.setVisibility(View.VISIBLE);
            String[] images = mlist.get(i).getFile().split(",");
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
       //点击评论
        myHodler.iamgepl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = WDActivity.getUser(context);
                if (user!=null) {
                    onclick.OnclickPl(view, mlist.get(i).getId());
                }else {
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    return;
                }
            }
        });
        myHodler.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = WDActivity.getUser(context);
                if (user!=null) {
                    Intent intent = new Intent(context, SpaceActivity.class);
                    int userId = mlist.get(i).getUserId();
                    intent.putExtra("id", userId);
                    context.startActivity(intent);
                }else {
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    return;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
    class MyHodler extends RecyclerView.ViewHolder {
        SimpleDraweeView avatar;
        TextView nickname;
        TextView time;
        TextView text;
        RecyclerView gridView;
        ImageView imageView ;
        TextView text_sum;
        TextView gq;
        TextView plsum;
        ImageAdapter imageAdapter;
        GridLayoutManager gridLayoutManager;
        RecyclerView plrecy;
        LinearLayoutManager layoutManager;
        PlImageAdapter imageAdapter1;
        ImageView iamgepl;
        TextView textView;
        public MyHodler(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.add_zan);
            text_sum = itemView.findViewById(R.id.circle_sum);
            avatar = itemView.findViewById(R.id.com_item_image);
            text = itemView.findViewById(R.id.com_item_text);
            gq = itemView.findViewById(R.id.com_item_gq);
            nickname = itemView.findViewById(R.id.nickname);
            time = itemView.findViewById(R.id.com_item_time);
            plsum = itemView.findViewById(R.id.circle_com_sum);
            gridView = itemView.findViewById(R.id.grid_view);
             gridLayoutManager = new GridLayoutManager(context,3);
            imageAdapter = new ImageAdapter();
            gridView.setLayoutManager(gridLayoutManager);
            gridView.setAdapter(imageAdapter);
            plrecy = itemView.findViewById(R.id.com_plrecy);
            imageAdapter1 = new PlImageAdapter();
            layoutManager = new LinearLayoutManager(context);
            plrecy.setLayoutManager(layoutManager);
            plrecy.setAdapter(imageAdapter1);
            textView = itemView.findViewById(R.id.com_ts);
            iamgepl = itemView.findViewById(R.id.com_item_pl);
        }
    }

    public  interface Onclick{
        void OnclickPl(View view,int s);
    }

    private class CancelData implements DataCall<Result> {
        @Override
        public void success(Result data) {
          Log.e("ll",data.getMessage());
        }

        @Override
        public void fail(ApiException e) {
            Log.e("ll",e+"");
        }
    }

    private class GreatData implements DataCall<Result> {
        @Override
        public void success(Result data) {

        }

        @Override
        public void fail(ApiException e) {

        }
    }
    private String toTime(long time){
        Date date = new Date();
        date.setTime(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(date);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = null;
        try {
            d = df.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String s = DateUtils.fromToday(d);
        return s;
    }

}
