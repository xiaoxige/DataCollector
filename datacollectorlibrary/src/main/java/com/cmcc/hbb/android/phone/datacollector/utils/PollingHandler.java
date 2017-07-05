package com.cmcc.hbb.android.phone.datacollector.utils;

import android.os.Handler;
import android.os.Message;

import com.cmcc.hbb.android.phone.datacollector.interfaces.IPollingListener;


/**
 * Created by zhuxiaoan on 2017/7/4.
 */

public class PollingHandler extends Handler {

    public static final int EMPTY = 0x00;

    private IPollingListener mPollingListener;
    private long mPollingTime;

    public PollingHandler(IPollingListener pollingListener, long pollingTime) {
        mPollingListener = pollingListener;
        mPollingTime = pollingTime;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (mPollingListener != null
                && mPollingListener.pollingTimeBack(msg)
                && mPollingTime > 0) {
            sendEmptyMessageDelayed(EMPTY, mPollingTime);
        }
    }
}
