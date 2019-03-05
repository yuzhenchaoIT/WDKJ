package com.wd.tech.view;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.wd.tech.presenter.AddFollowPresenter;
import com.wd.tech.presenter.CancelFollowPresenter;
import com.wd.tech.presenter.UserPostPresenter;

import java.util.List;

public class SpaceActivity extends WDActivity implements View.OnClickListener {

    private SimpleDraweeView headPic;
    private int id;
    private RecyclerView listView;
    private TextView headName;
    private TextView headGq;
    private SimpleDraweeView bgTop;
    private SpaceAdapter spaceAdapter;
    private TextView hyText;
    private TextView gzText;
    private CommunityUserVoBean communityUserVo;
    private User user;
    private LinearLayout linearLayout;
    private ObjectAnimator animator;
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
        hyText = (TextView) findViewById(R.id.as_haoyou);
        hyText .setOnClickListener(this);
        gzText = (TextView)findViewById(R.id.as_gz);
        gzText   .setOnClickListener(this);
        linearLayout = (LinearLayout) findViewById(R.id.as_lin);

        user = WDActivity.getUser(this);
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
        final ImageView more = (ImageView) findViewById(R.id.as_more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                more.setVisibility(View.GONE);
                animator = ObjectAnimator.ofFloat(linearLayout, "translationX", 0f, -420f);
                animator.setDuration(1000);
                animator.setInterpolator(new LinearInterpolator());
                animator.start();
            }
        });
    }

// animation.startNow();

    @Override
    protected void destoryData() {

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.as_gz:
                if (gzText.getText().equals("已关注")){
                    gzText.setText("+关注");
                    CancelFollowPresenter cancelFollowPresenter = new CancelFollowPresenter(new canfollow());
                    cancelFollowPresenter.request(user.getUserId(),user.getSessionId(),communityUserVo.getUserId());
                }else {
                    gzText.setText("已关注");
                    AddFollowPresenter addFollowPresenter = new AddFollowPresenter(new addFollow());
                    addFollowPresenter.request(user.getUserId(),user.getSessionId(),communityUserVo.getUserId());
                }
                break;
            case R.id.as_haoyou:
                Intent intent = new Intent(this,FindUserActivity.class);
                intent.putExtra("name",communityUserVo.getNickName());
                intent.putExtra("headPic",communityUserVo.getHeadPic());
                intent.putExtra("qian",communityUserVo.getSignature());
                intent.putExtra("userids",communityUserVo.getUserId());
                startActivity(intent);
                break;
        }


    }

    private class PostData implements DataCall<Result<List<UserPost>>> {

        @Override
        public void success(Result<List<UserPost>> data) {
            if (data.getStatus().equals("9999")){
                Toast.makeText(SpaceActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                finish();
            }else if(data.getStatus().equals("0000")){
                List<UserPost> result = data.getResult();
                communityUserVo = result.get(0).getCommunityUserVo();
                headPic.setImageURI(Uri.parse(communityUserVo.getHeadPic()));
                headGq.setText(communityUserVo.getSignature());
                headName.setText(communityUserVo.getNickName());
                bgTop.setImageURI(Uri.parse(communityUserVo.getHeadPic()));
                spaceAdapter.addList(data.getResult().get(0).getCommunityUserPostVoList());
                spaceAdapter.notifyDataSetChanged();

                if (communityUserVo.getWhetherFollow()==1){
                    gzText.setText("已关注");
                }else {
                    gzText.setText("+关注");
                }
                if (communityUserVo.getWhetherMyFriend()==1){
                    hyText.setText("已添加");
                }else {
                    hyText.setText("+好友");
                }
            }
        }
        @Override
        public void fail(ApiException e) {
           // Toast.makeText(SpaceActivity.this, e+"", Toast.LENGTH_SHORT).show();
        }
    }

    private class addFollow implements DataCall<Result> {
        @Override
        public void success(Result data) {
            Toast.makeText(SpaceActivity.this, data.getMessage()+"", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void fail(ApiException e) {

        }
    }

    private class canfollow implements DataCall<Result> {
        @Override
        public void success(Result data) {
            Toast.makeText(SpaceActivity.this, data.getMessage()+"", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
