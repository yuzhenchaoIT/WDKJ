package com.wd.tech.view;

import android.content.Intent;

import android.view.View;
import android.widget.TextView;

import com.hyphenate.chat.EMConversation;
import com.wd.tech.R;

import com.hyphenate.easeui.ui.EaseChatFragment;

import com.wd.tech.bean.FriendInfoList;
import com.wd.tech.core.WDActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ChatActivity extends WDActivity {
    @BindView(R.id.chat_name)
    TextView chatName;
    private FriendInfoList friendInfoList;
    private List<EMConversation> friendsationList;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Intent intent = getIntent();

        friendInfoList = (FriendInfoList) intent.getSerializableExtra("friendInfoList");
        chatName.setText(friendInfoList.getNickName());
        EaseChatFragment chatFragment = new EaseChatFragment();
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.hx_ok, chatFragment).commit();
    }

    @Override
    protected void destoryData() {

    }
    @OnClick({R.id.chat_back, R.id.chat_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.chat_back:
                finish();
                break;
            case R.id.chat_setting:
                Intent intent = new Intent(ChatActivity.this, ChatSettingsActivity.class);
                intent.putExtra("friendInfoList",friendInfoList);
                startActivity(intent);
                break;
        }
    }
}
