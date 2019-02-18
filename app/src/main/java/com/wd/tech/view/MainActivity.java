package com.wd.tech.view;

import android.os.Handler;
import android.os.Message;

import com.wd.tech.R;
import com.wd.tech.core.WDActivity;

public class MainActivity extends WDActivity {
    int i = 3;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                i--;
                if(i<=0){
                    intent(HomeActivity.class);
                    finish();
                    return;
                }
                handler.sendEmptyMessageDelayed(1,1000);
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        handler.sendEmptyMessageDelayed(1,1000);
    }

    @Override
    protected void destoryData() {
        handler.removeMessages(1);
    }
}
