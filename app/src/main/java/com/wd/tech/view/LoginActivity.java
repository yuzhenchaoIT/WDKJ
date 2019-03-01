package com.wd.tech.view;

import android.content.Intent;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
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
        mEditPhone = (EditText) findViewById(R.id.medit_phone);
        mEditPass = (EditText) findViewById(R.id.medit_pass);
        mCheckEye = (CheckBox) findViewById(R.id.mcheck_eye);
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
    @OnClick(R.id.mregister)
    public void mregsiter(){
        //跳转
        intent(RegisterActivity.class);
    }
    //点击登录
    @OnClick(R.id.mlogin)
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
                Log.i("aa", "success: "+data.getResult().getSessionId()+"    "+data.getResult().getUserId());
                Toast.makeText(LoginActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
                //添加数据库
                User result = data.getResult();
                DaoSession daoSession = DaoMaster.newDevSession(LoginActivity.this, UserDao.TABLENAME);
                UserDao userDao = daoSession.getUserDao();
                result.setStatu("1");
                userDao.insertOrReplace(result);
                EMClient.getInstance().login("b2RKkG17600042580","V3qmuOQnrFuLHZ7z0pz8+p/y0gg+LMVJCMvrFj+C2G1A+KlCOLB/jB0U+n+tubWpQ70XG9KUq+dks1/fyCkF23bHs+rNnMMiIfaQ7cHDTjFHxcw8WIWp9CWSZrSmbJplpLe/sHnu0Nuuz04DIobH9yIGqV2hOLSyraa2/93phns="
                        ,new EMCallBack() {//回调
                    @Override
                    public void onSuccess() {
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        Log.d("main", "登录聊天服务器成功！");

                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }

                    @Override
                    public void onError(int code, String message) {
                        Log.d("main", "登录聊天服务器失败！");
                    }
                });
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
    //点击微信登录
    @OnClick(R.id.mwei_xin)
    public void mwx(){
        Intent intent = new Intent(LoginActivity.this, WXLoginActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void destoryData() {

    }
}
