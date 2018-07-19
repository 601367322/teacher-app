package com.prance.lib.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

/**
 * 答题器
 *
 * @author bingbing
 */
@Entity(nameInDb = "keypad_table")
public class KeyPadEntity {

    @Id(autoincrement = true)
    private Long id;

    /**
     * 基站编号
     */
    @NotNull
    private String baseStationSN;

    /**
     * 答题器编号
     */
    private String keyId;

    @Transient
    private Float battery;

    public KeyPadEntity(String baseStationSN, String keyId) {
        this.baseStationSN = baseStationSN;
        this.keyId = keyId;
    }

    @Keep
    public KeyPadEntity() {
    }

    @Keep
    public KeyPadEntity(Long id, String baseStationSN, String keyId) {
        this.id = id;
        this.baseStationSN = baseStationSN;
        this.keyId = keyId;
    }

    public KeyPadEntity(String keyId, Float battery) {
        this.keyId = keyId;
        this.battery = battery;
    }

    public Float getBattery() {
        return battery;
    }

    public void setBattery(Float battery) {
        this.battery = battery;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBaseStationSN() {
        return baseStationSN;
    }

    public void setBaseStationSN(String baseStationSN) {
        this.baseStationSN = baseStationSN;
    }

    public String getKeyId() {
        return this.keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }
}
