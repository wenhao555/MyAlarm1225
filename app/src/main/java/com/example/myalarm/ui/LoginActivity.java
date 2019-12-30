package com.example.myalarm.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myalarm.Global;
import com.example.myalarm.MyService;
import com.example.myalarm.R;
import com.example.myalarm.db.Users;
import com.example.myalarm.utils.SetRing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvTitle;
    private EditText etName;
    private EditText etPass;
    private EditText etPass2;
    private Button btn1;
    private Button btn2;

    private int type = 0;//0-登录 1-注册

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        RefreshUI();

        startService(new Intent(this, MyService.class));

        SetRing.CreateDir();

        myRequetPermission();
    }
    private void myRequetPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }else {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                if (type == 0) {
                    submit();
                } else if (type == 1) {
                    type = 0;
                    RefreshUI();
                }
                break;
            case R.id.btn2:
                if (type == 0) {
                    type = 1;
                    RefreshUI();
                    etName.setText("");
                    etPass2.setText("");
                    etPass.setText("");
                } else if (type == 1) {
                    submit();
                }
                break;
        }
    }

    private void RefreshUI() {
        if (type == 0) {
            tvTitle.setText("登录");
            etPass2.setVisibility(View.GONE);
            btn1.setText("登录");
            btn2.setText("注册");
        } else if (type == 1) {
            tvTitle.setText("注册");
            etPass2.setVisibility(View.VISIBLE);
            btn1.setText("取消");
            btn2.setText("注册");

        }
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        etName = (EditText) findViewById(R.id.etName);
        etPass = (EditText) findViewById(R.id.etPass);
        etPass2 = (EditText) findViewById(R.id.etPass2);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
    }

    private void submit() {
        // validate
        String etNameString = etName.getText().toString().trim();
        if (TextUtils.isEmpty(etNameString)) {
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }

        String etPassString = etPass.getText().toString().trim();
        if (TextUtils.isEmpty(etPassString)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }

        String etPass2String = etPass2.getText().toString().trim();
        if (type == 1 && TextUtils.isEmpty(etPass2String)) {
            Toast.makeText(this, "请再次输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (type == 1 && !etPassString.equals(etPass2String)) {
            Toast.makeText(this, "两次密码不同", Toast.LENGTH_SHORT).show();
            return;
        }

        if (type == 0) {
            List<Users> usersList = Users.find(Users.class, "name = ? AND password = ?",
                    etNameString, etPassString);
            if (!usersList.isEmpty()) {
                Users u = usersList.get(0);
                Global.getInstance().setMe(u);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "登录错误", Toast.LENGTH_SHORT).show();
            }
        } else if (type == 1) {
            Users u = new Users();
            u.setName(etNameString);
            u.setPassword(etPassString);
            if (u.save() > 0) {
                type = 0;
                RefreshUI();
            }
        }
    }
}
