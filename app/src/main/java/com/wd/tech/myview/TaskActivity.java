package com.wd.tech.myview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.wd.tech.R;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.TaskList;
import com.wd.tech.bean.User;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.TaskPresenter;

import java.util.List;

import butterknife.ButterKnife;

public class TaskActivity extends WDActivity {
    private TaskPresenter taskPresenter;
    private User user;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_task;
    }

    @Override
    protected void initView() {
        //绑定
        ButterKnife.bind(this);
        //查询数据库
        user = WDActivity.getUser(this);
        //设置数据
        taskPresenter = new TaskPresenter(new TaskCall());
        taskPresenter.request(user.getUserId(),user.getSessionId());
    }
    //实现查询用户任务列表接口
    private class TaskCall implements DataCall<Result<List<TaskList>>>{

        @Override
        public void success(Result<List<TaskList>> data) {
            if (data.getStatus().equals("0000")){
                List<TaskList> result = data.getResult();
                Toast.makeText(TaskActivity.this, ""+result.toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
    @Override
    protected void destoryData() {

    }
}
