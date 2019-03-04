package com.wd.tech.frag;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.wd.tech.R;
import com.wd.tech.adapter.CommunityAdapter;
import com.wd.tech.bean.CommunityListBean;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.WDApplication;
import com.wd.tech.core.WDFragment;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.dao.DaoMaster;
import com.wd.tech.dao.DaoSession;
import com.wd.tech.dao.UserDao;
import com.wd.tech.myview.PerfectActivity;
import com.wd.tech.presenter.AddCommentPresenter;
import com.wd.tech.presenter.CommentListPresenter;
import com.wd.tech.presenter.CommunitPresenter;
import com.wd.tech.presenter.DoTheTaskPresenter;
import com.wd.tech.view.AddCircleActivity;

import java.util.List;

import javax.sql.CommonDataSource;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.Field;

public class FragCommunity extends WDFragment  {
    @BindView(R.id.frag03_xie)
    ImageView imageView;
    @BindView(R.id.com_recy)
    XRecyclerView recycler;
    @BindView(R.id.frag_03_edText)
    EditText editText;
    @BindView(R.id.frag_03_text)
    TextView textView;
    @BindView(R.id.frag_03_line)
    LinearLayout linearLayout;
    private CommunityAdapter communityAdapter;
    private CommunitPresenter communitPresenter;
    private DoTheTaskPresenter doTheTaskPresenter = new DoTheTaskPresenter(new DoTheTaskCall());
    private User user;

    @Override
    public String getPageName() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_03;
    }

    @Override
    protected void initView() {

        user = WDActivity.getUser(getActivity());

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),AddCircleActivity.class));
            }
        });
        communitPresenter = new CommunitPresenter(new ComData());
        communitPresenter.request(true);
        ButterKnife.bind(getActivity());
        communityAdapter = new CommunityAdapter(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(communityAdapter);
        communityAdapter.OnclickPl(new CommunityAdapter.Onclick() {
            @Override
            public void OnclickPl(View view,final int s) {
                linearLayout.setVisibility(View.VISIBLE);
                DaoSession daoSession = DaoMaster.newDevSession(getActivity(), UserDao.TABLENAME);
                UserDao userDao = daoSession.getUserDao();
                final List<User> list = userDao.queryBuilder().where(UserDao.Properties.Statu.eq("1")).build().list();
                if (list.size()>0) {
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            user = list.get(0);
                            String trim = editText.getText().toString().trim();
                            AddCommentPresenter addCommentPresenter = new AddCommentPresenter(new AddData());
                            addCommentPresenter.request(user.getUserId(), user.getSessionId(),s,trim);
                        }
                    });

                }else {
                    Toast.makeText(getActivity(), "请先登录!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        recycler.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
//
//                if (communitPresenter.isRunning()){
//                    recycler.refreshComplete();
//                    recycler.loadMoreComplete();
//                }
                communityAdapter.clearlist();
                communitPresenter.request(user.getUserId(),user.getSessionId(),true);
            }

            @Override
            public void onLoadMore() {
//                if (communitPresenter.isRunning()){
//                    recycler.refreshComplete();
//                    recycler.loadMoreComplete();
//                }
                communitPresenter.request(user.getUserId(),user.getSessionId(),false);
            }
        });
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dx < 0){
                    editText.setText("");
                    linearLayout.setVisibility(View.GONE);
                }else {
                    editText.setText("");
                    linearLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    class ComData implements DataCall<Result<List<CommunityListBean>>>{
         @Override
         public void success(Result<List<CommunityListBean>> data) {
             recycler.refreshComplete();
             recycler.loadMoreComplete();
             communityAdapter.addList(data.getResult());
             communityAdapter.notifyDataSetChanged();
           //  Toast.makeText(getActivity(), data.getResult().get(1).getCommunityCommentVoList().toString()+"1231", Toast.LENGTH_SHORT).show();
         }
         @Override
         public void fail(ApiException e) {
             recycler.refreshComplete();
             recycler.loadMoreComplete();
             Toast.makeText(getContext(), e + "失败", Toast.LENGTH_SHORT).show();

         }
     }

    @Override
    public void onDestroy() {
        super.onDestroy();
        communitPresenter.unBind();
    }

    private class AddData implements DataCall<Result> {
        @Override
        public void success(Result data) {
            Toast.makeText(getActivity(), data.getMessage()+"", Toast.LENGTH_SHORT).show();
            doTheTaskPresenter.request(user.getUserId(),user.getSessionId(),1002);
            linearLayout.setVisibility(View.GONE);
            communityAdapter.notifyDataSetChanged();
            editText.setText("");
            communityAdapter.clearlist();
            communitPresenter.request(user.getUserId(),user.getSessionId(),true);
        }

        @Override
        public void fail(ApiException e) {

        }
    }
    //实现做任务接口
    private class DoTheTaskCall implements DataCall<Result>{
        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")){
                Toast.makeText(getActivity(), ""+data.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
