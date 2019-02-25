package com.wd.tech.presenter;

import com.wd.tech.core.http.DataCall;
import com.wd.tech.core.http.IRequest;
import com.wd.tech.core.http.NotWorkUtils;

import io.reactivex.Observable;

public class PerfectPresenter extends BasePresenter{
    public PerfectPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    public Observable observable(Object... args) {
        IRequest iRequest = NotWorkUtils.getInstance().create(IRequest.class);
        return iRequest.perfectUserInfo((int)args[0],(String) args[1],(String) args[2],(int)args[3],(String) args[4],(String) args[5],(String) args[6]);
    }
}
