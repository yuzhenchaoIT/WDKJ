package com.wd.tech.presenter;



import com.wd.tech.core.http.DataCall;
import com.wd.tech.core.http.IRequest;
import com.wd.tech.core.http.NotWorkUtils;


import io.reactivex.Observable;


public class DeleteChatRecordPresenter extends BasePresenter {
    public DeleteChatRecordPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    public Observable observable(Object... args) {
        IRequest iRequest = NotWorkUtils.getInstance().create(IRequest.class);
        return iRequest.deleteChatRecord((int)args[0],(String)args[1],(int)args[2]);
    }
}
