package com.wd.tech.frag;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import com.wd.tech.util.ListDataSave;
import com.wd.tech.view.AddCircleActivity;
import com.wd.tech.view.LoginActivity;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
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
    private ListDataSave listDataSave;

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
                if (user!=null){
                    startActivity(new Intent(getActivity(),AddCircleActivity.class));
                }else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }

            }
        });
        if (user!=null){
            communitPresenter = new CommunitPresenter(new ComData());
            communitPresenter.request(user.getUserId(),user.getSessionId(),true);
        }else {
            communitPresenter = new CommunitPresenter(new ComData());
            communitPresenter.request(1010,"15320748258726",true);
        }

        ButterKnife.bind(getActivity());
        communityAdapter = new CommunityAdapter(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(communityAdapter);
        communityAdapter.OnclickPl(new CommunityAdapter.Onclick() {
            @Override
            public void OnclickPl(View view,final int s) {
                linearLayout.setVisibility(View.VISIBLE);

                if (user!=null) {
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String trim = editText.getText().toString().trim();
                            AddCommentPresenter addCommentPresenter = new AddCommentPresenter(new AddData());
                            addCommentPresenter.request(user.getUserId(), user.getSessionId(),s,trim);
                        }
                    });

                }else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
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
                if (user!=null){
                    communityAdapter.clearlist();
                    communitPresenter.request(user.getUserId(),user.getSessionId(),true);
                }else {
                    communityAdapter.clearlist();
                    communitPresenter.request(1010,"15320748258726",true);
                }

            }

            @Override
            public void onLoadMore() {
//                if (communitPresenter.isRunning()){
//                    recycler.refreshComplete();
//                    recycler.loadMoreComplete();
//                }
                if (user!=null){
                    communitPresenter.request(user.getUserId(),user.getSessionId(),false);
                }else {
                    communitPresenter.request(1010,"15320748258726",false);
                }

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
        //数据保存
        listDataSave = new ListDataSave(getActivity(),"Circile");

    }

    class ComData implements DataCall<Result<List<CommunityListBean>>>{
         @Override
         public void success(Result<List<CommunityListBean>> data) {
             recycler.refreshComplete();
             recycler.loadMoreComplete();
             communityAdapter.addList(data.getResult());
             communityAdapter.notifyDataSetChanged();
           //  Toast.makeText(getActivity(), data.getResult().get(1).getCommunityCommentVoList().toString()+"1231", Toast.LENGTH_SHORT).show();
             List<CommunityListBean> list = data.getResult();
             listDataSave.setDataList("list",list);

         }
         @Override
         public void fail(ApiException e) {
             recycler.refreshComplete();
             recycler.loadMoreComplete();
             Toast.makeText(getContext(), e + "失败", Toast.LENGTH_SHORT).show();
             int size = communityAdapter.getSize();
             if(size==0){
                 List<CommunityListBean> list = listDataSave.getDataList("list");
                 communityAdapter.addList(list);
                 communityAdapter.notifyDataSetChanged();
             }
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

    @Override
    public void onResume() {
        super.onResume();
           communitPresenter = new CommunitPresenter(new ComData());
         user = WDActivity.getUser(getActivity());
        if (user !=null){
            communityAdapter.clearlist();
            communitPresenter.request(this.user.getUserId(), this.user.getSessionId(),true);
        }else {
            communityAdapter.clearlist();
            communitPresenter.request(1010,"15320748258726",true);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
      //  super.onSaveInstanceState(outState);
    }
}
