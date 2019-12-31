package com.example.myalarm.db;

import com.orm.SugarRecord;

import java.io.Serializable;

public class Users extends SugarRecord implements Serializable
{
    String name;//用户名
    String password;//密码
    String remark;//重写密码
    String phone; //电话
    String sex;//性别
    String birth;//生日

    public String getBirth()
    {
        return birth;
    }

    public void setBirth(String birth)
    {
        this.birth = birth;
    }


    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }
}
