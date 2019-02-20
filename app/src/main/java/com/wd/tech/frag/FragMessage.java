package com.wd.tech.frag;

import android.graphics.Color;
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
import android.widget.TextView;

import com.wd.tech.R;
import com.wd.tech.adapter.FragmentViewAdapter;
import com.wd.tech.core.WDFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FragMessage extends WDFragment {
    @BindView(R.id.my_message)
    TextView myMessage;
    @BindView(R.id.my_message_contact)
    TextView myMessageContact;
    @BindView(R.id.my_message_add)
    ImageView myMessageAdd;
    @BindView(R.id.my_message_view_pager)
    ViewPager myMessageViewPager;
    Unbinder unbinder;

    @Override
    public String getPageName() {
        return "Frag_消息";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_02;
    }

    @Override
    protected void initView() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_02,container,false);
        unbinder = ButterKnife.bind(this, view);
        myMessage.setTextColor(Color.WHITE);
        myMessage.setBackgroundResource(R.drawable.text_magess_shape);
        List<Fragment> list = new ArrayList<>();
        list.add(new FragOneMessage());
        list.add(new FragOneContact());
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentViewAdapter(getActivity().getSupportFragmentManager(),list);
        myMessageViewPager.setAdapter(fragmentPagerAdapter);
        myMessageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i==0){
                    myMessage.setTextColor(Color.WHITE);
                    myMessage.setBackgroundResource(R.drawable.text_magess_shape);
                    myMessageContact.setTextColor(Color.BLACK);
                    myMessageContact.setBackgroundResource(R.drawable.text_magess_n_shape);
                }else {
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
                myMessageViewPager.setCurrentItem(0,false);
                break;
            case R.id.my_message_contact:
                myMessageContact.setTextColor(Color.WHITE);
                myMessageContact.setBackgroundResource(R.drawable.text_magess_shape);
                myMessage.setTextColor(Color.BLACK);
                myMessage.setBackgroundResource(R.drawable.text_magess_n_shape);
                myMessageViewPager.setCurrentItem(1,false );
                break;
            case R.id.my_message_add:

                break;
        }
    }

}
