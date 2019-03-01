package com.wd.tech.view;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.nostra13.universalimageloader.utils.L;
import com.wd.tech.R;
import com.wd.tech.bean.QueryUser;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.WDActivity;
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
    private TextView mTextName,mTextQian;
    private String sessionId;
    private String userName;
    private String pwd;
    private int userId;
    private User bean;
    private ImageView vip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_home);
        //绑定
        ButterKnife.bind(this);
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
        transaction.commit();
        //默认选中第一个
        mradio.check(mradio.getChildAt(0).getId());
        mdraw.setScrimColor(Color.TRANSPARENT);//去除阴影
        mlinear.measure(0,0);
        final float width=mlinear.getMeasuredWidth()*0.2f;//获取布局宽度，并获得左移大小
        mlinear.setTranslationX(-width);                 //底布局左移
        //点击侧滑
        mdraw.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {
                mlinear.setTranslationX(-width+width*v);               //底布局跟着移动
                mlinearhome.setTranslationX(view.getMeasuredWidth()*v);   //主界面布局移动，移动长度等于抽屉的移动长度


            }

            @Override
            public void onDrawerOpened(@NonNull View view) {

            }

            @Override
            public void onDrawerClosed(@NonNull View view) {

            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });
        mRelative.setVisibility(View.VISIBLE);
        mLinearShow.setVisibility(View.GONE);
    }
    //点击切换页面
    @OnClick({R.id.mrb1,R.id.mrb2,R.id.mrb3})
    public void onView(View view){
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        switch (view.getId()){
            case R.id.mrb1:
                transaction1.show(frag_InForMation);
                transaction1.hide(frag_Message);
                transaction1.hide(frag_Community);
                break;
            case R.id.mrb2:
                transaction1.show(frag_Message);
                transaction1.hide(frag_InForMation);
                transaction1.hide(frag_Community);
                break;
            case R.id.mrb3:
                transaction1.show(frag_Community);
                transaction1.hide(frag_InForMation);
                transaction1.hide(frag_Message);
                break;

        }
        transaction1.commit();
    }
    //初始化控件方法
    private void Initialize() {
        mradio = findViewById(R.id.mradio);
        mlinearhome = findViewById(R.id.mlinear_home);
        mlinear = findViewById(R.id.mlinear);
        mdraw = findViewById(R.id.mdraw);
        mRelative =  findViewById(R.id.mrelative);
        mLinearShow =  findViewById(R.id.mlinear_show);
        mSimple = findViewById(R.id.msimple);
        mTextName = findViewById(R.id.mtext_name);
        mTextQian = findViewById(R.id.mtext_qian);
        vip = findViewById(R.id.vip);

    }
    //点击头像跳转
    @OnClick(R.id.msimple)
    public void msim(){
        //跳转
        Intent intent = new Intent(HomeActivity.this, PerfectActivity.class);
        startActivity(intent);
    }
    //点击跳转签到
    @OnClick(R.id.mlinear_qiandao)
    public void qiandao(){
        //跳转
        Intent intent = new Intent(HomeActivity.this, SiginActivity.class);
        startActivity(intent);
    }
    //点击跳转登录页
    @OnClick(R.id.mlinear_jump)
    public void mlinearJump(){
        //跳转
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        User user = WDActivity.getUser(this);
        if (user!=null){
            mRelative.setVisibility(View.GONE);
            mLinearShow.setVisibility(View.VISIBLE);
            queryUserPresenter = new QueryUserPresenter(new QueryUserCall());
            queryUserPresenter.request(user.getUserId(),user.getSessionId());
        }else {
            mRelative.setVisibility(View.VISIBLE);
            mLinearShow.setVisibility(View.GONE);
        }
    }
    //实现查询用户信息接口
    class QueryUserCall implements DataCall<Result<QueryUser>>{

        @Override
        public void success(Result<QueryUser> data) {
            if (data.getStatus().equals("0000")){
                QueryUser result = data.getResult();
                mSimple.setImageURI(result.getHeadPic());
                mTextName.setText(result.getNickName());
                mTextQian.setText(result.getSignature());
                int whetherVip = result.getWhetherVip();
                if (whetherVip==1){
                    vip.setVisibility(View.VISIBLE);
                }else {
                    vip.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
    //点击跳转页面
    @OnClick({R.id.mlinear_sc,R.id.mlinear_gz,R.id.mlinear_tz,R.id.mlinear_no,R.id.mlinear_jf,R.id.mlinear_rw,R.id.mlinear_sz})
    public void tiaozhuan(View view){
        switch (view.getId()){
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
