package com.wd.tech.myview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wd.tech.R;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.bean.UserIntegral;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.IntegralPresenter;

import butterknife.ButterKnife;

public class IntegralActivity extends WDActivity {
    private IntegralPresenter integralPresenter;
    private TextView mTextjf;
    private User user;

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
        //查询数据库
        user = WDActivity.getUser(this);
        //设置数据
        integralPresenter = new IntegralPresenter(new IntegralCall());
        integralPresenter.request(user.getUserId(),user.getSessionId());
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
    @Override
    protected void destoryData() {
        integralPresenter.unBind();
    }
}
