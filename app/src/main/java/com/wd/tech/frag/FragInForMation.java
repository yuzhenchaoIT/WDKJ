package com.wd.tech.frag;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wd.tech.R;
import com.wd.tech.adapter.HomeListAdapter;
import com.wd.tech.bean.BannerBean;
import com.wd.tech.bean.HomeListBean;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.dao.UserDao;
import com.wd.tech.presenter.AddCollectPresenter;
import com.wd.tech.presenter.AllInfoPresenter;
import com.wd.tech.presenter.BannerPresenter;
import com.wd.tech.presenter.CancelPresenter;
import com.wd.tech.presenter.RecommendPresenter;
import com.wd.tech.view.AdvertWebActivity;
import com.wd.tech.view.SearchActivity;
import com.wd.tech.view.SortActivity;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import org.greenrobot.greendao.Property;

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
    //p层
    private RecommendPresenter mRecommendPresenter = new RecommendPresenter(new HomeListCall());
    private BannerPresenter mBanPresenter = new BannerPresenter(new BannerCall());
    //收藏 和 取消 收藏
    private AddCollectPresenter mAddCollectP = new AddCollectPresenter(new AddCollectCall());
    private CancelPresenter mCancelP = new CancelPresenter(new CancelCollectCall());
    //适配器
    private HomeListAdapter mHomeListAdapter;
    //线性布局
    private LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
    private View view;
    private Unbinder unbinder;
    private SmartRefreshLayout mRefreshLayout;
    private int itemId1;
    private User user;
    private int uid1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_01, container, false);

        unbinder = ButterKnife.bind(this, view);
        mRefreshLayout = view.findViewById(R.id.refreshLayout);

        user = WDActivity.getUser(getContext());


        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                refreshlayout.finishRefresh(2000);
                mRecommendPresenter.request(true, 18, "15320748258726", 0);
            }
        });

        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

                refreshlayout.finishLoadmore(2000);
                mRecommendPresenter.request(false, 18, "15320748258726", 0);
            }
        });

        //适配器
        mHomeListAdapter = new HomeListAdapter(getContext());
        mHomeXrecyclerView.setAdapter(mHomeListAdapter);

        //布局管理器
        mHomeXrecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecommendPresenter.request(true, 18, "15320748258726", 0);
        //banner图请求数据
        mBanPresenter.request();


        //实现收藏
        mHomeListAdapter.setCommPriceListener(new HomeListAdapter.CommPriceListener() {
            @Override
            public void onPriceSuccessLitener(int uid) {
                uid1 = uid;
                //请求收藏的接口
                mAddCollectP.request(user.getUserId(), user.getSessionId(), uid);
            }

            @Override
            public void onPriceFiureLitener(int uid) {
                //请求取消收藏的接口
                mCancelP.request(user.getUserId(), user.getSessionId(), uid + "");
            }
        });


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


    @Override
    public void onPause() {
        super.onPause();
        mHomeBanner.pause();//暂停轮播
    }

    @Override
    public void onResume() {
        super.onResume();
        mHomeBanner.start();//开始轮播
        if (user == null) {
            Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
            Toast.makeText(getContext(), "网络异常", Toast.LENGTH_SHORT).show();
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
                    Intent intent = new Intent(getContext(), AdvertWebActivity.class);
                    intent.putExtra("AdvertUrl", jumpUrl + "");
                    startActivity(intent);
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
                Toast.makeText(getContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
                //添加列表并刷新
                if (mRecommendPresenter.getPage() == 1) {
                    mHomeListAdapter.clear();
                }
                mHomeListAdapter.addItem(data.getResult());
            } else {
                Toast.makeText(getContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
            mHomeListAdapter.notifyDataSetChanged();
        }

        @Override
        public void fail(ApiException e) {
            Toast.makeText(getContext(), "网络异常", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), "网络异常", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), "网络异常", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecommendPresenter.unBind();
        mBanPresenter.unBind();
        mAddCollectP.unBind();
        mCancelP.unBind();
    }


}
