package com.example.myalarm.ui;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myalarm.App;
import com.example.myalarm.R;
import com.example.myalarm.db.Alarms;
import com.example.myalarm.utils.TimeUtil;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvTitle;
    private TextView tvInfo;
    private LinearLayout infoBox;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private LinearLayout bottomBox;

    private Alarms alarms;
    private MediaPlayer mediaPlayer;
    Ringtone ringtone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        initView();

        initData();
    }

    @TargetApi(Build.VERSION_CODES.P)
    private void initData() {
        try {
            String ids = getIntent().getExtras().getString("data");
            String[] id = ids.split(";");
            alarms = Alarms.findById(Alarms.class, Long.valueOf(id[0]).longValue());
            if (alarms != null) {
                tvTitle.setText(alarms.getName());
                tvInfo.setText(alarms.getInfos());

                RingNotice(alarms.getNotice());

                SetNewTaskNotice();
            } else {
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer !=null)
        {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
        if (ringtone != null)
        {
            ringtone.stop();
            ringtone = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                if (alarms.getType() == 0) {
                    try {
                        long time = TimeUtil.getAlarmTime(alarms.getTime());
                        Calendar c = Calendar.getInstance();
                        c.setTimeInMillis(time);
                        c.add(Calendar.YEAR, 1);
                        String nextTime = TimeUtil.getTime(c.getTime());
                        alarms.setTime(nextTime);
                        alarms.save();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        alarms.setOpen(false);
                        alarms.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                finish();
                break;
            case R.id.btn2:
                try {
                    long time = TimeUtil.getAlarmTime(alarms.getTime());
                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(time);
                    c.add(Calendar.MINUTE, 5);
                    String nextTime = TimeUtil.getTime(c.getTime());
                    alarms.setTime(nextTime);
                    alarms.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                finish();
                break;
            case R.id.btn3:
                try {
                    long time = TimeUtil.getAlarmTime(alarms.getTime());
                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(time);
                    c.add(Calendar.MINUTE, 10);
                    String nextTime = TimeUtil.getTime(c.getTime());
                    alarms.setTime(nextTime);
                    alarms.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                finish();
                break;
        }
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        infoBox = (LinearLayout) findViewById(R.id.infoBox);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        bottomBox = (LinearLayout) findViewById(R.id.bottomBox);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
    }

    /**
     * 铃声+振动播放方法
     *
     * @return
     * @throws Exception
     * @throws IOException
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void RingNotice(int type) throws Exception, IOException {
        if (type == 2 || type == 3) {
            Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
            vib.vibrate(800);
        }
        if (type == 1 || type == 3) {
            Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

//            File file = new File(alarms.getRing());
            if (!TextUtils.isEmpty(alarms.getRing()) && alarms.getRing().startsWith("ring")) {
//                ringtone = getRingtone(RingtoneManager.TYPE_RINGTONE,Integer.valueOf(alarms.getRing().substring(4)));
//                ringtone.setLooping(true);
//                ringtone.play();

                List<Ringtone> resArr = new ArrayList<Ringtone>();
                RingtoneManager manager = new RingtoneManager(this);
                manager.setType(RingtoneManager.TYPE_ALARM);
                Cursor cursor = manager.getCursor();
                int count = cursor.getCount();
                for(int i = 0 ; i < count ; i ++){
                    resArr.add(manager.getRingtone(i));
                }
                ringtone = manager.getRingtone(Integer.valueOf(alarms.getRing().substring(4)));
                ringtone.setLooping(true);
                ringtone.play();

            } else {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(InfoActivity.this, alert);
                final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                if (audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION) != 0) {
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
                    mediaPlayer.setLooping(false);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                }
            }


        }
    }

    private int m_minNotificationId = 10000;

    private void SetNewTaskNotice()
    {
        Context context = InfoActivity.this;
        Intent intent = new Intent(context, InfoActivity.class);
        intent.putExtra("data", alarms.getId());
        intent.putExtra("notice", m_minNotificationId);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = null;
        String id = "alarm_001";
        String name = "myalarm";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(context)
                    .setChannelId(id)
//                            .setCustomBigContentView(new RemoteViews(context.getPackageName(),
//                                    R.layout.layout_notification_simple_view))
                    .setContentText(alarms.getInfos())
                    .setContentTitle(alarms.getName())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(PendingIntent.getActivities(context, m_minNotificationId,
                            new Intent[]{intent}, PendingIntent.FLAG_UPDATE_CURRENT)).build();
        } else {
            android.support.v4.app.NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
//                            .setCustomBigContentView(new RemoteViews(context.getPackageName(),
//                                    R.layout.layout_notification_simple_view))
                    .setContentText(alarms.getInfos())
                    .setContentTitle(alarms.getName())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(PendingIntent.getActivities(context, m_minNotificationId,
                            new Intent[]{intent}, PendingIntent.FLAG_UPDATE_CURRENT))
                    .setOngoing(true);
            notification = notificationBuilder.build();
        }
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        manager.notify(m_minNotificationId++, notification);
    }
}
