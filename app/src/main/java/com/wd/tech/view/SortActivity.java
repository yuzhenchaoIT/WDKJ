package com.wd.tech.view;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.wd.tech.R;
import com.wd.tech.adapter.PlateAdapter;
import com.wd.tech.bean.AllInfoPlateBean;
import com.wd.tech.bean.Result;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.AllInfoPlatePresenter;

import java.util.List;

/**
 * 分类页面
 *
 * @author lmx
 * @date 2019/2/19
 */
public class SortActivity extends WDActivity {

    private AllInfoPlatePresenter mPlatePresenter = new AllInfoPlatePresenter(new PlateCall());
    private RecyclerView mPlateRecy;
    private PlateAdapter mPlateAdapter;
    private GridLayoutManager manager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO:OnCreate Method has been created, run FindViewById again to generate code
        setContentView(R.layout.activity_sort);
        initView();
        //网格
        manager = new GridLayoutManager(this, 2);
        mPlateRecy.setLayoutManager(manager);
        //适配器
        mPlateAdapter = new PlateAdapter(getBaseContext());
        mPlateRecy.setAdapter(mPlateAdapter);
        //请求数据
        mLoadDialog.show();
        mPlatePresenter.request();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sort;
    }

    @Override
    protected void initView() {
        mPlateRecy = (RecyclerView) findViewById(R.id.plate_recy);
    }


    class PlateCall implements DataCall<Result> {

        @Override
        public void success(Result data) {
            mLoadDialog.cancel();
            if (data.getStatus().equals("0000")) {
//                Toast.makeText(getBaseContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
                List<AllInfoPlateBean> beanList = (List<AllInfoPlateBean>) data.getResult();
                mPlateAdapter.addItem(beanList);
                mPlateAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getBaseContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void fail(ApiException e) {
            mLoadDialog.cancel();
//            Toast.makeText(getBaseContext(), "网络异常", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void destoryData() {
        mPlatePresenter.unBind();
    }


}
