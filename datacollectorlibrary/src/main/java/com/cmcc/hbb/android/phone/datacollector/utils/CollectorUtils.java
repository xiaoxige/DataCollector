package com.cmcc.hbb.android.phone.datacollector.utils;

import android.content.Context;

import com.cmcc.hbb.android.phone.datacollector.config.CollectorConfig;
import com.cmcc.hbb.android.phone.datacollector.entity.IEntity;
import com.cmcc.hbb.android.phone.datacollector.interfaceview.Collector;
import com.cmcc.hbb.android.phone.datacollector.interfaceview.ICollectorOpration;
import com.cmcc.hbb.android.phone.datacollector.interfaceview.ICollectorRelease;
import com.cmcc.hbb.android.phone.datacollector.interfaceview.ITimeManualListener;

import java.util.List;

/**
 * Created by 小稀革 on 2017/7/1.
 * ITimeManualListener 时间轮询接口
 */

public class CollectorUtils implements ITimeManualListener, ICollectorOpration, ICollectorRelease {

    private static CollectorUtils mCollector;
    private Context mContext;
    private CollectorConfig mConfig;

    private ManualHandler mManualHandler;

    private Collector mFileCollector;
    private Collector mDataBaseCollector;


    private CollectorUtils() {
    }

    public static CollectorUtils getInstance() {
        if (mCollector == null) {
            mCollector = new CollectorUtils();
        }
        return mCollector;
    }

    public CollectorUtils initCollectorUtils(Context context, CollectorConfig collectorConfig) {
        this.mContext = context;
        this.mConfig = collectorConfig;

        // App声明周期监听
        AppLifecycleCallBacks.getInstance().initLifecycle(mContext, this, this);

        mManualHandler = new ManualHandler(this.mConfig.getAndmanualTime(), this);
        return this;
    }

    public void start() {

        if (mConfig.getType_strategy() == CollectorConfig.TYPE_STRATEGY_FILE) {
            mFileCollector = new FileCollector(mContext, mConfig.isPrintLog(), mConfig.isErrorUpNow(), mConfig.getMaxLines(),
                    mConfig.getLocalFileDataOperation(), mConfig.getUpFileDataOpration());
        } else if (mConfig.getType_strategy() == CollectorConfig.TYPE_STRATEGY_DATABASE) {
            mDataBaseCollector = new DataBaseCollecor(mContext, mConfig.isPrintLog(), mConfig.isErrorUpNow(), mConfig.getMaxLines(),
                    mConfig.getLocalDataBaseDataOperation(), mConfig.getUpFileDataBaseDataOpration());
        } else if (mConfig.getType_strategy() == CollectorConfig.TYPE_STRATEGY_FILEANDDATABASE) {
            mFileCollector = new FileCollector(mContext, mConfig.isPrintLog(), mConfig.isErrorUpNow(), mConfig.getMaxLines(),
                    mConfig.getLocalFileDataOperation(), mConfig.getUpFileDataOpration());
            mDataBaseCollector = new DataBaseCollecor(mContext, mConfig.isPrintLog(), mConfig.isErrorUpNow(), mConfig.getMaxLines(),
                    mConfig.getLocalDataBaseDataOperation(), mConfig.getUpFileDataBaseDataOpration());
        }

        if (mFileCollector != null) {
            mFileCollector.start();
        }

        if (mDataBaseCollector != null) {
            mDataBaseCollector.start();
        }

        mManualHandler.sendEmptyMessage(0);
    }


    private void releaseCollector() {
        closeTimemanual();
    }

    private void closeTimemanual() {
        if (mManualHandler != null) {
            mManualHandler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * 手动提交
     */
    public void manualUp() {
        if (mConfig.getType_up() == CollectorConfig.TYPE_TIMEPOLLING) {
            return;
        }
        push();
    }

    /**
     * 提交正常数据到本地
     */
    public void commitNormalLocalData(IEntity entity) {
        if (mFileCollector != null) {
            mFileCollector.commitNormalData(entity);
        }
        if (mDataBaseCollector != null) {
            mDataBaseCollector.commitNormalData(entity);
        }
    }

    public void commitNormalLocalData(List<IEntity> entities) {
        if (mFileCollector != null) {
            mFileCollector.commitNormalData(entities);
        }
        if (mDataBaseCollector != null) {
            mDataBaseCollector.commitNormalData(entities);
        }
    }

    /**
     * 提交错误日志到本地
     */
    public void commitErrorLocalData(IEntity entity) {
        if (mFileCollector != null) {
            mFileCollector.commitErrorData(entity);
        }
        if (mDataBaseCollector != null) {
            mDataBaseCollector.commitErrorData(entity);
        }
    }

    public void commitErrorLocalData(List<IEntity> entities) {
        if (mFileCollector != null) {
            mFileCollector.commitErrorData(entities);
        }
        if (mDataBaseCollector != null) {
            mDataBaseCollector.commitErrorData(entities);
        }
    }

    /**
     * 时间轮询
     */
    @Override
    public boolean onTimeManualBack() {

        /**
         * 上传：轮询、 手动、 轮询及手动
         * 保存：数据库、 文件、 数据库及文件
         * 是否打印、 是否错误立即上传、最大行数
         */
        // 如果是手动上传， 就关闭轮询
        if (mConfig.getType_up() == CollectorConfig.TYPE_MANUALUP) {
            return false;
        }

        push();

        return true;
    }

    /**
     * 提交
     */
    private void push() {
        if (mFileCollector != null) {
            mFileCollector.push();
        }
        if (mDataBaseCollector != null) {
            mDataBaseCollector.push();
        }
    }

    @Override
    public void commitNormalData(List<IEntity> entitys) {
        commitNormalLocalData(entitys);
    }

    @Override
    public void commitNormalData(IEntity entity) {
        commitNormalLocalData(entity);
    }

    @Override
    public void commitErrorData(List<IEntity> entitys) {
        commitErrorLocalData(entitys);
    }

    @Override
    public void commitErrorData(IEntity entity) {
        commitErrorLocalData(entity);
    }

    @Override
    public void release() {
        releaseCollector();
    }
}
