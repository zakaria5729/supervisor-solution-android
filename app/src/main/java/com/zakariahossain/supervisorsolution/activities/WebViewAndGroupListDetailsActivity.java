package com.zakariahossain.supervisorsolution.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.utils.IntentAndBundleKey;

public class WebViewAndGroupListDetailsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ProgressBar progressBar;
    private String currentUrl;
    private WebView webView;
    private SwipeRefreshLayout swipeRefreshLayoutWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.wvProgressBar);
        swipeRefreshLayoutWebView = findViewById(R.id.swipeRefreshLayoutWebView);
        swipeRefreshLayoutWebView.setOnRefreshListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (getIntent() != null) {
            currentUrl = getIntent().getStringExtra(IntentAndBundleKey.KEY_PROFILE_LINK);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(currentUrl);
                getSupportActionBar().setElevation(0.0f);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            loadProfileUrl();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void loadProfileUrl() {
        if (currentUrl != null) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new MyWebViewClient());
            webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
            webView.loadUrl(currentUrl);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayoutWebView.setRefreshing(true);
        loadProfileUrl();
    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.equals(currentUrl)) {
                view.loadUrl(url);
            } else {
                Toast.makeText(WebViewAndGroupListDetailsActivity.this, "Other pages denied to load", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            swipeRefreshLayoutWebView.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
        }
    }
}
