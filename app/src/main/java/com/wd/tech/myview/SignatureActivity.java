package com.wd.tech.myview;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wd.tech.R;
import com.wd.tech.core.WDActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignatureActivity extends WDActivity {


    private EditText mEditQianSig;
    private TextView mTextNumSig;
    private int num = 40;
    private int nownum;
    private SharedPreferences sp;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_signature;
    }

    @Override
    protected void initView() {
        //绑定
        ButterKnife.bind(this);
        //初始化控件
        Initialize();
        //设置监听
        mEditQianSig.addTextChangedListener(new TextWatcher() {
            private CharSequence wordNum;//记录输入的字数
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wordNum= s;//实时记录输入的字数
            }

            @Override
            public void afterTextChanged(Editable s) {
                nownum = mEditQianSig.length();
                //TextView显示剩余字数
                mTextNumSig.setText("" +nownum+"/"+num);
                selectionStart = mEditQianSig.getSelectionStart();
                selectionEnd = mEditQianSig.getSelectionEnd();
                if (wordNum.length() > num) {
                    //删除多余输入的字（不会显示出来）
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    mEditQianSig.setText(s);
                    mEditQianSig.setSelection(tempSelection);//设置光标在最后
                }
            }
        });
        //sp
        sp = getSharedPreferences("signatrue",Context.MODE_PRIVATE);
        String qian = sp.getString("qian", "");
        mEditQianSig.setText(qian);
    }
    //初始化控件方法
    private void Initialize() {
        mEditQianSig = (EditText) findViewById(R.id.mEditQianSig);
        mTextNumSig = (TextView) findViewById(R.id.mTextNumSig);
    }
    //点击提交个性签名
    @OnClick(R.id.mButtonSig)
    public void mbutton(){
        String signatrue = mEditQianSig.getText().toString().trim();
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("sig",signatrue);
        edit.commit();
        finish();
    }
    @Override
    protected void destoryData() {

    }
}
