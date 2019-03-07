package com.wd.tech.myview;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wd.tech.R;
import com.wd.tech.adapter.IntegralAdapter;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.bean.UserInRecord;
import com.wd.tech.bean.UserIntegral;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.FindConSignPresenter;
import com.wd.tech.presenter.IntegralPresenter;
import com.wd.tech.presenter.UserInRecordPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class IntegralActivity extends WDActivity {
    private IntegralPresenter integralPresenter;
    private TextView mTextjf,mTexttian;
    private User user;
    private UserInRecordPresenter userInRecordPresenter = new UserInRecordPresenter(new UserInCall());
    private IntegralAdapter integralAdapter;
    private RecyclerView mRecyclerInteg;
    private FindConSignPresenter findConSignPresenter;
    private SmartRefreshLayout mSmartint;
    private List<UserInRecord> result = new ArrayList<>();
    private ImageView mImageint;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_integral;
    }

    @Override
    protected void initView() {
        //绑定
        ButterKnife.bind(this);
        //初始化控件
        mTextjf = (TextView) findViewById(R.id.mtext_jf);
        mRecyclerInteg = (RecyclerView) findViewById(R.id.mrecycler_integ);
        mTexttian = (TextView) findViewById(R.id.mtext_tian);
        mSmartint = (SmartRefreshLayout) findViewById(R.id.msmart_int);
        mImageint = (ImageView) findViewById(R.id.mimage_int);
        //查询数据库
        user = WDActivity.getUser(this);
        //设置数据
        integralPresenter = new IntegralPresenter(new IntegralCall());
        integralPresenter.request(user.getUserId(),user.getSessionId());
        //查询用户连续签到天数
        findConSignPresenter = new FindConSignPresenter(new FindConCall());
        findConSignPresenter.request(user.getUserId(),user.getSessionId());
        //设置适配器
        integralAdapter = new IntegralAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerInteg.setLayoutManager(linearLayoutManager);
        mRecyclerInteg.setAdapter(integralAdapter);
        //上下拉刷新
        mSmartint.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
                result.clear();
                userInRecordPresenter.request(user.getUserId(),user.getSessionId(),true,5);
            }
        });
        mSmartint.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore();
                userInRecordPresenter.request(user.getUserId(),user.getSessionId(),false,5);
            }
        });
        //查询用户积分明细接口
        userInRecordPresenter.request(user.getUserId(),user.getSessionId(),true,5);
    }
    //实现查询用户连续签到天数接口
    private class FindConCall implements DataCall<Result>{
        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")){
                double result = (double) data.getResult();
                int a = (int)result;
                mTexttian.setText("您已连续签到"+a+"天");
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
    //实现查询用户积分接口
    private class IntegralCall implements DataCall<Result<UserIntegral>>{

        @Override
        public void success(Result<UserIntegral> data) {
            if (data.getStatus().equals("0000")){
                UserIntegral result = data.getResult();
                mTextjf.setText(result.getAmount()+"");
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
    //实现查询用户积分明细接口
    private class UserInCall implements DataCall<Result<List<UserInRecord>>>{
        @Override
        public void success(Result<List<UserInRecord>> data) {
            if (data.getStatus().equals("0000")){
                result.addAll(data.getResult());
                if (result.size()!=0) {
                    integralAdapter.clear();
                    integralAdapter.addAll(result);
                    integralAdapter.notifyDataSetChanged();
                    mRecyclerInteg.setVisibility(View.VISIBLE);
                    mImageint.setVisibility(View.GONE);
                }else {
                    mRecyclerInteg.setVisibility(View.GONE);
                    mImageint.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
    @Override
    protected void destoryData() {
        integralPresenter.unBind();
    }
}
