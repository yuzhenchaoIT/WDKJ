package com.wd.tech.myview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
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
    private RecyclerView mRecycler;
    private AllInfoAdapter infoAdapter;
    private SmartRefreshLayout mSmartRefresh;
    private RelativeLayout mRelativeCol;
    private User user;
    private List<AllInfo> result;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collection;
    }
    //点击删除
    @OnClick(R.id.mImageDelete)
    public void mimage(){
        Toast.makeText(this, "111", Toast.LENGTH_SHORT).show();
        for (int i = 0; i < result.size(); i++) {
            boolean ischeck = result.get(i).isIscheck();
            if (ischeck){
                result.get(i).setIscheck(true);
            }else {
                result.get(i).setIscheck(false);
            }
        }
    }
    @Override
    protected void initView() {
        //绑定
        ButterKnife.bind(this);
        //初始化控件
        mRecycler = (RecyclerView) findViewById(R.id.mRecycler);
        mSmartRefresh = (SmartRefreshLayout) findViewById(R.id.mSmartRefresh);
        mRelativeCol = (RelativeLayout) findViewById(R.id.mRelativeCol);
        mSmartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
                allInfoPresenter.request(user.getUserId(),user.getSessionId(),true,5);
            }
        });
        mSmartRefresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
                allInfoPresenter.request(user.getUserId(),user.getSessionId(),false,5);
            }
        });
        //查询数据库
        user = WDActivity.getUser(this);
        //设置适配器
        infoAdapter = new AllInfoAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(linearLayoutManager);
        mRecycler.setAdapter(infoAdapter);
        allInfoPresenter = new AllInfoPresenter(new AllInfoCall());
        allInfoPresenter.request(user.getUserId(), user.getSessionId(),true,5);
    }
    //实现收藏列表的接口
    class AllInfoCall implements DataCall<Result<List<AllInfo>>>{
        @Override
        public void success(Result<List<AllInfo>> data) {
            if (data.getStatus().equals("0000")){
                result = data.getResult();
                if(result !=null) {
                    mSmartRefresh.setVisibility(View.VISIBLE);
                    mRelativeCol.setVisibility(View.GONE);
                    //添加列表并刷新
                    if (allInfoPresenter.getPage() == 1) {
                        infoAdapter.clear();
                    }
                    infoAdapter.addAll(result);
                    infoAdapter.notifyDataSetChanged();
                }else {
                    mSmartRefresh.setVisibility(View.GONE);
                    mRelativeCol.setVisibility(View.VISIBLE);
                }
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
