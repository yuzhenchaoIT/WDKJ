package com.wd.tech.view;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.bean.QueryUser;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.DrawLayoutEdge;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.WDPage;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.frag.FragInForMation;
import com.wd.tech.frag.FragMessage;
import com.wd.tech.frag.FragCommunity;
import com.wd.tech.myview.CollectionActivity;
import com.wd.tech.myview.FocusOnActivity;
import com.wd.tech.myview.IntegralActivity;
import com.wd.tech.myview.NoticeActivity;
import com.wd.tech.myview.PerfectActivity;
import com.wd.tech.myview.PostActivity;
import com.wd.tech.myview.SetUpActivity;
import com.wd.tech.myview.SiginActivity;
import com.wd.tech.myview.TaskActivity;
import com.wd.tech.presenter.QueryUserPresenter;
import com.wd.tech.util.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {


    private FragInForMation frag_InForMation;
    private FragMessage frag_Message;
    private FragCommunity frag_Community;
    private RadioGroup mradio;
    private LinearLayout mlinearhome;
    private LinearLayout mlinear;
    private DrawerLayout mdraw;
    private RelativeLayout mRelative;
    private LinearLayout mLinearShow;
    private QueryUserPresenter queryUserPresenter;
    private SimpleDraweeView mSimple;
    private TextView mTextName, mTextQian;
    private String sessionId;
    private String userName;
    private String pwd;
    private int userId;
    private User bean;
    private ImageView vip;
    private int monlick;
    private int mcurrent;
    private FragmentTransaction fragmentTransaction;
    private RadioButton mrb1,mrb2,mrb3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
       /* //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);*/
        /*setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);*/
        WDPage.fullScreen(this, false);
        setContentView(R.layout.activity_home);
        //绑定
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        //初始化控件
        Initialize();
        bean = WDActivity.getUser(this);
        if (bean != null) {
            sessionId = bean.getSessionId();
            userId = bean.getUserId();

        }


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        frag_InForMation = new FragInForMation();
        frag_Message = new FragMessage();
        frag_Community = new FragCommunity();
        transaction.add(R.id.mframe, frag_InForMation);
        transaction.add(R.id.mframe, frag_Message);
        transaction.add(R.id.mframe, frag_Community);
        transaction.show(frag_InForMation);
        transaction.hide(frag_Message);
        transaction.hide(frag_Community);
        mrb1.setTextColor(Color.BLACK);
        mrb2.setTextColor(Color.GRAY);
        mrb3.setTextColor(Color.GRAY);
        transaction.commit();
        //默认选中第一个
        mcurrent = R.id.mrb1;
        mradio.check(mradio.getChildAt(0).getId());
        mdraw.setScrimColor(Color.TRANSPARENT);//去除阴影
        mlinear.measure(0, 0);
        final float width = mlinear.getMeasuredWidth() * 0.2f;//获取布局宽度，并获得左移大小
        mlinear.setTranslationX(-width);                 //底布局左移
        //点击侧滑
        mdraw.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {
                mlinear.setTranslationX(-width + width * v);               //底布局跟着移动
                mlinearhome.setTranslationX(view.getMeasuredWidth() * v);   //主界面布局移动，移动长度等于抽屉的移动长度
            }

            @Override
            public void onDrawerOpened(@NonNull View view) {

            }

            @Override
            public void onDrawerClosed(@NonNull View view) {
                mdraw.closeDrawers();
            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });
        DrawLayoutEdge.setLeftEdgeSize(this,mdraw,0.5f);
    }

    //点击切换页面
    @OnClick({R.id.mrb1, R.id.mrb2, R.id.mrb3})
    public void onView(View view) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (view.getId()) {
            case R.id.mrb1:
                fragmentTransaction.show(frag_InForMation);
                fragmentTransaction.hide(frag_Message);
                fragmentTransaction.hide(frag_Community);
                mrb1.setTextColor(Color.BLACK);
                mrb2.setTextColor(Color.GRAY);
                mrb3.setTextColor(Color.GRAY);
                mcurrent = R.id.mrb1;
                break;
            case R.id.mrb2:
                User user = WDActivity.getUser(this);
                if (user != null) {
                    fragmentTransaction.show(frag_Message);
                    fragmentTransaction.hide(frag_InForMation);
                    fragmentTransaction.hide(frag_Community);
                    mrb2.setTextColor(Color.BLACK);
                    mrb1.setTextColor(Color.GRAY);
                    mrb3.setTextColor(Color.GRAY);
                    mcurrent = R.id.mrb2;
                }else {
                    monlick = R.id.mrb2;
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.mrb3:
                fragmentTransaction.show(frag_Community);
                fragmentTransaction.hide(frag_InForMation);
                fragmentTransaction.hide(frag_Message);
                mrb3.setTextColor(Color.BLACK);
                mrb2.setTextColor(Color.GRAY);
                mrb1.setTextColor(Color.GRAY);
                mcurrent = R.id.mrb3;
                break;

        }


        fragmentTransaction.commit();
    }

    //初始化控件方法
    private void Initialize() {
        mradio = findViewById(R.id.mradio);
        mlinearhome = findViewById(R.id.mlinear_home);
        mlinear = findViewById(R.id.mlinear);
        mdraw = findViewById(R.id.mdraw);
        mRelative = findViewById(R.id.mrelative);
        mLinearShow = findViewById(R.id.mlinear_show);
        mSimple = findViewById(R.id.msimple);
        mTextName = findViewById(R.id.mtext_name);
        mTextQian = findViewById(R.id.mtext_qian);
        vip = findViewById(R.id.vip);
        mrb1 = findViewById(R.id.mrb1);
        mrb2 = findViewById(R.id.mrb2);
        mrb3 = findViewById(R.id.mrb3);
    }

    //点击头像跳转
    @OnClick(R.id.msimple)
    public void msim() {
        //跳转
        Intent intent = new Intent(HomeActivity.this, PerfectActivity.class);
        startActivity(intent);
    }

    //点击跳转签到
    @OnClick(R.id.mlinear_qiandao)
    public void qiandao() {
        //跳转
        Intent intent = new Intent(HomeActivity.this, SiginActivity.class);
        startActivity(intent);
    }

    //点击跳转登录页
    @OnClick(R.id.mlinear_jump)
    public void mlinearJump() {
        //跳转
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        User user = WDActivity.getUser(this);
        if (user != null) {
            mRelative.setVisibility(View.GONE);
            mLinearShow.setVisibility(View.VISIBLE);
            queryUserPresenter = new QueryUserPresenter(new QueryUserCall());
            queryUserPresenter.request(user.getUserId(), user.getSessionId());
            if (monlick == R.id.mrb2){
                mradio.check(monlick);
                mcurrent = R.id.mrb2;
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.show(frag_Message);
                fragmentTransaction.hide(frag_InForMation);
                fragmentTransaction.hide(frag_Community);
                mrb2.setTextColor(Color.BLACK);
                mrb1.setTextColor(Color.GRAY);
                mrb3.setTextColor(Color.GRAY);
                fragmentTransaction.commit();
                monlick = 0;
            }
        } else {
            if (monlick == R.id.mrb2){
                mradio.check(mcurrent);
            }
            if (mcurrent == R.id.mrb2){
                mcurrent = R.id.mrb1;
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.show(frag_InForMation);
                fragmentTransaction.hide(frag_Message);
                fragmentTransaction.hide(frag_Community);
                mrb1.setTextColor(Color.BLACK);
                mrb2.setTextColor(Color.GRAY);
                mrb3.setTextColor(Color.GRAY);
                fragmentTransaction.commit();
                mradio.check(mcurrent);
            }
            monlick = 0;
            mRelative.setVisibility(View.VISIBLE);
            mLinearShow.setVisibility(View.GONE);
        }
        int jump = getIntent().getIntExtra("jump", 0);
        if (jump == 2){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.show(frag_Community);
            fragmentTransaction.hide(frag_InForMation);
            fragmentTransaction.hide(frag_Message);
            mradio.check(mradio.getChildAt(2).getId());
            fragmentTransaction.commit();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void shou(Message message) {
        if (message.what == 9) {
            User user = WDActivity.getUser(this);
            if (user!=null) {
                mRelative.setVisibility(View.GONE);
                mLinearShow.setVisibility(View.VISIBLE);
                queryUserPresenter = new QueryUserPresenter(new QueryUserCall());
                queryUserPresenter.request(user.getUserId(), user.getSessionId());
            }
        } else {
            mRelative.setVisibility(View.VISIBLE);
            mLinearShow.setVisibility(View.GONE);
        }
    }

    //实现查询用户信息接口
    class QueryUserCall implements DataCall<Result<QueryUser>> {

        @Override
        public void success(Result<QueryUser> data) {
            if (data.getStatus().equals("0000")) {
                QueryUser result = data.getResult();
                mSimple.setImageURI(result.getHeadPic());
                mTextName.setText(result.getNickName());
                mTextQian.setText(result.getSignature());
                int whetherVip = result.getWhetherVip();
                if (whetherVip == 1) {
                    vip.setVisibility(View.VISIBLE);
                } else {
                    vip.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void fail(ApiException e) {
            User user = WDActivity.getUser(HomeActivity.this);
            mSimple.setImageURI(user.getHeadPic());
            mTextName.setText(user.getNickName());
            int whetherVip = user.getWhetherVip();
            if (whetherVip == 1) {
                vip.setVisibility(View.VISIBLE);
            } else {
                vip.setVisibility(View.GONE);
            }
        }
    }

    //点击跳转页面
    @OnClick({R.id.mlinear_sc, R.id.mlinear_gz, R.id.mlinear_tz, R.id.mlinear_no, R.id.mlinear_jf, R.id.mlinear_rw, R.id.mlinear_sz})
    public void tiaozhuan(View view) {
        switch (view.getId()) {
            case R.id.mlinear_sc:
                //跳转
                Intent intent1 = new Intent(HomeActivity.this, CollectionActivity.class);
                startActivity(intent1);
                break;
            case R.id.mlinear_gz:
                //跳转
                Intent intent2 = new Intent(HomeActivity.this, FocusOnActivity.class);
                startActivity(intent2);
                break;
            case R.id.mlinear_tz:
                //跳转
                Intent intent3 = new Intent(HomeActivity.this, PostActivity.class);
                startActivity(intent3);
                break;
            case R.id.mlinear_no:
                //跳转
                Intent intent4 = new Intent(HomeActivity.this, NoticeActivity.class);
                startActivity(intent4);
                break;
            case R.id.mlinear_jf:
                //跳转
                Intent intent5 = new Intent(HomeActivity.this, IntegralActivity.class);
                startActivity(intent5);
                break;
            case R.id.mlinear_rw:
                //跳转
                Intent intent6 = new Intent(HomeActivity.this, TaskActivity.class);
                startActivity(intent6);
                break;
            case R.id.mlinear_sz:
                //跳转
                Intent intent7 = new Intent(HomeActivity.this, SetUpActivity.class);
                startActivity(intent7);
                break;
        }
    }

}
