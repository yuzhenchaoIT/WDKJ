package com.wd.tech.view.pay;

import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.bean.UserIntegral;
import com.wd.tech.bean.details.InforDetailsBean;
import com.wd.tech.core.PayExchangePopWindowF;
import com.wd.tech.core.PayExchangePopWindowT;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.myview.TaskActivity;
import com.wd.tech.presenter.InforDetailsPresenter;
import com.wd.tech.presenter.InforMation.PayByIntegralPresenter;
import com.wd.tech.presenter.IntegralPresenter;
import com.wd.tech.util.DateUtils;
import com.wd.tech.view.InforDetailsActivity;

import java.text.ParseException;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 积分兑换页面
 *
 * @author lmx
 * @date 2019/3/1
 */
public class PointsActivity extends WDActivity {


    @BindView(R.id.points_back)
    ImageView mPointsBack;
    @BindView(R.id.points_item_img)
    SimpleDraweeView mPointsItemImg;
    @BindView(R.id.points_item_title)
    TextView mPointsItemTitle;
    @BindView(R.id.points_item_summary)
    TextView mPointsItemSummary;
    @BindView(R.id.points_item_source)
    TextView mPointsItemSource;
    @BindView(R.id.points_item_time)
    TextView mPointsItemTime;
    @BindView(R.id.points_item_img_coll)
    ImageView mPointsItemImgColl;
    @BindView(R.id.points_item_txt_coll)
    TextView mPointsItemTxtColl;
    @BindView(R.id.points_item_img_share)
    ImageView mPointsItemImgShare;
    @BindView(R.id.points_item_txt_share)
    TextView mPointsItemTxtShare;
    @BindView(R.id.points_need_jf)
    TextView mPointsNeedJf;
    @BindView(R.id.points_my_jf)
    TextView mPointsMyJf;
    @BindView(R.id.points_once_exchange)
    TextView mPointsOnceExchange;
    private int mDataId;
    //p层
    private InforDetailsPresenter mDetailsPresenter = new InforDetailsPresenter(new Details2Call());
    //获得积分
    private IntegralPresenter mIntegralPresenter = new IntegralPresenter(new IntegralCall());
    private PayByIntegralPresenter mPayByIntegralPresenter = new PayByIntegralPresenter(new PayByIntegralCall());
    private User user;
    private int mIntegralCost;
    private PayExchangePopWindowT mPayExchangePopWindowT;
    private PayExchangePopWindowF mPayExchangePopWindowF;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_points;
    }

    @Override
    protected void initView() {

        ButterKnife.bind(this);

        user = WDActivity.getUser(this);

        mDataId = Integer.parseInt(getIntent().getStringExtra("DataId"));
        //获取当前条目的数据
        mDetailsPresenter.request(user.getUserId(), user.getSessionId(), mDataId);
        //积分请求接口
        mIntegralPresenter.request(user.getUserId(), user.getSessionId());

    }


    @OnClick({R.id.points_back, R.id.points_item_img_coll,
            R.id.points_item_img_share, R.id.points_once_exchange})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.points_back:
                finish();
                break;
            case R.id.points_item_img_coll:
                Toast.makeText(getBaseContext(), "本页面当前版本不支持收藏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.points_item_img_share:
                Toast.makeText(getBaseContext(), "本页面当前版本不支持分享", Toast.LENGTH_SHORT).show();
                break;
            case R.id.points_once_exchange:
                //积分请求
                mPayByIntegralPresenter.request(user.getUserId(), user.getSessionId(), mDataId, mIntegralCost);
                break;
        }
    }


    class Details2Call implements DataCall<Result> {

        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")) {
                InforDetailsBean mDetailsBean = (InforDetailsBean) data.getResult();
                mPointsItemImg.setImageURI(Uri.parse(mDetailsBean.getThumbnail()));
                mPointsItemTitle.setText(mDetailsBean.getTitle());
                mPointsItemSummary.setText(mDetailsBean.getSummary());
                mPointsItemSource.setText(mDetailsBean.getSource());
                try {
                    mPointsItemTime.setText(DateUtils.dateFormat(new Date(mDetailsBean.getReleaseTime()), DateUtils.MINUTE_PATTERN));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mPointsItemTxtColl.setText("50");
                mPointsItemTxtShare.setText("60");
                mIntegralCost = mDetailsBean.getIntegralCost();
                mPointsNeedJf.setText(mIntegralCost + "分");
            } else {
                Toast.makeText(getBaseContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void fail(ApiException e) {
            Toast.makeText(getBaseContext(), "网络异常", Toast.LENGTH_SHORT).show();
        }
    }

    //积分请求成功
    class IntegralCall implements DataCall<Result<UserIntegral>> {

        @Override
        public void success(Result<UserIntegral> data) {
            if (data.getStatus().equals("0000")) {
                UserIntegral result = data.getResult();
                mPointsMyJf.setText(result.getAmount() + "分");
            } else {
                Toast.makeText(getBaseContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }

    //积分兑换
    class PayByIntegralCall implements DataCall<Result> {

        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")) {
                mPayExchangePopWindowT = new PayExchangePopWindowT(PointsActivity.this, itemsOnClick);
                //显示窗口
                mPayExchangePopWindowT.showAtLocation(PointsActivity.this.findViewById(R.id.activity_points_main), Gravity.CENTER_VERTICAL, 0, 0); //设置layout在PopupWindow中显示的位置
            } else {
//                int mNeedJf = Integer.parseInt(mPointsNeedJf + "");
//                int mMyJf = Integer.parseInt(mPointsMyJf + "");
//                //兑换积分不足 调用弹出框
//                if (mNeedJf > mMyJf) {
//                    mPayExchangePopWindowF = new PayExchangePopWindowF(PointsActivity.this, itemsOnClick2);
//                    mPayExchangePopWindowF.showAtLocation(PointsActivity.this.findViewById(R.id.activity_points_main), Gravity.CENTER_VERTICAL, 0, 0);
//                }
                mPayExchangePopWindowF = new PayExchangePopWindowF(PointsActivity.this, itemsOnClick2);
                mPayExchangePopWindowF.showAtLocation(PointsActivity.this.findViewById(R.id.activity_points_main), Gravity.CENTER_VERTICAL, 0, 0);
                Toast.makeText(getBaseContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }


    //为弹出窗口实现监听类(兑换成功)
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            mPayExchangePopWindowT.dismiss();
            switch (v.getId()) {
                case R.id.pay_way_exchage_t_del:
                    mPayExchangePopWindowT.dismiss();
                    break;
                //继续阅读
                case R.id.pay_way_exchage_t_to_read:
                    Intent intent = new Intent(PointsActivity.this, InforDetailsActivity.class);
                    intent.putExtra("homeListId", mDataId + "");
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };
    //(兑换失败)赚积分
    private View.OnClickListener itemsOnClick2 = new View.OnClickListener() {
        public void onClick(View v) {
            mPayExchangePopWindowF.dismiss();
            switch (v.getId()) {
                case R.id.pay_way_exchage_f_del:
                    mPayExchangePopWindowF.dismiss();
                    break;
                case R.id.pay_way_exchage_f_cancel:
                    mPayExchangePopWindowF.dismiss();
                    break;
                //去做任务  赚积分
                case R.id.pay_way_exchage_f_earn:
                    startActivity(new Intent(PointsActivity.this, TaskActivity.class));
                    finish();
                    break;
            }
        }
    };


    //释放内存
    @Override
    protected void destoryData() {
        mDetailsPresenter.unBind();
        mIntegralPresenter.unBind();
        mPayByIntegralPresenter.unBind();
    }


}
