package com.wd.tech.presenter.InforMation;

import com.wd.tech.core.http.DataCall;
import com.wd.tech.core.http.IRequest;
import com.wd.tech.core.http.NotWorkUtils;
import com.wd.tech.presenter.BasePresenter;

import io.reactivex.Observable;

public class PayByIntegralPresenter extends BasePresenter {

    public PayByIntegralPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    public Observable observable(Object... args) {
        IRequest iRequest = NotWorkUtils.getInstance().create(IRequest.class);
        return iRequest.infoPayByIntegral((int) args[0], (String) args[1], (int) args[2], (int) args[3]);
    }
}
