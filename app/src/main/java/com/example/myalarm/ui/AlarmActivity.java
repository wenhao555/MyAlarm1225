package com.example.myalarm.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.example.myalarm.Global;
import com.example.myalarm.R;
import com.example.myalarm.db.Alarms;
import com.example.myalarm.utils.SetRing;
import com.example.myalarm.utils.TimeUtil;
import com.joybar.librarycalendar.data.CalendarDate;
import com.joybar.librarycalendar.fragment.CalendarViewFragment;
import com.joybar.librarycalendar.fragment.CalendarViewPagerFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AlarmActivity extends BaseActivity implements View.OnClickListener,
        CalendarViewPagerFragment.OnPageChangeListener,
        CalendarViewFragment.OnDateClickListener,
        CalendarViewFragment.OnDateCancelListener {

    private int type = 0;
    private int notice = 0;
    private String mDate, mTime;
    private Alarms alarms;
    private String fileName;

    private TextView tvTitle;
    private Button btnAdd;
    private Button btnBack;
    private RelativeLayout topBox;
    private RadioButton radioBirth;
    private RadioButton radioEvent;
    private EditText etName;
    private EditText etInfo;
    private Button btnTime;
    private Button btnDate;
    private RadioButton radio0;
    private RadioButton radio1;
    private RadioButton radio2;
    private RadioButton radio3;
    private FrameLayout frameLayout;
    private Button btnDateEsc;
    private Button btnDateok;
    private TextView tvDate;
    private LinearLayout dateBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        initView();

        initData();
    }

    private void initFragment() {
        dateBox.setVisibility(View.VISIBLE);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        // Fragment fragment = new CalendarViewPagerFragment();
        Fragment fragment = CalendarViewPagerFragment.newInstance(true);
        tx.replace(R.id.frameLayout, fragment);
        tx.commit();
    }

    @Override
    public void onDateCancel(CalendarDate calendarDate) {
    }

    private void SetTheme() {
        topBox.setBackgroundColor(getResources().getColor(themeColors[getThemeIndex()][0]));
    }

    @Override
    protected void onResume() {
        super.onResume();
        SetTheme();
    }

    @Override
    public void onDateClick(CalendarDate calendarDate) {
        int year = calendarDate.getSolar().solarYear;
        int month = calendarDate.getSolar().solarMonth;
        int day = calendarDate.getSolar().solarDay;
        tvDate.setText(year + "-" + month);
        mDate = (year + "-" + month + "-" + day);
    }

    @Override
    public void onPageChange(int year, int month) {
        tvDate.setText(year + "-" + month);
    }

    private void initData() {
        try {
            long ids = getIntent().getExtras().getLong("data");
            alarms = Alarms.findById(Alarms.class, ids);
            if (alarms != null) {
                etName.setText(alarms.getName());
                etInfo.setText(alarms.getInfos());
                String[] times = alarms.getRemark().split(" ");
                btnTime.setText(times[1]);
                btnDate.setText(times[0]);
                radioBirth.setChecked(alarms.getType() == 0);
                radioEvent.setChecked(alarms.getType() == 1);
                radio0.setChecked(alarms.getNotice() == 0);
                radio1.setChecked(alarms.getNotice() == 1);
                radio2.setChecked(alarms.getNotice() == 2);
                radio3.setChecked(alarms.getNotice() == 3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                submit();
                break;
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnTime:
                setTime();
                break;
            case R.id.btnDate:
                initFragment();
                break;
            case R.id.btnDateEsc:
                dateBox.setVisibility(View.GONE);
                break;
            case R.id.btnDateok:
                btnDate.setText(mDate);
                dateBox.setVisibility(View.GONE);
                break;
        }
    }

    private void setTime() {
        TimePickerView pvTime = new TimePickerView(this, TimePickerView.Type.HOURS_MINS);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                mTime = TimeUtil.getTimeHHmm(date);
                btnTime.setText(mTime);
            }
        });
        pvTime.show();
    }

    private void setDate() {
        TimePickerView pvTime = new TimePickerView(this,
                TimePickerView.Type.YEAR_MONTH_DAY);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                mDate = TimeUtil.getTimeYMD(date);
                btnDate.setText(mDate);
            }
        });
        pvTime.show();
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnBack = (Button) findViewById(R.id.btnBack);
        topBox = (RelativeLayout) findViewById(R.id.topBox);
        radioBirth = (RadioButton) findViewById(R.id.radioBirth);
        radioEvent = (RadioButton) findViewById(R.id.radioEvent);
        etName = (EditText) findViewById(R.id.etName);
        etInfo = (EditText) findViewById(R.id.etInfo);
        btnTime = (Button) findViewById(R.id.btnTime);

        btnAdd.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnTime.setOnClickListener(this);
        btnDate = (Button) findViewById(R.id.btnDate);
        btnDate.setOnClickListener(this);
        radio0 = (RadioButton) findViewById(R.id.radio0);
        radio1 = (RadioButton) findViewById(R.id.radio1);
        radio2 = (RadioButton) findViewById(R.id.radio2);
        radio3 = (RadioButton) findViewById(R.id.radio3);

        radioBirth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    type = 0;
                }
            }
        });

        radioEvent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    type = 1;
                }
            }
        });

        radio0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    notice = 0;
                }
            }
        });

        radio1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    notice = 1;
                    SetRing.getRingtoneTitleList(AlarmActivity.this, RingtoneManager.TYPE_ALARM, new SetRing.OnRingFileListener() {
                        @Override
                        public void onSelect(File file) {

                        }

                        @Override
                        public void onRingtone(String url) {
                            fileName = url;
                        }
                    });
                }
            }
        });

        radio2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    notice = 2;
                }
            }
        });

        radio3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    notice = 3;
                    SetRing.getRingtoneTitleList(AlarmActivity.this, RingtoneManager.TYPE_ALARM, new SetRing.OnRingFileListener() {
                        @Override
                        public void onSelect(File file) {

                        }

                        @Override
                        public void onRingtone(String url) {
                            fileName = url;
                        }
                    });
                }
            }
        });
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        btnDateEsc = (Button) findViewById(R.id.btnDateEsc);
        btnDateEsc.setOnClickListener(this);
        btnDateok = (Button) findViewById(R.id.btnDateok);
        btnDateok.setOnClickListener(this);
        tvDate = (TextView) findViewById(R.id.tvDate);
        dateBox = (LinearLayout) findViewById(R.id.dateBox);
        dateBox.setVisibility(View.GONE);
    }

    public List<String> getRingtoneTitleList(int type) {
        List<String> resArr = new ArrayList<String>();
        RingtoneManager manager = new RingtoneManager(AlarmActivity.this);
        manager.setType(type);
        Cursor cursor = manager.getCursor();
        if (cursor.moveToFirst()) {
            do {
                resArr.add(cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX));
            } while (cursor.moveToNext());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(AlarmActivity.this);
        final String[] sChoice = new String[resArr.size()];
        for (int i = 0; i < resArr.size(); i++) {
            sChoice[i] = resArr.get(i);
        }
        builder.setSingleChoiceItems(
                sChoice,
                0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //添加操作...
//                        onRingFileListener.onSelect(vcardList.get(which));

                        dialog.dismiss();
                    }
                });
        builder.show();

        return resArr;
    }

    private void submit() {
        // validate
        String etNameString = etName.getText().toString().trim();
        if (TextUtils.isEmpty(etNameString)) {
            Toast.makeText(this, "请输入名称", Toast.LENGTH_SHORT).show();
            return;
        }

        String etInfoString = etInfo.getText().toString().trim();
        if (TextUtils.isEmpty(etInfoString)) {
            Toast.makeText(this, "请输入信息", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(mTime)) {
            Toast.makeText(this, "请选择时间", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(mDate)) {
            Toast.makeText(this, "请选择日期", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something

        if (alarms == null) {
            alarms = new Alarms();
        }
        alarms.setOpen(true);
        alarms.setName(etName.getText().toString());
        alarms.setInfos(etInfo.getText().toString());
        alarms.setType(type);
        alarms.setTime(mDate + " " + mTime);
        alarms.setUser(Global.getInstance().getMe().getName());
        alarms.setNotice(notice);
        alarms.setRing(fileName);
        alarms.setRemark(mDate + " " + mTime);
        alarms.save();
        finish();

    }
}
