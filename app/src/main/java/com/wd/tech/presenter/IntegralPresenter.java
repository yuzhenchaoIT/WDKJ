package com.wd.tech.presenter;

import com.wd.tech.core.http.DataCall;
import com.wd.tech.core.http.IRequest;
import com.wd.tech.core.http.NotWorkUtils;

import io.reactivex.Observable;

public class IntegralPresenter extends BasePresenter{
    public IntegralPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    public Observable observable(Object... args) {
        IRequest iRequest = NotWorkUtils.getInstance().create(IRequest.class);
        return iRequest.findUserIntegral((int)args[0],(String) args[1]);
    }
}
