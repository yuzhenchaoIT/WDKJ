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
public class CommentListPresenter extends BasePresenter {
    public CommentListPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    public Observable observable(Object... args) {
        IRequest iRequest = NotWorkUtils.getInstance().create(IRequest.class);
        return iRequest.findCommunityUserCommentList(1010,"15380296316761010", (int) args[0],1,3);
    }
}
