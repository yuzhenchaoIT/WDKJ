package com.wd.tech.myview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wd.tech.R;
import com.wd.tech.bean.QueryUser;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.MyDialog;
import com.wd.tech.core.PickView;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.DoTheTaskPresenter;
import com.wd.tech.presenter.PerfectPresenter;
import com.wd.tech.presenter.QueryUserPresenter;
import com.wd.tech.view.HomeActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PerfectActivity extends WDActivity{

    private User user;
    private EditText mEditNamePer,mEditDatePer,mEditEmailPer;
    private TextView mTextSexPer;
    private QueryUserPresenter queryUserPresenter;
    private PerfectPresenter perfectPresenter;
    private SharedPreferences sp;
    private int sex;
    private MyDialog dialog;
    private String sigqian;
    private TextView mTextQianPer;
    private QueryUser result;
    private DoTheTaskPresenter doTheTaskPresenter = new DoTheTaskPresenter(new DoTheTaskCall());
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

    }
    //实现查询用户信息接口
    class QueryUserCall implements DataCall<Result<QueryUser>> {

        @Override
        public void success(Result<QueryUser> data) {
            if (data.getStatus().equals("0000")){
                result = data.getResult();
                mEditNamePer.setText(result.getNickName());
                sex = result.getSex();
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
                mTextQianPer.setText(result.getSignature());
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
    //初始化控件方法
    private void Initialize() {
        mEditNamePer = (EditText) findViewById(R.id.medit_name_per);
        mTextSexPer = (TextView) findViewById(R.id.mtext_sex_per);
        mEditDatePer = (EditText) findViewById(R.id.medit_date_per);
        mEditEmailPer = (EditText) findViewById(R.id.medit_email_per);
        mTextQianPer = (TextView) findViewById(R.id.mtext_qian_per);
        mTextSexPer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = View.inflate(PerfectActivity.this,R.layout.dialogsex_item,null);
                dialog = new MyDialog(PerfectActivity.this, view);
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                PickView pvPickView = view.findViewById(R.id.pvPickView);
                TextView tv_sexdialog_sure = view.findViewById(R.id.tv_sexdialog_sure);
                TextView tv_sexdialog_cancel = view.findViewById(R.id.tv_sexdialog_cancel);
                ArrayList<String> grade = new ArrayList<>();
                grade.add("男");
                grade.add("女");
                pvPickView.setData(grade);
                pvPickView.setOnSelectListener(new PickView.onSelectListener() {
                    @Override
                    public void onSelect(String text) {
                        mTextSexPer.setText(text);
                    }
                });
                tv_sexdialog_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                tv_sexdialog_sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }
    //点击完成
    @OnClick(R.id.mtext_finish)
    public void mfinish(){
        //声明当前性别
        int currentSex;
        //获取值
        String name = mEditNamePer.getText().toString().trim();
        String sex = mTextSexPer.getText().toString().trim();
        if (sex.equals("男")){
            currentSex = 1;
        }else {
            currentSex = 2;
        }
        String date = mEditDatePer.getText().toString().trim();
        String email = mEditEmailPer.getText().toString().trim();
        //接收值
        sp = getSharedPreferences("signatrue",Context.MODE_PRIVATE);
        sigqian = sp.getString("sigqian", "");
        perfectPresenter = new PerfectPresenter(new PerfectCall());
        perfectPresenter.request(user.getUserId(),user.getSessionId(), name,currentSex,sigqian,date,email);
        //传值
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("qian",sigqian);
        edit.commit();

    }
    //实现完善个人信息接口
    class PerfectCall implements DataCall<Result>{
        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")){
                Toast.makeText(PerfectActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
                doTheTaskPresenter.request(user.getUserId(),user.getSessionId(),1006);
                finish();
            }else {


                Toast.makeText(PerfectActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
    //实现做任务接口
    private class DoTheTaskCall implements DataCall<Result>{
        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")){
                Toast.makeText(PerfectActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
    //点击跳转个性签名
    @OnClick(R.id.mlinear_per)
    public void mimage(){
        //跳转
        Intent intent = new Intent(PerfectActivity.this, SignatureActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryUserPresenter.request(user.getUserId(),user.getSessionId());
    }

    @Override
    protected void destoryData() {

    }
}
