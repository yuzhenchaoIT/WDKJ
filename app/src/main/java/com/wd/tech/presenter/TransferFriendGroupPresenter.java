package com.wd.tech.presenter;




import com.wd.tech.core.http.DataCall;
import com.wd.tech.core.http.IRequest;
import com.wd.tech.core.http.NotWorkUtils;

import io.reactivex.Observable;


public class TransferFriendGroupPresenter extends BasePresenter {
    public TransferFriendGroupPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    public Observable observable(Object... args) {
        IRequest iRequest = NotWorkUtils.getInstance().create(IRequest.class);
        return iRequest.transferFriendGroup((int)args[0],(String)args[1],(int)args[2],(int)args[3]);
    }
}
