package com.wd.tech.myview;

import android.widget.EditText;
import android.widget.Toast;

import com.wd.tech.R;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.ChangePassPresenter;
import com.wd.tech.util.RsaCoder;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePassActivity extends WDActivity {


    private User user;
    private EditText mEditChange;
    private EditText mEditChangex;
    private ChangePassPresenter changePassPresenter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_pass;
    }

    @Override
    protected void initView() {
        //绑定
        ButterKnife.bind(this);
        //初始化控件
        mEditChange = (EditText) findViewById(R.id.meditj_change);
        mEditChangex = (EditText) findViewById(R.id.meditx_change);
        //查询数据库
        user = WDActivity.getUser(this);
    }
    //点击确认
    @OnClick(R.id.mbutton_change)
    public void mbutton(){
        //获取值
        String passj = mEditChange.getText().toString().trim();
        String passx = mEditChangex.getText().toString().trim();
        changePassPresenter = new ChangePassPresenter(new ChangePassCall());
        try {
            changePassPresenter.request(user.getUserId(),user.getSessionId(),RsaCoder.encryptByPublicKey(passj),RsaCoder.encryptByPublicKey(passx));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //实现修改密码接口
    private class ChangePassCall implements DataCall<Result>{
        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")){
                finish();
            }else {
                Toast.makeText(ChangePassActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void fail(ApiException e) {

        }
    }
    //点击返回
    @OnClick(R.id.mreturn)
    public void mreturn(){
        finish();
    }
    @Override
    protected void destoryData() {

    }
}
