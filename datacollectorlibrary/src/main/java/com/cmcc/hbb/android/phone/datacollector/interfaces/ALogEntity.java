package com.cmcc.hbb.android.phone.datacollector.interfaces;

/**
 * Created by zhuxiaoan on 2017/7/5.
 */

public abstract class ALogEntity implements IEntity {

    // 标示
    public static String tId;
    // 类型
    public String logType;
    // 时间
    public long time;
    // 描述
    public String desc;

    public ALogEntity() {
        time = System.currentTimeMillis();
    }
}
