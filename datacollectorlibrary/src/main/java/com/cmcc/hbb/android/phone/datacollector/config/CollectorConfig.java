package com.cmcc.hbb.android.phone.datacollector.config;

import android.support.annotation.IntDef;

import com.cmcc.hbb.android.phone.datacollector.interfaces.IUpServer;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by zhuxiaoan on 2017/7/4.
 * 收集日志的参数
 */

public class CollectorConfig {

    /**
     * 收集所有的信息
     */
    public static final int TYPE_COLLECTOR_ALL = 0x01;
    /**
     * 收集错误的信息
     */
    public static final int TYPE_COLLECTOR_ERROR = 0x02;
    /**
     * 轮询时间
     */
    private long pollingTime;

    /**
     * 限定文件大小
     */
    private long limitFileSize;

    /**
     * 是否显示Log
     */
    private boolean isShowLog;

    /**
     * 是否收集生命周期及点击事件
     */

    private boolean isCollectorLifeAndClick;

    /**
     * 上传的接口
     */
    private IUpServer upServer;

    /**
     * 收集的类型
     */
    @CollectorConfig.CollectorType
    private int collectorType;

    public CollectorConfig(Buidler buidler) {
        this.pollingTime = buidler.pollingTime;
        this.limitFileSize = buidler.limitFileSize;
        this.isShowLog = buidler.isShowLog;
        this.upServer = buidler.upServer;
        this.collectorType = buidler.collectorType;
        this.isCollectorLifeAndClick = buidler.isCollectorLifeAndClick;
    }


    public long getPollingTime() {
        return pollingTime;
    }

    public long getLimitFileSize() {
        return limitFileSize;
    }

    public boolean isShowLog() {
        return isShowLog;
    }

    @CollectorType
    public int getCollectorType() {
        return collectorType;
    }

    public boolean isCollectorLifeAndClick() {
        return isCollectorLifeAndClick;
    }

    public IUpServer getUpServer() {
        return upServer;
    }

    public static class Buidler {

        private long pollingTime;
        private long limitFileSize;

        private boolean isShowLog;
        private boolean isCollectorLifeAndClick;
        private IUpServer upServer;

        @CollectorConfig.CollectorType
        private int collectorType;

        public Buidler() {
            pollingTime = 3000;         // 默认3秒
            limitFileSize = 1024 * 100; // 默认100kb
            isShowLog = true;           // 默认显示Log
            isCollectorLifeAndClick = true;
            upServer = null;
            collectorType = TYPE_COLLECTOR_ERROR;
        }

        public Buidler setPollingTime(long pollingTime) {
            if (pollingTime >= 0) {
                this.pollingTime = pollingTime;
            }
            return this;
        }

        public Buidler setLimitFileSize(long limitFileSize) {
            if (limitFileSize >= 0) {
                this.limitFileSize = limitFileSize;
            }
            return this;
        }

        public Buidler setIsShowLog(boolean isShowLog) {
            this.isShowLog = isShowLog;
            return this;
        }

        public Buidler setUpServer(IUpServer upServer) {
            this.upServer = upServer;
            return this;
        }

        public Buidler setIsCollectorLifeAndClick(boolean isCollectorLifeAndClick) {
            this.isCollectorLifeAndClick = isCollectorLifeAndClick;
            return this;
        }

        public Buidler setCollectorType(@CollectorType int collectorType) {
            this.collectorType = collectorType;
            return this;
        }

        public CollectorConfig Build() {
            return new CollectorConfig(this);
        }

    }

    /**
     * 收集数据的策略
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_COLLECTOR_ALL, TYPE_COLLECTOR_ERROR})
    public @interface CollectorType {
    }

}
