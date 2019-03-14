package com.wd.tech.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wd.tech.R;
import com.wd.tech.adapter.HomeListAdapter;
import com.wd.tech.bean.HomeListBean;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.AddCollectPresenter;
import com.wd.tech.presenter.CancelPresenter;
import com.wd.tech.presenter.PlateListPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.wd.tech.core.WDApplication.getContext;

/**
 * 分类跳转至列表数据展示
 *
 * @author lmx
 * @date 2019/2/22
 */
public class SortListActivity extends WDActivity {

    @BindView(R.id.sort_list_back)
    ImageView mSortListBack;
    @BindView(R.id.sort_list_title)
    TextView mSortListTitle;
    @BindView(R.id.sort_list_search)
    ImageView mSortListSearch;
    @BindView(R.id.sort_list_recy)
    RecyclerView mSortListRecy;
    @BindView(R.id.sort_list_refreshLayout)
    SmartRefreshLayout mSortListRefreshLayout;
    private PlateListPresenter mPlateListP = new PlateListPresenter(new PlateListCall());
    private HomeListAdapter mHomeListAdapter;
    private LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
    private User user;
    //收藏 和 取消 收藏
    private AddCollectPresenter mAddCollectP = new AddCollectPresenter(new AddCollectCalls());
    private CancelPresenter mCancelP = new CancelPresenter(new CancelCollectCalls());
    private int uid1;
    private PopupWindow popupWindow;
    private String title1;
    private String summary1;
    private ImageView friends, sigleFriend;
    private TextView wxShareCancel;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_sort_list;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        //获取板块id 和板块名称
        final int plateId = Integer.parseInt(getIntent().getStringExtra("id"));
        String title = getIntent().getStringExtra("title");
        mSortListTitle.setText(title);


        user = WDActivity.getUser(getContext());

        mSortListRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                refreshlayout.finishRefresh(2000);
                if (user != null) {
                    mPlateListP.request(true, user.getUserId(), user.getSessionId(), plateId);
                } else {
                    mPlateListP.request(true, 1010, "15320748258726", plateId);
                }
            }
        });

        mSortListRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

                refreshlayout.finishLoadmore(2000);
                if (user != null) {
                    mPlateListP.request(false, user.getUserId(), user.getSessionId(), plateId);
                } else {
                    mPlateListP.request(false, 1010, "15320748258726", plateId);
                }
            }
        });

        //适配器
        mHomeListAdapter = new HomeListAdapter(getContext());
        mSortListRecy.setAdapter(mHomeListAdapter);

        //布局管理器
        mSortListRecy.setLayoutManager(mLinearLayoutManager);
        if (user != null) {
            mLoadDialog.show();
            mPlateListP.request(true, user.getUserId(), user.getSessionId(), plateId);
        } else {
            mLoadDialog.show();
            mPlateListP.request(true, 1010, "15320748258726", plateId);
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


        //实现收藏
        mHomeListAdapter.setCommPriceListener(new HomeListAdapter.CommPriceListener() {
            @Override
            public void onPriceSuccessLitener(int uid) {
                uid1 = uid;
                if (user != null) {
                    //请求收藏的接口
                    mAddCollectP.request(user.getUserId(), user.getSessionId(), uid);
                } else {
                    Snackbar snackbar = Snackbar.make(mSortListRecy, "未登录", Snackbar.LENGTH_LONG)
                            .setAction("去登陆", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getContext(), LoginActivity.class);
                                    startActivity(intent);
                                }
                            });
                    snackbar.show();
                }

            }

            @Override
            public void onPriceFiureLitener(int uid) {
                if (user != null) {
                    //请求取消收藏的接口
                    mCancelP.request(user.getUserId(), user.getSessionId(), uid + "");
                } else {
                    Snackbar snackbar = Snackbar.make(mSortListRecy, "未登录", Snackbar.LENGTH_LONG)
                            .setAction("去登陆", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getContext(), LoginActivity.class);
                                    startActivity(intent);
                                }
                            });
                    snackbar.show();
                }

            }
        });


        //实现分享
        mHomeListAdapter.setOnItemClickListener(new HomeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String title, String summary) {
                title1 = title;
                summary1 = summary;
                popupWindow.showAtLocation(SortListActivity.this.findViewById(R.id.activity_sort_list_main), Gravity.BOTTOM, 0, 0);
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
        msg.title = title1;
        msg.description = summary1;
        //这里替换一张自己工程里的图片资源
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.icon);
        msg.setThumbImage(thumb);


        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);

    }


    @OnClick({R.id.sort_list_back, R.id.sort_list_search})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sort_list_back:
                finish();
                break;
            case R.id.sort_list_search:
                startActivity(new Intent(getContext(), SearchActivity.class));
                break;
        }
    }


    class PlateListCall implements DataCall<Result<List<HomeListBean>>> {

        @Override
        public void success(Result<List<HomeListBean>> data) {
            if (data.getStatus().equals("0000")) {
                mLoadDialog.cancel();
//                Toast.makeText(getBaseContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
                //添加列表并刷新
                if (mPlateListP.getPage() == 1) {
                    mHomeListAdapter.clear();
                }
                mHomeListAdapter.addItem(data.getResult());
                mHomeListAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getBaseContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void fail(ApiException e) {
            mLoadDialog.cancel();
            Toast.makeText(getBaseContext(), "网络异常", Toast.LENGTH_SHORT).show();
        }
    }


    //添加收藏
    class AddCollectCalls implements DataCall<Result> {

        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")) {
                Toast.makeText(getContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
            } else {
                mCancelP.request(user.getUserId(), user.getSessionId(), uid1 + "");
            }
            mHomeListAdapter.notifyDataSetChanged();
        }

        @Override
        public void fail(ApiException e) {
            Toast.makeText(getContext(), "网络异常", Toast.LENGTH_SHORT).show();
        }
    }

    //取消收藏
    class CancelCollectCalls implements DataCall<Result> {

        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")) {
                Toast.makeText(getContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
            } else {
                mAddCollectP.request(user.getUserId(), user.getSessionId(), uid1);
            }
            mHomeListAdapter.notifyDataSetChanged();
        }

        @Override
        public void fail(ApiException e) {
            Toast.makeText(getContext(), "网络异常", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void destoryData() {
        mPlateListP.unBind();
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }
}
