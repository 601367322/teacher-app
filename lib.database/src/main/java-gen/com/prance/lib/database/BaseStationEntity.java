package com.prance.lib.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 基站
 *
 * @author bingbing
 */
@Entity(nameInDb = "base_station_table")
public class BaseStationEntity {

    static final long serialVersionUID = -1;

    @Id(autoincrement = true)
    private long id;

    @NotNull
    @Unique
    private String sn;

    @Keep
    public BaseStationEntity() {
    }

    @Keep
    public BaseStationEntity(long id, String sn) {
        this.id = id;
        this.sn = sn;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSn() {
        return this.sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }
}
