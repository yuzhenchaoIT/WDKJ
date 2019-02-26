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
import com.wd.tech.bean.FindGroupByid;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.AddGroupPersenter;
import com.wd.tech.presenter.FindGroupPersenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddGroupActivity extends WDActivity {

    @BindView(R.id.add_Group_back)
    ImageView addGroupBack;
    @BindView(R.id.text_send_group)
    TextView textSendGroup;
    @BindView(R.id.group_head_lt)
    ImageView groupHeadLt;
    @BindView(R.id.group_ming_lt)
    TextView groupMingLt;
    @BindView(R.id.group_qian_lt)
    TextView groupQianLt;
    @BindView(R.id.edit_search_group)
    EditText editSearchGroup;
    private FindGroupByid findGroupByid;
    private FindGroupPersenter findGroupPersenter;
    private String sessionId;
    private int userId;
    private User bean;
    private String message;
    private int userIdg;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_group;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Intent intent = getIntent();
        findGroupByid = (FindGroupByid) intent.getSerializableExtra("addGroup");
        Glide.with(AddGroupActivity.this).load(findGroupByid.getGroupImage()).into(groupHeadLt);
        groupMingLt.setText(findGroupByid.getGroupName());
        groupQianLt.setText(findGroupByid.getDescription());
        userIdg=findGroupByid.getGroupId();
        bean = WDActivity.getUser(this);
    }

    @Override
    protected void destoryData() {

    }


    @OnClick({R.id.add_Group_back, R.id.text_send_group})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_Group_back:
                finish();
                break;
            case R.id.text_send_group:
                String remark=editSearchGroup.getText().toString();
                findGroupPersenter = new FindGroupPersenter(new Groupadd());
                if (bean != null) {
                    sessionId = bean.getSessionId();
                    userId = bean.getUserId();
                    findGroupPersenter.request(userId,sessionId,userIdg,remark);

                }
                Log.i("dd", "onViewClicked: "+userIdg+""+remark);
                finish();
                break;
        }
    }

    private class Groupadd implements DataCall<Result> {
        @Override
        public void success(Result data) {
            message = data.getMessage();
            Log.i("ee", "success: "+data.getMessage());
            Toast.makeText(AddGroupActivity.this,message,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
