package com.wd.tech.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wd.tech.R;
import com.wd.tech.adapter.FindFriendGroupListAdapter;
import com.wd.tech.bean.FriendGroup;
import com.wd.tech.bean.Result;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.WDApplication;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.AddFriendGroupPresenter;
import com.wd.tech.presenter.FindFriendGroupListPresenter;
import com.wd.tech.presenter.TransferFriendGroupPresenter;
import com.wd.tech.util.UIUtils;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckGroupActivity extends WDActivity {

    @BindView(R.id.check_group_recycle)
    RecyclerView checkGroupRecycle;
    @BindView(R.id.check_group_smart)
    SmartRefreshLayout checkGroupSmart;
    private int userId;
    private String sessionId;
    private FindFriendGroupListPresenter findUserJoinedGroupPresenter;
    private FindFriendGroupListAdapter findFriendGroupListAdapter;
    private int friendId;
    private AddFriendGroupPresenter addFriendGroupPresenter;
    private TransferFriendGroupPresenter transferFriendGroupPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_check_group;
    }

    @Override
    protected void initView() {
        findUserJoinedGroupPresenter = new FindFriendGroupListPresenter(new FindFriendGroupList());
        addFriendGroupPresenter = new AddFriendGroupPresenter(new NewGroup());
        transferFriendGroupPresenter = new TransferFriendGroupPresenter(new NewGroup());

        Intent intent = getIntent();
        friendId = intent.getIntExtra("friendId", 0);

        checkGroupSmart.setEnableRefresh(true);//启用刷新
        checkGroupSmart.setEnableLoadmore(false);//启用加载
        //刷新
        checkGroupSmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                findUserJoinedGroupPresenter.request(userId, sessionId);
            }
        });
        findFriendGroupListAdapter = new FindFriendGroupListAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        checkGroupRecycle.setLayoutManager(linearLayoutManager);
        checkGroupRecycle.setAdapter(findFriendGroupListAdapter);
        findFriendGroupListAdapter.setOnItemClickListener(new FindFriendGroupListAdapter.ClickListener() {
            @Override
            public void click(int id) {
                transferFriendGroupPresenter.request(userId,sessionId,id,friendId);
            }
        });
    }

    @Override
    protected void destoryData() {
        findUserJoinedGroupPresenter.unBind();
        addFriendGroupPresenter.unBind();
        transferFriendGroupPresenter.unBind();
    }



    @OnClick({R.id.check_group_back, R.id.check_group_new_group})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.check_group_back:
                finish();
                break;
            case R.id.check_group_new_group:
                final EditText et = new EditText(this);
                new AlertDialog.Builder(this).setTitle("添加到新分组")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String input = et.getText().toString();
                                if (TextUtils.isEmpty(input)) {
                                    UIUtils.showToastSafe("分组不能为空");
                                }
                                else {
                                    addFriendGroupPresenter.request(userId,sessionId,input);
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();

                break;
        }
    }
    class FindFriendGroupList implements DataCall<Result<List<FriendGroup>>> {

        @Override
        public void success(Result<List<FriendGroup>> data) {
            if (data.getStatus().equals("0000")){
                findFriendGroupListAdapter.setList(data.getResult());
                findFriendGroupListAdapter.notifyDataSetChanged();
                checkGroupSmart.finishRefresh();
            }
        }

        @Override
        public void fail(ApiException e) {

        }


    }
    class NewGroup implements DataCall<Result>{

        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")){
                UIUtils.showToastSafe(data.getMessage());
                findUserJoinedGroupPresenter.request(userId, sessionId);

            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences share = WDApplication.getShare();
        userId = share.getInt("userid", 0);
        sessionId = share.getString("sessionid", "");
        findUserJoinedGroupPresenter.request(userId, sessionId);
    }
}
