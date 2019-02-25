package com.wd.tech.presenter;


import com.wd.tech.bean.BannerBean;
import com.wd.tech.bean.Result;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.core.http.IRequest;
import com.wd.tech.core.http.NotWorkUtils;

import java.util.List;

import io.reactivex.Observable;

public class AddCommentPresenter extends BasePresenter {

    public AddCommentPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    public Observable observable(Object... args) {
        IRequest iRequest = NotWorkUtils.getInstance().create(IRequest.class);

        return iRequest.addCommunityComment((int)args[0],(String)args[1],(int)args[2],(String)args[3]);
    }
}
