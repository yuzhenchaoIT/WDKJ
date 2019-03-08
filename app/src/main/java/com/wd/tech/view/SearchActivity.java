package com.wd.tech.view;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.library.AutoFlowLayout;
import com.wd.tech.R;
import com.wd.tech.core.WDActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 搜索页面
 *
 * @author lmx
 * @date 2019/2/19
 */
public class SearchActivity extends WDActivity {


    @BindView(R.id.search_edit_zi)
    EditText mSearchEditZi;
    @BindView(R.id.search_cancel_txt)
    TextView mSearchCancelTxt;
    @BindView(R.id.afl_cotent)
    AutoFlowLayout mAflCotent;
    private String[] mData = {"区块链", "中年危机", "锤子科技", "子弹短信", "民营企业", "特斯拉", "支付宝", "资本市场", "电视剧"};
    private LayoutInflater mLayoutInflater;
    private TextView tvAttrTag;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        mLayoutInflater = LayoutInflater.from(this);
        for (int i = 0; i < 9; i++) {
            final View item = View.inflate(this, R.layout.item_search, null);
            tvAttrTag = item.findViewById(R.id.tv_attr_tag);
            tvAttrTag.setText(mData[i]);
            mAflCotent.addView(item);
            final int finalI = i;
            tvAttrTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(getBaseContext(), mData[finalI] + "", Toast.LENGTH_SHORT).show();
                    mSearchEditZi.setText(mData[finalI] + "");
                    Intent intent = new Intent(getBaseContext(), SearchResultActivity.class);
                    intent.putExtra("mDataTxt", mData[finalI] + "");
                    startActivity(intent);

                }
            });
        }

    }


    @OnClick({R.id.search_edit_zi, R.id.search_cancel_txt})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_edit_zi:
                startActivity(new Intent(this, SearchResultActivity.class));
                break;
            case R.id.search_cancel_txt:
                finish();
                break;
        }
    }

    @Override
    protected void destoryData() {

    }


}
