package com.wd.tech.core;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.wd.tech.R;

public class MyDialog extends Dialog {
    Activity context;
    View view;

    public MyDialog(Activity context,View view) {
        super(context);
        this.context = context;
        this.view = view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(R.color.colorAccent);
        /*view = View.inflate(this.context, R.layout.dialog_user_headimg, null);*/
        setContentView(view);

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = WindowManager.LayoutParams.WRAP_CONTENT;   //高度设置为屏幕的0.6
        p.width = WindowManager.LayoutParams.MATCH_PARENT;    //宽度设置为屏幕的0.95
        getWindow().setAttributes(p);     //设置生效
    }
}
