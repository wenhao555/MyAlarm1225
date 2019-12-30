package com.example.myalarm.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.myalarm.R;
import com.example.myalarm.db.Users;

import java.util.List;

public class UserCenterActivity extends AppCompatActivity {
    private TextView nameCenter, userCenter2, sexCenter, phoneCenter, birCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);
        nameCenter = findViewById(R.id.nameCenter);
        userCenter2 = findViewById(R.id.userCenter2);
        sexCenter = findViewById(R.id.sexCenter);
        phoneCenter = findViewById(R.id.phoneCenter);
        birCenter = findViewById(R.id.birCenter);
        List<Users> usersList = Users.find(Users.class, "name = ? AND password = ?",
                "123", "234");
        Users users = usersList.get(0);
        nameCenter.setText("姓名："+users.getUserName());
        userCenter2.setText("用户名："+users.getName());
        phoneCenter.setText("电话：："+users.getPhone());
        birCenter.setText("生日："+users.getBirth());
        if (users.getSex().equals("男"))
            sexCenter.setText("性别：男");
        else
            sexCenter.setText("性别：女");
    }
}
