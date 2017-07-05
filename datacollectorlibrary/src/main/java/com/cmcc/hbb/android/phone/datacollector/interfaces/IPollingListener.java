package com.cmcc.hbb.android.phone.datacollector.interfaces;

import android.os.Message;

/**
 * Created by zhuxiaoan on 2017/7/4.
 * 轮询的监听
 */

public interface IPollingListener {

    boolean pollingTimeBack(Message message);

}
