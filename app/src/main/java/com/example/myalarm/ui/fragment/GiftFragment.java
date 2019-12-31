package com.example.myalarm.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myalarm.R;
import com.example.myalarm.ui.activity.GiftActivity;

public class GiftFragment extends Fragment implements View.OnClickListener
{
    //礼物列表界面
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    private LinearLayout liwu1;
    private LinearLayout liwu2;
    private LinearLayout liwu3;
    private LinearLayout liwu4;
    private LinearLayout liwu5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_gift, container, false);
        // Inflate the layout for this fragment
        liwu1 = (LinearLayout) view.findViewById(R.id.liwu1);
        liwu2 = (LinearLayout) view.findViewById(R.id.liwu2);
        liwu3 = (LinearLayout) view.findViewById(R.id.liwu3);
        liwu4 = (LinearLayout) view.findViewById(R.id.liwu4);
        liwu5 = (LinearLayout) view.findViewById(R.id.liwu5);

        liwu1.setOnClickListener(this);
        liwu2.setOnClickListener(this);
        liwu3.setOnClickListener(this);
        liwu4.setOnClickListener(this);
        liwu5.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.liwu1://跳转并传递一个类别值
                startActivity(new Intent(getActivity(), GiftActivity.class).putExtra("tag", "1"));
                break;
            case R.id.liwu2:
                startActivity(new Intent(getActivity(), GiftActivity.class).putExtra("tag", "2"));
                break;
            case R.id.liwu3:
                startActivity(new Intent(getActivity(), GiftActivity.class).putExtra("tag", "3"));
                break;
            case R.id.liwu4:
                startActivity(new Intent(getActivity(), GiftActivity.class).putExtra("tag", "4"));
                break;
            case R.id.liwu5:
                startActivity(new Intent(getActivity(), GiftActivity.class).putExtra("tag", "5"));
                break;
        }
    }
}
