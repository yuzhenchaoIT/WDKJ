package com.wd.tech.presenter;

import com.wd.tech.bean.HomeListBean;
import com.wd.tech.bean.Result;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.core.http.IRequest;
import com.wd.tech.core.http.NotWorkUtils;

import java.util.List;

import io.reactivex.Observable;

public class RecommendPresenter extends BasePresenter {

//    private int page = 1;
//    private boolean isRefresh = true;

    public RecommendPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    public Observable observable(Object... args) {
//        isRefresh = (Boolean) args[0];//是否需要刷新
//        if (isRefresh) {//刷新
//            page = 1;
//        } else {
//            page++;
//        }
        IRequest iRequest = NotWorkUtils.getInstance().create(IRequest.class);
        Observable<Result<List<HomeListBean>>> recommendList = iRequest.recommendList((int) args[0], (String) args[1], (int) args[2], (int) args[3],(int) args[4]);
        return recommendList;
    }

//    public boolean isResresh() {
//        return isRefresh;
//    }

}
