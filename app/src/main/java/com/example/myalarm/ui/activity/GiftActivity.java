package com.example.myalarm.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myalarm.R;
import com.example.myalarm.db.Users;
import com.example.myalarm.utils.PrefUtils;

import java.util.List;

public class GiftActivity extends AppCompatActivity
{//礼物详情界面
    private TextView names, bir;
    private ImageView liwu;
    private LinearLayout tiaoxuan;
    private TextView tvname;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift);
        names = findViewById(R.id.name);
        tvname = findViewById(R.id.tvname);
        bir = findViewById(R.id.bir);
        tvname.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        liwu = findViewById(R.id.liwu);
        tiaoxuan = findViewById(R.id.tiaoxuan);
        String tag = getIntent().getStringExtra("tag");
        String name = PrefUtils.getString(this, "name", "");//得到账号缓存
        String pwd = PrefUtils.getString(this, "pwd", "");//得到密码缓存
        List<Users> usersList = Users.find(Users.class, "name = ? AND password = ?",
                name, pwd);//遍历数据库
        Users users = usersList.get(0);//得到该用户对象
        names.setText("用户名：" + users.getName());
        bir.setText("生日：" + users.getBirth());
        if (tag.equals("1"))
        {
            liwu.setImageResource(R.mipmap.liwu1);
        } else if (tag.equals("2"))
        {
            liwu.setImageResource(R.mipmap.liwu2);
        } else if (tag.equals("3"))
        {
            liwu.setImageResource(R.mipmap.liwu3);
        } else if (tag.equals("4"))
        {
            liwu.setImageResource(R.mipmap.liwu4);
        } else if (tag.equals("5"))
        {
            liwu.setImageResource(R.mipmap.liwu5);

        }
        tiaoxuan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(GiftActivity.this, WebActivity.class));
            }
        });
    }
}
