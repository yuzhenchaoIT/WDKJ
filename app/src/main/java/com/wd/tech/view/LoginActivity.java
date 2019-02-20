package com.wd.tech.view;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.wd.tech.R;
import com.wd.tech.core.WDActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends WDActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        //绑定
        ButterKnife.bind(this);
        closeSwipeBack();
    }
    //点击跳转注册页
    @OnClick(R.id.mRegister)
    public void mregsiter(){
        //跳转
        intent(RegisterActivity.class);
    }
    //点击登录
    @OnClick(R.id.mLogin)
    public void mlogin(){
        //跳转
        finish();
    }
    @Override
    protected void destoryData() {

    }
}
