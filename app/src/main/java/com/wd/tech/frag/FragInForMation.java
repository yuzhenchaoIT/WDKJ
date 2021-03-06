package com.wd.tech.frag;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
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
import com.wd.tech.bean.BannerBean;
import com.wd.tech.bean.HomeListBean;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.AddCollectPresenter;
import com.wd.tech.presenter.BannerPresenter;
import com.wd.tech.presenter.CancelPresenter;
import com.wd.tech.presenter.DoTheTaskPresenter;
import com.wd.tech.presenter.RecommendPresenter;
import com.wd.tech.util.ListDataSave;
import com.wd.tech.view.AddCircleActivity;
import com.wd.tech.view.AdvertWebActivity;
import com.wd.tech.view.InforDetailsActivity;
import com.wd.tech.view.LoginActivity;
import com.wd.tech.view.SearchActivity;
import com.wd.tech.view.SortActivity;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 资讯页面
 *
 * @author lmx
 * @date 2019/2/19
 */
public class FragInForMation extends Fragment {
    @BindView(R.id.home_menu)
    ImageView mHomeMenu;
    @BindView(R.id.home_search)
    ImageView mHomeSearch;
    @BindView(R.id.home_xrecyclerView)
    RecyclerView mHomeXrecyclerView;
    @BindView(R.id.home_banner)
    MZBannerView mHomeBanner;
    //资讯首页请求数据
    private RecommendPresenter mRecommendPresenter;
    //banner数据
    private BannerPresenter mBanPresenter = new BannerPresenter(new BannerCall());
    //收藏 和 取消 收藏
    private AddCollectPresenter mAddCollectP = new AddCollectPresenter(new AddCollectCall());
    private CancelPresenter mCancelP = new CancelPresenter(new CancelCollectCall());
    //资讯首页适配器
    private HomeListAdapter mHomeListAdapter;
    //线性布局
    private LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
    private View view;
    private Unbinder unbinder;
    private SmartRefreshLayout mRefreshLayout;
    private User user;
    private int uid1;
    //微信（朋友圈和好友）分享
    private PopupWindow popupWindow;
    private ImageView friends, sigleFriend;
    private TextView wxShareCancel;
    private String title1;
    private String summary1;
    private DoTheTaskPresenter doTheTaskPresenter = new DoTheTaskPresenter(new DoTheTaskCall());
    private ListDataSave listDataSave;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frag_01, container, false);
        mRecommendPresenter = new RecommendPresenter(new HomeListCall());
        unbinder = ButterKnife.bind(this, view);
        mRefreshLayout = view.findViewById(R.id.refreshLayout);


        //获取用户信息
        user = WDActivity.getUser(getContext());

        //下拉刷新
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                refreshlayout.finishRefresh(2000);
                if (user != null) {
                    mRecommendPresenter.request(true, user.getUserId(), user.getSessionId(), 0);
                } else {
                    mRecommendPresenter.request(true, 1010, "15320748258726", 0);
                }
            }
        });

        //上拉加载
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

                refreshlayout.finishLoadmore(2000);
                if (user != null) {
                    mRecommendPresenter.request(false, user.getUserId(), user.getSessionId(), 0);
                } else {
                    mRecommendPresenter.request(false, 1010, "15320748258726", 0);
                }
            }
        });

        //适配器
        mHomeListAdapter = new HomeListAdapter(getContext());
        mHomeXrecyclerView.setAdapter(mHomeListAdapter);

        //布局管理器
        mHomeXrecyclerView.setLayoutManager(mLinearLayoutManager);
        //判断用户是否登录
        if (user != null) {
            mRecommendPresenter.request(true, user.getUserId(), user.getSessionId(), 0);
        } else {
            mRecommendPresenter.request(true, 1010, "15320748258726", 0);
        }
        //banner图请求数据
        mBanPresenter.request();

        //实现收藏
        mHomeListAdapter.setCommPriceListener(new HomeListAdapter.CommPriceListener() {
            @Override
            public void onPriceSuccessLitener(int uid) {
                uid1 = uid;
                if (user != null) {
                    //请求收藏的接口
                    mAddCollectP.request(user.getUserId(), user.getSessionId(), uid);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onPriceFiureLitener(int uid) {
                if (user != null) {
                    //请求取消收藏的接口
                    mCancelP.request(user.getUserId(), user.getSessionId(), uid + "");
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);

                }

            }
        });

        //微信分享  popupwindow弹出
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
        //朋友圈分享
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

        //实现分享  弹框弹出
        mHomeListAdapter.setOnItemClickListener(new HomeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String title, String summary) {
                title1 = title;
                summary1 = summary;
                popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            }
        });


        listDataSave = new ListDataSave(getActivity(), "Infor");


        return view;
    }


    @OnClick({R.id.home_menu, R.id.home_search})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_menu:
                startActivity(new Intent(getContext(), SortActivity.class));
                break;
            case R.id.home_search:
                startActivity(new Intent(getContext(), SearchActivity.class));
                break;
        }
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
        doTheTaskPresenter.request(user.getUserId(), user.getSessionId(), 1004);
    }

    //实现做任务接口
    private class DoTheTaskCall implements DataCall<Result> {
        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")) {

            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mHomeBanner.pause();//暂停轮播
    }

    @Override
    public void onResume() {
        super.onResume();
        mHomeBanner.start();//开始轮播
        user = WDActivity.getUser(getActivity());
        mRecommendPresenter = new RecommendPresenter(new HomeListCall());
        if (this.user != null) {
            mHomeListAdapter.clear();
            mRecommendPresenter.request(false, this.user.getUserId(), this.user.getSessionId(), 0);
        } else {
            mHomeListAdapter.clear();
            mRecommendPresenter.request(false, 1010, "15320748258726", 0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mRecommendPresenter.unBind();
        mBanPresenter.unBind();
        mAddCollectP.unBind();
        mCancelP.unBind();
    }


    /**
     * 内部类
     */


    //魅族Banner
    class BannerCall implements DataCall<Result<List<BannerBean>>> {

        @Override
        public void success(Result<List<BannerBean>> data) {
            if (data.getStatus().equals("0000")) {
                //指示器
                mHomeBanner.setIndicatorVisible(false);
                mHomeBanner.setPages(data.getResult(), new MZHolderCreator<BannerViewHolder>() {
                    @Override
                    public BannerViewHolder createViewHolder() {
                        return new BannerViewHolder();
                    }
                });
                mHomeBanner.start();
            } else {
                Toast.makeText(getContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void fail(ApiException e) {
//            Toast.makeText(getContext(), "网络异常", Toast.LENGTH_SHORT).show();
        }
    }

    class BannerViewHolder implements MZViewHolder<BannerBean> {

        private SimpleDraweeView mImageView;
        private TextView mBanName;
        private String jumpUrl;

        @Override
        public View createView(Context context) {
            // 返回页面布局
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
            mImageView = view.findViewById(R.id.banner_image);
            mBanName = view.findViewById(R.id.banner_name);
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //网址需判断  原生  还是 webview
                    if (jumpUrl.contains("wd://information")) {
                        Intent intent = new Intent(getContext(), InforDetailsActivity.class);
                        intent.putExtra("homeListId", 1 + "");
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getContext(), AdvertWebActivity.class);
                        intent.putExtra("AdvertUrl", jumpUrl + "");
                        startActivity(intent);
                    }

                }
            });
            return view;
        }

        @Override
        public void onBind(Context context, int position, BannerBean data) {
            // 数据绑定
            mImageView.setImageURI(Uri.parse(data.getImageUrl()));
            mBanName.setText(data.getTitle());
            //获取图片地址
            jumpUrl = data.getJumpUrl();
        }


    }


    //列表展示
    class HomeListCall implements DataCall<Result<List<HomeListBean>>> {

        @Override
        public void success(Result<List<HomeListBean>> data) {
            if (data.getStatus().equals("0000")) {
                //添加列表并刷新
                if (mRecommendPresenter.getPage() == 1) {
                    mHomeListAdapter.clear();
                }
                mHomeListAdapter.addItem(data.getResult());
            } else {
                Toast.makeText(getContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
            mHomeListAdapter.notifyDataSetChanged();
            //数据缓存
            listDataSave.setDataList("data", data.getResult());
        }

        @Override
        public void fail(ApiException e) {
//            Toast.makeText(getContext(), "网络异常", Toast.LENGTH_SHORT).show();
            //数据缓存
            int size = mHomeListAdapter.getItemCount();
            if (size == 0) {
                List<HomeListBean> list = listDataSave.getDataList1("data");
                mHomeListAdapter.addItem(list);
                mHomeListAdapter.notifyDataSetChanged();
            }
        }
    }

    //添加收藏
    class AddCollectCall implements DataCall<Result> {

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
//            Toast.makeText(getContext(), "网络异常", Toast.LENGTH_SHORT).show();
        }
    }

    //取消收藏
    class CancelCollectCall implements DataCall<Result> {

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
//            Toast.makeText(getContext(), "网络异常", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecommendPresenter.unBind();
        mBanPresenter.unBind();
        mAddCollectP.unBind();
        mCancelP.unBind();
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }


}
