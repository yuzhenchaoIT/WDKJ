package com.wd.tech.view;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.bean.FindUserJoinGroup;
import com.wd.tech.bean.FriendInfoList;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.FindGroupPersenter;
import com.wd.tech.presenter.GroupListPersenter;
import com.wd.tech.presenter.UserJoinedGroupPersenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyGroupActivity extends WDActivity {
    @BindView(R.id.myGroup_back)
    ImageView myGroupBack;
    @BindView(R.id.search_edit_mygroup)
    EditText searchEditMygroup;
    @BindView(R.id.search_image_mygroup)
    ImageView searchImageMygroup;
    @BindView(R.id.list_group)
    ListView listGroup;
    private List<FindUserJoinGroup> Mygroup;

    //注意，字符数组不要写成{{"A1,A2,A3,A4"}, {"B1,B2,B3,B4，B5"}, {"C1,C2,C3,C4"}}*/
    private UserJoinedGroupPersenter userJoinedGroupPersenter;
    private String sessionId;
    private int userId;
    private User bean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_group;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        userJoinedGroupPersenter = new UserJoinedGroupPersenter(new MyGroup());
        bean = WDActivity.getUser(this);
        if (bean != null) {
            sessionId = bean.getSessionId();
            userId = bean.getUserId();
            userJoinedGroupPersenter.request(userId,sessionId);

        }

    }

    @Override
    protected void destoryData() {

    }


    @OnClick({R.id.myGroup_back, R.id.search_edit_mygroup})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.myGroup_back:
                break;
            case R.id.search_edit_mygroup:
                break;
        }
    }

    private class MyGroup implements DataCall<Result<List<FindUserJoinGroup>>> {
        @Override
        public void success(Result<List<FindUserJoinGroup>> data) {
            if (data.getStatus().equals("0000")){
                Mygroup = data.getResult();
                listGroup.setAdapter(new MyListView());
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }

    private class MyListView extends BaseAdapter {
        @Override
        public int getCount() {
            return Mygroup.size();
        }

        @Override
        public Object getItem(int position) {
            return Mygroup.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyHolder holder;
            if (convertView == null){
                convertView = View.inflate(parent.getContext(),R.layout.mygroup_list_item,null);
                holder = new MyHolder();
                holder.groupImage = convertView.findViewById(R.id.image_group_head);
                holder.groupName = convertView.findViewById(R.id.text_group_name);
                convertView.setTag(holder);
            }else{
                holder = (MyHolder) convertView.getTag();
            }
            FindUserJoinGroup friendInfoList = Mygroup.get(position);

            holder.groupImage.setImageURI(friendInfoList.getGroupImage());
            holder.groupName.setText(friendInfoList.getGroupName());
            return convertView;
        }
        class MyHolder{
            SimpleDraweeView groupImage;
            TextView groupName;

        }
    }
}
