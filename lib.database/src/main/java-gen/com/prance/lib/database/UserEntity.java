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
    private long Id;

    @Keep
    public UserEntity() {
    }

    @Keep
    public UserEntity(long id) {
        Id = id;
    }

    public long getId() {
        return this.Id;
    }

    public void setId(long Id) {
        this.Id = Id;
    }
}
