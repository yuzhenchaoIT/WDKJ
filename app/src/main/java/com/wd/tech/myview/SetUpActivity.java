package com.wd.tech.myview;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.wd.tech.presenter.QueryUserPresenter;
import com.wd.tech.view.HomeActivity;
import com.wd.tech.view.LoginActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetUpActivity extends WDActivity {
    private QueryUserPresenter queryUserPresenter;
    private SimpleDraweeView mImageUp;
    private TextView mTextNameUp,mTextSexUp,mTextDateUp
    ,mTextPhoneUp,mTextEmailUp,mTextjfUp,mTextVipUp;

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
        User user = WDActivity.getUser(this);
        queryUserPresenter = new QueryUserPresenter(new QueryUserCall());
        queryUserPresenter.request(user.getUserId(),user.getSessionId());
    }
    //初始化控件方法
    private void Initialize() {
        mImageUp = (SimpleDraweeView) findViewById(R.id.mImageUp);
        mTextNameUp = (TextView) findViewById(R.id.mTextNameUp);
        mTextSexUp = (TextView) findViewById(R.id.mTextSexUp);
        mTextDateUp = (TextView) findViewById(R.id.mTextDateUp);
        mTextPhoneUp = (TextView) findViewById(R.id.mTextPhoneUp);
        mTextEmailUp = (TextView) findViewById(R.id.mTextEmailUp);
        mTextjfUp = (TextView) findViewById(R.id.mTextjfUp);
        mTextVipUp = (TextView) findViewById(R.id.mTextVipUp);
    }

    //实现查询用户信息接口
    class QueryUserCall implements DataCall<Result<QueryUser>> {

        @Override
        public void success(Result<QueryUser> data) {
            if (data.getStatus().equals("0000")){
                QueryUser result = data.getResult();
                mImageUp.setImageURI(result.getHeadPic());
                mTextNameUp.setText(result.getNickName());
                int sex = result.getSex();
                if (sex == 1){
                    mTextSexUp.setText("男");
                }else {
                    mTextSexUp.setText("女");
                }
                long birthday = result.getBirthday();
                Date date = new Date(birthday);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String s = format.format(date);
                mTextDateUp.setText(s);
                mTextPhoneUp.setText(result.getPhone());
                mTextEmailUp.setText(result.getEmail());
                mTextjfUp.setText(result.getIntegral()+"");
                int whetherVip = result.getWhetherVip();
                if (whetherVip == 1){
                    mTextVipUp.setText("是");
                }else {
                    mTextVipUp.setText("否");
                }

            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
    //点击退出登录
    @OnClick(R.id.mTexttuiUp)
    public void tui(){
        User user = WDActivity.getUser(this);
        if (user != null){
            DaoSession daoSession = DaoMaster.newDevSession(SetUpActivity.this, UserDao.TABLENAME);
            UserDao userDao = daoSession.getUserDao();
            userDao.deleteAll();
            finish();
        }
    }
    //点击按钮返回
    @OnClick(R.id.mReturn)
    public void mreturn(){
        finish();
    }
    @Override
    protected void destoryData() {

    }
}
