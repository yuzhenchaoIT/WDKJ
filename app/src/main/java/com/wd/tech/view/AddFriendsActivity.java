package com.wd.tech.view;

import android.content.Intent;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.bean.FindGroupByid;
import com.wd.tech.bean.FindUserByPhone;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.AddGroupPersenter;
import com.wd.tech.presenter.PhoneUserPersenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddFriendsActivity extends WDActivity {

    @BindView(R.id.add_friend_back)
    ImageView addFriendBack;
    @BindView(R.id.find_r)
    TextView findR;
    @BindView(R.id.find_r_ok)
    View findROk;
    @BindView(R.id.find_relate_ren)
    RelativeLayout findRelateRen;
    @BindView(R.id.find_q)
    TextView findQ;
    @BindView(R.id.find_q_ok)
    View findQOk;
    @BindView(R.id.find_relate_qun)
    RelativeLayout findRelateQun;
    @BindView(R.id.find_search)
    ImageView findSearch;
    @BindView(R.id.find_search_edit)
    EditText findSearchEdit;
    @BindView(R.id.find_r_icon)
    SimpleDraweeView findRIcon;
    @BindView(R.id.find_r_name)
    TextView findRName;
    @BindView(R.id.find_r_next)
    ImageView findRNext;
    @BindView(R.id.find_r_relative)
    RelativeLayout findRRelative;
    @BindView(R.id.find_q_icon)
    SimpleDraweeView findQIcon;
    @BindView(R.id.find_q_name)
    TextView findQName;
    @BindView(R.id.find_q_next)
    ImageView findQNext;
    @BindView(R.id.find_q_relative)
    RelativeLayout findQRelative;
    @BindView(R.id.find_not)
    TextView findNot;




    private PhoneUserPersenter userPersenter;
    private AddGroupPersenter addGroupPersenter;
    private String sessionId;
    private int userId;
    private User bean;
    private FindUserByPhone userbypehone;
    private Editable searchEdit;
    private FindGroupByid findGroupByid;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_friends;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        findRNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddFriendsActivity.this,FindUserActivity.class);
                intent.putExtra("name",userbypehone.getNickName());
                intent.putExtra("headPic",userbypehone.getHeadPic());
                intent.putExtra("phone",userbypehone.getPhone());
                intent.putExtra("qian",userbypehone.getSignature());
                intent.putExtra("userids",userbypehone.getUserId());
                startActivity(intent);
            }
        });
        findQNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_q = new Intent(AddFriendsActivity.this,FindGroupActivity.class);
                intent_q.putExtra("findGroup",findGroupByid);
                startActivity(intent_q);
            }
        });
        }

    private class UserPhone implements DataCall<Result<FindUserByPhone>> {


        @Override
        public void success(Result<FindUserByPhone> data) {
            if (data.getStatus().equals("0000")) {
                findRRelative.setVisibility(View.VISIBLE);
                findQRelative.setVisibility(View.GONE);
                findNot.setVisibility(View.GONE);
                userbypehone = data.getResult();
                findRName.setText(userbypehone.getNickName());
                findRIcon.setImageURI(userbypehone.getHeadPic());
                Log.i("aa", "success: "+userbypehone.getNickName());
                }else {
                findRRelative.setVisibility(View.GONE);
                findQRelative.setVisibility(View.GONE);
                findNot.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }


   @Override
    protected void destoryData() {

    }

    @OnClick({R.id.add_friend_back, R.id.find_relate_ren, R.id.find_relate_qun, R.id.find_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_friend_back:

                break;
            case R.id.find_relate_ren:
                findROk.setVisibility(View.VISIBLE);
                findQOk.setVisibility(View.GONE);
                findRRelative.setVisibility(View.GONE);
                findQRelative.setVisibility(View.GONE);
                break;
            case R.id.find_relate_qun:
                findROk.setVisibility(View.GONE);
                findQOk.setVisibility(View.VISIBLE);
                findRRelative.setVisibility(View.GONE);
                findQRelative.setVisibility(View.GONE);
                break;
            case R.id.find_search:
                searchEdit = findSearchEdit.getText();
                if (findROk.getVisibility()==View.VISIBLE){
                    userPersenter = new PhoneUserPersenter(new UserPhone());
                    bean = WDActivity.getUser(this);
                    if (bean != null) {
                        sessionId = bean.getSessionId();
                        userId = bean.getUserId();
                        userPersenter.request(userId,sessionId,searchEdit.toString());

                    }
                }else if (findQOk.getVisibility()==View.VISIBLE){
                    addGroupPersenter=new AddGroupPersenter(new AddGroup());
                    bean = WDActivity.getUser(this);
                    if (bean != null) {
                        sessionId = bean.getSessionId();
                        userId = bean.getUserId();
                        addGroupPersenter.request(userId,sessionId,Integer.parseInt(searchEdit.toString()));

                    }
                }

                break;
        }
    }

    private class AddGroup implements DataCall<Result<FindGroupByid>> {
        @Override
        public void success(Result<FindGroupByid> data) {
            if (data.getStatus().equals("0000")) {
                findRRelative.setVisibility(View.GONE);
                findQRelative.setVisibility(View.VISIBLE);
                findNot.setVisibility(View.GONE);
                findGroupByid = data.getResult();
                findQName.setText(findGroupByid.getGroupName());
                findQIcon.setImageURI(findGroupByid.getGroupImage());
                Log.i("aa", "success: "+userbypehone.getNickName());
            }else {
                findQRelative.setVisibility(View.GONE);
                findNot.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void fail(ApiException e) {
            findQRelative.setVisibility(View.GONE);
            findNot.setVisibility(View.VISIBLE);
        }
    }
}
