package com.wd.tech.myview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wd.tech.R;
import com.wd.tech.adapter.FocusOnAdapter;
import com.wd.tech.bean.FollowUser;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.CancelFollPresenter;
import com.wd.tech.presenter.FollowUserPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FocusOnActivity extends WDActivity implements FocusOnAdapter.Shan {
    private FollowUserPresenter followUserPresenter;
    private User user;
    private RecyclerView mRecyclerFoc;
    private FocusOnAdapter focusOnAdapter;
    private CancelFollPresenter cancelFollPresenter;
    private List<FollowUser> result = new ArrayList<>();
    private RelativeLayout mrelativeFoc;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_focus_on;
    }

    @Override
    protected void initView() {
        //绑定
        ButterKnife.bind(this);
        //初始化控件
        mRecyclerFoc = (RecyclerView) findViewById(R.id.mrecycler_foc);
        mrelativeFoc = (RelativeLayout) findViewById(R.id.mrelative_foc);
        //查询数据库
        user = WDActivity.getUser(this);
        //设置适配器
        focusOnAdapter = new FocusOnAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerFoc.setLayoutManager(linearLayoutManager);
        mRecyclerFoc.setAdapter(focusOnAdapter);
        //设置数据
        followUserPresenter = new FollowUserPresenter(new FollowUserCall());
        followUserPresenter.request(user.getUserId(),user.getSessionId(),1,20);
        //-----
        focusOnAdapter.getShan(this);
    }

    @Override
    public void onshan(int i) {
        int focusUid = result.get(i).getFocusUid();
        cancelFollPresenter = new CancelFollPresenter(new CancelFollCall());
        cancelFollPresenter.request(user.getUserId(),user.getSessionId(),focusUid);
    }
    //实现取消关注接口
    class CancelFollCall implements DataCall<Result>{

        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")){
                Toast.makeText(FocusOnActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
                result.clear();
                followUserPresenter.request(user.getUserId(),user.getSessionId(),1,20);
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
    //实现我的关注接口
    class FollowUserCall implements DataCall<Result<List<FollowUser>>>{
        @Override
        public void success(Result<List<FollowUser>> data) {
            if (data.getStatus().equals("0000")){

                result = data.getResult();
                if (result.size()!=0) {
                    focusOnAdapter.clear();
                    focusOnAdapter.addAll(result);
                    focusOnAdapter.notifyDataSetChanged();
                    mRecyclerFoc.setVisibility(View.VISIBLE);
                    mrelativeFoc.setVisibility(View.GONE);
                }else {
                    mRecyclerFoc.setVisibility(View.GONE);
                    mrelativeFoc.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
    //点击按钮返回
    @OnClick(R.id.mreturn)
    public void mreturn(){
        finish();
    }
    @Override
    protected void destoryData() {

    }
}
