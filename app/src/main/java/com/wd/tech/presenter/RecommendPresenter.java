package com.wd.tech.presenter;

import com.wd.tech.core.http.DataCall;
import com.wd.tech.core.http.IRequest;
import com.wd.tech.core.http.NetworkManager;

import io.reactivex.Observable;

public class RecommendPresenter extends BasePresenter {

    private int page = 1;
    private boolean isRefresh = true;

    public RecommendPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    public Observable observable(Object... args) {
        isRefresh = (Boolean) args[0];//是否需要刷新
        if (isRefresh) {//刷新
            page = 1;
        } else {
            page++;
        }
        IRequest iRequest = NetworkManager.getInstance().create(IRequest.class);
        return iRequest.recommendList((int) args[1], (String) args[2], page, (int) args[3]);
    }

    public boolean isResresh() {
        return isRefresh;
    }

}
