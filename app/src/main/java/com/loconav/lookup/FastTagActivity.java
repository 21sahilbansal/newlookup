package com.loconav.lookup;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FastTagActivity extends AppCompatActivity {
    @BindView(R.id.wv_fast_tag) WebView wbFastTag;
    String fastTagUrl = "http://192.168.2.188:3000/installers";
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_tag);
        ButterKnife.bind(this);
        wbFastTag.setWebViewClient(new MyBrowser());
        wbFastTag.getSettings().setLoadsImagesAutomatically(true);
        wbFastTag.getSettings().setJavaScriptEnabled(true);
        wbFastTag.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wbFastTag.loadUrl(fastTagUrl);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
