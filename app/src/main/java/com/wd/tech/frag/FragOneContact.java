package com.wd.tech.frag;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.wd.tech.R;
import com.wd.tech.bean.FriendInfoList;
import com.wd.tech.bean.InitFriendlist;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.WDFragment;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.GroupListPersenter;
import com.wd.tech.view.MyGroupActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class FragOneContact extends WDFragment {

    @BindView(R.id.search_edit)
    EditText searchEdit;
    @BindView(R.id.search_image)
    ImageView searchImage;
    @BindView(R.id.x_recyclerview)
    SmartRefreshLayout pullToRefreshScrollView;
    @BindView(R.id.layout_newsyou)
    LinearLayout layoutNewsyou;
    @BindView(R.id.layout_qun)
    LinearLayout layoutQun;
    @BindView(R.id.ex_pandable_listview)
    ExpandableListView exPandableListview;
    //Model：定义的数据
    private List<InitFriendlist> groups;
    private List<FriendInfoList> childs;

    //注意，字符数组不要写成{{"A1,A2,A3,A4"}, {"B1,B2,B3,B4，B5"}, {"C1,C2,C3,C4"}}*/
    private GroupListPersenter listPresenter;
    private String sessionId;
    private int userId;
    private User bean;


    @Override
    public String getPageName() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.linkman_layout;
    }

    @Override
    protected void initView() {
        Fresco.initialize(getContext());
        listPresenter = new GroupListPersenter(new InitFr());
        bean = WDActivity.getUser(getContext());
        if (bean != null) {
            sessionId = bean.getSessionId();
            userId = bean.getUserId();
            listPresenter.request(userId,sessionId);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bean != null) {
            sessionId = bean.getSessionId();
            userId = bean.getUserId();
            listPresenter.request(userId,sessionId);

        }
    }

    @OnClick({R.id.layout_newsyou, R.id.layout_qun})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_newsyou:
                break;
            case R.id.layout_qun:
                Intent intent = new Intent(getContext(), MyGroupActivity.class);
                startActivity(intent);
                break;
        }
    }



    class MyExpandableListView extends BaseExpandableListAdapter {


        @Override
        public int getGroupCount() {
            return groups.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return groups.get(groupPosition).getFriendInfoList().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groups.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return groups.get(groupPosition).getFriendInfoList().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
        //父布局
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHodler hodler;
            if (convertView == null){
                convertView = View.inflate(parent.getContext(),R.layout.expandablelistview_one_item,null);
                hodler = new GroupHodler();
                hodler.groupname = convertView.findViewById(R.id.tv_group);
                hodler.groupsum=convertView.findViewById(R.id.tv_sum);
                convertView.setTag(hodler);
            }else{
                hodler = (GroupHodler)convertView.getTag();
            }
            InitFriendlist initFriendlist = groups.get(groupPosition);

            hodler.groupname.setText(initFriendlist.getGroupName());
            hodler.groupsum.setText(initFriendlist.getCurrentNumber()+"");
            return convertView;
        }
        //子布局
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            MyHolder holder;
            if (convertView == null){
                convertView = View.inflate(parent.getContext(),R.layout.expandablelistview_two_item,null);
                holder = new MyHolder();
                holder.headric = convertView.findViewById(R.id.sdv_child);
                holder.qianming = convertView.findViewById(R.id.tv_qian);
                holder.name = convertView.findViewById(R.id.tv_child);
                convertView.setTag(holder);
            }else{
                holder = (MyHolder) convertView.getTag();
            }
            FriendInfoList friendInfoList = groups.get(groupPosition).getFriendInfoList().get(childPosition);
            holder.headric.setImageURI(friendInfoList.getHeadPic());
            holder.qianming.setText(friendInfoList.getSignature());
            holder.name.setText(friendInfoList.getNickName());
            return convertView;
        }


        //父框件
        class GroupHodler {
            TextView groupname,groupsum;

        }
        //子框件 (一个复选框 ,, 文字 ,, 价格 ,, 图片 ,, 还有自定义一个类)
        class MyHolder{
            SimpleDraweeView headric;
            TextView qianming;
            TextView name;

        }
    }

    private class InitFr implements DataCall<Result<List<InitFriendlist>>> {
        @Override
        public void success(Result<List<InitFriendlist>> data) {
            if (data.getStatus().equals("0000")){
                groups = data.getResult();
                exPandableListview.setAdapter(new MyExpandableListView());
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
