package com.wd.tech.frag;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.wd.tech.R;
import com.wd.tech.adapter.CommunityAdapter;
import com.wd.tech.bean.CommunityListBean;
import com.wd.tech.bean.Result;
import com.wd.tech.core.WDFragment;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.CommunitPresenter;

import java.util.List;

import javax.sql.CommonDataSource;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.Field;

public class FragCommunity extends WDFragment  {

    @BindView(R.id.com_recy)
    XRecyclerView recycler;
    private CommunityAdapter communityAdapter;
    private CommunitPresenter communitPresenter;

    @Override
    public String getPageName() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_03;
    }

    @Override
    protected void initView() {
        communitPresenter = new CommunitPresenter(new ComData());
        communitPresenter.request(true);
        ButterKnife.bind(getActivity());
        communityAdapter = new CommunityAdapter(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(communityAdapter);
        recycler.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

                if (communitPresenter.isRunning()){
                    recycler.refreshComplete();
                    recycler.loadMoreComplete();
                }
                communityAdapter.clearlist();
                    communitPresenter.request(true);
            }

            @Override
            public void onLoadMore() {
                if (communitPresenter.isRunning()){
                    recycler.refreshComplete();
                    recycler.loadMoreComplete();
                }
                     communitPresenter.request(false);
            }
        });
    }



    class ComData implements DataCall<Result<List<CommunityListBean>>>{
         @Override
         public void success(Result<List<CommunityListBean>> data) {
             recycler.refreshComplete();
             recycler.loadMoreComplete();
             //Toast.makeText(getActivity(), data.getStatus()+"列表", Toast.LENGTH_SHORT).show();
             communityAdapter.addList(data.getResult());
             communityAdapter.notifyDataSetChanged();

         }
         @Override
         public void fail(ApiException e) {
             recycler.refreshComplete();
             recycler.loadMoreComplete();
             Toast.makeText(getContext(), e + "失败", Toast.LENGTH_SHORT).show();

         }
     }

    @Override
    public void onDestroy() {
        super.onDestroy();
        communitPresenter.unBind();
    }
}
