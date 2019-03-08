package com.wd.tech.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;

import com.wd.tech.bean.Result;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.WDApplication;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class GroupDetailsSettingsActivity extends WDActivity {


    @BindView(R.id.group_details_setting_back)
    ImageView groupDetailsSettingBack;
    @BindView(R.id.group_details_setting_name)
    TextView groupDetailsSettingName;
    @BindView(R.id.group_details_setting_icon)
    SimpleDraweeView groupDetailsSettingIcon;
    @BindView(R.id.group_details_setting_num)
    TextView groupDetailsSettingNum;
    @BindView(R.id.group_details_setting_yes)
    Button groupDetailsSettingYes;
    private int flag = 2;//判断是否是群成员 默认不是
    private int userid;
    private String session1d;
    private int groupId;
    private String name;
    private String icon;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_details_settings;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        name = intent.getStringExtra("groupName");
        groupId = intent.getIntExtra("groupId",0);
        icon = intent.getStringExtra("icon");
        SharedPreferences share = WDApplication.getShare();
        userid = share.getInt("userid", 0);
        session1d = share.getString("sessionid", "");
        Glide.with(GroupDetailsSettingsActivity.this).load(icon).into(groupDetailsSettingIcon);
        groupDetailsSettingName.setText(name);

    }

    @Override
    protected void destoryData() {

    }
    @OnClick({R.id.group_details_setting_back, R.id.group_details_setting_icon, R.id.group_details_setting_member, R.id.group_details_setting_intro, R.id.group_details_setting_message, R.id.group_details_setting_manage, R.id.group_details_setting_chat, R.id.group_details_setting_yes})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.group_details_setting_back:
                finish();
                break;
            case R.id.group_details_setting_icon:

                break;
            case R.id.group_details_setting_member:

                break;
            case R.id.group_details_setting_intro:
                break;
            case R.id.group_details_setting_message:
//                Intent intent_g = new Intent(GroupDetailsSettingsActivity.this, GroupActivity.class);
//                startActivity(intent_g);
                break;
            case R.id.group_details_setting_manage:
//                Intent intent = new Intent(GroupDetailsSettingsActivity.this,GroupMemberActivity.class);
//                intent.putExtra("groupId",groupId);
//                startActivity(intent);
                break;
            case R.id.group_details_setting_chat:
                break;
            case R.id.group_details_setting_yes:

                break;
        }
    }


    class WhetherInGroup implements DataCall<Result> {

        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")) {
               // flag = data.getFlag();

            }

        }

        @Override
        public void fail(ApiException e) {

        }


    }


}

