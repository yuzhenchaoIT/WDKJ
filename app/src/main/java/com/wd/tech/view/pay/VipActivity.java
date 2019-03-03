package com.wd.tech.view.pay;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wd.tech.R;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.bean.details.VipGoodsBean;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.InforMation.BuyVipPresenter;
import com.wd.tech.presenter.InforMation.VipGoodsPresenter;
import com.wd.tech.presenter.InforMation.WxPayPresenter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 会员开通页面
 *
 * @author lmx
 * @date 2019/3/1
 */
public class VipActivity extends WDActivity {


    @BindView(R.id.activity_vip_main_back)
    ImageView mActivityVipMainBack;
    @BindView(R.id.year)
    RadioButton mYear;
    @BindView(R.id.quarter)
    RadioButton mQuarter;
    @BindView(R.id.month)
    RadioButton mMonth;
    @BindView(R.id.week)
    RadioButton mWeek;
    @BindView(R.id.activity_vip_main_money)
    TextView mActivityVipMainMoney;
    @BindView(R.id.chose_wx)
    RadioButton mChoseWx;
    @BindView(R.id.chose_zfb)
    RadioButton mChoseZfb;
    @BindView(R.id.activity_vip_main_once_vip)
    Button mActivityVipMainOnceVip;
    @BindView(R.id.activity_vip_main)
    RelativeLayout mActivityVipMain;
    //p层
    private VipGoodsPresenter mVipGoodsPresenter = new VipGoodsPresenter(new VipGoodsCall());
    private BuyVipPresenter mBuyVipPresenter = new BuyVipPresenter(new BuyVipCall());
    private WxPayPresenter mWxPayPresenter = new WxPayPresenter(new WxPayCall());
    private List<VipGoodsBean> mBeanList;
    private User user;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vip;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        user = WDActivity.getUser(this);

        mVipGoodsPresenter.request();

    }

    @OnClick({R.id.activity_vip_main_back, R.id.year, R.id.quarter,
            R.id.month, R.id.week, R.id.chose_wx, R.id.chose_zfb,
            R.id.activity_vip_main_once_vip})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_vip_main_back:
                finish();
                break;
            case R.id.year:
                mActivityVipMainMoney.setText(mBeanList.get(3).getPrice() + "");
                break;
            case R.id.quarter:
                mActivityVipMainMoney.setText(mBeanList.get(2).getPrice() + "");
                break;
            case R.id.month:
                mActivityVipMainMoney.setText(mBeanList.get(1).getPrice() + "");
                break;
            case R.id.week:
                mActivityVipMainMoney.setText(mBeanList.get(0).getPrice() + "");
                break;
            case R.id.chose_wx:
                break;
            case R.id.chose_zfb:
                break;
            case R.id.activity_vip_main_once_vip:
                if (mYear.isChecked()) {
                    String md5 = MD5(user.getUserId() + "" + mBeanList.get(3).getCommodityId() + "tech");
                    mBuyVipPresenter.request(user.getUserId(), user.getSessionId(), mBeanList.get(3).getCommodityId(), md5);
                    Log.i("xzm--------------", user.getUserId() + "-----" + mBeanList.get(3).getCommodityId());
                } else if (mQuarter.isChecked()) {
                    String md5 = MD5(user.getUserId() + "" + mBeanList.get(2).getCommodityId() + "tech");
                    mBuyVipPresenter.request(user.getUserId(), user.getSessionId(), mBeanList.get(2).getCommodityId(), md5);
                    Log.i("xzm--------------", user.getUserId() + "-----" + mBeanList.get(2).getCommodityId());
                } else if (mMonth.isChecked()) {
                    String md5 = MD5(user.getUserId() + "" + mBeanList.get(1).getCommodityId() + "tech");
                    mBuyVipPresenter.request(user.getUserId(), user.getSessionId(), mBeanList.get(1).getCommodityId(), md5);
                    Log.i("xzm--------------", user.getUserId() + "-----" + mBeanList.get(1).getCommodityId());
                } else if (mWeek.isChecked()) {
                    String md5 = MD5(user.getUserId() + "" + mBeanList.get(0).getCommodityId() + "tech");
                    mBuyVipPresenter.request(user.getUserId(), user.getSessionId(), mBeanList.get(0).getCommodityId(), md5);
                    Log.i("xzm--------------", user.getUserId() + "-----" + mBeanList.get(0).getCommodityId());
                } else {
                    Toast.makeText(getBaseContext(), "至少选择一种vip", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void destoryData() {

    }

    class VipGoodsCall implements DataCall<Result<List<VipGoodsBean>>> {

        @Override
        public void success(Result<List<VipGoodsBean>> data) {
            if (data.getStatus().equals("0000")) {
                mBeanList = data.getResult();
                Toast.makeText(getBaseContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getBaseContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void fail(ApiException e) {
            Toast.makeText(getBaseContext(), "网络异常", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 下单是否成功
     */
    class BuyVipCall implements DataCall<Result> {

        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")) {
                Toast.makeText(getBaseContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
                //下单成功  拿到 getOrderId 去支付
                mWxPayPresenter.request(user.getUserId(), user.getSessionId(), data.getOrderId(), 1);
            } else {
                Toast.makeText(getBaseContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void fail(ApiException e) {
            Toast.makeText(getBaseContext(), "网络异常", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 微信支付是否成功
     */
    class WxPayCall implements DataCall<Result> {

        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")) {
//                Toast.makeText(getBaseContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
                IWXAPI api = WXAPIFactory.createWXAPI(VipActivity.this, "wx4c96b6b8da494224", false);//填写自己的APPID
                api.registerApp("wx4c96b6b8da494224");//填写自己的APPID，注册本身APP
                PayReq req = new PayReq();//PayReq就是订单信息对象
                //给req对象赋值
                req.appId = data.getAppId();//APPID
                req.partnerId = data.getPartnerId();//    商户号
                req.prepayId = data.getPrepayId();//  预付款ID
                req.nonceStr = data.getNonceStr();//随机数
                req.timeStamp = data.getTimeStamp();//时间戳
                req.packageValue = data.getPackageValue();//固定值Sign=WXPay
                req.sign = data.getSign();//签名
                api.sendReq(req);//将订单信息对象发送给微信服务器，即发送支付请求
            } else {
                Toast.makeText(getBaseContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void fail(ApiException e) {
            Toast.makeText(getBaseContext(), "网络异常" + e, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * MD5加密
     *
     * @param sourceStr
     * @return
     */
    public static String MD5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return result;
    }


}
