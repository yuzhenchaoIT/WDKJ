package com.wd.tech.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.View;
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
import com.wd.tech.myview.SiginActivity;
import com.wd.tech.presenter.AddCircilePresenter;
import com.wd.tech.presenter.DoTheTaskPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnTextChanged;

public class AddCircleActivity extends WDActivity implements View.OnClickListener {

    List<Object> objects = new ArrayList<>();
    private AddImageAdapter add_image_adapter;
    private TextView textSum;
    private EditText editTex;
    private AddCircilePresenter addCircilePresenter;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 6;
    private DoTheTaskPresenter doTheTaskPresenter = new DoTheTaskPresenter(new DoTheTaskCall());
    private User user;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_adcircle;
    }
    @OnTextChanged(value = R.id.id_editor_detail, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void editTextDetailChange(Editable editable) {
        int detailLength = editable.length();
        textSum.setText(detailLength + "/300");
//        if (detailLength == 139) {
//            islMaxCount = true;
//        }
//        // 不知道为什么执行俩次，所以增加一个标识符去标识
//        if (detailLength == 140 && islMaxCount) {
//            UIHelper.getShortToast(self, (String) StringUtils.getResourceContent(self, Convention.RESOURCE_TYPE_STRING, R.string.string_editor_detail_input_limit));
//            islMaxCount = false;
//        }
        RecyclerView recyclerView;
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


        objects.add(R.drawable.common_nav_btn_add_n_hdpi);
        bo_image_list.setLayoutManager(new GridLayoutManager(this,4));
        add_image_adapter = new AddImageAdapter(this, objects, new AddImageAdapter.Dakai() {
            @Override
            public void onDakaiXiangCe() {
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(intent1,0);
//                Intent  intent = new Intent();
//                              // 指定开启系统相机的Action
//                          intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//                             intent.addCategory(Intent.CATEGORY_DEFAULT);
//                startActivityForResult(intent,2);
            }
        });
        bo_image_list.setAdapter(add_image_adapter);
        //读写权限
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
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
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_send:
                user = WDActivity.getUser(this);
                if (user != null){
                    addCircilePresenter.request(user.getUserId(), user.getSessionId(),editTex.getText(),objects);
                }else {
                    Toast.makeText(this, "请先登录！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.add_qx:
                finish();
                break;
        }
    }
    //实现做任务接口
    private class DoTheTaskCall implements DataCall<Result>{
        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")){
                Toast.makeText(AddCircleActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
            }else {
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
}
