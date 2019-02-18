package com.wd.tech.core.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author dingtao
 * @date 2018/12/28 10:07
 * qq:1940870847
 */
public class NetworkManager {

    private static NetworkManager mInstance;
    private Retrofit retrofit;
    //    private static final String BASE_URL = "http://mobile.bwstudent.com/small/";
    private static final String BASE_URL = "http://mobile.bwstudent.com/";

    private NetworkManager() {
        init();
    }

    public static NetworkManager getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkManager();
        }
        return mInstance;
    }

    /**
     * 初始化必要对象和参数
     */
    private void init() {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new OkHeaderInterceptor())
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();


        // 初始化Retrofit
        retrofit = new Retrofit.Builder()
                .client(client)//添加自定义的OKHhttp
                .baseUrl(BASE_URL)//设置base_url
                //rxjava处理回调的数据
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //gson数据转换器
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    //把接口的注解翻译为OKhttp请求
    public <T> T create(final Class<T> service) {
        return retrofit.create(service);
    }
}
