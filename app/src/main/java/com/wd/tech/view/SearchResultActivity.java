package com.wd.tech.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wd.tech.R;
import com.wd.tech.adapter.FindByTitleAdapter;
import com.wd.tech.bean.FindTitleBean;
import com.wd.tech.bean.Result;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.InforMation.ByTitlePresenter;

import java.util.List;

import butterknife.BindView;

import static com.wd.tech.core.WDApplication.getContext;

/**
 * 搜索结果列表页面
 *
 * @author lmx
 * @date 2019/2/26
 */
public class SearchResultActivity extends WDActivity {


    @BindView(R.id.search_result_edit_zi)
    EditText mSearchResultEditZi;
    @BindView(R.id.search_result_cancel_txt)
    TextView mSearchResultCancelTxt;
    @BindView(R.id.search_result_recy)
    RecyclerView mSearchResultRecy;
    @BindView(R.id.search_result_recy_no)
    RelativeLayout mSearchResultRecyNo;

    //p层
    private ByTitlePresenter mByTitlePre = new ByTitlePresenter(new ByTitleCall());
    //适配器
    private FindByTitleAdapter mFindByTitleAda;
    //线性布局
    private LinearLayoutManager mManager = new LinearLayoutManager(getContext());

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_result;
    }

    @Override
    protected void initView() {

        String mDataTxt = getIntent().getStringExtra("mDataTxt");
        mSearchResultEditZi.setText(mDataTxt);
        mByTitlePre.request(mDataTxt, 1, 50);
        //取消按钮
        mSearchResultCancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mSearchResultRecy.setLayoutManager(mManager);
        mFindByTitleAda = new FindByTitleAdapter(this);
        mSearchResultRecy.setAdapter(mFindByTitleAda);

        //开启android软键盘搜索功能
        mSearchResultEditZi.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    final String s = mSearchResultEditZi.getText().toString();
                    mByTitlePre.request(s, 1, 50);
                    return true;
                }

                return false;
            }
        });

    }


    class ByTitleCall implements DataCall<Result<List<FindTitleBean>>> {

        @Override
        public void success(Result<List<FindTitleBean>> data) {
            if (data.getStatus().equals("0000")) {

                if (data.getResult().size() == 0) {
                    mSearchResultRecyNo.setVisibility(View.VISIBLE);
                    mSearchResultRecy.setVisibility(View.GONE);
                } else {
                    mSearchResultRecyNo.setVisibility(View.GONE);
                    mFindByTitleAda.clearData();
                    mFindByTitleAda.addList(data.getResult());
                    mFindByTitleAda.notifyDataSetChanged();
                }
                Toast.makeText(getBaseContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getBaseContext(), data.getMessage() + "", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void fail(ApiException e) {
            Toast.makeText(getBaseContext(), "网络异常", Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    protected void destoryData() {
        mByTitlePre.unBind();
    }


}
