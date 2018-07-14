package com.prance.lib.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
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
    private long baseStationId;

    private String keyId;

    @Generated(hash = 1914328320)
    public KeyPadEntity(long id, long baseStationId, String keyId) {
        this.id = id;
        this.baseStationId = baseStationId;
        this.keyId = keyId;
    }

    @Generated(hash = 1655136874)
    public KeyPadEntity() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBaseStationId() {
        return this.baseStationId;
    }

    public void setBaseStationId(long baseStationId) {
        this.baseStationId = baseStationId;
    }

    public String getKeyId() {
        return this.keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }
}
