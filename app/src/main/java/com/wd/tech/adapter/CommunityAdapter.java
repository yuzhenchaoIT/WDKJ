package com.wd.tech.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.bean.CommentList;
import com.wd.tech.bean.CommunityListBean;
import com.wd.tech.bean.Result;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.CommentListPresenter;
import com.wd.tech.util.StringUtils;
import com.wd.tech.view.MyGridView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommunityAdapter extends  RecyclerView.Adapter {
    private PlImageAdapter imageAdapter1;
    private Context context;
    private  TextView textView;
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
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.com_item,viewGroup,false);
        return  new MyHodler(view);
    }
    @Override
    public  void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        MyHodler myHodler = new MyHodler(viewHolder.itemView);
        myHodler.text.setText(mlist.get(i).getContent());
//        String[] images = mlist.get(i).getFile().split(",");
//        Log.e("llll",Arrays.asList(images).toString()+"0.0.0.");
        myHodler.avatar.setImageURI(Uri.parse(mlist.get(i).getHeadPic()));
        myHodler.nickname.setText(mlist.get(i).getNickName());
        myHodler.time.setText(mlist.get(i).getPublishTime()+"");
        myHodler.gq.setText(mlist.get(i).getSignature());
        myHodler.text_sum.setText(""+mlist.get(i).getWhetherGreat());
        myHodler.plsum.setText(mlist.get(i).getComment()+"");

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
        myHodler.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        CommentListPresenter commentListPresenter = new CommentListPresenter(new ListData());
        commentListPresenter.request(mlist.get(i).getId());

        myHodler.iamgepl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclick.OnclickPl(view,mlist.get(i).getId());
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
        CheckBox checkBox;
        TextView text_sum;
        TextView gq;
        TextView plsum;
        ImageAdapter imageAdapter;
          GridLayoutManager gridLayoutManager;
         RecyclerView plrecy;
        LinearLayoutManager layoutManager;
          ImageView iamgepl;


        public MyHodler(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.add_zan);
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

    private class ListData implements DataCall<Result<List<CommentList>>> {
        @Override
        public void success(Result<List<CommentList>> data) {
            Log.e("lll",data.getMessage()+"");
            Log.e("lll",data.getResult().toString()+"11");
            if (data.getResult().size()>0){
                imageAdapter1.clear();
                imageAdapter1.addAll(data.getResult());
                imageAdapter1.notifyDataSetChanged();
                if (data.getResult().size()==3){
                    textView.setVisibility(View.VISIBLE);
                }
            }

        }

        @Override
        public void fail(ApiException e) {

        }
    }

    public  interface Onclick{
        void OnclickPl(View view,int s);
    }
}
