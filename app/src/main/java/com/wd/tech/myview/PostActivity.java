package com.wd.tech.myview;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wd.tech.R;
import com.wd.tech.adapter.PostAdapter;
import com.wd.tech.bean.MyPost;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.DeletePostPresenter;
import com.wd.tech.presenter.MyPostPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostActivity extends WDActivity implements PostAdapter.Shan,View.OnClickListener {
    private MyPostPresenter myPostPresenter;
    private User user;
    private RecyclerView mrecyclerPost;
    private RelativeLayout mrelativePost;
    private PostAdapter postAdapter;
    private SmartRefreshLayout smartRefreshLayout;
    private List<MyPost> result = new ArrayList<>();
    private Dialog dialog;
    private View inflate;
    private DeletePostPresenter deletePostPresenter;
    private int j;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_post;
    }

    @Override
    protected void initView() {
        //绑定
        ButterKnife.bind(this);
        //初始化控件
        mrecyclerPost = (RecyclerView) findViewById(R.id.mrecycler_post);
        mrelativePost = (RelativeLayout) findViewById(R.id.mrelative_post);
        smartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.msmart_post);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
                result.clear();
                myPostPresenter.request(user.getUserId(),user.getSessionId(),true,5);
            }
        });
        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore();
                myPostPresenter.request(user.getUserId(),user.getSessionId(),false,5);
            }
        });
        //查询数据库
        user = WDActivity.getUser(this);
        //设置适配器
        postAdapter = new PostAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mrecyclerPost.setLayoutManager(linearLayoutManager);
        mrecyclerPost.setAdapter(postAdapter);
        //设置数据
        myPostPresenter = new MyPostPresenter(new MyPostCall());
        myPostPresenter.request(user.getUserId(),user.getSessionId(),true,5);
        postAdapter.getShan(this);
    }

    @Override
    public void onShan(int i) {
        j = i;
        dialog = new Dialog(this, R.style.DialogTheme);
        //填充对话框的布局
        inflate = LayoutInflater.from(this).inflate(R.layout.dialogdel_item, null);
        //初始化控件
        inflate.findViewById(R.id.mdelete_dia).setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mdelete_dia:
                deletePostPresenter = new DeletePostPresenter(new DeleteCall());
                deletePostPresenter.request(user.getUserId(),user.getSessionId(),result.get(j).getId()+"");
                dialog.cancel();
                break;
            case R.id.cancel:
                dialog.cancel();
                break;
        }
    }
    //实现删除帖子接口
    class DeleteCall implements DataCall<Result>{

        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")){
                result.clear();
                myPostPresenter.request(user.getUserId(),user.getSessionId(),true,5);
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
    //实现我的帖子接口
    class MyPostCall implements DataCall<Result<List<MyPost>>>{

        @Override
        public void success(Result<List<MyPost>> data) {
            if (data.getStatus().equals("0000")){
                result.addAll(data.getResult());
                if (result.size()!=0){
                    postAdapter.clear();
                    postAdapter.addAll(result);
                    postAdapter.notifyDataSetChanged();
                    smartRefreshLayout.setVisibility(View.VISIBLE);
                    mrelativePost.setVisibility(View.GONE);
                }else {
                    mrecyclerPost.setVisibility(View.GONE);
                    mrelativePost.setVisibility(View.VISIBLE);
                }

            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
    //点击按钮返回
    @OnClick(R.id.mreturn)
    public void mreturn(){
        finish();
    }
    @Override
    protected void destoryData() {

    }
}
