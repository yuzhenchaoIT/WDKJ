package com.wd.tech.view;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wd.tech.R;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.CreateGroupPersenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FlockActivity extends WDActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.qun_edittext_name)
    EditText qunEdittextName;
    @BindView(R.id.qun_edittext_content)
    EditText qunEdittextContent;
    @BindView(R.id.text_login)
    TextView textLogin;
    private CreateGroupPersenter createGroupPersenter;
    private String sessionId;
    private int userId;
    private User bean;
    private String message;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_flock;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        bean = WDActivity.getUser(this);
    }

    @Override
    protected void destoryData() {

    }

    @OnClick({R.id.back, R.id.text_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.text_login:

                createGroupPersenter = new CreateGroupPersenter(new CreatGroup());
                if (bean != null) {
                    sessionId = bean.getSessionId();
                    userId = bean.getUserId();
                    createGroupPersenter.request(userId,sessionId,qunEdittextName.getText().toString(),qunEdittextContent.getText().toString());
                    }

                finish();
                break;
        }
    }

    private class CreatGroup implements DataCall<Result> {
        @Override
        public void success(Result data) {
            message = data.getMessage();

            Toast.makeText(FlockActivity.this,message,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
