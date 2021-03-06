package com.example.myalarm.ui.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myalarm.R;
import com.example.myalarm.db.Users;
import com.example.myalarm.ui.LoginActivity;
import com.example.myalarm.ui.activity.AboutActivity;
import com.example.myalarm.ui.activity.UpdateActivity;
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

    private LinearLayout userCenter, updatePwd, about;
    private Button outLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        userCenter = (LinearLayout) view.findViewById(R.id.userCenter);
        updatePwd = (LinearLayout) view.findViewById(R.id.updatePwd);
        about = (LinearLayout) view.findViewById(R.id.about);
        outLogin = (Button) view.findViewById(R.id.outLogin);

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
                startActivity(new Intent(getActivity(), UpdateActivity.class));
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
