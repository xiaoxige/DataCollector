package com.cmcc.hbb.android.phone.datacollector.config;

import android.support.annotation.IntDef;

import com.cmcc.hbb.android.phone.datacollector.interfaceview.ILocalDataOperation;
import com.cmcc.hbb.android.phone.datacollector.interfaceview.IUpDataOpration;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Created by 小稀革 on 2017/7/1.
 */

public class CollectorConfig {
    /**
     * 文件保存策略
     */
    public static final int TYPE_STRATEGY_FILE = 0x01;
    /**
     * r
     * 数据库保存策略
     */
    public static final int TYPE_STRATEGY_DATABASE = 0x02;
    /**
     * 文件和数据库保存策略
     */
    public static final int TYPE_STRATEGY_FILEANDDATABASE = TYPE_STRATEGY_FILE | TYPE_STRATEGY_DATABASE;

    /**
     * 时间轮询
     */
    public static final int TYPE_TIMEPOLLING = 0x01;

    /**
     * 手动上传
     */
    public static final int TYPE_MANUALUP = 0x02;

    /**
     * 手动和轮询
     */
    public static final int TYPE_TIMEANDMANUAL = TYPE_TIMEPOLLING | TYPE_MANUALUP;


    @collectorSaveType
    private int type_strategy;

    @collectorUpTye
    private int type_up;

    private long maxLines;

    private long andmanualTime;

    private boolean isPrintLog;

    private boolean isErrorUpNow;

    private ILocalDataOperation localFileDataOperation;
    private IUpDataOpration upFileDataOpration;

    private ILocalDataOperation localDataBaseDataOperation;
    private IUpDataOpration upFileDataBaseDataOpration;


    public CollectorConfig(Builder builder) {
        this.type_strategy = builder.typeStrategy;
        this.type_up = builder.upType;
        this.maxLines = builder.maxLines;
        this.andmanualTime = builder.andmanualTime;
        this.isPrintLog = builder.isPrintLog;
        this.isErrorUpNow = builder.isErrorUpNow;
        this.localFileDataOperation = builder.localFileDataOperation;
        this.upFileDataOpration = builder.upFileDataOpration;
        this.localDataBaseDataOperation = builder.localDataBaseDataOperation;
        this.upFileDataBaseDataOpration = builder.upFileDataBaseDataOpration;
    }

    public int getType_strategy() {
        return type_strategy;
    }

    public int getType_up() {
        return type_up;
    }

    public long getMaxLines() {
        return maxLines;
    }

    public long getAndmanualTime() {
        return andmanualTime;
    }

    public boolean isPrintLog() {
        return isPrintLog;
    }

    public boolean isErrorUpNow() {
        return isErrorUpNow;
    }

    public ILocalDataOperation getLocalFileDataOperation() {
        return localFileDataOperation;
    }

    public IUpDataOpration getUpFileDataOpration() {
        return upFileDataOpration;
    }

    public ILocalDataOperation getLocalDataBaseDataOperation() {
        return localDataBaseDataOperation;
    }

    public IUpDataOpration getUpFileDataBaseDataOpration() {
        return upFileDataBaseDataOpration;
    }

    public static class Builder {

        @collectorSaveType
        private int typeStrategy;
        @collectorUpTye
        private int upType;

        private long maxLines;

        private long andmanualTime;

        private boolean isPrintLog;

        private boolean isErrorUpNow;

        private ILocalDataOperation localFileDataOperation;
        private IUpDataOpration upFileDataOpration;

        private ILocalDataOperation localDataBaseDataOperation;
        private IUpDataOpration upFileDataBaseDataOpration;

        public Builder() {
            isPrintLog = true;
            typeStrategy = TYPE_STRATEGY_FILEANDDATABASE;
            upType = TYPE_TIMEANDMANUAL;
            maxLines = 100;
            andmanualTime = 5 * 1000;
        }

        public Builder setSaveStrategy(@collectorSaveType int strategy) {
            if (strategy == TYPE_STRATEGY_FILE) {
                typeStrategy = TYPE_STRATEGY_FILE;
            } else if (strategy == TYPE_STRATEGY_DATABASE) {
                typeStrategy = TYPE_STRATEGY_DATABASE;
            } else if (strategy == TYPE_STRATEGY_FILEANDDATABASE) {
                typeStrategy = TYPE_STRATEGY_FILEANDDATABASE;
            }
            return this;
        }

        public Builder setUpType(@collectorUpTye int upType) {
            if (upType == TYPE_TIMEPOLLING) {
                this.upType = TYPE_TIMEPOLLING;
            } else if (upType == TYPE_MANUALUP) {
                this.upType = TYPE_MANUALUP;
            } else if (upType == TYPE_TIMEANDMANUAL) {
                this.upType = TYPE_TIMEANDMANUAL;
            }
            return this;
        }

        public Builder setMaxFileLine(long maxLines) {
            this.maxLines = maxLines;
            return this;
        }

        public Builder setAndmanualTime(long andmanualTime) {
            this.andmanualTime = andmanualTime;
            return this;
        }

        public Builder setIsPrintLog(boolean isPrintLog) {
            this.isPrintLog = isPrintLog;
            return this;
        }

        public Builder setIsErrorNow(boolean isErrorUpNow) {
            this.isErrorUpNow = isErrorUpNow;
            return this;
        }

        public Builder setFileDataOperation(ILocalDataOperation fileDataOperation) {
            this.localFileDataOperation = fileDataOperation;
            return this;
        }

        public Builder setUpFileDataOpration(IUpDataOpration upFileDataOpration) {
            this.upFileDataOpration = upFileDataOpration;
            return this;
        }

        public Builder localDataBaseDataOperation(ILocalDataOperation localDataBaseDataOperation) {
            this.localDataBaseDataOperation = localDataBaseDataOperation;
            return this;
        }

        public Builder setUpDataBaseDataOpration(IUpDataOpration upFileDataBaseDataOpration) {
            this.upFileDataBaseDataOpration = upFileDataBaseDataOpration;
            return this;
        }

        public CollectorConfig Build() {
            return new CollectorConfig(this);
        }

    }


    /**
     * 保存策略注解
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_STRATEGY_DATABASE, TYPE_STRATEGY_FILE, TYPE_STRATEGY_FILEANDDATABASE})
    public @interface collectorSaveType {
    }

    /**
     * 长传策略注解
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_TIMEPOLLING, TYPE_MANUALUP, TYPE_TIMEANDMANUAL})
    public @interface collectorUpTye {
    }

}
