package com.wd.tech.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.wd.tech.R;
import com.wd.tech.core.WDActivity;

/**
 * 广告的webView
 *
 * @author lmx
 * @date 2019/2/28
 */
public class AdvertWebActivity extends WDActivity {

    private ProgressBar mProgressBar;
    private WebView mWebView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_advert_web;
    }

    @Override
    protected void initView() {
        mWebView = (WebView) findViewById(R.id.wed);
        mProgressBar = (ProgressBar) findViewById(R.id.prog);
        Intent intent = getIntent();
        String url = intent.getStringExtra("AdvertUrl");

        mWebView.loadUrl(url);

        SettingsP();
    }

    private void SettingsP() {
        WebSettings seting = mWebView.getSettings();
        seting.setJavaScriptEnabled(true);//设置webview支持javascript脚本
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
                } else {
                    // 加载中
                    mProgressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    mProgressBar.setProgress(newProgress);//设置进度值
                }
            }
        });

    }

    @Override
    protected void destoryData() {

    }
}
