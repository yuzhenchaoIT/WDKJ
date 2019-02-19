package com.wd.tech.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wd.tech.R;
import com.wd.tech.core.WDActivity;

import butterknife.ButterKnife;

public class RegisterActivity extends WDActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        //绑定
        ButterKnife.bind(this);
        closeSwipeBack();
    }

    @Override
    protected void destoryData() {

    }
}
