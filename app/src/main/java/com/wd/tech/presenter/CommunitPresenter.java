package com.wd.tech.presenter;

import com.wd.tech.bean.HomeListBean;
import com.wd.tech.bean.Result;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.core.http.IRequest;
import com.wd.tech.core.http.NotWorkUtils;

import java.util.List;

import io.reactivex.Observable;

public class CommunitPresenter extends BasePresenter {
    int page =1;
    public CommunitPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    public Observable observable(Object... args) {
        boolean refresh = (boolean)args[0];
        if (refresh){
            page = 1;
        }else{
            page++;
        }
        IRequest iRequest = NotWorkUtils.getInstance().create(IRequest.class);
        return iRequest.communityList(/*(int)(args[0]),(String) args[1]*/page,10);
    }
}
