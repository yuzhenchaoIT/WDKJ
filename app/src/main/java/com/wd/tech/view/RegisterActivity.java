package com.wd.tech.view;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.wd.tech.R;
import com.wd.tech.bean.Result;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.RegisterPresenter;
import com.wd.tech.util.RsaCoder;
import com.wd.tech.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends WDActivity {


    private EditText mEditNameReg,mEditPhoneReg,mEditPassReg;
    private RegisterPresenter registerPresenter;
    private String phone;
    private String passss;

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
        mEditNameReg = (EditText) findViewById(R.id.medit_name_reg);
        mEditPhoneReg = (EditText) findViewById(R.id.medit_phone_reg);
        mEditPassReg = (EditText) findViewById(R.id.medit_pass_reg);
    }
    //点击注册
    @OnClick(R.id.mregister_bt)
    public void mRegisterBt(){
        //获取输入框的值
        String name = mEditNameReg.getText().toString().trim();
        phone = mEditPhoneReg.getText().toString().trim();
        try {
            passss = RsaCoder.encryptByPublicKey(phone);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String pass = mEditPassReg.getText().toString().trim();
        boolean b = compileExChar(name);
        if (b){
            Toast.makeText(this, "不能有特殊符号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtils.isMobileNO(phone)&& phone.length()<=11){
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean b1 = validatePhonePass(pass);
        if (!b1){
            Toast.makeText(this, "必须大小写密码至少8位", Toast.LENGTH_SHORT).show();
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
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            // call method in SDK
                            EMClient.getInstance().createAccount(phone, passss);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registered_successfully), Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        } catch (final HyphenateException e) {
                            runOnUiThread(new Runnable() {
                                public void run() {

                                    int errorCode = e.getErrorCode();
                                    if(errorCode== EMError.NETWORK_ERROR){
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
                                    }else if(errorCode == EMError.USER_ALREADY_EXIST){
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
                                    }else if(errorCode == EMError.USER_AUTHENTICATION_FAILED){
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
                                    }else if(errorCode == EMError.USER_ILLEGAL_ARGUMENT){
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name),Toast.LENGTH_SHORT).show();
                                    }else if(errorCode == EMError.EXCEED_SERVICE_LIMIT){
                                        Toast.makeText(RegisterActivity.this, getResources().getString(R.string.register_exceed_service_limit), Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registration_failed), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }).start();

                Toast.makeText(RegisterActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
                //跳转登录页
                finish();
            }else {
                Toast.makeText(RegisterActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void fail(ApiException e) {

        }
    }
    @Override
    protected void destoryData() {

    }
    //验证用户名
    private static boolean compileExChar(String str) {

        String limitEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";

        Pattern pattern = Pattern.compile(limitEx);
        Matcher m = pattern.matcher(str);
        return m.find();
    }
    //验证密码
    public static boolean validatePhonePass(String pass) {
        String passRegex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
        return !TextUtils.isEmpty(pass) && pass.matches(passRegex);
    }
}
