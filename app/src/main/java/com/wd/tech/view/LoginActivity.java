package com.wd.tech.view;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.wd.tech.R;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.dao.DaoMaster;
import com.wd.tech.dao.DaoSession;
import com.wd.tech.dao.UserDao;
import com.wd.tech.presenter.LoginPresenter;
import com.wd.tech.util.RsaCoder;
import com.wd.tech.util.StringUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends WDActivity {


    private EditText mEditPhone,mEditPass;
    private LoginPresenter loginPresenter;
    private CheckBox mCheckEye;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
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
    private void Initialize() {
        mEditPhone = (EditText) findViewById(R.id.mEditPhone);
        mEditPass = (EditText) findViewById(R.id.mEditPass);
        mCheckEye = (CheckBox) findViewById(R.id.mCheckEye);
        //点击显示和隐藏密码
        mCheckEye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //如果选中，显示密码
                    mEditPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    //否则隐藏密码
                    mEditPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
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
        //获取输入框的值
        String phone = mEditPhone.getText().toString().trim();
        String pass = mEditPass.getText().toString().trim();
      /*  if (!StringUtils.isMobileNO(phone)&&phone.length()<=11){
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!(pass.length() >=16) &&pass.length()<=7){
            Toast.makeText(this, "请输入至少8-16位的密码", Toast.LENGTH_SHORT).show();
            return;
        }*/
        loginPresenter = new LoginPresenter(new LoginCall());
        try {
            loginPresenter.request(phone,RsaCoder.encryptByPublicKey(pass));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //实现登录接口
    class LoginCall implements DataCall<Result<User>>{

        @Override
        public void success(Result<User> data) {
            if (data.getStatus().equals("0000")){
                Toast.makeText(LoginActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
                //添加数据库
                User result = data.getResult();
                DaoSession daoSession = DaoMaster.newDevSession(LoginActivity.this, UserDao.TABLENAME);
                UserDao userDao = daoSession.getUserDao();
                result.setStatu("1");
                userDao.insertOrReplace(result);
                //跳转
                finish();
            }else {
                Toast.makeText(LoginActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
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
