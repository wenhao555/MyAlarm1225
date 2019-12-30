package com.example.myalarm.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;

import com.example.myalarm.ui.AlarmActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SetRing {

    public static final String BACKUP_PATH = "/AlarmRing/";

    public static void CreateDir()
    {
        String savePath = Environment.getExternalStorageDirectory().getPath()
                + BACKUP_PATH;
        File fileDir = new File(savePath);
        if (!fileDir.exists())
            fileDir.mkdir();
    }

    public static void LoadContacts(Context context, final OnRingFileListener onRingFileListener)
    {
        String savePath = Environment.getExternalStorageDirectory().getPath()
                + BACKUP_PATH;
        File fileDir = new File(savePath);
        if(!fileDir.exists())
        {
            return;
        }
        else
        {
            File[] files = fileDir.listFiles();
            final List<File> vcardList = new ArrayList<>();
            for(int i = 0; i < files.length; i++)
            {
                if(files[i].getName().toLowerCase().endsWith(".wav") ||
                        files[i].getName().toLowerCase().endsWith(".mp3"))
                {
                    vcardList.add(files[i]);
                }

            }

            if(vcardList.size()>0)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final String[] sChoice = new String[vcardList.size()];
                for(int i = 0;i<vcardList.size();i++)
                {
                    sChoice[i] = vcardList.get(i).getName();
                }
                builder.setSingleChoiceItems(
                        sChoice,
                        0,
                        new DialogInterface.OnClickListener()                {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                //添加操作...
                                onRingFileListener.onSelect(vcardList.get(which));

                                dialog.dismiss();
                            }
                        });
                builder.show();
            }
            else
            {
                return;
            }
        }
    }

    public static List<String> getRingtoneTitleList(final Context context, int type, final OnRingFileListener onRingFileListener){
        List<String> resArr = new ArrayList<String>();
        RingtoneManager manager = new RingtoneManager(context);
        manager.setType(type);
        Cursor cursor = manager.getCursor();
        if(cursor.moveToFirst()){
            do{
                resArr.add(cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX));
            }while(cursor.moveToNext());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final String[] sChoice = new String[resArr.size()];
        for(int i = 0;i<resArr.size();i++)
        {
            sChoice[i] = resArr.get(i);
        }
        builder.setSingleChoiceItems(
                sChoice,
                0,
                new DialogInterface.OnClickListener()                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //添加操作...
                        onRingFileListener.onRingtone("ring"+which);

                        dialog.dismiss();
                    }
                });
        builder.show();

        return resArr;
    }

    public interface OnRingFileListener
    {
        void onSelect(File file);
        void onRingtone(String url);
    }

}
