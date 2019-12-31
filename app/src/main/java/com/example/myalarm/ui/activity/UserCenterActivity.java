package com.example.myalarm.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.myalarm.R;
import com.example.myalarm.db.Users;
import com.example.myalarm.utils.CircleImageView;
import com.example.myalarm.utils.PrefUtils;

import java.util.List;

public class UserCenterActivity extends AppCompatActivity
{//个人中心界面
    private TextView userCenter2, sexCenter, phoneCenter, birCenter;
    private CircleImageView touxiang;
    private TextView tvname;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);
        userCenter2 = findViewById(R.id.userCenter2);
        tvname = findViewById(R.id.tvname);
        sexCenter = findViewById(R.id.sexCenter);
        phoneCenter = findViewById(R.id.phoneCenter);
        birCenter = findViewById(R.id.birCenter);
        touxiang = (CircleImageView) findViewById(R.id.tuoxiang);
        String name = PrefUtils.getString(this, "name", "");//得到账号缓存
        String pwd = PrefUtils.getString(this, "pwd", "");//得到密码缓存
        List<Users> usersList = Users.find(Users.class, "name = ? AND password = ?",
                name, pwd);//遍历数据库
        Users users = usersList.get(0);//得到该用户对象
        birCenter.setText("生日：" + users.getBirth());
        userCenter2.setText("用户名：" + users.getName());
        phoneCenter.setText("电话：：" + users.getPhone());
        if (users.getSex().equals("男"))
        {
            sexCenter.setText("性别：男");
            touxiang.setImageDrawable(getResources().getDrawable(R.drawable.man));//为男时的头像
        } else
        {
            sexCenter.setText("性别：女");
            touxiang.setImageDrawable(getResources().getDrawable(R.drawable.man));
        }
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
