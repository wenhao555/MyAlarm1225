package com.example.myalarm;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.myalarm.ui.InfoActivity;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //在这里写上相关的处理代码，一般来说，不要此添加过多的逻辑或者是进行任何的耗时操作
        //因为广播接收器中是不允许开启多线程的，过久的操作就会出现报错
        //因此广播接收器更多的是扮演一种打开程序其他组件的角色，比如创建一条状态栏通知，或者启动某个服务
        Log.e("roc-receiver","get");
        if(intent.hasExtra("data"))
        {
            Intent intent1 = new Intent(context, InfoActivity.class);
            intent1.putExtra("data",(String)intent.getExtras().get("data"));
            intent1.setFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
    }
}
