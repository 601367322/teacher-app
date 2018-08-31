package com.prance.lib.database;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import org.greenrobot.greendao.annotation.Generated;


/**
 * 收到的历史消息记录表
 * 为了防止消息重复
 */
@Entity(nameInDb = "message_db_table")
public class MessageEntity implements Serializable {

    static final long serialVersionUID = -1;

    @Id(autoincrement = true)
    private Long id;

    private int cmd;

    /**
     * 消息内容，不存这个，存json
     */
    @Transient
    private Object data;

    private String dataJson;

    @Index
    private String msgId;

    @Keep
    public MessageEntity() {
    }

    @Keep
    public MessageEntity(Long id, int cmd, String dataJson, String msgId) {
        this.id = id;
        this.cmd = cmd;
        this.dataJson = dataJson;
        this.msgId = msgId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getDataJson() {
        return dataJson;
    }

    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;
    }

    public <T> T getData(Class<T> clazz) {
        if (dataJson == null) {
            setData(data);
        }
        return new Gson().fromJson(dataJson, clazz);
    }

    public <T> ArrayList<T> getArrayData(Class<T> classOfT) {
        if (dataJson == null) {
            setData(data);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        ArrayList<JsonObject> jsonObjs = gson.fromJson(dataJson, type);
        ArrayList<T> listOfT = null;
        try {
            listOfT = new ArrayList<>();
            for (JsonObject jsonObj : jsonObjs) {
                listOfT.add(gson.fromJson(jsonObj, classOfT));
            }
            return listOfT;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setData(Object data) {
        this.data = data;
        this.dataJson = new Gson().toJson(data);
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
