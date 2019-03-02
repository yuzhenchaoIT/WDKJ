package com.wd.tech.wxapi;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wd.tech.R;
import com.wd.tech.core.WDActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 微信支付
 *
 * @author lmx
 * @date 2019/3/2
 */
public class WXPayEntryActivity extends WDActivity implements IWXAPIEventHandler {

    @BindView(R.id.txt_pay_result)
    TextView mTxtPayResult;
    private IWXAPI api;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wxpay_entry;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        api = WXAPIFactory.createWXAPI(this, "wx4c96b6b8da494224");
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    protected void destoryData() {

    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        String result = "";
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    //支付成功后的逻辑
                    result = "微信支付成功";
                    break;
                case BaseResp.ErrCode.ERR_COMM:
                    result = "微信支付失败";
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    result = "微信支付取消";
                    break;
                default:
                    result = "微信支付未知异常";
                    break;
            }
            mTxtPayResult.setText(result);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_tip);
            builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
            builder.show();
        }
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }


}
