package com.wd.tech.presenter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.example.myframe.bean.Result;
import com.example.myframe.core.DataCall;
import com.example.myframe.core.WDActivity;
import com.example.myframe.core.exception.CustomException;
import com.example.myframe.core.exception.ResponseTransformer;
import com.example.myframe.core.http.NetworkManager;
import com.example.myframe.util.UIUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author dingtao
 * @date 2018/12/28 11:30
 * qq:1940870847
 */
public abstract class BasePresenter {
    private DataCall dataCall;

    private boolean running;
    private Observable observable;

    public BasePresenter(DataCall dataCall) {
        this.dataCall = dataCall;
    }

    protected abstract Observable observable(Object... args);

    public void reqeust(Object... args) {
        if (running) {
            return;
        }

        running = true;
        boolean net = NetworkManager.instance().isNet(WDActivity.getForegroundActivity());
        if (!net){
            UIUtils.showToastSafe("没有网络");
            //Log.i("aaa", "reqeust: "+"没有网络");
            if (dataCall!=null) {
                dataCall.fail(CustomException.handleException(new Throwable()));
            }
            return;
        }
        observable = observable(args);
        observable.compose(ResponseTransformer.handleResult())//添加了一个全局的异常-观察者
                .compose(new ObservableTransformer() {
                    @Override
                    public ObservableSource apply(Observable upstream) {
                        return upstream.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                })
//                .subscribeOn(Schedulers.newThread())//将请求调度到子线程上
//                .observeOn(AndroidSchedulers.mainThread())//观察响应结果，把响应结果调度到主线程中处理
                .subscribe(new Consumer<Result>() {
                    @Override
                    public void accept(Result result) throws Exception {
                        running = false;
                       if (result.getStatus().equals("9999")){
                           Dialog dialog = new AlertDialog.Builder(WDActivity.getForegroundActivity()).setMessage("请登录")
                                   .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialogInterface, int i) {
                                          // WDActivity.getForegroundActivity().startActivity(new Intent(WDActivity.getForegroundActivity(), LoginActivity.class));
                                       }
                                   })
                                   .setNegativeButton("取消",null)
                                   .show();

                       }else {
                        if (dataCall!=null){
                            dataCall.success(result);
                        }
                       }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        running = false;
                        // 处理异常
//                        UIUtils.showToastSafe("请求失败");
                        //通过异常工具类封装成自定义的ApiException
                        if (dataCall!=null) {
                            dataCall.fail(CustomException.handleException(throwable));
                        }
                    }
                });

    }

    public void cancelRequest() {
        if (observable!=null){
            observable.unsubscribeOn(Schedulers.io());
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void unBind() {
        dataCall = null;
    }
}
