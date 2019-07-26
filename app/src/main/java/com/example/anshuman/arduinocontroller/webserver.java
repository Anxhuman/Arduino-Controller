package com.example.anshuman.arduinocontroller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class webserver extends AppCompatActivity {
WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webserver);
        WebView containerWebView =(WebView)findViewById(R.id.webView);
        containerWebView.setWebViewClient(new MyWebViewClient());
        String url="https://google.com";
        containerWebView.getSettings().setJavaScriptEnabled(true);
        containerWebView.loadUrl(url);

    }
    private class MyWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view,String url){
            view.loadUrl(url);
            return true;
        }


    }
}
