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
public class AddCircilePresenter extends BasePresenter {

    public AddCircilePresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    public Observable observable(Object... args) {
        IRequest iRequest = NotWorkUtils.getInstance().create(IRequest.class);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("content", String.valueOf(args[2]));
        List<Object> list = (List<Object>) args[3];
        if (list.size()>1) {
            for (int i = 1; i < list.size(); i++) {
                File file = new File((String) list.get(i));
                builder.addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse("multipart/octet-stream"),
                                file));
            }
        }
        return iRequest.fabuquanzi((int)args[0],(String)args[1],builder.build());
    }
}
