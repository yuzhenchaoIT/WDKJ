package com.wd.tech.frag;

import android.content.Intent;
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

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.wd.tech.adapter.HomeListAdapter;
import com.wd.tech.R;
import com.wd.tech.bean.HomeListBean;
import com.wd.tech.bean.Result;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.RecommendPresenter;
import com.wd.tech.view.SearchActivity;
import com.wd.tech.view.SortActivity;

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
    private View view;
    private Unbinder unbinder;
    //p层
    private RecommendPresenter mRecommendPresenter = new RecommendPresenter(new HomeListCall());
    //适配器
    private HomeListAdapter mHomeListAdapter;
    //线性布局
    private LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_01, container, false);
        unbinder = ButterKnife.bind(this, view);

        mHomeListAdapter = new HomeListAdapter(getContext());
        mHomeXrecyclerView.setAdapter(mHomeListAdapter);
        mHomeXrecyclerView.setLayoutManager(mLinearLayoutManager);

        //添加下拉和刷新的监听器
        mHomeXrecyclerView.setLoadingListener(this);

        mHomeXrecyclerView.refresh();
        mRecommendPresenter.request(18, "15320748258726", 12, 1, 10);

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
        mRecommendPresenter.request(18, "15320748258726", 12, 1, 10);
    }

    @Override
    public void onLoadMore() {
        mRecommendPresenter.request(18, "15320748258726", 12, 1, 10);
    }


    /**
     * 内部类
     */
    class HomeListCall implements DataCall<Result> {

        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")) {
                mHomeXrecyclerView.refreshComplete();//结束刷新
                mHomeXrecyclerView.loadMoreComplete();//结束加载更多
                Toast.makeText(getContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
                List<HomeListBean> beanList = (List<HomeListBean>) data.getResult();
                mHomeListAdapter.addItem(beanList);
                mHomeListAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void fail(ApiException e) {
            Toast.makeText(getContext(), e + "失败", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecommendPresenter.unBind();
    }


}
