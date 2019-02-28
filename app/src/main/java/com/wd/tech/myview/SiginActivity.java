package com.wd.tech.myview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wd.tech.R;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.SignCalendar;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.BasePresenter;
import com.wd.tech.presenter.FindConSignPresenter;
import com.wd.tech.presenter.FindUserSignPresenter;
import com.wd.tech.presenter.FindUserSignRecPresenter;
import com.wd.tech.presenter.UserSignPresenter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SiginActivity extends WDActivity {
    private UserSignPresenter userSignPresenter;
    private User user;
    private TextView mTextSign;
    private SignCalendar calendar;
    private Button rlBtnSign;
    private int month;
    private int year;
    private String date;
    private FindUserSignPresenter findUserSignPresenter;
    private FindConSignPresenter findConSignPresenter;
    private FindUserSignRecPresenter findUserSignRecPresenter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_sigin;
    }

    @Override
    protected void initView() {
        //绑定
        ButterKnife.bind(this);
        //查询数据库
        user = WDActivity.getUser(this);
        //初始化控件
        mTextSign = (TextView) findViewById(R.id.tv_sign_year_month);
        calendar = (SignCalendar) findViewById(R.id.sc_main);
        rlBtnSign = (Button) findViewById(R.id.mrl_btn_sign);
        //日期
        month = Calendar.getInstance().get(Calendar.MONTH);
        year = Calendar.getInstance().get(Calendar.YEAR);
        //设置日期
        calendar.setOnCalendarDateChangedListener(new SignCalendar.OnCalendarDateChangedListener() {
            @Override
            public void onCalendarDateChanged(int year, int month) {
                mTextSign.setText(year+"年"+(month)+"月");
            }
        });
        mTextSign.setText(year+"年"+(month+1)+"月");
        //获取当前时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        date = formatter.format(curDate);
        //查询当天签到状态
        findUserSignPresenter = new FindUserSignPresenter(new FindUserCall());
        findUserSignPresenter.request(user.getUserId(),user.getSessionId());
        //查询用户连续签到天数
       /* findConSignPresenter = new FindConSignPresenter(new FindConCall());
        findConSignPresenter.request(user.getUserId(),user.getSessionId());*/
        //查询用户当月所有签到的日期
        findUserSignRecPresenter = new FindUserSignRecPresenter(new FindUserSignCall());
        findUserSignRecPresenter.request(user.getUserId(),user.getSessionId());
    }
    //实现查询用户当月所有签到的日期
    class FindUserSignCall implements DataCall<Result>{

        @Override
        public void success(Result data) {
            List<String> signList = (List<String>) data.getResult();
            for (int i = 0; i < signList.size(); i++) {
                calendar.setCalendarDayBgColor(signList.get(i), R.drawable.yiqiandao);
            }
            calendar.textColor();
        }

        @Override
        public void fail(ApiException e) {

        }
    }
    //实现查询用户连续签到天数接口
    class FindConCall implements DataCall<Result>{
        private Double result;
        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")){
                result = (Double) data.getResult();
                if (result==1){
                    calendar.setCalendarDayBgColor(date, R.drawable.yiqiandao);
                    calendar.textColor();
                    rlBtnSign.setText("已签到");
                    rlBtnSign.setClickable(false);
                }else {
                    calendar.textColor();
                }
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
    //实现查询当天签到状态接口
    class FindUserCall implements DataCall<Result>{
        private Double resultInt;
        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")){
                resultInt = (Double) data.getResult();
                if (resultInt == 1){
                    calendar.setCalendarDayBgColor(date, R.drawable.yiqiandao);
                    calendar.textColor();
                    rlBtnSign.setText("已签到");
                    rlBtnSign.setClickable(false);
                }else {
                    calendar.textColor();
                }
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
    //点击签到
    @OnClick(R.id.mrl_btn_sign)
    public void mrl(){
        userSignPresenter = new UserSignPresenter(new UserSignCall());
        userSignPresenter.request(user.getUserId(),user.getSessionId());
    }
    //点击实现签到接口
    class UserSignCall implements DataCall<Result>{

        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")){
                Toast.makeText(SiginActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
                calendar.setCalendarDayBgColor(date, R.drawable.yiqiandao);
                calendar.textColor();
                rlBtnSign.setText("已签到");
                rlBtnSign.setClickable(false);
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
        findUserSignRecPresenter.unBind();
        findConSignPresenter.unBind();
        findUserSignPresenter.unBind();
    }

}
