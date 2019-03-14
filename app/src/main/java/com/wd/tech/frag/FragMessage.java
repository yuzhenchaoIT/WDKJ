package com.wd.tech.frag;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.wd.tech.R;
import com.wd.tech.adapter.FragmentViewAdapter;
import com.wd.tech.core.WDFragment;
import com.wd.tech.view.AddFriendsActivity;
import com.wd.tech.view.ChatActivity;
import com.wd.tech.view.FlockActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FragMessage extends WDFragment implements View.OnClickListener {
    @BindView(R.id.my_message)
    TextView myMessage;
    @BindView(R.id.my_message_contact)
    TextView myMessageContact;
    @BindView(R.id.my_message_add)
    ImageView myMessageAdd;
    @BindView(R.id.my_message_view_pager)
    ViewPager myMessageViewPager;
    Unbinder unbinder;
    private PopupWindow popupWindow;
    private ImageView imageadd;
    private EaseConversationListFragment easeConversationListFragment;


    @Override
    public String getPageName() {
        return "Frag_消息";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_02;
    }

    @Override
    protected void initView() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_02, container, false);

        unbinder = ButterKnife.bind(this, view);
        imageadd = view.findViewById(R.id.my_message_add);
        imageadd.setOnClickListener(this);
        myMessage.setTextColor(Color.WHITE);
        myMessage.setBackgroundResource(R.drawable.text_magess_shape);
        List<Fragment> list = new ArrayList<>();
        easeConversationListFragment = new EaseConversationListFragment();
        list.add(easeConversationListFragment);
        list.add(new FragOneContact());
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentViewAdapter(getActivity().getSupportFragmentManager(), list);
        easeConversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {
                EMConversation.EMConversationType type = conversation.getType();
                if (type== EMConversation.EMConversationType.Chat){

                    Intent intent = new Intent(getContext(), ChatActivity.class);
                    intent.putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId());
                    //intent.putExtra("userNames", conversation.conversationId());
                    //intent.putExtra("friendInfoList",friendInfoList);
                    startActivity(intent);
                }else {
//                    Intent intent = new Intent(getContext(), WantGroupChatActivity.class);
//                    intent.putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId());
//                    intent.putExtra("userNames", conversation.conversationId());
//                    startActivity(intent);
                }


            }
        });
        myMessageViewPager.setAdapter(fragmentPagerAdapter);
        myMessageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    myMessage.setTextColor(Color.WHITE);
                    myMessage.setBackgroundResource(R.drawable.text_magess_shape);
                    myMessageContact.setTextColor(Color.BLACK);
                    myMessageContact.setBackgroundResource(R.drawable.text_magess_n_shape);
                } else {
                    myMessageContact.setTextColor(Color.WHITE);
                    myMessageContact.setBackgroundResource(R.drawable.text_magess_shape);
                    myMessage.setTextColor(Color.BLACK);
                    myMessage.setBackgroundResource(R.drawable.text_magess_n_shape);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.my_message, R.id.my_message_contact, R.id.my_message_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.my_message:
                myMessage.setTextColor(Color.WHITE);
                myMessage.setBackgroundResource(R.drawable.text_magess_shape);
                myMessageContact.setTextColor(Color.BLACK);
                myMessageContact.setBackgroundResource(R.drawable.text_magess_n_shape);
                myMessageViewPager.setCurrentItem(0, false);
                break;
            case R.id.my_message_contact:
                myMessageContact.setTextColor(Color.WHITE);
                myMessageContact.setBackgroundResource(R.drawable.text_magess_shape);
                myMessage.setTextColor(Color.BLACK);
                myMessage.setBackgroundResource(R.drawable.text_magess_n_shape);
                myMessageViewPager.setCurrentItem(1, false);
                break;
            case R.id.my_message_add:

                break;
        }
    }

    @Override
    public void onClick(View v) {
        View view = View.inflate(getContext(), R.layout.append_popwind, null);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        TextView text_you=view.findViewById(R.id.text_you);
        text_you.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddFriendsActivity.class);
                startActivity(intent);
            }
        });
        TextView text_qun=view.findViewById(R.id.text_qun);
        text_qun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FlockActivity.class);
                startActivity(intent);
            }
        });
        popupWindow.showAsDropDown(imageadd,0,55);


    }
}
