package com.wd.tech.presenter;




import com.wd.tech.core.http.DataCall;
import com.wd.tech.core.http.IRequest;
import com.wd.tech.core.http.NotWorkUtils;

import io.reactivex.Observable;

/**
 * 用户购买VIP
 */

public class AddFriendGroupPresenter extends BasePresenter {

    public AddFriendGroupPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    public Observable observable(Object... args) {
        IRequest iRequest = NotWorkUtils.getInstance().create(IRequest.class);
        return iRequest.addFriendGroup((int)args[0],(String)args[1],(String) args[2]);
    }


}

