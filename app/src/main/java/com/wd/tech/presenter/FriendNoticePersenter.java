package com.wd.tech.presenter;

import com.wd.tech.core.http.DataCall;
import com.wd.tech.core.http.IRequest;
import com.wd.tech.core.http.NotWorkUtils;

import io.reactivex.Observable;

public class FriendNoticePersenter extends BasePresenter {
    public FriendNoticePersenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    public Observable observable(Object... args) {
        IRequest iRequest = NotWorkUtils.getInstance().create(IRequest.class);
        return iRequest.friendNoticePageList((int) args[0],(String) args[1],(int) args[2],(int) args[3]);
    }
}
