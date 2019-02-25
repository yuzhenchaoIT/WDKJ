package com.wd.tech.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.bean.User;
import com.wd.tech.core.WDActivity;
import com.wd.tech.presenter.AddFriendPersenter;



import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindUserActivity extends WDActivity {


    @BindView(R.id.image_head)
    SimpleDraweeView imageHead;
    @BindView(R.id.user_niname)
    TextView userNiname;
    @BindView(R.id.user_qianmin)
    TextView userQianmin;
    @BindView(R.id.user_sex)
    TextView userSex;
    @BindView(R.id.user_phone)
    TextView userPhone;
    @BindView(R.id.btn_r_add)
    Button btnRAdd;
    private AddFriendPersenter addPersenter;
    private String sessionId;
    private int userId;
    private User bean;
    private String name;
    private String headPic;
    private String phone;
    private String qian;
    private int userIds;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_find_user;
    }

    @Override
    protected void initView() {
        Fresco.initialize(this);
        ButterKnife.bind(this);
        Intent intent=getIntent();
       Bundle bundle=intent.getExtras();
        name = bundle.getString("name");
        headPic = bundle.getString("headPic");
        phone = bundle.getString("phone");
        qian=bundle.getString("qian");
        userIds=bundle.getInt("userids");
       imageHead.setImageURI(headPic);
       Log.i("bb", "initView: "+Uri.parse(headPic));
        userNiname.setText(name);
        userPhone.setText(phone);
        userQianmin.setText(qian);
        btnRAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FindUserActivity.this,AddListActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("headPic",headPic);
                intent.putExtra("phone",phone);
                intent.putExtra("qian",qian);
                intent.putExtra("userids",userIds);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void destoryData() {

    }

    @OnClick(R.id.btn_r_add)
    public void onViewClicked() {
    }
}
