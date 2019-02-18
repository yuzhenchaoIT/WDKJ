package com.wd.tech.core.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;

public class WDKJApp extends Application {
    /**
     * 主线程ID
     */
    private static int mMainThreadId = -1;
    /**
     * 主线程ID
     */
    private static Thread mMainThread;
    /**
     * 主线程Handler
     */
    private static Handler mMainThreadHandler;
    /**
     * 主线程Looper
     */
    private static Looper mMainLooper;

    /**
     * context 全局唯一的上下文
     */
    private static Context context;

    private static SharedPreferences sharedPreferences;


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        mMainThreadId = android.os.Process.myTid();
        mMainThread = Thread.currentThread();
        mMainThreadHandler = new Handler();
        mMainLooper = getMainLooper();
        sharedPreferences = getSharedPreferences("share.xml", MODE_PRIVATE);

        //初始化fresco
        Fresco.initialize(this);
        //第三方登录
       // registToWX();
        //Bugly集成
      //  CrashReport.initCrashReport(getApplicationContext(), "89c03187f1", false);
        //友盟集成
       // UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);


    }

//    private void registToWX() {
//        //AppConst.WEIXIN.APP_ID是指你应用在微信开放平台上的AppID，记得替换。
//        mWxApi = WXAPIFactory.createWXAPI(this, "wxb3852e6a6b7d9516", false);
//        // 将该app注册到微信
//        mWxApi.registerApp("wxb3852e6a6b7d9516");
//    }


    public static SharedPreferences getShare() {
        return sharedPreferences;
    }
    /**
     * @return 全局唯一的上下文
     * @describe: 获取全局Application的上下文
     */
    public static Context getContext() {
        return context;
    }

    /**
     * 获取主线程ID
     */
    public static int getMainThreadId() {
        return mMainThreadId;
    }

    /**
     * 获取主线程
     */
    public static Thread getMainThread() {
        return mMainThread;
    }

    /**
     * 获取主线程的handler
     */
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    /**
     * 获取主线程的looper
     */
    public static Looper getMainThreadLooper() {
        return mMainLooper;
    }
}
