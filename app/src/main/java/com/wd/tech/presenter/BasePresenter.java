package com.wd.tech.presenter;


import com.wd.tech.bean.Result;
import com.wd.tech.core.exception.CustomException;
import com.wd.tech.core.exception.ResponseTransformer;
import com.wd.tech.core.http.DataCall;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public abstract class BasePresenter {
    private DataCall dataCall;

    private boolean running;

    public BasePresenter(DataCall dataCall) {
        this.dataCall = dataCall;
    }

    //抽象方法
    public abstract Observable observable(Object... args);

    //请求方法
    public void request(Object... args) {
        if (running) {
            return;
        }
        running = true;
        observable(args)
                .compose(ResponseTransformer.handleResult())
                .compose(new ObservableTransformer() {
                    @Override
                    public ObservableSource apply(Observable upstream) {
                        Observable observable = upstream.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                        return observable;
                    }
                })
                .subscribe(new Consumer<Result>() {
                    @Override
                    public void accept(Result result) throws Exception {
                        running = false;
                        if (dataCall != null) {
                            dataCall.success(result);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //处理异常
                        running = false;
                        if (dataCall != null) {
                            dataCall.fail(CustomException.handleException(throwable));
                        }
                    }
                });
    }


    /**
     * 暴露一个运行的方法
     */
    public boolean isRunning() {
        return running;
    }


    /**
     * 释放内存
     */
    public void unBind() {
        dataCall = null;
    }

}
