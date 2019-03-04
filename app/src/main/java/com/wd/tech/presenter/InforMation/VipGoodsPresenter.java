package com.wd.tech.presenter.InforMation;

import com.wd.tech.core.http.DataCall;
import com.wd.tech.core.http.IRequest;
import com.wd.tech.core.http.NotWorkUtils;
import com.wd.tech.presenter.BasePresenter;

import io.reactivex.Observable;

public class VipGoodsPresenter extends BasePresenter {

    public VipGoodsPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    public Observable observable(Object... args) {
        IRequest iRequest = NotWorkUtils.getInstance().create(IRequest.class);
        return iRequest.findVipCommodityList();
    }
}
