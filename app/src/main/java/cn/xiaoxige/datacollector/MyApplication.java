package cn.xiaoxige.datacollector;

import android.app.Application;
import android.os.Message;

import com.cmcc.hbb.android.phone.datacollector.config.CollectorConfig;
import com.cmcc.hbb.android.phone.datacollector.interfaces.IPollingListener;
import com.cmcc.hbb.android.phone.datacollector.utils.CollectorUtil;

/**
 * Created by 小稀革 on 2017/7/2.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CollectorConfig config = new CollectorConfig.Buidler()
                .setIsShowLog(true)
                .setCollectorType(CollectorConfig.TYPE_COLLECTOR_ERROR)
                .setPollingTime(3 * 1000)
                .setLimitFileSize(200 * 1024)
                .setUpServer(new UpCollector())
                .Build();

        CollectorUtil.getInstance().initCollector(this, config)
                .setPollingTimeBack(new IPollingListener() {
                    @Override
                    public boolean pollingTimeBack(Message message) {
                        return true;
                    }
                })
                .start();

    }
}
