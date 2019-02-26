package com.wd.tech.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wd.tech.R;
import com.wd.tech.core.WDActivity;

public class WXLoginActivity extends WDActivity {

    private IWXAPI iwxapi;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wx;
    }

    @Override
    protected void initView() {
        iwxapi = WXAPIFactory.createWXAPI(this, "wx4c96b6b8da494224", true);
        iwxapi.registerApp("wx4c96b6b8da494224");
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        iwxapi.sendReq(req);
        finish();
    }

    @Override
    protected void destoryData() {

    }
}
