package com.wd.tech.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.library.AutoFlowLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wd.tech.R;
import com.wd.tech.adapter.DetailAllCommentAdapter;
import com.wd.tech.adapter.DetailRecommendAdapter;
import com.wd.tech.bean.AllInfoPlateBean;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.bean.details.FindAllCommentListBean;
import com.wd.tech.bean.details.InforDetailsBean;
import com.wd.tech.bean.details.InformationListBean;
import com.wd.tech.core.SelectPayPopupWindow;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.AddCollectPresenter;
import com.wd.tech.presenter.CancelPresenter;
import com.wd.tech.presenter.InforDetailsPresenter;
import com.wd.tech.presenter.InforMation.AddGreatPresenter;
import com.wd.tech.presenter.InforMation.CancelGreatPresenter;
import com.wd.tech.presenter.InforMation.DetailAddCommentPresenter;
import com.wd.tech.presenter.InforMation.DetailAllCommentPresenter;
import com.wd.tech.util.DateUtils;
import com.wd.tech.view.pay.PointsActivity;
import com.wd.tech.view.pay.VipActivity;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.wd.tech.core.WDApplication.getContext;

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
    TextView mInforDetailsComment;
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
    @BindView(R.id.infor_details_ll_no_comment)
    LinearLayout mInforDetailsLlNoComment;
    @BindView(R.id.infor_details_ll)
    LinearLayout mInforDetailsLl;
    @BindView(R.id.infor_details_afl)
    AutoFlowLayout mInforAflt;
    @BindView(R.id.infor_details_recom_recy)
    RecyclerView mIfordrr;
    @BindView(R.id.infor_details_comm_recy)
    RecyclerView mIforDetailCommR;
    @BindView(R.id.rl_comment)
    RelativeLayout mRlComment;
    @BindView(R.id.hide_down)
    TextView mHideDown;
    @BindView(R.id.comment_content)
    EditText mCommentContent;
    @BindView(R.id.comment_send)
    Button mCommentSend;
    @BindView(R.id.infor_details_bottom)
    LinearLayout mInforDetailsBottom;
    @BindView(R.id.infor_details_go_pay)
    TextView mInforDetailsGoPay;
    @BindView(R.id.infor_details_ll_no_login)
    LinearLayout mInforDetailsLlNoLogin;
    @BindView(R.id.infor_details_ll_data)
    LinearLayout mInforDetailsLlData;

    private TextView mInforAfltZi;
    //请求详情页面内容
    private InforDetailsPresenter mDetailsPresenter = new InforDetailsPresenter(new DetailsCall());
    //查看详情所有评论
    private DetailAllCommentPresenter mDetAllCommP;
    //详情 发布 评论
    private DetailAddCommentPresenter detailAddCommentPresenter = new DetailAddCommentPresenter(new DetailAddCommentCall());
    //详情 点赞
    private AddGreatPresenter mAddGreatP = new AddGreatPresenter(new AddGreatCall());
    private CancelGreatPresenter mCancelGreatP = new CancelGreatPresenter(new CancelGreatCall());
    //收藏 和 取消 收藏
    private AddCollectPresenter mAddCollectP = new AddCollectPresenter(new AddCollectCall2());
    private CancelPresenter mCancelP = new CancelPresenter(new CancelCollectCall2());
    //加载富文本的图片
    private URLImageParser mImageGetter;
    private InforDetailsBean mInforDetailsBean;
    //线性
    private LinearLayoutManager mManager = new LinearLayoutManager(this);
    private LinearLayoutManager mManager2 = new LinearLayoutManager(this);
    //适配器
    private DetailRecommendAdapter mDetailRecommendA;
    private DetailAllCommentAdapter mDetailAllCommentA;
    private User user;
    private int homeListId;
    private String text;
    private SelectPayPopupWindow menuWindow;
    private int isCollection;
    private PopupWindow popupWindow;
    private ImageView friends, sigleFriend;
    private TextView wxShareCancel;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_infor_details;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        user = WDActivity.getUser(this);

        mDetAllCommP = new DetailAllCommentPresenter(new DetailAllCommentCall());

        ImageLoader imageLoader = ImageLoader.getInstance();//ImageLoader需要实例化
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));

        mImageGetter = new URLImageParser(mInforDetailsContent);

        //获取条目id
        homeListId = Integer.parseInt(getIntent().getStringExtra("homeListId"));

        if (user != null) {
            mDetailsPresenter.request(user.getUserId(), user.getSessionId(), homeListId);
        } else {
            mInforDetailsLlNoLogin.setVisibility(View.VISIBLE);
            mInforDetailsLlData.setVisibility(View.GONE);
            Toast.makeText(getBaseContext(), "请先登录", Toast.LENGTH_SHORT).show();
        }


        //详情页面的“推荐”
        mDetailRecommendA = new DetailRecommendAdapter(getBaseContext());
        //详情页面的“评论查看”
        mDetailAllCommentA = new DetailAllCommentAdapter(getBaseContext());
        mManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mManager2.setOrientation(LinearLayoutManager.VERTICAL);

        mIfordrr.setLayoutManager(mManager);
        mIfordrr.setAdapter(mDetailRecommendA);

        mIforDetailCommR.setLayoutManager(mManager2);
        mIforDetailCommR.setAdapter(mDetailAllCommentA);

        if (user != null) {
            //详情 所有评论查看 p层请求
            mDetAllCommP.request(user.getUserId(), user.getSessionId(), homeListId, 1, 20);
        } else {
            mInforDetailsLlNoLogin.setVisibility(View.VISIBLE);
            mInforDetailsLlData.setVisibility(View.GONE);
        }

        View contentView = View.inflate(getContext(), R.layout.share_layout, null);
        popupWindow = new PopupWindow(contentView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //通过popupwindow的视图对象去找到里面的控件
        friends = contentView.findViewById(R.id.friends);
        sigleFriend = contentView.findViewById(R.id.sigle_friends);
        wxShareCancel = contentView.findViewById(R.id.wx_share_cancel);
        //点击按钮,,弹出popupwindow
        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wechatShare(1);
            }
        });
        //好友分享
        sigleFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wechatShare(0);
            }
        });
        //取消 按钮
        wxShareCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 微信分享 （这里仅提供一个分享网页的示例，其它请参看官网示例代码）
     *
     * @param flag(0:分享到微信好友，1：分享到微信朋友圈)
     */
    private void wechatShare(int flag) {
        IWXAPI api = WXAPIFactory.createWXAPI(getContext(), "wx4c96b6b8da494224", false);
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "www.hooxiao.com";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = mInforDetailsBean.getTitle();
        msg.description = mInforDetailsBean.getSummary();
        //这里替换一张自己工程里的图片资源
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.icon);
        msg.setThumbImage(thumb);


        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);

    }

    @OnClick({R.id.infor_details_back, R.id.infor_details_comment,
            R.id.infor_details_comment_img, R.id.infor_details_zan_img,
            R.id.infor_details_coll_img, R.id.infor_details_share_img,
            R.id.hide_down, R.id.comment_send, R.id.infor_details_go_pay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.infor_details_back:
                finish();
                break;
            //评论输入框
            case R.id.infor_details_comment:
                // 弹出输入法
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                // 显示评论框
                mInforDetailsBottom.setVisibility(View.GONE);
                mRlComment.setVisibility(View.VISIBLE);
                break;
            //评论图片
            case R.id.infor_details_comment_img:
                break;
            //点赞图片
            case R.id.infor_details_zan_img:
                if (user != null) {
                    mInforDetailsZanImg.setImageResource(R.drawable.common_icon_praise_s);
                    mAddGreatP.request(user.getUserId(), user.getSessionId(), homeListId);
                } else {
                    Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
                }

                break;
            //收藏图片
            case R.id.infor_details_coll_img:
                if (isCollection == 1) {
                    //请求取消收藏的接口
                    if (user != null) {
                        mCancelP.request(user.getUserId(), user.getSessionId(), homeListId + "");
                        mInforDetailsCollImg.setImageResource(R.drawable.common_icon_collect_n);
                    } else {
                        Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
                    }

                } else if (isCollection == 2) {
                    //请求收藏的接口
                    if (user != null) {
                        mAddCollectP.request(user.getUserId(), user.getSessionId(), homeListId);
                        mInforDetailsCollImg.setImageResource(R.drawable.common_icon_collect_s);
                    } else {
                        Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            //分享图片
            case R.id.infor_details_share_img:
                popupWindow.showAtLocation(InforDetailsActivity.this.findViewById(R.id.activity_infor_details_main), Gravity.BOTTOM, 0, 0);
                break;
            case R.id.hide_down:
                // 隐藏评论框
                mInforDetailsBottom.setVisibility(View.VISIBLE);
                mRlComment.setVisibility(View.GONE);
                // 隐藏输入法，然后暂存当前输入框的内容，方便下次使用
                InputMethodManager im = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(mCommentContent.getWindowToken(), 0);
                break;
            case R.id.comment_send:
                if (mCommentContent.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "评论不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    if (user != null) {
                        text = mCommentContent.getText().toString().trim();
                        detailAddCommentPresenter.request(user.getUserId(), user.getSessionId(), text, homeListId);
                        // 发送完，清空输入框
                        mCommentContent.setText("");
                    } else {
                        Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.infor_details_go_pay:
                //实例化SelectPicPopupWindow
                menuWindow = new SelectPayPopupWindow(InforDetailsActivity.this, itemsOnClick);
                //显示窗口
                menuWindow.showAtLocation(InforDetailsActivity.this.findViewById(R.id.activity_infor_details_main), Gravity.BOTTOM, 0, 0); //设置layout在PopupWindow中显示的位置
                break;
        }
    }

    //vip   积分 为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.r:
                    Intent intent = new Intent(InforDetailsActivity.this, PointsActivity.class);
                    intent.putExtra("DataId", homeListId + "");
                    startActivity(intent);
//                    finish();
                    break;
                case R.id.t:
                    startActivity(new Intent(InforDetailsActivity.this, VipActivity.class));
//                    finish();
                    break;
            }
        }
    };


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


    /**
     * 详情页面赋值
     */
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
                    mInforDetailsLl.setVisibility(View.VISIBLE);
                    mInforDetailsContent.setText(Html.fromHtml(mInforDetailsBean.getContent(), mImageGetter, null));
                    mInforDetailsLlNoPay.setVisibility(View.GONE);
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
                //当前用户是否过点赞(1为是，2为否)
                isCollection = mInforDetailsBean.getWhetherCollection();
                //当前用户是否收藏 1=是，2=否
                if (isCollection == 1) {
                    mInforDetailsCollImg.setImageResource(R.drawable.common_icon_collect_s);
                } else if (isCollection == 2) {
                    mInforDetailsCollImg.setImageResource(R.drawable.common_icon_collect_n);
                }
                mInforDetailsShareTxt.setText(mInforDetailsBean.getShare() + "");

                //详情 推荐列表的 展示
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

    //查看评论
    class DetailAllCommentCall implements DataCall<Result> {

        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")) {
                List<FindAllCommentListBean> beanList = (List<FindAllCommentListBean>) data.getResult();
                if (beanList.size() == 0) {
                    mInforDetailsLlNoComment.setVisibility(View.VISIBLE);
                    mIforDetailCommR.setVisibility(View.GONE);
                } else {
                    mDetailAllCommentA.addItem(beanList);
                    mDetailAllCommentA.notifyDataSetChanged();
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

    //评论发布
    class DetailAddCommentCall implements DataCall<Result> {

        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")) {
                Toast.makeText(getBaseContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
                //详情  p层请求
                mDetAllCommP.request(user.getUserId(), user.getSessionId(), homeListId, 1, 20);
                mDetailAllCommentA.clear();
                mDetailAllCommentA.notifyDataSetChanged();
                // 隐藏评论框
                mInforDetailsBottom.setVisibility(View.VISIBLE);
                mRlComment.setVisibility(View.GONE);
                // 隐藏输入法，然后暂存当前输入框的内容，方便下次使用
                InputMethodManager im = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(mCommentContent.getWindowToken(), 0);
            } else {
                Toast.makeText(getBaseContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void fail(ApiException e) {
            Toast.makeText(getBaseContext(), "网络异常", Toast.LENGTH_SHORT).show();
        }
    }

    //点赞
    class AddGreatCall implements DataCall<Result> {

        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")) {
                Toast.makeText(getBaseContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
                mInforDetailsZanTxt.setText(String.valueOf(mInforDetailsBean.getPraise() + 1));
            } else {
//                Toast.makeText(getBaseContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
                mCancelGreatP.request(user.getUserId(), user.getSessionId(), homeListId);
                mInforDetailsZanImg.setImageResource(R.drawable.common_icon_prise_n);
                mInforDetailsZanTxt.setText(String.valueOf(mInforDetailsBean.getPraise()));
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }

    //取消点赞
    class CancelGreatCall implements DataCall<Result> {

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

        }
    }

    //收藏
    class AddCollectCall2 implements DataCall<Result> {

        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")) {
                Toast.makeText(getBaseContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(getBaseContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
//                mCancelP.request(user.getUserId(), user.getSessionId(), homeListId + "");
//                mInforDetailsCollImg.setImageResource(R.drawable.common_icon_collect_n);
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }

    //取消收藏
    class CancelCollectCall2 implements DataCall<Result> {

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

        }
    }

    /**
     * 内存释放
     */
    @Override
    protected void destoryData() {
        mDetailsPresenter.unBind();
        mDetAllCommP.unBind();
        detailAddCommentPresenter.unBind();
        mAddCollectP.unBind();
        mCancelP.unBind();
        mAddGreatP.unBind();
        mCancelGreatP.unBind();
    }

}
