package com.wd.tech.view;

import android.widget.EditText;
import android.widget.Toast;

import com.wd.tech.R;
import com.wd.tech.bean.Result;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.RegisterPresenter;
import com.wd.tech.util.RsaCoder;
import com.wd.tech.util.StringUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends WDActivity {


    private EditText mEditNameReg,mEditPhoneReg,mEditPassReg;
    private RegisterPresenter registerPresenter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        //绑定
        ButterKnife.bind(this);
        //初始化控件
        Initialize();
        closeSwipeBack();
    }
    //初始化控件方法
    public void Initialize() {
        mEditNameReg = (EditText) findViewById(R.id.mEditNameReg);
        mEditPhoneReg = (EditText) findViewById(R.id.mEditPhoneReg);
        mEditPassReg = (EditText) findViewById(R.id.mEditPassReg);
    }
    //点击注册
    @OnClick(R.id.mRegisterBt)
    public void mRegisterBt(){
        //获取输入框的值
        String name = mEditNameReg.getText().toString().trim();
        String phone = mEditPhoneReg.getText().toString().trim();
        String pass = mEditPassReg.getText().toString().trim();
        if (!StringUtils.isMobileNO(phone)&&phone.length()<=11){
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!(pass.length() >=16 &&pass.length()<=7)){
            Toast.makeText(this, "请输入至少8-16位的密码", Toast.LENGTH_SHORT).show();
            return;
        }
        registerPresenter = new RegisterPresenter(new RegisterCall());
        try {
            registerPresenter.request(phone,name,RsaCoder.encryptByPublicKey(pass));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //实现注册的接口
    class RegisterCall implements DataCall<Result>{

        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")){
                Toast.makeText(RegisterActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
                //跳转登录页
                finish();
            }
        }
        @Override
        public void fail(ApiException e) {

        }
    }
    @Override
    protected void destoryData() {

    }
}
