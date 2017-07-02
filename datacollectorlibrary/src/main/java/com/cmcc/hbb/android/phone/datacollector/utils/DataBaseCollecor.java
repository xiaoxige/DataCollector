package com.cmcc.hbb.android.phone.datacollector.utils;

import android.content.Context;

import com.cmcc.hbb.android.phone.datacollector.interfaceview.Collector;
import com.cmcc.hbb.android.phone.datacollector.interfaceview.ILocalDataOperation;
import com.cmcc.hbb.android.phone.datacollector.interfaceview.IUpDataOpration;

/**
 * Created by 小稀革 on 2017/7/2.
 */

public class DataBaseCollecor extends Collector {

    public DataBaseCollecor(Context context, boolean isPrintLog, boolean isErrorUpNow, long maxLines, ILocalDataOperation localDataOperation, IUpDataOpration upDataOpration) {
        super(context, isPrintLog, isErrorUpNow, maxLines, localDataOperation, upDataOpration);
    }

    @Override
    public void start() {

    }

    @Override
    public void push() {

    }
}
