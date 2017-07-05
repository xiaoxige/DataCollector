package com.cmcc.hbb.android.phone.datacollector;

import android.app.Activity;

import com.cmcc.hbb.android.phone.datacollector.interfaces.ALogEntity;


/**
 * Created by zhuxiaoan on 2017/7/5.
 */

public class ClickEntity extends ALogEntity {

    public String appName;
    public String module;

    public ClickEntity(Activity activity) {
        super();
        logType = "click";
        appName = activity.getPackageName();
        module = activity.getClass().getSimpleName();
    }

    @Override
    public String toContent() {
        return "tId: " + (tId + "_" + time) +
                ", logType: " + logType +
                ", time: " + time +
                ", appName: " + appName +
                ", module: " + module +
                ", desc: " + desc;
    }
}
