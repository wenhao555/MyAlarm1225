package com.example.myalarm.ui.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myalarm.R;
import com.example.myalarm.db.Users;
import com.example.myalarm.ui.LoginActivity;
import com.example.myalarm.ui.activity.AboutActivity;
import com.example.myalarm.ui.activity.UserCenterActivity;
import com.example.myalarm.utils.PrefUtils;
import com.orm.SugarRecord;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment implements View.OnClickListener
{
    /**
     * 我的界面
     */

    public MineFragment()
    {
        // Required empty public constructor
    }

    private LinearLayout userCenter, updatePwd, about, outLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        userCenter = view.findViewById(R.id.userCenter);
        updatePwd = view.findViewById(R.id.updatePwd);
        about = view.findViewById(R.id.about);
        outLogin = view.findViewById(R.id.outLogin);

        userCenter.setOnClickListener(this);
        updatePwd.setOnClickListener(this);
        about.setOnClickListener(this);
        outLogin.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.userCenter://点击用户中心
                startActivity(new Intent(getActivity(), UserCenterActivity.class));
                break;
            case R.id.updatePwd:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final EditText editText = new EditText(getActivity());
                builder.setTitle("修改密码")//设置弹窗的头
                        .setView(editText)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                //点击确定的时候升级密码
                                String name = PrefUtils.getString(getActivity(), "name", "");
                                String pwd = PrefUtils.getString(getActivity(), "pwd", "");
                                List<Users> usersList = Users.find(Users.class, "name = ? AND password = ?",
                                        name, pwd);
                                Users users = usersList.get(0);
                                users.setPassword(editText.getText().toString().trim());
                                users.update();
                                Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();

                break;
            case R.id.about:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.outLogin:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
        }
    }
}
