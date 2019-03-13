package com.wd.tech.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.wd.tech.R;
import com.wd.tech.bean.Conversation;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.WDApplication;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.dao.DaoMaster;
import com.wd.tech.dao.DaoSession;
import com.wd.tech.dao.UserDao;
import com.wd.tech.face.DetecterActivity;
import com.wd.tech.presenter.LoginPresenter;
import com.wd.tech.util.DaoUtils;
import com.wd.tech.util.RsaCoder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends WDActivity {


    private EditText mEditPhone,mEditPass;
    private LoginPresenter loginPresenter;
    private CheckBox mCheckEye;
    private static final int REQUEST_CODE_OP = 7;

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
        ImageView mface_book = (ImageView) findViewById(R.id.mface_book);
        mface_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( ((WDApplication)getApplicationContext()).mFaceDB.mRegister.isEmpty() ) {
                    Toast.makeText(LoginActivity.this, "没有注册人脸，请先注册！", Toast.LENGTH_SHORT).show();
                } else {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("请选择相机")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setItems(new String[]{"后置相机", "前置相机"}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startDetector(which);
                                    finish();
                                }
                            })
                            .show();
                }
            }
        });
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
                Conversation conversation = new Conversation();
                conversation.setUserName(data.getResult().getUserName().toLowerCase());
                conversation.setHeadPic(data.getResult().getHeadPic());
                conversation.setNickName(data.getResult().getNickName());
                conversation.setUserId(data.getResult().getUserId());
                DaoUtils.getInstance().getConversationDao().insertOrReplaceInTx(conversation);
                Log.i("ee", data.getResult().getUserName()+"--"+data.getResult().getPwd());
                EMClient.getInstance().login(data.getResult().getUserName(),data.getResult().getPwd()
                        ,new EMCallBack() {//回调
                    @Override
                    public void onSuccess() {
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        Log.i("yy", "登录聊天服务器成功！");

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

    private void startDetector(int camera) {
        Intent it = new Intent(LoginActivity.this, DetecterActivity.class);
        it.putExtra("Camera", camera);
        startActivityForResult(it, REQUEST_CODE_OP);
    }
}
