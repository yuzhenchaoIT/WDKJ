package com.wd.tech.view;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.adapter.SpaceAdapter;
import com.wd.tech.bean.CommunityUserVoBean;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.bean.UserPost;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.UserPostPresenter;

import java.util.List;

public class SpaceActivity extends WDActivity {


    private SimpleDraweeView headPic;
    private int id;
    private RecyclerView listView;
    private TextView headName;
    private TextView headGq;
    private SimpleDraweeView bgTop;
    private SpaceAdapter spaceAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_space;
    }

    @Override
    protected void initView() {
        id = getIntent().getIntExtra("id", 0);
        listView = (RecyclerView) findViewById(R.id.space_list);
        headPic = (SimpleDraweeView) findViewById(R.id.head_pic);
        headName =(TextView) findViewById(R.id.head_name);
        headGq = (TextView) findViewById(R.id.head_gq);
        bgTop = (SimpleDraweeView) findViewById(R.id.head_top);
        User user = WDActivity.getUser(this);
        if(user!=null){
            UserPostPresenter userPostPresenter = new UserPostPresenter(new PostData());
            userPostPresenter.request(user.getUserId(),user.getSessionId(),id);
        }else {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            finish();
        }
        spaceAdapter = new SpaceAdapter(SpaceActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(spaceAdapter);
    }
    @Override
    protected void destoryData() {

    }

    private class PostData implements DataCall<Result<List<UserPost>>> {

        @Override
        public void success(Result<List<UserPost>> data) {
            if (data.getStatus().equals("9999")){
                Toast.makeText(SpaceActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                finish();
            }else if(data.getStatus().equals("0000")){
                List<UserPost> result = data.getResult();
                CommunityUserVoBean communityUserVo = result.get(0).getCommunityUserVo();
                headPic.setImageURI(Uri.parse(communityUserVo.getHeadPic()));
                headGq.setText(communityUserVo.getSignature());
                headName.setText(communityUserVo.getNickName());
                bgTop.setImageURI(Uri.parse(communityUserVo.getHeadPic()));
                spaceAdapter.addList(data.getResult().get(0).getCommunityUserPostVoList());
                spaceAdapter.notifyDataSetChanged();
            }
        }
        @Override
        public void fail(ApiException e) {
           // Toast.makeText(SpaceActivity.this, e+"", Toast.LENGTH_SHORT).show();
        }
    }
}
