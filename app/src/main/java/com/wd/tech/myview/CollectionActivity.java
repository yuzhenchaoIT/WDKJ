package com.wd.tech.myview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.wd.tech.R;
import com.wd.tech.adapter.AllInfoAdapter;
import com.wd.tech.bean.AllInfo;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.dao.DaoMaster;
import com.wd.tech.dao.DaoSession;
import com.wd.tech.dao.UserDao;
import com.wd.tech.presenter.AllInfoPresenter;
import com.wd.tech.view.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CollectionActivity extends WDActivity {

    private AllInfoPresenter allInfoPresenter;
    private XRecyclerView mXRecycler;
    private AllInfoAdapter infoAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collection;
    }

    @Override
    protected void initView() {
        //绑定
        ButterKnife.bind(this);
        //初始化控件
        mXRecycler = (XRecyclerView) findViewById(R.id.mXRecycler);
        //查询数据库
        User user = WDActivity.getUser(this);
        //设置适配器
        infoAdapter = new AllInfoAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mXRecycler.setLayoutManager(linearLayoutManager);
        mXRecycler.setAdapter(infoAdapter);
        allInfoPresenter = new AllInfoPresenter(new AllInfoCall());
        allInfoPresenter.request(user.getUserId(),user.getSessionId(),1,5);
    }
    //实现收藏列表的接口
    class AllInfoCall implements DataCall<Result<List<AllInfo>>>{
        @Override
        public void success(Result<List<AllInfo>> data) {
            if (data.getStatus().equals("0000")){
                List<AllInfo> result = data.getResult();
                infoAdapter.addAll(result);
                infoAdapter.notifyDataSetChanged();
                Toast.makeText(CollectionActivity.this, ""+result.toString(), Toast.LENGTH_SHORT).show();
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
