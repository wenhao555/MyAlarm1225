package com.example.myalarm.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.myalarm.R;

public class WebActivity extends AppCompatActivity
{
    private WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        web = findViewById(R.id.web);
        web.loadUrl("http://www.lipin-bj.cn/news/6014");//加载网页
    }
}
