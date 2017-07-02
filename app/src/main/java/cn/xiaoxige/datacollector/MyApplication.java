package cn.xiaoxige.datacollector;

import android.app.Application;

import com.cmcc.hbb.android.phone.datacollector.config.CollectorConfig;
import com.cmcc.hbb.android.phone.datacollector.utils.CollectorUtils;

/**
 * Created by 小稀革 on 2017/7/2.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CollectorConfig collectorConfig = new CollectorConfig.Builder()
                .setIsPrintLog(true)
                .setIsErrorNow(true)
                .setSaveStrategy(CollectorConfig.TYPE_STRATEGY_FILEANDDATABASE)
                .setUpType(CollectorConfig.TYPE_TIMEANDMANUAL)
                .setMaxFileLine(1000)
                .setAndmanualTime(10 * 1000)
                .Build();

        CollectorUtils.getInstance()
                .initCollectorUtils(this, collectorConfig)
                .start();
    }
}
