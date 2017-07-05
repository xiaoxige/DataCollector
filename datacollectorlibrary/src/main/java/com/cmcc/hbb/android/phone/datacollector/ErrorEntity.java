package com.cmcc.hbb.android.phone.datacollector;


import com.cmcc.hbb.android.phone.datacollector.interfaces.ALogEntity;

/**
 * Created by zhuxiaoan on 2017/7/5.
 */

public class ErrorEntity extends ALogEntity {

    // 设备信息
    public String infos;

    public ErrorEntity() {
        logType = "error";
    }

    @Override
    public String toContent() {
        return "tId: " + (tId + "_" + time) +
                ", logType: " + logType +
                ", time: " + time +
                ", desc: " + desc +
                ", infos: " + infos;
    }

}
