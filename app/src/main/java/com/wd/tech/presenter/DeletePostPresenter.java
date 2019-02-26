package com.wd.tech.presenter;

import com.wd.tech.core.http.DataCall;
import com.wd.tech.core.http.IRequest;
import com.wd.tech.core.http.NotWorkUtils;

import io.reactivex.Observable;

public class DeletePostPresenter extends BasePresenter{
    public DeletePostPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    public Observable observable(Object... args) {
        IRequest iRequest = NotWorkUtils.getInstance().create(IRequest.class);
        return iRequest.deletePost((int)args[0],(String) args[1],(String) args[2]);
    }
}
