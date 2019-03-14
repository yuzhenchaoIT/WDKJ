package com.wd.tech.core;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.wd.tech.bean.Conversation;
import com.wd.tech.dao.ConversationDao;
import com.wd.tech.face.FaceDB;
import com.wd.tech.util.DaoUtils;


import java.io.File;
import java.util.Iterator;
import java.util.List;


public class WDApplication extends Application {

    private final String TAG = this.getClass().toString();
   public FaceDB mFaceDB;
    Uri mImage;

    /** 主线程ID */
    private static int mMainThreadId = -1;
    /** 主线程ID */
    private static Thread mMainThread;
    /** 主线程Handler */
    private static Handler mMainThreadHandler;
    /** 主线程Looper */
    private static Looper mMainLooper;

    /**
     * context 全局唯一的上下文
     */
    private static Context context;

    private static SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();

        mFaceDB = new FaceDB(this.getExternalCacheDir().getPath());
        mImage = null;

        context=this;
        mMainThreadId = android.os.Process.myTid();
        mMainThread = Thread.currentThread();
        mMainThreadHandler = new Handler();
        mMainLooper = getMainLooper();
        sharedPreferences = getSharedPreferences("share.xml",MODE_PRIVATE);
        Fresco.initialize(this);//图片加载框架初始化
        EaseUI.getInstance().init(this,null);
        EMClient.getInstance().setDebugMode(true);
        EaseUI.getInstance().init(this,null);
        EMClient.getInstance().setDebugMode(true);
        EaseUI.getInstance().setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
            @Override
            public EaseUser getUser(String username) {
                username = username.toLowerCase();
                EaseUser easeUser = new EaseUser(username);
                List<Conversation> aa = DaoUtils.getInstance().getConversationDao().loadAll();
                Conversation conversation = DaoUtils.getInstance().getConversationDao().queryBuilder().where(ConversationDao.Properties.UserName.eq(username)).build().unique();
                if (conversation!=null) {
                    easeUser.setNickname(conversation.getNickName());
                    easeUser.setAvatar(conversation.getHeadPic());
                }
                return easeUser;
            }
        });


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this) ;
    }
   /* private ImagePipelineConfig getConfig() {
        File file = new File(Environment.getExternalStorageDirectory()+File.separator+"image");
        if (!file.exists()){
            file.mkdir();
        }
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setMainDiskCacheConfig(DiskCacheConfig.newBuilder(this)
                        .setBaseDirectoryPath(file).build())
                .build();
        return config;
    }*/

    public static SharedPreferences getShare(){
        return sharedPreferences;
    }

    /**
     * @author: 康海涛 QQ2541849981
     * @describe: 获取全局Application的上下文
     * @return 全局唯一的上下文
     */
    public static Context getContext() {
        return context;
    }

    /** 获取主线程ID */
    public static int getMainThreadId() {
        return mMainThreadId;
    }

    /** 获取主线程 */
    public static Thread getMainThread() {
        return mMainThread;
    }

    /** 获取主线程的handler */
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    /** 获取主线程的looper */
    public static Looper getMainThreadLooper() {
        return mMainLooper;
    }


    public void setCaptureImage(Uri uri) {
        mImage = uri;
    }

    public Uri getCaptureImage() {
        return mImage;
    }

    /**
     * @param path
     * @return
     */
    public static Bitmap decodeImage(String path) {
        Bitmap res;
        try {
            ExifInterface exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            BitmapFactory.Options op = new BitmapFactory.Options();
            op.inSampleSize = 1;
            op.inJustDecodeBounds = false;
            //op.inMutable = true;
            res = BitmapFactory.decodeFile(path, op);
            //rotate and scale.
            Matrix matrix = new Matrix();

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                matrix.postRotate(90);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                matrix.postRotate(180);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                matrix.postRotate(270);
            }

            Bitmap temp = Bitmap.createBitmap(res, 0, 0, res.getWidth(), res.getHeight(), matrix, true);
            Log.d("com.arcsoft", "check target Image:" + temp.getWidth() + "X" + temp.getHeight());

            if (!temp.equals(res)) {
                res.recycle();
            }
            return temp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}