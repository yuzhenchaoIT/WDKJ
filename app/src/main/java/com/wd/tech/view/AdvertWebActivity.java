package com.wd.tech.view;

import android.content.Intent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wd.tech.R;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.User;
import com.wd.tech.core.WDActivity;
import com.wd.tech.core.exception.ApiException;
import com.wd.tech.core.http.DataCall;
import com.wd.tech.presenter.DoTheTaskPresenter;

/**
 * 广告的webView
 *
 * @author lmx
 * @date 2019/2/28
 */
public class AdvertWebActivity extends WDActivity {

    private ProgressBar mProgressBar;
    private WebView mWebView;
    private DoTheTaskPresenter doTheTaskPresenter = new DoTheTaskPresenter(new DoTheTaskCall());
    private User user;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_advert_web;
    }

    @Override
    protected void initView() {
        mWebView = (WebView) findViewById(R.id.wed);
        mProgressBar = (ProgressBar) findViewById(R.id.prog);
        //查询数据库
        user = WDActivity.getUser(this);
        Intent intent = getIntent();
        String url = intent.getStringExtra("AdvertUrl");

        mWebView.loadUrl(url);

        SettingsP();
    }

    private void SettingsP() {
        WebSettings seting = mWebView.getSettings();
        seting.setJavaScriptEnabled(true);//设置webview支持javascript脚本
        seting.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.
        seting.setDomStorageEnabled(true);
        seting.setAllowFileAccessFromFileURLs(true);
        //支持屏幕缩放
        seting.setSupportZoom(true);
        seting.setBuiltInZoomControls(true);
        // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress == 100) {
                    // 网页加载完成
                    mProgressBar.setVisibility(View.GONE);//加载完网页进度条消失
                    if (user != null) {
                        doTheTaskPresenter.request(user.getUserId(), user.getSessionId(), 1005);
                    } else {
                        doTheTaskPresenter.request(0, "", 1005);
                    }
                } else {
                    // 加载中
                    mProgressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    mProgressBar.setProgress(newProgress);//设置进度值
                }
            }
        });

    }

    //实现做任务接口
    private class DoTheTaskCall implements DataCall<Result> {
        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")) {
                Toast.makeText(AdvertWebActivity.this, "" + data.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }

    @Override
    protected void destoryData() {

    }
}
