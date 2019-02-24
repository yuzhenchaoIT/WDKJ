package com.wd.tech.presenter;

import com.wd.tech.core.http.DataCall;
import com.wd.tech.core.http.IRequest;
import com.wd.tech.core.http.NotWorkUtils;
import com.wd.tech.frag.FragOneContact;

import io.reactivex.Observable;

public class GroupListPersenter extends BasePresenter {


    public GroupListPersenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    public Observable observable(Object... args) {

        IRequest iRequest = NotWorkUtils.getInstance().create(IRequest.class);
        return iRequest.groupList((int) args[0],(String) args[1]);
    }
}
