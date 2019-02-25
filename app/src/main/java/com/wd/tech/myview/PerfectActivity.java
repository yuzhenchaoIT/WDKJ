package com.wd.tech.myview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wd.tech.R;
import com.wd.tech.bean.QueryUser;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.PerfectPresenter;
import com.wd.tech.presenter.QueryUserPresenter;
import com.wd.tech.view.HomeActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PerfectActivity extends WDActivity{

    private User user;
    private EditText mEditNamePer,mEditDatePer,mEditEmailPer,mTextSexPer;
    private QueryUserPresenter queryUserPresenter;
    private PerfectPresenter perfectPresenter;
    private SharedPreferences sp;
    private String sig;
    private QueryUser result;
    private int index = 0;// 记录单选对话框的下标
    private String newsex;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_perfect;
    }

    @Override
    protected void initView() {
        //绑定
        ButterKnife.bind(this);
        //初始化控件
        Initialize();
        //查询数据库
        user = WDActivity.getUser(this);
        //设置数据
        queryUserPresenter = new QueryUserPresenter(new QueryUserCall());
        queryUserPresenter.request(user.getUserId(),user.getSessionId());
        //接收值
        sp = getSharedPreferences("signatrue",Context.MODE_PRIVATE);
        sig = sp.getString("sig", "");
        //传值
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("qian",sig);
        edit.commit();
    }
    //实现查询用户信息接口
    class QueryUserCall implements DataCall<Result<QueryUser>> {

        @Override
        public void success(Result<QueryUser> data) {
            if (data.getStatus().equals("0000")){
                result = data.getResult();
                mEditNamePer.setText(result.getNickName());
                int sex = result.getSex();
                if (sex == 1){
                    mTextSexPer.setText("男");
                }else {
                    mTextSexPer.setText("女");
                }
                long birthday = result.getBirthday();
                Date date = new Date(birthday);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String s = format.format(date);
                mEditDatePer.setText(s);
                mEditEmailPer.setText(result.getEmail());
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
    //初始化控件方法
    private void Initialize() {
        mEditNamePer = (EditText) findViewById(R.id.mEditNamePer);
        mTextSexPer = (EditText) findViewById(R.id.mTextSexPer);
        mEditDatePer = (EditText) findViewById(R.id.mEditDatePer);
        mEditEmailPer = (EditText) findViewById(R.id.mEditEmailPer);
    }
    //点击完成
    @OnClick(R.id.mTextFinish)
    public void mfinish(){
        //获取值
        String name = mEditNamePer.getText().toString().trim();
        String sex = mTextSexPer.getText().toString().trim();
        int a = Integer.parseInt(sex);

        String date = mEditDatePer.getText().toString().trim();
        String email = mEditEmailPer.getText().toString().trim();
        perfectPresenter = new PerfectPresenter(new PerfectCall());
        perfectPresenter.request(user.getUserId(),user.getSessionId(),name,a,sig,date,email);

    }
    //实现完善个人信息接口
    class PerfectCall implements DataCall<Result>{
        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")){
                Toast.makeText(PerfectActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(PerfectActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
    //点击跳转个性签名
    @OnClick(R.id.mImagePer)
    public void mimage(){
        //跳转
        Intent intent = new Intent(PerfectActivity.this, SignatureActivity.class);
        startActivity(intent);
    }
    @Override
    protected void destoryData() {

    }
}
