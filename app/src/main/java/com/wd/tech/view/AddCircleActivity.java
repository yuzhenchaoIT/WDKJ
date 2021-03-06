package com.wd.tech.view;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wd.tech.R;
import com.wd.tech.adapter.AddImageAdapter;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.AddCircilePresenter;
import com.wd.tech.presenter.DoTheTaskPresenter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.OnTextChanged;

public class AddCircleActivity extends WDActivity implements View.OnClickListener {

    List<Object> objects = new ArrayList<>();
    private AddImageAdapter add_image_adapter;
    private TextView textSum;
    private EditText editTex;
    private static final int TAKE_PICTURE = 1;
    private AddCircilePresenter addCircilePresenter;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 6;
    private DoTheTaskPresenter doTheTaskPresenter = new DoTheTaskPresenter(new DoTheTaskCall());
    private User user;
    private Dialog dialog;
    private String picturePath;
    List<File> files = new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.activity_adcircle;
    }
    @OnTextChanged(value = R.id.id_editor_detail, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void editTextDetailChange(Editable editable) {
        int detailLength = editable.length();
        textSum.setText(detailLength + "/300");
        if (detailLength==300){
            Toast.makeText(this, "别输入了,", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void initView() {
        textSum = (TextView) findViewById(R.id.id_editor_detail_font_count);
        editTex = (EditText) findViewById(R.id.id_editor_detail);
        findViewById(R.id.add_send).setOnClickListener(this);
        findViewById(R.id.add_qx).setOnClickListener(this);
        RecyclerView bo_image_list = (RecyclerView) findViewById(R.id.bo_image_list);
        addCircilePresenter = new AddCircilePresenter(new AddData());
        objects.add(R.drawable.adds);

        bo_image_list.setLayoutManager(new GridLayoutManager(this,4));
        add_image_adapter = new AddImageAdapter(this, objects, new AddImageAdapter.Dakai() {
            @Override
            public void onDakaiXiangCe() {
                doaLog();
            }
        });
        add_image_adapter.onitemclick(new AddImageAdapter.Onitemclick() {
            @Override
            public void Onitemclick(View view, int s) {

                objects.remove(s);
                files.remove((s-1));
                add_image_adapter.notifyDataSetChanged();
            }
        });
        bo_image_list.setAdapter(add_image_adapter);
        //读写权限
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            //权限发生了改变 true  //  false 小米
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){
                new AlertDialog.Builder(this).setTitle("title")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 请求授权
                                ActivityCompat.requestPermissions(AddCircleActivity.this,new String[]{Manifest.permission.CAMERA},1);
                            }
                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
            }else {
                ActivityCompat.requestPermissions(AddCircleActivity.this,new String[]{Manifest.permission.CAMERA},1);
            }
        }else{
        }
    }
    @Override
    protected void destoryData() {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null){
            return;
        }
        if(requestCode==0){
            String filePath = getFilePath(null,requestCode,data);
            objects.add(filePath);
            add_image_adapter.notifyDataSetChanged();
            Uri data1 = data.getData();
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor actualimagecursor = managedQuery(data1,proj,null,null,null);
            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            String img_path = actualimagecursor.getString(actual_image_column_index);
             File file = new File(img_path);
            files.add(file);
        }
        if (requestCode==2){
                Bundle extras = data.getExtras();
                Bitmap data1 = (Bitmap) extras.get("data");
                File file = compressImage(data1);
            String uri = file.getAbsolutePath();
            objects.add(uri);
                files.add(file);
            add_image_adapter.notifyDataSetChanged();
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
        if(requestCode == 1){
            // camear 权限回调
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // 表示用户授权
                Toast.makeText(this, " user Permission" , Toast.LENGTH_SHORT).show();
            } else {
                //用户拒绝权限
                Toast.makeText(this, " no Permission" , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_send:
                user = WDActivity.getUser(this);
                if (user != null){
                    addCircilePresenter.request(user.getUserId(), user.getSessionId(),editTex.getText(),files);
                }
                break;
            case R.id.add_qx:
                finish();
                break;
            case R.id.mcamear:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 2);
                dialog.dismiss();
                break;
            case R.id.mpictrue:
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(intent1,0);
                dialog.dismiss();
                break;
            case R.id.cancel:
                    dialog.dismiss();
                break;

        }
    }
    //实现做任务接口
    private class DoTheTaskCall implements DataCall<Result>{
        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")){
                Toast.makeText(AddCircleActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
    private class AddData implements DataCall<Result>  {
        @Override
        public void success(Result data) {
            if(data.getStatus().equals("0000")){
                Toast.makeText(AddCircleActivity.this, data.getMessage()+"", Toast.LENGTH_SHORT).show();
                doTheTaskPresenter.request(user.getUserId(),user.getSessionId(),1003);
                finish();
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
    public String getFilePath(String fileName, int requestCode, Intent data) {
        if (requestCode == 1) {
            return fileName;
        } else if (requestCode == 0) {
            Uri uri = data.getData();
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            String img_path = actualimagecursor.getString(actual_image_column_index);
            // 4.0以上平台会自动关闭cursor,所以加上版本判断,OK
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH){
                actualimagecursor.close();
            }
            return img_path;
        }
        return null;
    }
    private void doaLog(){
        dialog = new Dialog(this, R.style.DialogTheme);
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_item, null);
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

}
