package com.example.myalarm;

import com.example.myalarm.db.Users;

public class Global {

    public static Global instance;
    public static Global getInstance()
    {
        if (instance == null)
        {
            instance = new Global();
            return instance;
        }
        return instance;
    }

    private Users me;

    public Users getMe() {
        return me;
    }

    public void setMe(Users me) {
        this.me = me;
    }
}
