package com.wd.tech.myview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.wd.tech.R;
import com.wd.tech.adapter.FocusOnAdapter;
import com.wd.tech.bean.FollowUser;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.FollowUserPresenter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FocusOnActivity extends WDActivity {
    private FollowUserPresenter followUserPresenter;
    private User user;
    private RecyclerView mRecyclerFoc;
    private FocusOnAdapter focusOnAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_focus_on;
    }

    @Override
    protected void initView() {
        //绑定
        ButterKnife.bind(this);
        //初始化控件
        mRecyclerFoc = (RecyclerView) findViewById(R.id.mRecyclerFoc);
        //查询数据库
        user = WDActivity.getUser(this);
        //设置适配器
        focusOnAdapter = new FocusOnAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerFoc.setLayoutManager(linearLayoutManager);
        mRecyclerFoc.setAdapter(focusOnAdapter);
        //设置数据
        followUserPresenter = new FollowUserPresenter(new FollowUserCall());
        followUserPresenter.request(user.getUserId(),user.getSessionId(),1,5);
    }
    //实现我的关注接口
    class FollowUserCall implements DataCall<Result<List<FollowUser>>>{
        @Override
        public void success(Result<List<FollowUser>> data) {
            if (data.getStatus().equals("0000")){
                List<FollowUser> result = data.getResult();
                focusOnAdapter.addAll(result);
                focusOnAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
    //点击按钮返回
    @OnClick(R.id.mReturn)
    public void mreturn(){
        finish();
    }
    @Override
    protected void destoryData() {

    }
}
