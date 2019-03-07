package com.wd.tech.view;

import android.support.v7.widget.LinearLayoutManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.wd.tech.R;
import com.wd.tech.adapter.FindFriendNoticePageListAdapter;
import com.wd.tech.bean.FriendNoticePageList;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.FriendNoticePersenter;
import com.wd.tech.presenter.ReviewFriendPersenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyFriendnewsActivity extends WDActivity implements XRecyclerView.LoadingListener {

    @BindView(R.id.myFridend_back)
    ImageView myFridendBack;
    @BindView(R.id.fridend_news_view)
    XRecyclerView fridendNewsView;
    private List<FriendNoticePageList> friendNoticePageLists;
    private FindFriendNoticePageListAdapter adapter;
    //注意，字符数组不要写成{{"A1,A2,A3,A4"}, {"B1,B2,B3,B4，B5"}, {"C1,C2,C3,C4"}}*/
    private FriendNoticePersenter friendNoticePersenter;
    private ReviewFriendPersenter reviewFriendPersenter;
    private String sessionId;
    private int userId;
    private User bean;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_friendnews;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        friendNoticePersenter = new FriendNoticePersenter(new FriendNot());
        reviewFriendPersenter = new ReviewFriendPersenter(new Review());
        bean = WDActivity.getUser(this);
        if (bean != null) {
            sessionId = bean.getSessionId();
            userId = bean.getUserId();
            }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        fridendNewsView.setLayoutManager(linearLayoutManager);
        adapter = new FindFriendNoticePageListAdapter(this);
        fridendNewsView.setAdapter(adapter);
        fridendNewsView.setLoadingListener(this);
        friendNoticePersenter.request(userId,sessionId,1,5);
        adapter.setOnCheckedLinter(new FindFriendNoticePageListAdapter.CheckedLinter() {
            @Override
            public void checkY(int noticeId) {
                reviewFriendPersenter.request(userId,sessionId,noticeId,2);
                Toast.makeText(MyFriendnewsActivity.this,"同意",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void checkN(int noticeId) {
                reviewFriendPersenter.request(userId,sessionId,noticeId,3);
                Toast.makeText(MyFriendnewsActivity.this,"拒绝",Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void destoryData() {

    }

    @OnClick(R.id.myFridend_back)
    public void onViewClicked() {
    }

    @Override
    public void onRefresh() {
        if (friendNoticePersenter.isRunning()){
            fridendNewsView.refreshComplete();
            return;
        }
        friendNoticePersenter.request(userId,sessionId,1,5);
    }

    @Override
    public void onLoadMore() {
        if (friendNoticePersenter.isRunning()){
            fridendNewsView.loadMoreComplete();
            return;
        }
        friendNoticePersenter.request(userId,sessionId,1,5);
    }

    private class FriendNot implements DataCall<Result<List<FriendNoticePageList>>> {
        @Override
        public void success(Result<List<FriendNoticePageList>> data) {
            fridendNewsView.refreshComplete();
            fridendNewsView.loadMoreComplete();
            adapter.clear();
            if (data.getStatus().equals("0000")){
                adapter.addAll(data.getResult());
                adapter.notifyDataSetChanged();

            }

        }

        @Override
        public void fail(ApiException e) {

        }
    }


    private class Review implements DataCall<Result> {
        @Override
        public void success(Result data) {
            friendNoticePersenter.request(userId,sessionId,1,5);
        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
