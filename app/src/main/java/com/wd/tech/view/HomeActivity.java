package com.wd.tech.view;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.wd.tech.R;
import com.wd.tech.core.WDActivity;
import com.wd.tech.frag.Frag_01;
import com.wd.tech.frag.Frag_02;
import com.wd.tech.frag.Frag_03;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends WDActivity {


    private Frag_01 frag_01;
    private Frag_02 frag_02;
    private Frag_03 frag_03;
    private RadioGroup mradio;
    private LinearLayout mlinearhome;
    private LinearLayout mlinear;
    private DrawerLayout mdraw;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        //绑定
        ButterKnife.bind(this);
        //初始化控件
        mradio = findViewById(R.id.mRadio);
        mlinearhome = findViewById(R.id.mLinearHome);
        mlinear = findViewById(R.id.mLinear);
        mdraw = findViewById(R.id.mDraw);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        frag_01 = new Frag_01();
        frag_02 = new Frag_02();
        frag_03 = new Frag_03();
        transaction.add(R.id.mFrame,frag_01);
        transaction.add(R.id.mFrame,frag_02);
        transaction.add(R.id.mFrame,frag_03);
        transaction.show(frag_01);
        transaction.hide(frag_02);
        transaction.hide(frag_03);
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
    @OnClick({R.id.mRB1,R.id.mRB2,R.id.mRB3})
    public void onView(View view){
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        switch (view.getId()){
            case R.id.mRB1:
                transaction1.show(frag_01);
                transaction1.hide(frag_02);
                transaction1.hide(frag_03);
                break;
            case R.id.mRB2:
                transaction1.show(frag_02);
                transaction1.hide(frag_01);
                transaction1.hide(frag_03);
                break;
            case R.id.mRB3:
                transaction1.show(frag_03);
                transaction1.hide(frag_01);
                transaction1.hide(frag_02);
                break;

        }
        transaction1.commit();
    }
    @Override
    protected void destoryData() {

    }
}
