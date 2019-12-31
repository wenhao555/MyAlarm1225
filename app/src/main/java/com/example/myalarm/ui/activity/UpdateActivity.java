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
import com.example.myalarm.ui.LoginActivity;
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
        oldPwd = (EditText) findViewById(R.id.oldPwd);
        tvname = (TextView) findViewById(R.id.tvname);
        newPwd = (EditText) findViewById(R.id.newPwd);
        newPwd2 = (EditText) findViewById(R.id.newPwd2);
        fin = (Button) findViewById(R.id.fin);


        fin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String name = PrefUtils.getString(UpdateActivity.this, "name", "");
                String pwd = PrefUtils.getString(UpdateActivity.this, "pwd", "");
                List<Users> usersList = Users.find(Users.class, "name = ? AND password = ?",
                        name, pwd);
                Users users = usersList.get(0);
                if (!oldPwd.getText().toString().equals(""))
                {
                    if (oldPwd.getText().toString().equals(pwd))
                    {
                        if (!newPwd.getText().toString().equals(""))
                        {
                            if (!newPwd2.getText().toString().equals(""))
                            {
                                if (newPwd2.getText().toString().equals(newPwd.getText().toString()))
                                {
                                    users.setPassword(newPwd.getText().toString());
                                    users.update();
                                    PrefUtils.setString(UpdateActivity.this, "pwd", newPwd.getText().toString());
                                    Toast.makeText(UpdateActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else
                                {
                                    Toast.makeText(UpdateActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                                }
                            } else
                            {
                                Toast.makeText(UpdateActivity.this, "请输入确认密码", Toast.LENGTH_SHORT).show();
                            }
                        } else
                        {
                            Toast.makeText(UpdateActivity.this, "请输入新密码", Toast.LENGTH_SHORT).show();
                        }
                    } else
                    {
                        Toast.makeText(UpdateActivity.this, "旧密码错误", Toast.LENGTH_SHORT).show();
                    }
                } else
                {
                    Toast.makeText(UpdateActivity.this, "原密码输入为空", Toast.LENGTH_SHORT).show();
                }

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
