package com.wd.tech.presenter;


import com.wd.tech.core.http.DataCall;
import com.wd.tech.core.http.IRequest;
import com.wd.tech.core.http.NotWorkUtils;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * @author dingtao
 * @date 2018/12/28 11:23
 * qq:1940870847
 */
public class BindFaceidPresenter extends BasePresenter {

    public BindFaceidPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    public Observable observable(Object... args) {
        IRequest iRequest = NotWorkUtils.getInstance().create(IRequest.class);
        return iRequest.bindingFaceId((int)args[0],(String)args[1],(String)args[2]);
    }
}
