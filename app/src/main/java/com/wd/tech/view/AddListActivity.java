package com.wd.tech.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wd.tech.R;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.AddFriendPersenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddListActivity extends WDActivity {
    @BindView(R.id.add_friend_back)
    ImageView addFriendBack;
    @BindView(R.id.text_send)
    TextView textSend;
    @BindView(R.id.image_head_lt)
    ImageView imageHeadLt;
    @BindView(R.id.text_ming_lt)
    TextView textMingLt;
    @BindView(R.id.text_qian_lt)
    TextView textQianLt;
    @BindView(R.id.edit_search)
    EditText editSearch;
    private String name;
    private String headPic;
    private String phone;
    private String qian;
    private AddFriendPersenter addPersenter;
    private String sessionId;
    private int userId;
    private User bean;
    private String message;
    private int userIds;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_list;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        name = bundle.getString("name");
        headPic = bundle.getString("headPic");
        phone = bundle.getString("phone");
        qian = bundle.getString("qian");
        userIds=bundle.getInt("userids");
        Glide.with(AddListActivity.this).load(headPic).into(imageHeadLt);
        textMingLt.setText(name);
        textQianLt.setText(qian);
        bean = WDActivity.getUser(this);


    }

    @Override
    protected void destoryData() {

    }

    @OnClick({R.id.add_friend_back, R.id.text_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_friend_back:
                finish();
                break;
            case R.id.text_send:
                String remark=editSearch.getText().toString();
                addPersenter = new AddFriendPersenter(new Useradd());
                if (bean != null) {
                    sessionId = bean.getSessionId();
                    userId = bean.getUserId();
                    addPersenter.request(userId,sessionId,userIds,remark);

                }

                finish();
                break;
        }
    }


    private class Useradd implements DataCall<Result> {
        @Override
        public void success(Result data) {
            message = data.getMessage();
            Log.i("cc", "success: "+data.getMessage());
            Toast.makeText(AddListActivity.this,message,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
