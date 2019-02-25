package com.wd.tech.presenter;

import com.wd.tech.core.http.DataCall;
import com.wd.tech.core.http.IRequest;
import com.wd.tech.core.http.NotWorkUtils;

import io.reactivex.Observable;

/**
 * 详情p层
 *
 * @author lmx
 * @date 2019/2/23
 */
public class InforDetailsPresenter extends BasePresenter {
    public InforDetailsPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    public Observable observable(Object... args) {
        IRequest iRequest = NotWorkUtils.getInstance().create(IRequest.class);
        return iRequest.informationDetails((int) args[0], (String) args[1], (int) args[2]);
    }
}
