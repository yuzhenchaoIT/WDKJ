package com.wd.tech.core.http;






import com.wd.tech.core.app.WDKJApp;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author lmx
 * @date 2019/1/24
 * 添加头部拦截
 */
public class OkHeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        int userId = WDKJApp.getShare().getInt("userId", 0);
        String sessionId = WDKJApp.getShare().getString("sessionId", "");
        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("userId", userId + "");
        builder.addHeader("sessionId", sessionId);
        return chain.proceed(builder.build());
    }
}
