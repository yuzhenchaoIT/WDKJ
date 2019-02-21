package com.wd.tech.view;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import com.wd.tech.frag.FragInForMation;
import com.wd.tech.frag.FragMessage;
import com.wd.tech.frag.FragCommunity;
import com.wd.tech.myview.CollectionActivity;
import com.wd.tech.myview.FocusOnActivity;
import com.wd.tech.myview.IntegralActivity;
import com.wd.tech.myview.NoticeActivity;
import com.wd.tech.myview.PostActivity;
import com.wd.tech.myview.SetUpActivity;
import com.wd.tech.myview.TaskActivity;
import com.wd.tech.presenter.QueryUserPresenter;
import com.wd.tech.util.StringUtils;

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
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        frag_InForMation = new FragInForMation();
        frag_Message = new FragMessage();
        frag_Community = new FragCommunity();
        transaction.add(R.id.mFrame, frag_InForMation);
        transaction.add(R.id.mFrame, frag_Message);
        transaction.add(R.id.mFrame, frag_Community);
        transaction.show(frag_InForMation);
        transaction.hide(frag_Message);
        transaction.hide(frag_Community);
        transaction.commit();
        //默认选中第一个
        mradio.check(mradio.getChildAt(0).getId());
        //点击侧滑
        mdraw.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {
                //获取屏幕的宽高
                WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                //设置右面的布局位置  根据左面菜单的right作为右面布局的left   左面的right+屏幕的宽度（或者right的宽度这里是相等的）为右面布局的right
                mlinearhome.layout(mlinear.getRight(), 0, mlinear.getRight() + display.getWidth(), display.getHeight());
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
    }
    //点击切换页面
    @OnClick({R.id.mRB1,R.id.mRB2,R.id.mRB3})
    public void onView(View view){
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        switch (view.getId()){
            case R.id.mRB1:
                transaction1.show(frag_InForMation);
                transaction1.hide(frag_Message);
                transaction1.hide(frag_Community);
                break;
            case R.id.mRB2:
                transaction1.show(frag_Message);
                transaction1.hide(frag_InForMation);
                transaction1.hide(frag_Community);
                break;
            case R.id.mRB3:
                transaction1.show(frag_Community);
                transaction1.hide(frag_InForMation);
                transaction1.hide(frag_Message);
                break;

        }
        transaction1.commit();
    }
    //初始化控件方法
    private void Initialize() {
        mradio = findViewById(R.id.mRadio);
        mlinearhome = findViewById(R.id.mLinearHome);
        mlinear = findViewById(R.id.mLinear);
        mdraw = findViewById(R.id.mDraw);
        mRelative =  findViewById(R.id.mRelative);
        mLinearShow =  findViewById(R.id.mLinearShow);
        mSimple = findViewById(R.id.mSimple);
        mTextName = findViewById(R.id.mTextName);
        mTextQian = findViewById(R.id.mTextQian);
    }
    //点击跳转登录页
    @OnClick(R.id.mLinearJump)
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
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
    //点击跳转页面
    @OnClick({R.id.mLinearsc,R.id.mLineargz,R.id.mLineartz,R.id.mLinearno,R.id.mLinearjf,R.id.mLinearrw,R.id.mLinearsz})
    public void tiaozhuan(View view){
        switch (view.getId()){
            case R.id.mLinearsc:
                //跳转
                Intent intent1 = new Intent(HomeActivity.this, CollectionActivity.class);
                startActivity(intent1);
                break;
            case R.id.mLineargz:
                //跳转
                Intent intent2 = new Intent(HomeActivity.this, FocusOnActivity.class);
                startActivity(intent2);
                break;
            case R.id.mLineartz:
                //跳转
                Intent intent3 = new Intent(HomeActivity.this, PostActivity.class);
                startActivity(intent3);
                break;
            case R.id.mLinearno:
                //跳转
                Intent intent4 = new Intent(HomeActivity.this, NoticeActivity.class);
                startActivity(intent4);
                break;
            case R.id.mLinearjf:
                //跳转
                Intent intent5 = new Intent(HomeActivity.this, IntegralActivity.class);
                startActivity(intent5);
                break;
            case R.id.mLinearrw:
                //跳转
                Intent intent6 = new Intent(HomeActivity.this, TaskActivity.class);
                startActivity(intent6);
                break;
            case R.id.mLinearsz:
                //跳转
                Intent intent7 = new Intent(HomeActivity.this, SetUpActivity.class);
                startActivity(intent7);
                break;
        }
    }
}
