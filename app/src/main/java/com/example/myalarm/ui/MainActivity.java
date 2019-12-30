package com.example.myalarm.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bigkoo.pickerview.TimePickerView;
import com.example.myalarm.Global;
import com.example.myalarm.R;
import com.example.myalarm.db.Alarms;
import com.example.myalarm.utils.SetRing;
import com.example.myalarm.utils.TimeUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private String startTime, endTime;
    private TextView tvname;
    private Button btnAdd;
    private RelativeLayout topBox;
    private ListView alarmList;

    private List<Alarms> alarmsList = new ArrayList<>();
    private AlarmAdapter alarmAdapter;
    private Button btnSearch;
    private TextView tvSearch;
    private RelativeLayout searchLine;
    private EditText etKeyword;
    private TextView tvTimeFlag;
    private Button btnStartTime;
    private Button btnEndTime;
    private RelativeLayout searchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        tvname.setText(Global.getInstance().getMe().getName());

        SetRing.CreateDir();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                ShowPopMenu(v);
                break;
            case R.id.btnSearch:
                if (searchBox.getVisibility() == View.GONE) {
                    searchBox.setVisibility(View.VISIBLE);
                    btnStartTime.setText("start");
                    btnEndTime.setText("end");
                    etKeyword.setText("");
                    startTime = endTime = "";
                } else {
                    searchBox.setVisibility(View.GONE);
                }

                break;
            case R.id.btnStartTime:
                startTime = "";
                TimePickerView pvTime = new TimePickerView(this, TimePickerView.Type.ALL);
                pvTime.setTime(new Date());
                pvTime.setCyclic(false);
                pvTime.setCancelable(true);
                //时间选择后回调
                pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

                    @Override
                    public void onTimeSelect(Date date) {
                        startTime = TimeUtil.getTime(date);
                        btnStartTime.setText(startTime);
                    }
                });
                pvTime.show();
                break;
            case R.id.btnEndTime:
                endTime = "";
                TimePickerView pvTime2 = new TimePickerView(this, TimePickerView.Type.ALL);
                pvTime2.setTime(new Date());
                pvTime2.setCyclic(false);
                pvTime2.setCancelable(true);
                //时间选择后回调
                pvTime2.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

                    @Override
                    public void onTimeSelect(Date date) {
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

    private void ShowAdd() {
        startActivity(new Intent(MainActivity.this, AlarmActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        RefreshList();
        SetTheme();
    }

    private void RefreshList() {
        alarmsList = Alarms.listAll(Alarms.class);
        Collections.sort(alarmsList, new Comparator<Alarms>() {
            @Override
            public int compare(Alarms o1, Alarms o2) {
                try {
                    return (int) (TimeUtil.getAlarmTime(o2.getRemark())-TimeUtil.getAlarmTime(o1.getRemark()));
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

    private void initView() {
        tvname = (TextView) findViewById(R.id.tvname);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        topBox = (RelativeLayout) findViewById(R.id.topBox);
        alarmList = (ListView) findViewById(R.id.alarmList);

        btnAdd.setOnClickListener(this);
        alarmAdapter = new AlarmAdapter();
        alarmList.setAdapter(alarmAdapter);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);
        tvSearch = (TextView) findViewById(R.id.tvSearch);
        tvSearch.setOnClickListener(this);
        searchLine = (RelativeLayout) findViewById(R.id.searchLine);
        etKeyword = (EditText) findViewById(R.id.etKeyword);
        tvTimeFlag = (TextView) findViewById(R.id.tvTimeFlag);
        btnStartTime = (Button) findViewById(R.id.btnStartTime);
        btnStartTime.setOnClickListener(this);
        btnEndTime = (Button) findViewById(R.id.btnEndTime);
        btnEndTime.setOnClickListener(this);
        searchBox = (RelativeLayout) findViewById(R.id.searchBox);
        searchBox.setVisibility(View.GONE);
    }

    private void submit() {
        // validate
        List<Alarms> tempList = Alarms.listAll(Alarms.class);
        if (Global.getInstance().getMe() != null) {
            for (int i = 0; i < tempList.size(); i++) {
                if (!tempList.get(i).getUser().equals(Global.getInstance().getMe().getName())) {
                    tempList.remove(i--);
                }
            }
        }

        String etKeywordString = etKeyword.getText().toString().trim();
        for (int i = 0; i < tempList.size(); i++) {
            if (!tempList.get(i).getName().contains(etKeywordString) && !tempList.get(i).getInfos().contains(etKeywordString)) {
                tempList.remove(i--);
                continue;
            }
            if (!TextUtils.isEmpty(startTime)) {
                try {
                    if (TimeUtil.getAlarmTime(tempList.get(i).getTime()) <= TimeUtil.getAlarmTime(startTime)) {
                        tempList.remove(i--);
                        continue;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (!TextUtils.isEmpty(endTime)) {
                try {
                    if (TimeUtil.getAlarmTime(tempList.get(i).getTime()) >= TimeUtil.getAlarmTime(endTime)) {
                        tempList.remove(i--);
                        continue;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        alarmsList.clear();
        alarmsList.addAll(tempList);
        Collections.sort(alarmsList, new Comparator<Alarms>() {
            @Override
            public int compare(Alarms o1, Alarms o2) {
                try {
                    return (int) (TimeUtil.getAlarmTime(o2.getRemark())-TimeUtil.getAlarmTime(o1.getRemark()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });
        alarmAdapter.notifyDataSetChanged();

    }

    class AlarmAdapter extends BaseAdapter {
        class ViewHolder {
            public TextView tvname, tvinfo, tvFlag, tvtime;
            public ImageView imgFlag;
            public Switch aSwitch;
            public RelativeLayout box;
            public ToggleButton btn;
        }

        ViewHolder viewHolder = null;

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

            convertView = LayoutInflater.from(MainActivity.this).inflate(
                    R.layout.alarm_item, null);

            viewHolder = new ViewHolder();
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
            viewHolder.imgFlag.setImageResource(entity.getType().intValue() == 0 ?R.drawable.birth : R.drawable.event);
            viewHolder.aSwitch.setChecked(entity.getOpen());

            viewHolder.box.setTag(entity.getId());
            viewHolder.aSwitch.setTag(entity.getId());

            viewHolder.box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Alarms alarms = Alarms.findById(Alarms.class, (Long) (v.getTag()));
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this)
                            .setTitle(alarms.getName())
                            .setPositiveButton("编辑", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(MainActivity.this,AlarmActivity.class);
                                    intent.putExtra("data",alarms.getId());
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

    private void ShowPopMenu(View v)
    {
        //创建弹出式菜单对象（最低版本11）
        PopupMenu popup = new PopupMenu(MainActivity.this, v);//第二个参数是绑定的那个view
        //获取菜单填充器
        MenuInflater inflater = popup.getMenuInflater();
        //填充菜单
        inflater.inflate(R.menu.menu_set, popup.getMenu());
        //绑定菜单项的点击事件
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getTitle().toString().equals("新增"))
                {
                    ShowAdd();
                }
                else if (menuItem.getTitle().toString().equals("设置"))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    final String[] sChoice = new String[themeColors.length];
                    for(int i = 0;i<sChoice.length;i++)
                    {
                        sChoice[i] = "主题"+(i+1);
                    }
                    builder.setSingleChoiceItems(
                            sChoice,
                            0,
                            new DialogInterface.OnClickListener()                {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    //添加操作...
                                    setThemeIndex(which);
                                    SetTheme();
                                    dialog.dismiss();
                                }
                            });
                    builder.show();
                }
                return false;
            }
        });
        //显示(这一行代码不要忘记了)
        popup.show();
    }

    private void SetTheme()
    {
        topBox.setBackgroundColor(getResources().getColor(themeColors[getThemeIndex()][0]));
    }
}
