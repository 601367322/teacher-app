package com.prance.lib.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 答题器
 *
 * @author bingbing
 */
@Entity(nameInDb = "keypad_table")
public class KeyPadEntity {

    @Id(autoincrement = true)
    private long id;

    @NotNull
    private String baseStationSN;

    private String keyId;

    public KeyPadEntity(String baseStationSN, String keyId) {
        this.baseStationSN = baseStationSN;
        this.keyId = keyId;
    }

    @Keep
    public KeyPadEntity() {
    }

    @Keep
    public KeyPadEntity(long id, String baseStationSN, String keyId) {
        this.id = id;
        this.baseStationSN = baseStationSN;
        this.keyId = keyId;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
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
