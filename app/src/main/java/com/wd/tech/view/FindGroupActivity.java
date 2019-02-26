package com.wd.tech.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.bean.FindGroupByid;
import com.wd.tech.core.WDActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindGroupActivity extends WDActivity {
    @BindView(R.id.image_back_addgroup)
    ImageView imageBackAddgroup;
    @BindView(R.id.image_addgroup)
    SimpleDraweeView imageAddgroup;
    @BindView(R.id.text_peoplesum)
    TextView textPeoplesum;
    @BindView(R.id.text_addGroup)
    TextView textAddGroup;
    @BindView(R.id.btn_addGroup)
    Button btnAddGroup;
    private FindGroupByid findGroupByid;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_find_group;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Intent intent = getIntent();
        findGroupByid = (FindGroupByid) intent.getSerializableExtra("findGroup");
        imageAddgroup.setImageURI(findGroupByid.getGroupImage());
        textPeoplesum.setText(findGroupByid.getMaxCount()+"");
        textAddGroup.setText(findGroupByid.getDescription());
    }

    @Override
    protected void destoryData() {

    }

    @OnClick({R.id.image_back_addgroup, R.id.btn_addGroup})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_back_addgroup:
                finish();
                break;
            case R.id.btn_addGroup:
                Intent intent_q = new Intent(FindGroupActivity.this,AddGroupActivity.class);
                intent_q.putExtra("addGroup",findGroupByid);
                startActivity(intent_q);
                break;
        }
    }
}
