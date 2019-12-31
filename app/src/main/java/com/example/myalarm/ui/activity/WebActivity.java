package com.example.myalarm.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.myalarm.R;

import org.w3c.dom.Text;

public class WebActivity extends AppCompatActivity
{
    private WebView web;
    private TextView tvname;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        web = (WebView) findViewById(R.id.web);
        web.loadUrl("http://www.lipin-bj.cn/news/6014");//加载网页
        tvname =(TextView) findViewById(R.id.tvname);
        tvname.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }
}
