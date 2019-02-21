package com.wd.tech.presenter;


import com.wd.tech.bean.BannerBean;
import com.wd.tech.bean.Result;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.core.http.IRequest;
import com.wd.tech.core.http.NotWorkUtils;

import java.util.List;

import io.reactivex.Observable;

public class BannerPresenter extends BasePresenter {

    public BannerPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    public Observable observable(Object... args) {
        IRequest iRequest = NotWorkUtils.getInstance().create(IRequest.class);
        Observable<Result<List<BannerBean>>> bannerShow = iRequest.bannerShow();
        return bannerShow;
    }
}
