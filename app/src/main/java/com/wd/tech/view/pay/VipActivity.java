package com.wd.tech.view.pay;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wd.tech.R;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.bean.details.PayResult;
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
import java.util.Map;

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
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(VipActivity.this, "支付成功",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(VipActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(VipActivity.this, "支付失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(VipActivity.this, "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };


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
//                    Log.i("xzm--------------", user.getUserId() + "-----" + mBeanList.get(3).getCommodityId());
                } else if (mQuarter.isChecked()) {
                    String md5 = MD5(user.getUserId() + "" + mBeanList.get(2).getCommodityId() + "tech");
                    mBuyVipPresenter.request(user.getUserId(), user.getSessionId(), mBeanList.get(2).getCommodityId(), md5);
//                    Log.i("xzm--------------", user.getUserId() + "-----" + mBeanList.get(2).getCommodityId());
                } else if (mMonth.isChecked()) {
                    String md5 = MD5(user.getUserId() + "" + mBeanList.get(1).getCommodityId() + "tech");
                    mBuyVipPresenter.request(user.getUserId(), user.getSessionId(), mBeanList.get(1).getCommodityId(), md5);
//                    Log.i("xzm--------------", user.getUserId() + "-----" + mBeanList.get(1).getCommodityId());
                } else if (mWeek.isChecked()) {
                    String md5 = MD5(user.getUserId() + "" + mBeanList.get(0).getCommodityId() + "tech");
                    mBuyVipPresenter.request(user.getUserId(), user.getSessionId(), mBeanList.get(0).getCommodityId(), md5);
//                    Log.i("xzm--------------", user.getUserId() + "-----" + mBeanList.get(0).getCommodityId());
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
                if (mChoseWx.isChecked()) {
                    Toast.makeText(getBaseContext(), "选择了微信支付", Toast.LENGTH_SHORT).show();
                    //下单成功  拿到 getOrderId 去微信支付
                    mWxPayPresenter.request(user.getUserId(), user.getSessionId(), data.getOrderId(), 1);
                } else if (mChoseZfb.isChecked()) {
                    Toast.makeText(getBaseContext(), "选择了支付宝支付", Toast.LENGTH_SHORT).show();
                    mWxPayPresenter.request(user.getUserId(), user.getSessionId(), data.getOrderId(), 2);
                } else {
                    Toast.makeText(getBaseContext(), "至少选择一种支付方式", Toast.LENGTH_SHORT).show();
                }

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
    class WxPayCall implements DataCall<Result<String>> {

        private String payInfo;

        @Override
        public void success(final Result<String> data) {
            if (data.getStatus().equals("0000")) {

                if (mChoseWx.isChecked()) {
                    IWXAPI api = WXAPIFactory.createWXAPI(VipActivity.this, "wx4c96b6b8da494224", true);//填写自己的APPID
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
                } else if (mChoseZfb.isChecked()) {

                    payInfo = data.getResult();

                    Log.i("xzm123", data.getResult() + "");

                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            // 构造PayTask 对象
                            PayTask alipay = new PayTask(VipActivity.this);
                            // 调用支付接口，获取支付结果
                            Map<String, String> result = alipay.payV2(payInfo, true);

                            Message msg = new Message();
                            msg.what = SDK_PAY_FLAG;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }
                    };

                    // 必须异步调用
                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                }

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
