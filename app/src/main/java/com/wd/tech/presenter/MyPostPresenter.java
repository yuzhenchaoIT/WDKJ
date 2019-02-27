package com.wd.tech.presenter;

import com.wd.tech.core.http.DataCall;
import com.wd.tech.core.http.IRequest;
import com.wd.tech.core.http.NotWorkUtils;

import io.reactivex.Observable;

public class MyPostPresenter extends BasePresenter{
    private int page = 1;
    public MyPostPresenter(DataCall dataCall) {
        super(dataCall);
    }
    public int getPage() {
        return page;
    }
    @Override
    public Observable observable(Object... args) {
        IRequest iRequest = NotWorkUtils.getInstance().create(IRequest.class);
        boolean refresh = (boolean) args[2];
        if (refresh){
            page = 1;
        }else {
            page++;
        }
        return iRequest.findMyPostById((int)args[0],(String) args[1],page,(int)args[3]);
    }
}
