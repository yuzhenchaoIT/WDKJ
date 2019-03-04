package com.wd.tech.presenter;



import com.wd.tech.core.http.DataCall;
import com.wd.tech.core.http.IRequest;
import com.wd.tech.core.http.NotWorkUtils;


import io.reactivex.Observable;

/**
 * Created by ${LinJiangtao}
 * on 2019/2/21
 */
public class FindFriendGroupListPresenter extends BasePresenter {
    public FindFriendGroupListPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    public Observable observable(Object... args) {
        IRequest iRequest = NotWorkUtils.getInstance().create(IRequest.class);
        return iRequest.findFriendGroupList((int)args[0],(String)args[1]);
    }
}
