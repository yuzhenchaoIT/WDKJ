package com.wd.tech.myview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wd.tech.R;
import com.wd.tech.adapter.NoticeAdapter;
import com.wd.tech.bean.NoticeList;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.NoticePresenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoticeActivity extends WDActivity {
    private NoticePresenter noticePresenter;
    private User user;
    private SmartRefreshLayout mSmartNotice;
    private List<NoticeList> result = new ArrayList<>();
    private NoticeAdapter noticeAdapter;
    private RecyclerView mRecyclerNotice;
    private RelativeLayout mRelativeNotice;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_notice;
    }

    @Override
    protected void initView() {
        //绑定
        ButterKnife.bind(this);
        //初始化控件
        mSmartNotice = (SmartRefreshLayout) findViewById(R.id.msmart_notice);
        mRecyclerNotice = (RecyclerView) findViewById(R.id.mrecycler_notice);
        mRelativeNotice = (RelativeLayout) findViewById(R.id.mrelative_notice);
        mSmartNotice.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
                result.clear();
                noticePresenter.request(user.getUserId(),user.getSessionId(),true,5);
            }
        });
        mSmartNotice.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore();
                noticePresenter.request(user.getUserId(),user.getSessionId(),false,5);
            }
        });
        //查询数据库
        user = WDActivity.getUser(this);
        //设置适配器
        noticeAdapter = new NoticeAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerNotice.setLayoutManager(linearLayoutManager);
        mRecyclerNotice.setAdapter(noticeAdapter);
        //设置数据
        noticePresenter = new NoticePresenter(new NoticeCall());
        noticePresenter.request(user.getUserId(),user.getSessionId(),true,5);
    }
    //实现查询用户系统通知接口
    class NoticeCall implements DataCall<Result<List<NoticeList>>>{

        @Override
        public void success(Result<List<NoticeList>> data) {
            if (data.getStatus().equals("0000")){
                result.addAll(data.getResult());
                if (result.size()!=0){
                    noticeAdapter.clear();
                    noticeAdapter.addAll(result);
                    noticeAdapter.notifyDataSetChanged();
                    mSmartNotice.setVisibility(View.VISIBLE);
                    mRelativeNotice.setVisibility(View.GONE);
                }else {
                    mSmartNotice.setVisibility(View.GONE);
                    mRelativeNotice.setVisibility(View.VISIBLE);
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
