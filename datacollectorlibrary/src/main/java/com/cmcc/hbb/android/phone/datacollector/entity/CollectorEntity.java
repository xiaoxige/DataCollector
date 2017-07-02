package com.cmcc.hbb.android.phone.datacollector.entity;

/**
 * Created by 小稀革 on 2017/7/2.
 */

public class CollectorEntity implements IEntity {

    /**
     * 标识
     */
    private String id;
    /**
     * 类型
     */
    private String type;

    private long time;

    private String msg;

    public CollectorEntity() {

    }

    public CollectorEntity(String id, String type, String msg) {
        this.id = id;
        this.type = type;
        this.msg = msg;
        this.time = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "id: " + id + ", type: " + type + ", time: " + time + ", msg: " + msg;
    }
}
