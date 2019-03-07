package com.wd.tech.myview;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.bean.QueryUser;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.MyDialog;
import com.wd.tech.core.PickView;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.WDApplication;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.dao.DaoMaster;
import com.wd.tech.dao.DaoSession;
import com.wd.tech.dao.UserDao;
import com.wd.tech.face.DetecterActivity;
import com.wd.tech.face.Register1Activity;
import com.wd.tech.presenter.ModifyEmailPresenter;
import com.wd.tech.presenter.ModifyHeadPicPresenter;
import com.wd.tech.presenter.ModifyNickNamePresenter;
import com.wd.tech.presenter.PerfectPresenter;
import com.wd.tech.presenter.QueryUserPresenter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetUpActivity extends WDActivity implements View.OnClickListener {

    private final String TAG = this.getClass().toString();

    private static final int REQUEST_CODE_IMAGE_CAMERA = 5;
    private static final int REQUEST_CODE_IMAGE_OP = 6;
    private static final int REQUEST_CODE_OP = 7;

    private QueryUserPresenter queryUserPresenter;
    private SimpleDraweeView mImageUp;
    private TextView mTextNameUp, mTextSexUp, mTextDateUp,mTextEmailUp, mTextPhoneUp, mTextjfUp, mTextVipUp,mTextQianSet;
    private View inflate;
    private TextView camera;
    private TextView pic;
    private TextView cancel;
    private Dialog dialog;
    private User user;
    private QueryUser result;
    private int sex;
    private ModifyEmailPresenter modifyEmailPresenter;
    private ModifyNickNamePresenter modifyNickNamePresenter;
    private ModifyHeadPicPresenter modifyHeadPicPresenter = new ModifyHeadPicPresenter(new HeadPicCall());
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 6;
    private PerfectPresenter perfectPresenter = new PerfectPresenter(new PerfectCall());
    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_up;
    }

    @Override
    protected void initView() {
        //绑定
        ButterKnife.bind(this);
        //初始化控件
        Initialize();
        //查询数据库
        user = WDActivity.getUser(this);
        queryUserPresenter = new QueryUserPresenter(new QueryUserCall());
        queryUserPresenter.request(user.getUserId(), user.getSessionId());
    }

    //初始化控件方法
    private void Initialize() {
        mImageUp = (SimpleDraweeView) findViewById(R.id.mimage_up);
        mTextNameUp = (TextView) findViewById(R.id.mtext_name_up);
        mTextSexUp = (TextView) findViewById(R.id.mtext_sex_up);
        mTextDateUp = (TextView) findViewById(R.id.mtext_date_up);
        mTextPhoneUp = (TextView) findViewById(R.id.mtext_phone_up);
        mTextEmailUp = (TextView) findViewById(R.id.mtext_email_up);
        mTextjfUp = (TextView) findViewById(R.id.mtextjf_up);
        mTextVipUp = (TextView) findViewById(R.id.mtext_vip_up);
        mTextQianSet = (TextView) findViewById(R.id.mtext_qian_set);
        //点击修改用户名
        mTextNameUp.setOnClickListener(this);
        //点击修改邮箱
       mTextEmailUp.setOnClickListener(this);
       //点击修改性别
        mTextSexUp.setOnClickListener(this);
        //点击修改出生日期
        mTextDateUp.setOnClickListener(this);
        TextView mtext_face_up = (TextView) findViewById(R.id.mtext_face_up);
        mtext_face_up.setOnClickListener(this);
    }

    //实现查询用户信息接口
    private class QueryUserCall implements DataCall<Result<QueryUser>> {
        @Override
        public void success(Result<QueryUser> data) {
            if (data.getStatus().equals("0000")) {
                result = data.getResult();
                mImageUp.setImageURI(result.getHeadPic());
                mTextNameUp.setText(result.getNickName());
                sex = result.getSex();
                if (sex == 1) {
                    mTextSexUp.setText("男");
                } else {
                    mTextSexUp.setText("女");
                }
                long birthday = result.getBirthday();
                Date date = new Date(birthday);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String s = format.format(date);
                mTextDateUp.setText(s);
                mTextPhoneUp.setText(result.getPhone());
                mTextEmailUp.setText(result.getEmail());
                mTextjfUp.setText(result.getIntegral() + "");
                int whetherVip = result.getWhetherVip();
                if (whetherVip == 1) {
                    mTextVipUp.setText("是");
                } else {
                    mTextVipUp.setText("否");
                }
                mTextQianSet.setText(result.getSignature());
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }

    //点击头像弹出提示框
    @OnClick(R.id.mimage_up)
    public void miangeu() {
        //读写权限
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
        dialog = new Dialog(this, R.style.DialogTheme);
        //填充对话框的布局
        inflate = LayoutInflater.from(this).inflate(R.layout.dialog_item, null);
        //初始化控件
        inflate.findViewById(R.id.mcamear).setOnClickListener(this);
        inflate.findViewById(R.id.mpictrue).setOnClickListener(this);
        inflate.findViewById(R.id.cancel).setOnClickListener(this);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);

        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;//设置Dialog距离底部的距离//
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //  将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框

    }
    //点击拍照和相册
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mcamear:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,2);
                dialog.cancel();
                break;
            case R.id.mpictrue:
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(intent1,1);
                dialog.cancel();
                break;
            case R.id.cancel:
                dialog.cancel();
                break;
            case R.id.mtext_name_up://修改用户名
                final EditText editText = new EditText(this);
                editText.setText(result.getNickName());
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("修改用户名称");
                builder.setView(editText);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String s = editText.getText().toString().trim();
                        mTextNameUp.setText(s);
                        modifyNickNamePresenter = new ModifyNickNamePresenter(new NickNameCall());
                        modifyNickNamePresenter.request(user.getUserId(),user.getSessionId(),s);
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
                break;
            case R.id.mtext_sex_up://修改性别
                View view = View.inflate(SetUpActivity.this,R.layout.dialogsex_item,null);
                dialog = new MyDialog(SetUpActivity.this, view);
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                PickView pvPickView = view.findViewById(R.id.pvPickView);
                TextView tv_sexdialog_sure = view.findViewById(R.id.tv_sexdialog_sure);
                TextView tv_sexdialog_cancel = view.findViewById(R.id.tv_sexdialog_cancel);
                ArrayList<String> grade = new ArrayList<>();
                grade.add("男");
                grade.add("女");
                pvPickView.setData(grade);
                pvPickView.setOnSelectListener(new PickView.onSelectListener() {
                    @Override
                    public void onSelect(String text) {
                        mTextSexUp.setText(text);
                    }
                });
                tv_sexdialog_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                tv_sexdialog_sure.setOnClickListener(new View.OnClickListener() {
                    //声明当前性别
                    int currentSex;
                    @Override
                    public void onClick(View v) {
                        String sex = mTextSexUp.getText().toString().trim();
                        if (sex.equals("男")){
                            currentSex = 1;
                        }else {
                            currentSex = 2;
                        }
                        perfectPresenter = new PerfectPresenter(new PerfectCall());
                        perfectPresenter.request(user.getUserId(),user.getSessionId(),result.getNickName(),currentSex,result.getSignature(),result.getBirthday()+"",result.getEmail());
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.mtext_email_up://修改邮箱
                final EditText editemail = new EditText(SetUpActivity.this);
                editemail.setText(result.getEmail());
                AlertDialog builder3 = new AlertDialog.Builder(SetUpActivity.this)
                        .setTitle("修改邮箱")
                        .setView(editemail)//设置输入框
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String trim = editemail.getText().toString().trim();
                                mTextEmailUp.setText(trim);
                                modifyEmailPresenter = new ModifyEmailPresenter(new EmailCall());
                                modifyEmailPresenter.request(user.getUserId(), user.getSessionId(),trim);
                            }
                        }).setNegativeButton("取消", null).create();
                builder3.show();
                break;
            case R.id.mtext_date_up:
                TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

                        String s = sf.format(date) + "";
                        perfectPresenter = new PerfectPresenter(new PerfectCall());
                        perfectPresenter.request(user.getUserId(),user.getSessionId(),result.getNickName(),result.getSex(),result.getSignature(),s,result.getEmail());
                    }
                })
                        .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示.setCancelText("取消")
                        .setSubmitText("确定").build();
                pvTime.show();

                break;
            case R.id.mtext_face_up:
                new AlertDialog.Builder(this)
                        .setTitle("请选择注册方式")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setItems(new String[]{"打开图片", "拍摄照片"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 1:
                                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                        ContentValues values = new ContentValues(1);
                                        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                                        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                                        ((WDApplication)(SetUpActivity.this.getApplicationContext())).setCaptureImage(uri);
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                        startActivityForResult(intent, REQUEST_CODE_IMAGE_CAMERA);
                                        break;
                                    case 0:
                                        Intent getImageByalbum = new Intent(Intent.ACTION_GET_CONTENT);
                                        getImageByalbum.addCategory(Intent.CATEGORY_OPENABLE);
                                        getImageByalbum.setType("image/jpeg");
                                        startActivityForResult(getImageByalbum, REQUEST_CODE_IMAGE_OP);
                                        break;
                                    default:;
                                }
                            }
                        })
                        .show();
                break;
        }
    }
    //上传头像回调方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE_OP && resultCode == RESULT_OK) {
            Uri mPath = data.getData();
            String file = getPath(mPath);
            Bitmap bmp = WDApplication.decodeImage(file);
            if (bmp == null || bmp.getWidth() <= 0 || bmp.getHeight() <= 0 ) {
                Log.e(TAG, "error");
            } else {
                Log.i(TAG, "bmp [" + bmp.getWidth() + "," + bmp.getHeight());
            }
            startRegister(bmp, file);
        } else if (requestCode == REQUEST_CODE_OP) {
            Log.i(TAG, "RESULT =" + resultCode);
            if (data == null) {
                return;
            }
            Bundle bundle = data.getExtras();
            String path = bundle.getString("imagePath");
            Log.i(TAG, "path="+path);
        } else if (requestCode == REQUEST_CODE_IMAGE_CAMERA && resultCode == RESULT_OK) {
            Uri mPath = ((WDApplication)(SetUpActivity.this.getApplicationContext())).getCaptureImage();
            String file = getPath(mPath);
            Bitmap bmp = WDApplication.decodeImage(file);
            startRegister(bmp, file);
        }

        if(data==null){
            return;
        }
        if(requestCode==1){
            String icon = getFilePath("icon",0,data);
            modifyHeadPicPresenter.request(user.getUserId(),user.getSessionId(),icon,1);
        }else if (requestCode == 2){
            Bundle extras = data.getExtras();
            Bitmap data1 = (Bitmap) extras.get("data");
            File file = compressImage(data1);
            modifyHeadPicPresenter.request(user.getUserId(),user.getSessionId(),file,2);
        }
    }
    //申请权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
                if (grantResults[i]==-1){
                    finish();
                }
            }
        }
    }
    //实现修改用户名接口
    private class NickNameCall implements DataCall<Result>{
        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")){

            }else {
                Toast.makeText(SetUpActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
            }


        }
        @Override
        public void fail(ApiException e) {

        }
    }
    //实现修改邮箱接口
    private class EmailCall implements DataCall<Result>{
        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")){

            }else {
                Toast.makeText(SetUpActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
    //实现用户修改头像
    private class HeadPicCall implements DataCall<Result>{
        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")){
                Toast.makeText(SetUpActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
                queryUserPresenter.request(user.getUserId(), user.getSessionId());
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
    //点击跳转个性签名
    @OnClick(R.id.mlinear_set)
    public void mimage() {
        Intent intent = new Intent(SetUpActivity.this, SignatureActivity.class);
        startActivity(intent);
    }

    //点击退出登录
    @OnClick(R.id.mtexttui_up)
    public void tui() {
        User user = WDActivity.getUser(this);
        if (user != null) {
            DaoSession daoSession = DaoMaster.newDevSession(SetUpActivity.this, UserDao.TABLENAME);
            UserDao userDao = daoSession.getUserDao();
            userDao.deleteAll();
            finish();
        }
    }
    //点击修改密码
    @OnClick(R.id.mtextxiu_up)
    public void xiu(){
        //跳转
        Intent intent = new Intent(SetUpActivity.this, ChangePassActivity.class);
        startActivity(intent);
    }
    //实现完善个人信息接口
    class PerfectCall implements DataCall<Result>{
        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")){
                Toast.makeText(SetUpActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
                queryUserPresenter.request(user.getUserId(), user.getSessionId());
            }else {


                Toast.makeText(SetUpActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        queryUserPresenter.request(user.getUserId(), user.getSessionId());
    }

    //点击按钮返回
    @OnClick(R.id.mreturn)
    public void mreturn() {
        finish();
    }

    @Override
    protected void destoryData() {

    }
    /**
     * 压缩图片（质量压缩）
     * @param bitmap
     */
    public static File compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 500) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            long length = baos.toByteArray().length;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String filename = format.format(date);
        File file = new File(Environment.getExternalStorageDirectory(),filename+".png");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                Log.e("---",e.getMessage());
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            Log.e("----",e.getMessage());
            e.printStackTrace();
        }
        recycleBitmap(bitmap);
        return file;
    }
    public static void recycleBitmap(Bitmap... bitmaps) {
        if (bitmaps==null) {
            return;
        }
        for (Bitmap bm : bitmaps) {
            if (null != bm && !bm.isRecycled()) {
                bm.recycle();
            }
        }
    }


    /**
     * @param uri
     * @return
     */
    private String getPath(Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(this, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                } else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(this, contentUri, null, null);
                } else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[] {
                            split[1]
                    };

                    return getDataColumn(this, contentUri, selection, selectionArgs);
                }
            }
        }
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor actualimagecursor = this.getContentResolver().query(uri, proj, null, null, null);
        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualimagecursor.moveToFirst();
        String img_path = actualimagecursor.getString(actual_image_column_index);
        String end = img_path.substring(img_path.length() - 4);
        if (0 != end.compareToIgnoreCase(".jpg") && 0 != end.compareToIgnoreCase(".png")) {
            return null;
        }
        return img_path;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param mBitmap
     */
    private void startRegister(Bitmap mBitmap, String file) {
        Intent it = new Intent(SetUpActivity.this, Register1Activity.class);
        Bundle bundle = new Bundle();
        bundle.putString("imagePath", file);
        it.putExtras(bundle);
        startActivityForResult(it, REQUEST_CODE_OP);
    }

    private void startDetector(int camera) {
        Intent it = new Intent(SetUpActivity.this, DetecterActivity.class);
        it.putExtra("Camera", camera);
        startActivityForResult(it, REQUEST_CODE_OP);
    }


}
