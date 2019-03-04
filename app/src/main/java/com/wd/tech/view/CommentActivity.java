package com.wd.tech.view;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.adapter.DePlAdapter;
import com.wd.tech.bean.CommentList;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.CommentListPresenter;

import java.util.List;

public class CommentActivity extends WDActivity {

    private int id;

    private TextView name,sum;
    private RecyclerView recyclerView;
    private DePlAdapter dePlAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_comment;
    }

    @Override
    protected void initView() {
        id = getIntent().getIntExtra("id", 0);
        String headpic = getIntent().getStringExtra("headpic");
        String namestr = getIntent().getStringExtra("name");
        User user = WDActivity.getUser(this);
        if(user!=null){
            CommentListPresenter commentListPresenter = new CommentListPresenter(new ComData());
            commentListPresenter.request(user.getUserId(),user.getSessionId(),id);
        }else {
            Toast.makeText(this, "请先登录！", Toast.LENGTH_SHORT).show();
            finish();
        }
         SimpleDraweeView simpleDraweeView = (SimpleDraweeView) findViewById(R.id.ac_head);
        simpleDraweeView.setImageURI(Uri.parse(headpic));
        name = (TextView) findViewById(R.id.ac_name);
        name.setText(namestr);
        sum = (TextView) findViewById(R.id.ac_sum);
        recyclerView = (RecyclerView) findViewById(R.id.ac_recyler);
        dePlAdapter = new DePlAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(dePlAdapter);
        findViewById(R.id.ac_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void destoryData() {

    }

    private class ComData implements DataCall<Result<List<CommentList>>> {
        @Override
        public void success(Result<List<CommentList>> data) {
          //  Toast.makeText(CommentActivity.this, data.getResult().toString()+"", Toast.LENGTH_SHORT).show();
            dePlAdapter.addList(data.getResult());
            dePlAdapter.notifyDataSetChanged();
            sum.setText(data.getResult().size()+"条评论");

        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
