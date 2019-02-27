package com.wd.tech.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.library.AutoFlowLayout;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.wd.tech.R;
import com.wd.tech.adapter.DetailRecommendAdapter;
import com.wd.tech.bean.AllInfoPlateBean;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.details.InforDetailsBean;
import com.wd.tech.bean.details.InformationListBean;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.InforDetailsPresenter;
import com.wd.tech.util.DateUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 详情页面
 *
 * @author lmx
 * @date 2019/2/24
 */
public class InforDetailsActivity extends WDActivity {

    @BindView(R.id.infor_details_title)
    TextView mInforDetailsTitle;
    @BindView(R.id.infor_details_content)
    TextView mInforDetailsContent;
    @BindView(R.id.infor_details_time)
    TextView mInforDetailsTime;
    @BindView(R.id.infor_details_source)
    TextView mInforDetailsSource;
    @BindView(R.id.infor_details_back)
    ImageView mInforDetailsBack;
    @BindView(R.id.infor_details_comment)
    EditText mInforDetailsComment;
    @BindView(R.id.infor_details_comment_img)
    ImageView mInforDetailsCommentImg;
    @BindView(R.id.infor_details_comment_txt)
    TextView mInforDetailsCommentTxt;
    @BindView(R.id.infor_details_zan_img)
    ImageView mInforDetailsZanImg;
    @BindView(R.id.infor_details_zan_txt)
    TextView mInforDetailsZanTxt;
    @BindView(R.id.infor_details_coll_img)
    ImageView mInforDetailsCollImg;
    @BindView(R.id.infor_details_share_img)
    ImageView mInforDetailsShareImg;
    @BindView(R.id.infor_details_share_txt)
    TextView mInforDetailsShareTxt;
    @BindView(R.id.infor_details_ll_no_pay)
    LinearLayout mInforDetailsLlNoPay;
    @BindView(R.id.infor_details_ll)
    LinearLayout mInforDetailsLl;
    @BindView(R.id.infor_details_afl)
    AutoFlowLayout mInforAflt;
    @BindView(R.id.infor_details_recom_recy)
    RecyclerView mIfordrr;
    private TextView mInforAfltZi;
    //p层
    private InforDetailsPresenter mDetailsPresenter = new InforDetailsPresenter(new DetailsCall());
    private URLImageParser mImageGetter;
    private InforDetailsBean mInforDetailsBean;
    //线性
    private LinearLayoutManager mManager = new LinearLayoutManager(this);
    //适配器
    private DetailRecommendAdapter mDetailRecommendA;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_infor_details;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);


        ImageLoader imageLoader = ImageLoader.getInstance();//ImageLoader需要实例化
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));

        mImageGetter = new URLImageParser(mInforDetailsContent);

        //获取条目id
        int homeListId = Integer.parseInt(getIntent().getStringExtra("homeListId"));
        mDetailsPresenter.request(18, "15320748258726", homeListId);


        mDetailRecommendA = new DetailRecommendAdapter(getBaseContext());
        mManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        mIfordrr.setLayoutManager(mManager);
        mIfordrr.setAdapter(mDetailRecommendA);


    }


    @OnClick({R.id.infor_details_back, R.id.infor_details_comment, R.id.infor_details_comment_img, R.id.infor_details_zan_img, R.id.infor_details_coll_img, R.id.infor_details_share_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.infor_details_back:
                finish();
                break;
            //评论输入框
            case R.id.infor_details_comment:
                break;
            //评论图片
            case R.id.infor_details_comment_img:
                break;
            //点赞图片
            case R.id.infor_details_zan_img:
                break;
            //收藏图片
            case R.id.infor_details_coll_img:
                break;
            //分享图片
            case R.id.infor_details_share_img:
                break;
        }
    }


    /**
     * 富文本里面的图片解决方法
     */
    public class URLImageParser implements Html.ImageGetter {
        TextView mTextView;

        public URLImageParser(TextView textView) {
            this.mTextView = textView;
        }

        @Override
        public Drawable getDrawable(String source) {
            final URLDrawable urlDrawable = new URLDrawable();
            ImageLoader.getInstance().loadImage(source, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    urlDrawable.bitmap = loadedImage;
                    urlDrawable.setBounds(loadedImage.getWidth(), loadedImage.getHeight(), loadedImage.getWidth(), loadedImage.getHeight());
                    mTextView.invalidate();
                    mTextView.setText(mTextView.getText());
                }
            });
            return urlDrawable;
        }
    }

    public class URLDrawable extends BitmapDrawable {
        protected Bitmap bitmap;

        @Override
        public void draw(Canvas canvas) {
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, 0, 0, getPaint());
            }
        }
    }


    class DetailsCall implements DataCall<Result> {

        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")) {
                mInforDetailsBean = (InforDetailsBean) data.getResult();
                List<AllInfoPlateBean> plateBeans = mInforDetailsBean.getPlate();
                List<InformationListBean> informationList = mInforDetailsBean.getInformationList();
                mInforDetailsTitle.setText(mInforDetailsBean.getTitle());
                try {
                    mInforDetailsTime.setText(DateUtils.dateFormat(new Date(mInforDetailsBean.getReleaseTime()), DateUtils.MINUTE_PATTERN));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mInforDetailsSource.setText(mInforDetailsBean.getSource());
                //字段判断 当前用户是否有阅读权限 1=是，2=否
                if (mInforDetailsBean.getReadPower() == 2) {
                    mInforDetailsLlNoPay.setVisibility(View.VISIBLE);
                    mInforDetailsLl.setVisibility(View.GONE);
                } else {
                    mInforDetailsContent.setText(Html.fromHtml(mInforDetailsBean.getContent(), mImageGetter, null));
                    //展示该内容的类别
                    for (int i = 0; i < plateBeans.size(); i++) {
                        final View item = View.inflate(InforDetailsActivity.this, R.layout.item_search_infor_deatail, null);
                        mInforAfltZi = item.findViewById(R.id.infor_details_afl_zi);
                        mInforAfltZi.setText(plateBeans.get(i).getName());
                        mInforAflt.addView(item);
                    }

                }
                mInforDetailsCommentTxt.setText(mInforDetailsBean.getComment() + "");
                mInforDetailsZanTxt.setText(mInforDetailsBean.getPraise() + "");
                mInforDetailsShareTxt.setText(mInforDetailsBean.getShare() + "");

                //详情推荐列表的展示
                mDetailRecommendA.addItem(informationList);
                mDetailRecommendA.notifyDataSetChanged();

            } else {
                Toast.makeText(getBaseContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void fail(ApiException e) {
            Toast.makeText(getBaseContext(), "网络异常", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void destoryData() {
        mDetailsPresenter.unBind();
    }

}
