package com.wd.tech.myview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.bean.QueryUser;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.dao.DaoMaster;
import com.wd.tech.dao.DaoSession;
import com.wd.tech.dao.UserDao;
import com.wd.tech.presenter.ModifyEmailPresenter;
import com.wd.tech.presenter.ModifyNickNamePresenter;
import com.wd.tech.presenter.QueryUserPresenter;
import com.wd.tech.view.HomeActivity;
import com.wd.tech.view.LoginActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetUpActivity extends WDActivity implements View.OnClickListener {
    private QueryUserPresenter queryUserPresenter;
    private SimpleDraweeView mImageUp;
    private TextView mTextNameUp, mTextSexUp, mTextDateUp,mTextEmailUp, mTextPhoneUp, mTextjfUp, mTextVipUp,mTextQianSet;
    private View inflate;
    private TextView camera;
    private TextView pic;
    private TextView cancel;
    private Dialog dialog;
    private User user;
    private QueryUser result;
    private int sex;
    private ModifyEmailPresenter modifyEmailPresenter;
    private ModifyNickNamePresenter modifyNickNamePresenter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_up;
    }

    @Override
    protected void initView() {
        //绑定
        ButterKnife.bind(this);
        //初始化控件
        Initialize();
        //查询数据库
        user = WDActivity.getUser(this);
        queryUserPresenter = new QueryUserPresenter(new QueryUserCall());
        queryUserPresenter.request(user.getUserId(), user.getSessionId());
    }

    //初始化控件方法
    private void Initialize() {
        mImageUp = (SimpleDraweeView) findViewById(R.id.mimage_up);
        mTextNameUp = (TextView) findViewById(R.id.mtext_name_up);
        mTextSexUp = (TextView) findViewById(R.id.mtext_sex_up);
        mTextDateUp = (TextView) findViewById(R.id.mtext_date_up);
        mTextPhoneUp = (TextView) findViewById(R.id.mtext_phone_up);
        mTextEmailUp = (TextView) findViewById(R.id.mtext_email_up);
        mTextjfUp = (TextView) findViewById(R.id.mtextjf_up);
        mTextVipUp = (TextView) findViewById(R.id.mtext_vip_up);
        mTextQianSet = (TextView) findViewById(R.id.mtext_qian_set);
        //点击修改用户名
        mTextNameUp.setOnClickListener(this);
        //点击修改邮箱
       mTextEmailUp.setOnClickListener(this);
    }

    //实现查询用户信息接口
    private class QueryUserCall implements DataCall<Result<QueryUser>> {
        @Override
        public void success(Result<QueryUser> data) {
            if (data.getStatus().equals("0000")) {
                result = data.getResult();
                mImageUp.setImageURI(result.getHeadPic());
                mTextNameUp.setText(result.getNickName());
                sex = result.getSex();
                if (sex == 1) {
                    mTextSexUp.setText("男");
                } else {
                    mTextSexUp.setText("女");
                }
                long birthday = result.getBirthday();
                Date date = new Date(birthday);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String s = format.format(date);
                mTextDateUp.setText(s);
                mTextPhoneUp.setText(result.getPhone());
                mTextEmailUp.setText(result.getEmail());
                mTextjfUp.setText(result.getIntegral() + "");
                int whetherVip = result.getWhetherVip();
                if (whetherVip == 1) {
                    mTextVipUp.setText("是");
                } else {
                    mTextVipUp.setText("否");
                }
                mTextQianSet.setText(result.getSignature());
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }

    //点击头像弹出提示框
    @OnClick(R.id.mimage_up)
    public void miangeu() {
        dialog = new Dialog(this, R.style.DialogTheme);
        //填充对话框的布局
        inflate = LayoutInflater.from(this).inflate(R.layout.dialog_item, null);
        //初始化控件
        inflate.findViewById(R.id.mcamear).setOnClickListener(this);
        inflate.findViewById(R.id.mpictrue).setOnClickListener(this);
        inflate.findViewById(R.id.cancel).setOnClickListener(this);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);

        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;//设置Dialog距离底部的距离//
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //  将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框

    }
    //点击拍照和相册
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mcamear:
                Toast.makeText(this, "111", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mpictrue:
                Toast.makeText(this, "222", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cancel:
                dialog.cancel();
                break;
            case R.id.mtext_name_up:
                final EditText editText = new EditText(this);
                editText.setText(result.getNickName());
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("修改用户名称");
                builder.setView(editText);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String s = editText.getText().toString().trim();
                        mTextNameUp.setText(s);
                        modifyNickNamePresenter = new ModifyNickNamePresenter(new NickNameCall());
                        modifyNickNamePresenter.request(user.getUserId(),user.getSessionId(),s);
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
                break;
            case R.id.mtext_email_up:
                final EditText editemail = new EditText(SetUpActivity.this);
                editemail.setText(result.getEmail());
                AlertDialog builder3 = new AlertDialog.Builder(SetUpActivity.this)
                        .setTitle("修改邮箱")
                        .setView(editemail)//设置输入框
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String trim = editemail.getText().toString().trim();
                                mTextEmailUp.setText(trim);
                                modifyEmailPresenter = new ModifyEmailPresenter(new EmailCall());
                                modifyEmailPresenter.request(user.getUserId(), user.getSessionId(),trim);
                            }
                        }).setNegativeButton("取消", null).create();
                builder3.show();
                break;
        }
    }
    //实现修改用户名接口
    private class NickNameCall implements DataCall<Result>{
        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")){
                Toast.makeText(SetUpActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(SetUpActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void fail(ApiException e) {

        }
    }
    //实现修改邮箱接口
    private class EmailCall implements DataCall<Result>{
        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")){
                Toast.makeText(SetUpActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(SetUpActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
    //点击跳转个性签名
    @OnClick(R.id.mlinear_set)
    public void mimage() {
        Intent intent = new Intent(SetUpActivity.this, SignatureActivity.class);
        startActivity(intent);
    }

    //点击退出登录
    @OnClick(R.id.mtexttui_up)
    public void tui() {
        User user = WDActivity.getUser(this);
        if (user != null) {
            DaoSession daoSession = DaoMaster.newDevSession(SetUpActivity.this, UserDao.TABLENAME);
            UserDao userDao = daoSession.getUserDao();
            userDao.deleteAll();
            finish();
        }
    }
    //点击修改密码
    @OnClick(R.id.mtextxiu_up)
    public void xiu(){
        //跳转
        Intent intent = new Intent(SetUpActivity.this, ChangePassActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        queryUserPresenter.request(user.getUserId(), user.getSessionId());
    }

    //点击按钮返回
    @OnClick(R.id.mreturn)
    public void mreturn() {
        finish();
    }

    @Override
    protected void destoryData() {

    }
}
