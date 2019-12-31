package com.example.myalarm.ui.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bigkoo.pickerview.TimePickerView;
import com.example.myalarm.Global;
import com.example.myalarm.R;
import com.example.myalarm.db.Alarms;
import com.example.myalarm.ui.AlarmActivity;
import com.example.myalarm.utils.TimeUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class SearchFragment extends Fragment implements View.OnClickListener
{
    //搜索界面    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    private String startTime, endTime;
    private Button btnSearch;
    private RelativeLayout searchBox;
    private TextView tvSearch;
    private ListView alarmList;
    private AlarmAdapter alarmAdapter;
    private Button btnStartTime;
    private Button btnEndTime;
    private EditText etKeyword;

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        btnStartTime = (Button) view.findViewById(R.id.btnStartTime);
        btnEndTime = (Button) view.findViewById(R.id.btnEndTime);
        etKeyword = (EditText) view.findViewById(R.id.etKeyword);
        btnSearch = (Button) view.findViewById(R.id.btnSearch);
        alarmList = (ListView) view.findViewById(R.id.alarmList);
        searchBox = (RelativeLayout) view.findViewById(R.id.searchBox);
        tvSearch = (Button) view.findViewById(R.id.tvSearch);
        searchBox.setVisibility(View.GONE);
        alarmAdapter = new AlarmAdapter();
        alarmList.setAdapter(alarmAdapter);
        btnStartTime.setOnClickListener(this);
        btnEndTime.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        return view;

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnSearch://点击搜索按钮
                if (searchBox.getVisibility() == View.GONE)
                {
                    searchBox.setVisibility(View.VISIBLE);//显示界面
                    btnStartTime.setText("start");
                    btnEndTime.setText("end");
                    etKeyword.setText("");
                    startTime = endTime = "";
                } else
                {
                    searchBox.setVisibility(View.GONE);
                }
                break;
            case R.id.btnStartTime://起始时间
                startTime = "";
                TimePickerView pvTime = new TimePickerView(getActivity(), TimePickerView.Type.ALL);
                pvTime.setTime(new Date());
                pvTime.setCyclic(false);
                pvTime.setCancelable(true);
                //时间选择后回调
                pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener()
                {

                    @Override
                    public void onTimeSelect(Date date)
                    {
                        startTime = TimeUtil.getTime(date);
                        btnStartTime.setText(startTime);
                    }
                });
                pvTime.show();
                break;
            case R.id.btnEndTime://结束时间
                endTime = "";
                TimePickerView pvTime2 = new TimePickerView(getActivity(), TimePickerView.Type.ALL);
                pvTime2.setTime(new Date());
                pvTime2.setCyclic(false);
                pvTime2.setCancelable(true);
                //时间选择后回调
                pvTime2.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener()
                {

                    @Override
                    public void onTimeSelect(Date date)
                    {
                        endTime = TimeUtil.getTime(date);
                        btnEndTime.setText(endTime);
                    }
                });
                pvTime2.show();
                break;
            case R.id.tvSearch:
                searchBox.setVisibility(View.GONE);
                submit();
                break;
        }
    }

    /**
     * 提交数据到数据库进行显示
     */
    private void submit()
    {
        // validate
        List<Alarms> tempList = Alarms.listAll(Alarms.class);
        if (Global.getInstance().getMe() != null)
        {
            for (int i = 0; i < tempList.size(); i++)
            {
                if (!tempList.get(i).getUser().equals(Global.getInstance().getMe().getName()))
                {
                    tempList.remove(i--);
                }
            }
        }

        String etKeywordString = etKeyword.getText().toString().trim();
        for (int i = 0; i < tempList.size(); i++)
        {
            if (!tempList.get(i).getName().contains(etKeywordString) && !tempList.get(i).getInfos().contains(etKeywordString))
            {
                tempList.remove(i--);
                continue;
            }
            if (!TextUtils.isEmpty(startTime))
            {
                try
                {
                    if (TimeUtil.getAlarmTime(tempList.get(i).getTime()) <= TimeUtil.getAlarmTime(startTime))
                    {
                        tempList.remove(i--);
                        continue;
                    }
                } catch (ParseException e)
                {
                    e.printStackTrace();
                }
            }
            if (!TextUtils.isEmpty(endTime))
            {
                try
                {
                    if (TimeUtil.getAlarmTime(tempList.get(i).getTime()) >= TimeUtil.getAlarmTime(endTime))
                    {
                        tempList.remove(i--);
                        continue;
                    }
                } catch (ParseException e)
                {
                    e.printStackTrace();
                }
            }
        }
        alarmsList.clear();
        alarmsList.addAll(tempList);
        Collections.sort(alarmsList, new Comparator<Alarms>()
        {
            @Override
            public int compare(Alarms o1, Alarms o2)
            {
                try
                {
                    return (int) (TimeUtil.getAlarmTime(o2.getRemark()) - TimeUtil.getAlarmTime(o1.getRemark()));
                } catch (ParseException e)
                {
                    e.printStackTrace();
                    return 0;
                }
            }
        });
        alarmAdapter.notifyDataSetChanged();//刷新适配器

    }

    class AlarmAdapter extends BaseAdapter
    {
        class ViewHolder
        {
            public TextView tvname, tvinfo, tvFlag, tvtime;
            public ImageView imgFlag;
            public Switch aSwitch;
            public RelativeLayout box;
            public ToggleButton btn;
        }

        AlarmAdapter.ViewHolder viewHolder = null;

        @Override
        public int getCount()
        {
            return alarmsList.size();
        }

        @Override
        public Alarms getItem(int position)
        {
            return alarmsList.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            final Alarms entity = getItem(position);

            convertView = LayoutInflater.from(getActivity()).inflate(
                    R.layout.alarm_item, null);

            viewHolder = new AlarmAdapter.ViewHolder();
            viewHolder.tvname = (TextView) convertView
                    .findViewById(R.id.tvname);
            viewHolder.tvFlag = (TextView) convertView
                    .findViewById(R.id.tvFlag);
            viewHolder.imgFlag = (ImageView) convertView
                    .findViewById(R.id.imgFlag);
            viewHolder.tvinfo = (TextView) convertView
                    .findViewById(R.id.tvinfo);
            viewHolder.tvtime = (TextView) convertView
                    .findViewById(R.id.tvtime);
            viewHolder.box = (RelativeLayout) convertView
                    .findViewById(R.id.itemBox);
            viewHolder.btn = (ToggleButton) convertView
                    .findViewById(R.id.tbOn);
            viewHolder.aSwitch = (Switch) convertView
                    .findViewById(R.id.switch1);
            convertView.setTag(viewHolder);

            viewHolder.tvname.setText(entity.getName());
            viewHolder.tvinfo.setText(entity.getInfos());
            viewHolder.tvtime.setText(entity.getRemark());
//            viewHolder.tvFlag.setText(entity.getType().intValue() == 0 ? "生日" : "事件");
//            viewHolder.tvFlag.setBackgroundColor(entity.getType().intValue() == 0 ?
//                    getResources().getColor(R.color.colorAccent) : getResources().getColor(R.color.colorPrimary));
            viewHolder.imgFlag.setImageResource(entity.getType().intValue() == 0 ? R.drawable.birth : R.drawable.event);
            viewHolder.aSwitch.setChecked(entity.getOpen());

            viewHolder.box.setTag(entity.getId());
            viewHolder.aSwitch.setTag(entity.getId());

            viewHolder.box.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    final Alarms alarms = Alarms.findById(Alarms.class, (Long) (v.getTag()));
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                            .setTitle(alarms.getName())
                            .setPositiveButton("编辑", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    Intent intent = new Intent(getActivity(), AlarmActivity.class);
                                    intent.putExtra("data", alarms.getId());
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("删除", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    alarms.delete();
                                    RefreshList();
                                }
                            });
                    dialog.show();
                }
            });
            viewHolder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    Alarms alarms = Alarms.findById(Alarms.class, (Long) (buttonView.getTag()));
                    alarms.setOpen(isChecked);
                    alarms.save();
                }
            });
            return convertView;
        }
    }

    private List<Alarms> alarmsList = new ArrayList<>();

    private void RefreshList()
    {
        alarmsList = Alarms.listAll(Alarms.class);
        Collections.sort(alarmsList, new Comparator<Alarms>()
        {
            @Override
            public int compare(Alarms o1, Alarms o2)
            {
                try
                {
                    return (int) (TimeUtil.getAlarmTime(o2.getRemark()) - TimeUtil.getAlarmTime(o1.getRemark()));
                } catch (ParseException e)
                {
                    e.printStackTrace();
                    return 0;
                }
            }
        });
        if (Global.getInstance().getMe() != null)
        {
            for (int i = 0; i < alarmsList.size(); i++)
            {
                if (!alarmsList.get(i).getUser().equals(Global.getInstance().getMe().getName()))
                {
                    alarmsList.remove(i--);
                }
            }
        }
        if (alarmAdapter != null)
        {
            alarmAdapter.notifyDataSetChanged();
        }
    }
}
