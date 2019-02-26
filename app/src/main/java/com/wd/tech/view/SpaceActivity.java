package com.wd.tech.view;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.core.WDActivity;

public class SpaceActivity extends WDActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_space;
    }

    @Override
    protected void initView() {
        int userid = getIntent().getIntExtra("userid", 0);
        Toast.makeText(this, userid+"", Toast.LENGTH_SHORT).show();
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) findViewById(R.id.space_top);
        simpleDraweeView.setImageURI(Uri.parse("http://thirdwx.qlogo.cn/mmopen/vi_32/LW1875Nbmzp2cD4rTgibVNU2u4u9icpaE3IPorAcNJuL1w6vykhib0PZZeH9JABm7vorNkPoAia7dxiaxmGRXjbC8ZQ/132"));
    }
    @Override
    protected void destoryData() {

    }
}
