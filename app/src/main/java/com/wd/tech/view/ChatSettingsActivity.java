package com.wd.tech.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;

import com.wd.tech.bean.FriendInfoList;
import com.wd.tech.bean.Result;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.WDApplication;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.DeleteChatRecordPresenter;
import com.wd.tech.presenter.DeleteFriendRelationPresenter;
import com.wd.tech.util.UIUtils;


import butterknife.BindView;
import butterknife.OnClick;

public class ChatSettingsActivity extends WDActivity {
    @BindView(R.id.chat_settings_icon)
    SimpleDraweeView chatSettingsIcon;
    @BindView(R.id.chat_settings_nickname)
    TextView chatSettingsNickname;
    @BindView(R.id.chat_settings_name)
    TextView chatSettingsName;
    @BindView(R.id.chat_settings_group)
    RelativeLayout chatSettingsGroup;
    @BindView(R.id.chat_settings_chatting_records)
    RelativeLayout chatSettingsChattingRecords;
    @BindView(R.id.chat_settings_clear_records)
    RelativeLayout chatSettingsClearRecords;
    @BindView(R.id.chat_settings_delete)
    Button chatSettingsDelete;
    private PopupWindow window;
    private PopupWindow clearWindow;
    private View inflate;
    private FriendInfoList friendInfoList;
    private DeleteFriendRelationPresenter deleteFriendRelationPresenter;
    private int userid;
    private String session1d;
    private View parent;
    private DeleteChatRecordPresenter deleteChatRecordPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat_settings;
    }

    @Override
    protected void initView() {
        parent = View.inflate(this,R.layout.activity_chat_settings,null);
        deleteChatRecordPresenter = new DeleteChatRecordPresenter(new DeleteChatRecord());
        Intent intent = getIntent();
        friendInfoList = (FriendInfoList) intent.getSerializableExtra("friendInfoList");
        chatSettingsIcon.setImageURI(friendInfoList.getHeadPic());
        chatSettingsName.setText(friendInfoList.getNickName());
        chatSettingsNickname.setText(friendInfoList.getRemarkName());
        deleteFriendRelationPresenter = new DeleteFriendRelationPresenter(new DeleteFriendRelation());
        SharedPreferences share = WDApplication.getShare();
        userid = share.getInt("userid", 0);
        session1d = share.getString("sessionid", "");

        getShow();

    }

    @Override
    protected void destoryData() {
        deleteFriendRelationPresenter.unBind();
        deleteChatRecordPresenter.unBind();
    }


    @OnClick({R.id.chat_settings_back, R.id.chat_settings_icon, R.id.chat_settings_group, R.id.chat_settings_chatting_records, R.id.chat_settings_clear_records, R.id.chat_settings_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.chat_settings_back:
                finish();
                break;
            case R.id.chat_settings_icon:

                break;
            case R.id.chat_settings_group:
                Intent intent = new Intent(ChatSettingsActivity.this,CheckGroupActivity.class);
                intent.putExtra("friendId",friendInfoList.getFriendUid());
                startActivity(intent);
                break;
            case R.id.chat_settings_chatting_records:
                break;
            case R.id.chat_settings_clear_records:
                clearWindow.showAtLocation(parent, Gravity.BOTTOM,0,0);
                break;
            case R.id.chat_settings_delete:
                window.showAtLocation(parent, Gravity.BOTTOM,0,0);
                break;
        }

        }

    private void getShow(){
        inflate = View.inflate(this, R.layout.popu_delete_friend, null);
        window = new PopupWindow(inflate, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setTouchable(true);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        getWindow(inflate);
        View clearView = View.inflate(this, R.layout.popu_clear_chat, null);
        clearWindow = new PopupWindow(clearView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        clearWindow.setTouchable(true);
        clearWindow.setFocusable(true);
        clearWindow.setOutsideTouchable(true);
        clearWindow.setBackgroundDrawable(new BitmapDrawable());
        getClearWindow(clearView);
    }

    private void getClearWindow(View clearView) {
        LinearLayout popuClearChatOk= clearView.findViewById(R.id.popu_clear_chat_ok);
        LinearLayout popuClearChatNo= clearView.findViewById(R.id.popu_clear_chat_no);
        popuClearChatOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteChatRecordPresenter.request(userid,session1d,friendInfoList.getFriendUid());

            }
        });
        popuClearChatNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                window.dismiss();
            }
        });
    }

    private void getWindow(View inflate) {
        TextView popuDeleteMessage= inflate.findViewById(R.id.popu_delete_message);
        LinearLayout popuDeleteOk= inflate.findViewById(R.id.popu_delete_ok);
        LinearLayout popuDeleteNo= inflate.findViewById(R.id.popu_delete_no);
        popuDeleteMessage.setText("将联系人 "+friendInfoList.getRemarkName()+" 删除，同时删除与该联系人的聊天记录" );
        popuDeleteOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFriendRelationPresenter.request(userid,session1d,friendInfoList.getFriendUid());
            }
        });
        popuDeleteNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                window.dismiss();
            }
        });
    }
    class DeleteFriendRelation implements DataCall<Result> {

        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")){
                window.dismiss();
                UIUtils.showToastSafe(data.getMessage());
                finish();
            }
        }

        @Override
        public void fail(ApiException e) {

        }

        }
    class DeleteChatRecord implements DataCall<Result>{

        @Override
        public void success(Result data) {
            window.dismiss();
            if (data.getStatus().equals("0000")){

            }
            UIUtils.showToastSafe(data.getMessage());
        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
