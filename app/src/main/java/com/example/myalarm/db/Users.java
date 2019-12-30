package com.example.myalarm.db;

import com.orm.SugarRecord;

import java.io.Serializable;

public class Users extends SugarRecord implements Serializable {
    String name;

    String password;

    String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
