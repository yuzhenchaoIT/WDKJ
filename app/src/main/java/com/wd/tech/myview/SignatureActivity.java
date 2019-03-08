package com.wd.tech.myview;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wd.tech.R;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.ModifySigPresenter;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignatureActivity extends WDActivity {


    private EditText mEditQianSig;
    private TextView mTextNumSig;
    private int num = 40;
    private int nownum;
    private SharedPreferences sp;
    private ModifySigPresenter modifySigPresenter;
    private User user;
    private String trim;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_signature;
    }

    @Override
    protected void initView() {
        //绑定
        ButterKnife.bind(this);
        //查询数据库
        user = WDActivity.getUser(this);

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
        //接收值
        sp = getSharedPreferences("signatrue",Context.MODE_PRIVATE);
        String qian = sp.getString("qian", "");
        mEditQianSig.setText(qian);
    }
    //初始化控件方法
    private void Initialize() {
        mEditQianSig = (EditText) findViewById(R.id.medit_qian_sig);
        mTextNumSig = (TextView) findViewById(R.id.mtext_num_sig);
    }
    //点击提交个性签名
    @OnClick(R.id.mbutton_sig)
    public void mbutton(){
        trim = mEditQianSig.getText().toString().trim();
        modifySigPresenter = new ModifySigPresenter(new ModifyCall());
        modifySigPresenter.request(user.getUserId(),user.getSessionId(), trim);
    }
    //实现修改个性签名接口
    class ModifyCall implements DataCall<Result>{

        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")){
                Toast.makeText(SignatureActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("sigqian",trim);
                edit.commit();
                finish();
            }else {
                Toast.makeText(SignatureActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
    //点击按钮返回
    @OnClick(R.id.mreturn)
    public void mreturn(){
        finish();
    }
    @Override
    protected void destoryData() {

    }
}
