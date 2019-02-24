package com.wd.tech.presenter;

import com.wd.tech.core.http.DataCall;
import com.wd.tech.core.http.IRequest;
import com.wd.tech.core.http.NotWorkUtils;

import io.reactivex.Observable;

public class AllInfoPlatePresenter extends BasePresenter {


    public AllInfoPlatePresenter(DataCall dataCall) {
        super(dataCall);
    }



    @Override
    public Observable observable(Object... args) {
        IRequest iRequest = NotWorkUtils.getInstance().create(IRequest.class);
        return iRequest.allInfoPlate();
    }
}
