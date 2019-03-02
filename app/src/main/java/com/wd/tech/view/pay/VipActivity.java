package com.wd.tech.view.pay;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wd.tech.R;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.bean.details.VipGoodsBean;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.InforMation.BuyVipPresenter;
import com.wd.tech.presenter.InforMation.VipGoodsPresenter;

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

    class BuyVipCall implements DataCall<Result> {

        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")) {
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

}
