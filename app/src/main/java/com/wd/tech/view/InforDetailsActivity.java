package com.wd.tech.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.wd.tech.R;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.details.InforDetailsBean;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.InforDetailsPresenter;
import com.wd.tech.util.DateUtils;

import java.text.ParseException;
import java.util.Date;

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
    //p层
    private InforDetailsPresenter detailsPresenter = new InforDetailsPresenter(new DetailsCall());
    private URLImageParser imageGetter;
    private InforDetailsBean inforDetailsBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_infor_details;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        ImageLoader imageLoader = ImageLoader.getInstance();//ImageLoader需要实例化
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));

        imageGetter = new URLImageParser(mInforDetailsContent);

        //获取条目id
        int homeListId = Integer.parseInt(getIntent().getStringExtra("homeListId"));
        detailsPresenter.request(18, "15320748258726", homeListId);
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
                inforDetailsBean = (InforDetailsBean) data.getResult();
                mInforDetailsTitle.setText(inforDetailsBean.getTitle());
                try {
                    mInforDetailsTime.setText(DateUtils.dateFormat(new Date(inforDetailsBean.getReleaseTime()), DateUtils.MINUTE_PATTERN));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mInforDetailsSource.setText(inforDetailsBean.getSource());
                //字段判断 当前用户是否有阅读权限 1=是，2=否
                if (inforDetailsBean.getReadPower() == 2) {
                    mInforDetailsLlNoPay.setVisibility(View.VISIBLE);
                    mInforDetailsContent.setVisibility(View.GONE);
                } else {
                    mInforDetailsContent.setText(Html.fromHtml(inforDetailsBean.getContent(), imageGetter, null));
                }
                mInforDetailsCommentTxt.setText(inforDetailsBean.getComment() + "");
                mInforDetailsZanTxt.setText(inforDetailsBean.getPraise() + "");
                mInforDetailsShareTxt.setText(inforDetailsBean.getShare() + "");
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

    }

}
