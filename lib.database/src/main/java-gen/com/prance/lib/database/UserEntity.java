package com.prance.lib.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "user_table")
public class UserEntity implements Serializable {

    static final long serialVersionUID = -1;

    @Id(autoincrement = true)
    private long id;

    private String name;

    private String phone;

    @Keep
    public UserEntity() {
    }

    @Keep
    public UserEntity(long id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
