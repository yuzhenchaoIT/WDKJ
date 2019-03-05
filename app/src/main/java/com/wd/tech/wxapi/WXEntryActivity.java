package com.wd.tech.wxapi;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wd.tech.R;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.dao.DaoMaster;
import com.wd.tech.dao.DaoSession;
import com.wd.tech.dao.UserDao;
import com.wd.tech.presenter.WXPresenter;
import com.wd.tech.view.LoginActivity;

public class WXEntryActivity extends WDActivity implements IWXAPIEventHandler {

    private IWXAPI api;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_wxentry;
    }

    @Override
    protected void initView() {
        getSwipeBackLayout();
        api = WXAPIFactory.createWXAPI(this, "wx4c96b6b8da494224");
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void destoryData() {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent,this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        finish();
    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if (baseResp instanceof SendAuth.Resp) {
                    SendAuth.Resp sendAuthResp = (SendAuth.Resp) baseResp;
                    try {
                        String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                        WXPresenter wxLoginPresenter = new WXPresenter(new WxLogin());
                        wxLoginPresenter.request(versionName,sendAuthResp.code);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if (baseResp instanceof SendAuth.Resp) {}
                Log.e("flag", "-----授权取消:");
                Toast.makeText(this, "授权取消:", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                if (baseResp instanceof SendAuth.Resp) {}
                Log.e("flag", "-----授权失败:");
                Toast.makeText(this, "授权失败:", Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                break;
        }
    }
    //实现微信登录接口
    class WxLogin implements DataCall<Result<User>> {

        @Override
        public void success(Result<User> data) {
            if (data.getStatus().equals("0000")){
                User result = data.getResult();
                //添加数据库
                DaoSession daoSession = DaoMaster.newDevSession(WXEntryActivity.this, UserDao.TABLENAME);
                UserDao userDao = daoSession.getUserDao();
                result.setStatu("1");
                userDao.insertOrReplace(result);
                finish();
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
