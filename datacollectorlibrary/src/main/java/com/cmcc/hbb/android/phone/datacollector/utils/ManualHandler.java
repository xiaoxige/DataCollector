package com.cmcc.hbb.android.phone.datacollector.utils;

import android.os.Handler;
import android.os.Message;

import com.cmcc.hbb.android.phone.datacollector.interfaceview.ITimeManualListener;

/**
 * Created by 小稀革 on 2017/7/2.
 * 时间轮询
 */

public class ManualHandler extends Handler {

    private long mTimeManual;
    private ITimeManualListener mTimeManualListener;

    public ManualHandler(long timeManual, ITimeManualListener timeManualListener) {
        mTimeManual = timeManual;
        mTimeManualListener = timeManualListener;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (mTimeManualListener != null) {
            if (mTimeManualListener.onTimeManualBack()) {
                sendEmptyMessageDelayed(0, mTimeManual);
            }
        }
    }
}
