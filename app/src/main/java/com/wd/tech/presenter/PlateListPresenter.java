package com.wd.tech.presenter;


import com.wd.tech.core.http.DataCall;
import com.wd.tech.core.http.IRequest;
import com.wd.tech.core.http.NotWorkUtils;

import io.reactivex.Observable;

public class PlateListPresenter extends BasePresenter {

    private int page = 1;

    public PlateListPresenter(DataCall dataCall) {
        super(dataCall);
    }

    public int getPage() {
        return page;
    }

    @Override
    public Observable observable(Object... args) {
        IRequest iRequest = NotWorkUtils.getInstance().create(IRequest.class);
        boolean refresh = (Boolean) args[0];
        if (refresh) {
            page = 1;
        } else {
            page++;
        }
        return iRequest.plateList((int) args[1], (String) args[2], (int) args[3], page, 10);
    }


}
