package com.example.myalarm.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.myalarm.App;
import com.example.myalarm.R;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private int themeIndex;

    public int getThemeIndex() {
        SharedPreferences m_preferences = getSharedPreferences("set", Context.MODE_PRIVATE);
        themeIndex = m_preferences.getInt("theme",0);
        return themeIndex;
    }

    public void setThemeIndex(int themeIndex) {
        this.themeIndex = themeIndex;
        SharedPreferences m_preferences = getSharedPreferences("set", Context.MODE_PRIVATE);
        SharedPreferences.Editor m_editor = m_preferences.edit();
        m_editor.putInt("theme",themeIndex);
        m_editor.commit();

    }

    public int[][] themeColors = {
            {R.color.colorAccent,R.color.colorPrimary},
            {R.color.colorPrimaryDark,R.color.colorAccent},
            {R.color.colorPrimary,R.color.colorPrimaryDark},
            {R.color.color1,R.color.color2},
    };
}
