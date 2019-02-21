package com.wd.tech.frag;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.wd.tech.R;
import com.wd.tech.adapter.HomeListAdapter;
import com.wd.tech.bean.BannerBean;
import com.wd.tech.bean.HomeListBean;
import com.wd.tech.bean.Result;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.BannerPresenter;
import com.wd.tech.presenter.RecommendPresenter;
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
public class FragInForMation extends Fragment implements XRecyclerView.LoadingListener {
    @BindView(R.id.home_menu)
    ImageView mHomeMenu;
    @BindView(R.id.home_search)
    ImageView mHomeSearch;
    @BindView(R.id.home_xrecyclerView)
    XRecyclerView mHomeXrecyclerView;
//    @BindView(R.id.home_banner)
//    MZBannerView mHomeBanner;
    //p层
    private RecommendPresenter mRecommendPresenter = new RecommendPresenter(new HomeListCall());
//    private BannerPresenter mBanPresenter = new BannerPresenter(new BannerCall());
    //适配器
    private HomeListAdapter mHomeListAdapter;
    //线性布局
    private LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
    private View view;
    private Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_01, container, false);

        unbinder = ButterKnife.bind(this, view);

        //添加头部轮播
//        View header = LayoutInflater.from(getContext()).inflate(R.layout.layout_header_banner, container, false);

//        mHomeXrecyclerView.addHeaderView(header);


        //适配器
        mHomeListAdapter = new HomeListAdapter(getContext());
        mHomeXrecyclerView.setAdapter(mHomeListAdapter);
        //布局管理器
        mHomeXrecyclerView.setLayoutManager(mLinearLayoutManager);

        //添加下拉和刷新的监听器
        mHomeXrecyclerView.setLoadingListener(this);
        //刷新
        mHomeXrecyclerView.refresh();
        //banner图请求数据
//        mBanPresenter.request();


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
    public void onRefresh() {
        if (mRecommendPresenter.isRunning()) {
            mHomeXrecyclerView.refreshComplete();
            return;
        }
        mRecommendPresenter.request(true, 18, "15320748258726");
    }

    @Override
    public void onLoadMore() {
        if (mRecommendPresenter.isRunning()) {
            mHomeXrecyclerView.loadMoreComplete();
            return;
        }
        mRecommendPresenter.request(false, 18, "15320748258726");
    }


//    @Override
//    public void onPause() {
//        super.onPause();
//        mHomeBanner.pause();//暂停轮播
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        mHomeBanner.start();//开始轮播
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 内部类
     */


    //魅族Banner
//    class BannerCall implements DataCall<Result<List<BannerBean>>> {
//
//        @Override
//        public void success(Result<List<BannerBean>> data) {
//            if (data.getStatus().equals("0000")) {
//                mHomeBanner.setIndicatorVisible(false);
//                mHomeBanner.setPages(data.getResult(), new MZHolderCreator<BannerViewHolder>() {
//                    @Override
//                    public BannerViewHolder createViewHolder() {
//                        return new BannerViewHolder();
//                    }
//                });
//                mHomeBanner.start();
//            } else {
//                Toast.makeText(getContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
//            }
//
//        }
//
//        @Override
//        public void fail(ApiException e) {
//            Toast.makeText(getContext(), "网络异常", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    class BannerViewHolder implements MZViewHolder<BannerBean> {
//
//        private SimpleDraweeView mImageView;
//
//        @Override
//        public View createView(Context context) {
//            // 返回页面布局
//            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
//            mImageView = view.findViewById(R.id.banner_image);
//            return view;
//        }
//
//        @Override
//        public void onBind(Context context, int position, BannerBean data) {
//            // 数据绑定
//            mImageView.setImageURI(Uri.parse(data.getImageUrl()));
//        }
//
//    }


    //列表展示
    class HomeListCall implements DataCall<Result<List<HomeListBean>>> {

        @Override
        public void success(Result<List<HomeListBean>> data) {
            mHomeXrecyclerView.refreshComplete();//结束刷新
            mHomeXrecyclerView.loadMoreComplete();//结束加载更多
            if (data.getStatus().equals("0000")) {
                Toast.makeText(getContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
                //添加列表并刷新
                if (mRecommendPresenter.getPage() == 1) {
                    mHomeListAdapter.clear();
                }
//                List<HomeListBean> beanList = (List<HomeListBean>) data.getResult();
                mHomeListAdapter.addItem(data.getResult());
                mHomeListAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void fail(ApiException e) {
            mHomeXrecyclerView.refreshComplete();//结束刷新
            mHomeXrecyclerView.loadMoreComplete();//结束加载更多
            Toast.makeText(getContext(), "网络异常", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecommendPresenter.unBind();
    }


}
