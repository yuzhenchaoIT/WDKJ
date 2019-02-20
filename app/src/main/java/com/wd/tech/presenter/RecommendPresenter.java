package com.wd.tech.presenter;


import com.wd.tech.core.http.DataCall;
import com.wd.tech.core.http.IRequest;
import com.wd.tech.core.http.NotWorkUtils;

import java.util.List;

import io.reactivex.Observable;

public class RecommendPresenter extends BasePresenter {

    private int page = 1;

    public RecommendPresenter(DataCall dataCall) {
        super(dataCall);
    }

    public int getPage() {
        return page;
    }

    @Override
    public Observable observable(Object... args) {
        IRequest iRequest = NotWorkUtils.getInstance().create(IRequest.class);
        boolean refresh = (Boolean) args[0];
        if (refresh) {
            page = 1;
        } else {
            page++;
        }
        return iRequest.recommendList((int) args[1], (String) args[2], page, 100);
    }


}
