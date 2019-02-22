package com.wd.tech.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.wd.tech.R;
import com.wd.tech.adapter.MyImageAdapter;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private PhotoViewPager mViewPager;
    private int currentPosition;
    private MyImageAdapter adapter;
    private TextView mTvImageCount;
    private TextView mTvSaveImage;
    private List<String> Urls = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        initView();
        initData();
    }
    private void initView() {
        mViewPager =  findViewById(R.id.view_pager_photo);
        mTvImageCount =  findViewById(R.id.tv_image_count);
        mTvSaveImage =  findViewById(R.id.tv_save_image_photo);
        ArrayList<String> list = getIntent().getStringArrayListExtra("list");
        Urls.addAll(list);
    }

    private void initData() {
        Intent intent = getIntent();
        currentPosition = intent.getIntExtra("currentPosition", 0);
        adapter = new MyImageAdapter(Urls, this);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(currentPosition, false);
        mTvImageCount.setText(currentPosition+1 + "/" + Urls.size());
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPosition = position;
                mTvImageCount.setText(currentPosition + 1 + "/" + Urls.size());
            }
        });
    }
}
