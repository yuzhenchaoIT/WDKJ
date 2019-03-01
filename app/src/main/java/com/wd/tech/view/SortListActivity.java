package com.wd.tech.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wd.tech.R;
import com.wd.tech.adapter.HomeListAdapter;
import com.wd.tech.bean.HomeListBean;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
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
                mPlateListP.request(true, 18, "15320748258726", plateId);
            }
        });

        mSortListRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

                refreshlayout.finishLoadmore(2000);
                mPlateListP.request(false, 18, "15320748258726", plateId);
            }
        });

        //适配器
        mHomeListAdapter = new HomeListAdapter(getContext());
        mSortListRecy.setAdapter(mHomeListAdapter);

        //布局管理器
        mSortListRecy.setLayoutManager(mLinearLayoutManager);
        mPlateListP.request(true, 18, "15320748258726", plateId);
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
                Toast.makeText(getBaseContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getBaseContext(), "网络异常", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void destoryData() {
        mPlateListP.unBind();
    }
}
