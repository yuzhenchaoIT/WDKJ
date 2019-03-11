package com.wd.tech.myview;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.wd.tech.R;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.TaskList;
import com.wd.tech.bean.User;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.frag.FragCommunity;
import com.wd.tech.presenter.TaskPresenter;
import com.wd.tech.view.AddCircleActivity;
import com.wd.tech.view.HomeActivity;

import java.util.List;

import butterknife.ButterKnife;

public class TaskActivity extends WDActivity implements View.OnClickListener {
    private TaskPresenter taskPresenter;
    private User user;
    private Button mButtonyqd, mButtonyqc,mButtonyws,
            mButtonypj,mButtonyck,mButtonqfx;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_task;
    }

    @Override
    protected void initView() {
        //绑定
        ButterKnife.bind(this);
        //初始化控件
        findViewById(R.id.mbutton_qqd).setOnClickListener(this);
        findViewById(R.id.mbutton_qft).setOnClickListener(this);
        findViewById(R.id.mbutton_qws).setOnClickListener(this);
        findViewById(R.id.mbutton_qpj).setOnClickListener(this);
        findViewById(R.id.mbutton_qck).setOnClickListener(this);
        mButtonyqd = (Button) findViewById(R.id.mButton_yqd);
        mButtonyqc = (Button) findViewById(R.id.mbutton_ywc);
        mButtonyws = (Button) findViewById(R.id.mbutton_yws);
        mButtonypj = (Button) findViewById(R.id.mbutton_ypj);
        mButtonyck = (Button) findViewById(R.id.mbutton_yck);
        mButtonqfx = (Button) findViewById(R.id.mbutton_qfx);
        //查询数据库
        user = WDActivity.getUser(this);
        //设置数据
        taskPresenter = new TaskPresenter(new TaskCall());
        taskPresenter.request(user.getUserId(),user.getSessionId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mbutton_qqd:
                Intent intent = new Intent(TaskActivity.this, SiginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.mbutton_qft:
                Intent intent1 = new Intent(TaskActivity.this, AddCircleActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.mbutton_qws:
                Intent intent2 = new Intent(TaskActivity.this, PerfectActivity.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.mbutton_qpj:
                Intent intent3 = new Intent(TaskActivity.this, HomeActivity.class);
                intent3.putExtra("jump",2);
                startActivity(intent3);
                finish();
                break;
            case R.id.mbutton_qck:
                Intent intent4 = new Intent(TaskActivity.this, HomeActivity.class);
                startActivity(intent4);
                finish();
                break;
            case R.id.mbutton_qfx:
                Intent intent5 = new Intent(TaskActivity.this, HomeActivity.class);
                startActivity(intent5);
                finish();
                break;
        }
    }

    //实现查询用户任务列表接口
    private class TaskCall implements DataCall<Result<List<TaskList>>>{

        @Override
        public void success(Result<List<TaskList>> data) {
            if (data.getStatus().equals("0000")){
                List<TaskList> result = data.getResult();
                for (int i = 0; i < result.size(); i++) {
                    int status = result.get(i).getStatus();
                    if (result.get(i).getTaskId()==1001&&status==1){
                        mButtonyqd.setVisibility(View.VISIBLE);
                    }else if (result.get(i).getTaskId()==1003&&status==1){
                        mButtonyqc.setVisibility(View.VISIBLE);
                    }else if (result.get(i).getTaskId()==1006&&status==1){
                        mButtonyws.setVisibility(View.VISIBLE);
                    }else if (result.get(i).getTaskId()==1002&&status==1){
                        mButtonypj.setVisibility(View.VISIBLE);
                    }else if (result.get(i).getTaskId()==1005&&status==1){
                        mButtonyck.setVisibility(View.VISIBLE);
                    }else if (result.get(i).getTaskId()==1004&&status==1){
                        mButtonqfx.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        taskPresenter.request(user.getUserId(),user.getSessionId());
    }

    @Override
    protected void destoryData() {

    }
}
