package com.wd.tech.core.http;


import com.wd.tech.core.exception.ApiException;

public interface DataCall<T> {

    void success(T data);
    void fail(ApiException e);

}
