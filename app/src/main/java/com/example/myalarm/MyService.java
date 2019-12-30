package com.example.myalarm;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.example.myalarm.db.Alarms;
import com.example.myalarm.utils.TimeUtil;

import java.text.ParseException;
import java.util.List;

public class MyService extends Service {

    public static final String REV_TAG = "com.example.myalarm.alarm";
    private AlarmThread alarmThread;
    private MyReceiver myReceiver;
    private int timeDelay = 5000;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.e("roc-service","start");

        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(REV_TAG);
        //当网络发生变化的时候，系统广播会发出值为android.net.conn.CONNECTIVITY_CHANGE这样的一条广播
        registerReceiver(myReceiver,intentFilter);

        StartThread();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myReceiver!=null)
        {
            unregisterReceiver(myReceiver);
        }

    }

    private void StartThread()
    {
        if (alarmThread == null)
        {
            alarmThread = new AlarmThread();
            alarmThread.isRunning = true;
            alarmThread.start();
        }
    }

    class AlarmThread extends Thread
    {
        protected boolean isRunning;
        protected long lastTime;
        @Override
        public void run() {
            super.run();
            //完成线程内功能
            while (isRunning) {
                if (System.currentTimeMillis() - lastTime > timeDelay)
                {
                    Log.e("roc-service","thread-run");
                    lastTime = System.currentTimeMillis();

                    List<Alarms> tempList = Alarms.listAll(Alarms.class);
                    for (int i=0;i<tempList.size();i++)
                    {
                        try {
                            if (!tempList.get(i).getOpen() ||
                                    TimeUtil.getAlarmTime(tempList.get(i).getTime())<=System.currentTimeMillis() ||
                                    TimeUtil.getAlarmTime(tempList.get(i).getTime())>System.currentTimeMillis()+timeDelay)
                            {
                                tempList.remove(i--);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    if (!tempList.isEmpty())
                    {
                        String ids = "";
                        for (Alarms a:tempList)
                        {
                            ids += a.getId()+";";
                        }
                        if (ids.endsWith(";"))
                        {
                            ids = ids.substring(0,ids.length()-1);
                        }
                        Intent intent = new Intent();
                        intent.setAction(REV_TAG);
                        intent.putExtra("data",ids);
                        sendBroadcast(intent);//发送标准广播
                        Log.e("roc-alarm","send**"+ids+"***"+System.currentTimeMillis());
                    }

                }
            }
        }
    }
}
