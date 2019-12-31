package com.example.myalarm.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myalarm.R;
import com.example.myalarm.db.Users;
import com.example.myalarm.utils.PrefUtils;

import java.util.List;

public class UpdateActivity extends AppCompatActivity
{
    private Button fin;
    private EditText oldPwd, newPwd, newPwd2;
    private TextView tvname;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        oldPwd = findViewById(R.id.oldPwd);
        tvname = findViewById(R.id.tvname);
        newPwd = findViewById(R.id.newPwd);
        newPwd2 = findViewById(R.id.newPwd2);
        fin = findViewById(R.id.fin);
        String name = PrefUtils.getString(UpdateActivity.this, "name", "");
        String pwd = PrefUtils.getString(UpdateActivity.this, "pwd", "");
        List<Users> usersList = Users.find(Users.class, "name = ? AND password = ?",
                name, pwd);
        Users users = usersList.get(0);
        if (oldPwd.getText().toString().equals(pwd))
        {
            if (newPwd.getText().toString().equals(""))
            {
                if (newPwd2.getText().toString().equals(""))
                {
                    if (newPwd2.getText().toString().equals(newPwd.getText().toString()))
                    {
                        users.setPassword(newPwd.getText().toString());
                        users.update();
                        Toast.makeText(UpdateActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    }
                } else
                {
                    Toast.makeText(this, "请输入确认密码", Toast.LENGTH_SHORT).show();
                }
            } else
            {
                Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
            }
        } else
        {
            Toast.makeText(this, "旧密码错误", Toast.LENGTH_SHORT).show();
        }

        fin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
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
