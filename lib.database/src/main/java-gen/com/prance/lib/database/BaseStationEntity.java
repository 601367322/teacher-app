package com.prance.lib.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * 基站
 *
 * @author bingbing
 */
@Entity(nameInDb = "base_station_table")
public class BaseStationEntity implements Serializable {

    static final long serialVersionUID = -1;

    @Id(autoincrement = true)
    private long id;

    @NotNull
    @Unique
    private String sn;

    /**
     * 基站编号
     */
    @Transient
    private Integer stationId;

    /**
     * 基站信道
     */
    @Transient
    private Long stationChannel;

    @Keep
    public BaseStationEntity() {
    }

    @Keep
    public BaseStationEntity(long id, @NotNull String sn) {
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

    public Long getStationChannel() {
        return stationChannel;
    }

    public void setStationChannel(Long stationChannel) {
        this.stationChannel = stationChannel;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }
}
