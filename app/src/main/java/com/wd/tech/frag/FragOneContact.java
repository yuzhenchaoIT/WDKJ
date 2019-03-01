package com.wd.tech.frag;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.nostra13.universalimageloader.utils.L;
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
import com.wd.tech.presenter.DeleteFriendRelationPresenter;
import com.wd.tech.presenter.GroupListPersenter;
import com.wd.tech.presenter.TransferFriendGroupPresenter;
import com.wd.tech.util.UIUtils;
import com.wd.tech.view.ChatActivity;
import com.wd.tech.view.HomeActivity;
import com.wd.tech.view.MyFriendnewsActivity;
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
    private String groupName;
    private int black;
    //注意，字符数组不要写成{{"A1,A2,A3,A4"}, {"B1,B2,B3,B4，B5"}, {"C1,C2,C3,C4"}}*/
    private GroupListPersenter listPresenter;
    private String sessionId;
    private String userName;
    private String nickName;
    private String pwd;
    private int userId;
    private User bean;
    private DeleteFriendRelationPresenter deleteFriendRelationPresenter;
    private TransferFriendGroupPresenter transferFriendGroupPresenter;
    private PopupWindow window;
    private View inflate;


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
        deleteFriendRelationPresenter = new DeleteFriendRelationPresenter(new DeleteFriend());
        transferFriendGroupPresenter = new TransferFriendGroupPresenter(new DeleteFriend());
        bean = WDActivity.getUser(getContext());
        if (bean != null) {
            sessionId = bean.getSessionId();
            userId = bean.getUserId();
            userName=bean.getUserName();
            listPresenter.request(userId,sessionId);
            userName=bean.getUserName();
            pwd=bean.getPwd();
            Log.i("eee", "initView: "+userName+"--"+pwd);
            }
        getShow();
        exPandableListview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                FriendInfoList friendInfoList = groups.get(groupPosition).getFriendInfoList().get(childPosition);
                Intent intent=new Intent(getActivity(),ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID,"Xi0Fe118811690458");
                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EMMessage.ChatType.Chat);
                startActivity(intent);
                return true;
            }
        });
        exPandableListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                   long packedPos = ((ExpandableListView) parent).getExpandableListPosition(position);
                   int groupPosition = ExpandableListView.getPackedPositionGroup(packedPos);
                   int childPosition = ExpandableListView.getPackedPositionChild(packedPos);
                   FriendInfoList friendInfoList = groups.get(groupPosition).getFriendInfoList().get(childPosition);
                   int friendUid = friendInfoList.getFriendUid();
                   int tv_width = view.findViewById(R.id.item_pop_show).getWidth();//获取对应的控件view宽度px值
                   int popup_width = dip2px(120);//将popupWindow宽度转为px值(这里的popup宽度是写死了的)
                   int width = (tv_width - inflate.getMeasuredWidth()) / 2;//获取x轴偏移量px

                   window.showAsDropDown(view.findViewById(R.id.item_pop_show),width,0);
                   for (int i = 0; i < groups.size(); i++) {
                       if (groups.get(i).getGroupName().equals("黑名单")){
                           black=groups.get(i).getGroupId();
                       }
                   }

                   getWindow(inflate,friendUid);
                   return true;
               }
               return false;
           }
       });

    }
    private int dip2px(float dipValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private int px2dip(float pxValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    private void getShow() {
        inflate = View.inflate(getContext(), R.layout.popu_item_long_layout, null);
        window = new PopupWindow(inflate, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setTouchable(true);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(new BitmapDrawable());

    }

    private void getWindow(View inflate, final int id) {
        TextView popuItemLongDelete= inflate.findViewById(R.id.popu_item_long_delete);
        TextView popuItemLongBad= inflate.findViewById(R.id.popu_item_long_bad);
        popuItemLongDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFriendRelationPresenter.request(userId,sessionId,id);
                window.dismiss();
            }
        });
        popuItemLongBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (black==0){
                    window.dismiss();
                    UIUtils.showToastSafe("没有黑名单");
                    return;
                }
                transferFriendGroupPresenter.request(userId,sessionId,black,id);
                window.dismiss();
            }
        });
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
                Intent intent1 = new Intent(getContext(), MyFriendnewsActivity.class);
                startActivity(intent1);
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
            return true;
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

    private class DeleteFriend implements DataCall<Result> {
        @Override
        public void success(Result data) {
            UIUtils.showToastSafe(data.getMessage());
            if (data.getStatus().equals("0000")) {
                listPresenter.request(userId,sessionId);

            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
