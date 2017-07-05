package com.cmcc.hbb.android.phone.datacollector;

import com.cmcc.hbb.android.phone.datacollector.interfaces.ALogEntity;

/**
 * Created by zhuxiaoan on 2017/7/5.
 */

public class NetEntity extends ALogEntity {

    public NetEntity() {
        logType = "net";
    }

    @Override
    public String toContent() {
        return "tId: " + (tId + "_" + time) +
                ", logType: " + logType +
                ", time: " + time +
                ", desc: " + desc;
    }
}
