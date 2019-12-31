package com.example.myalarm.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.myalarm.R;

public class AboutActivity extends AppCompatActivity
{
    //关于界面
    private TextView tvname;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        tvname = findViewById(R.id.tvname);
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
