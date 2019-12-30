package com.example.myalarm.ui.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.myalarm.Global;
import com.example.myalarm.R;
import com.example.myalarm.db.Alarms;
import com.example.myalarm.ui.AlarmActivity;
import com.example.myalarm.ui.MainActivity;
import com.example.myalarm.utils.SetRing;
import com.example.myalarm.utils.TimeUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmFragment extends Fragment implements View.OnClickListener {


    public AlarmFragment() {
        // Required empty public constructor
    }

    private Button btnAdd;
    private ListView alarmList;
    private AlarmAdapter alarmAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);
        btnAdd = view.findViewById(R.id.btnAdd);
        alarmList = view.findViewById(R.id.alarmList);
        alarmAdapter = new AlarmAdapter();
        alarmList.setAdapter(alarmAdapter);
        btnAdd.setOnClickListener(this);
        SetRing.CreateDir();
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAdd) {
            ShowPopMenu(v);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        RefreshList();
    }

    private void ShowPopMenu(View v) {
        //创建弹出式菜单对象（最低版本11）
        PopupMenu popup = new PopupMenu(getActivity(), v);//第二个参数是绑定的那个view
        //获取菜单填充器
        MenuInflater inflater = popup.getMenuInflater();
        //填充菜单
        inflater.inflate(R.menu.menu_set, popup.getMenu());
        //绑定菜单项的点击事件
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getTitle().toString().equals("新增")) {
                    ShowAdd();
                }
//                else if (menuItem.getTitle().toString().equals("设置")) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                    final String[] sChoice = new String[themeColors.length];
//                    for (int i = 0; i < sChoice.length; i++) {
//                        sChoice[i] = "主题" + (i + 1);
//                    }
//                    builder.setSingleChoiceItems(
//                            sChoice,
//                            0,
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    //添加操作...
//                                    setThemeIndex(which);
//                                    SetTheme();
//                                    dialog.dismiss();
//                                }
//                            });
//                    builder.show();
//                }
                return false;
            }
        });
        //显示(这一行代码不要忘记了)
        popup.show();
    }

    private void ShowAdd() {
        startActivity(new Intent(getActivity(), AlarmActivity.class));
    }

    private List<Alarms> alarmsList = new ArrayList<>();

    class AlarmAdapter extends BaseAdapter {
        class ViewHolder {
            public TextView tvname, tvinfo, tvFlag, tvtime;
            public ImageView imgFlag;
            public Switch aSwitch;
            public RelativeLayout box;
            public ToggleButton btn;
        }

        AlarmAdapter.ViewHolder viewHolder = null;

        @Override
        public int getCount() {
            return alarmsList.size();
        }

        @Override
        public Alarms getItem(int position) {
            return alarmsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
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

            viewHolder.box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Alarms alarms = Alarms.findById(Alarms.class, (Long) (v.getTag()));
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                            .setTitle(alarms.getName())
                            .setPositiveButton("编辑", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getActivity(), AlarmActivity.class);
                                    intent.putExtra("data", alarms.getId());
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("删除", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alarms.delete();
                                    RefreshList();
                                }
                            });
                    dialog.show();
                }
            });
            viewHolder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Alarms alarms = Alarms.findById(Alarms.class, (Long) (buttonView.getTag()));
                    alarms.setOpen(isChecked);
                    alarms.save();
                }
            });
            return convertView;
        }
    }

    private void RefreshList() {
        alarmsList = Alarms.listAll(Alarms.class);
        Collections.sort(alarmsList, new Comparator<Alarms>() {
            @Override
            public int compare(Alarms o1, Alarms o2) {
                try {
                    return (int) (TimeUtil.getAlarmTime(o2.getRemark()) - TimeUtil.getAlarmTime(o1.getRemark()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });
        if (Global.getInstance().getMe() != null) {
            for (int i = 0; i < alarmsList.size(); i++) {
                if (!alarmsList.get(i).getUser().equals(Global.getInstance().getMe().getName())) {
                    alarmsList.remove(i--);
                }
            }
        }
        if (alarmAdapter != null) {
            alarmAdapter.notifyDataSetChanged();
        }
    }
}
