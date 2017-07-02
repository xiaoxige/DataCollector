package com.cmcc.hbb.android.phone.datacollector.interfaceview;

import android.content.Context;
import android.util.Log;

import com.cmcc.hbb.android.phone.datacollector.entity.IEntity;

import java.util.List;

/**
 * Created by 小稀革 on 2017/7/2.
 */

public abstract class Collector implements ICollector, ICollectorOpration {
    private static final String TAG = Collector.class.getSimpleName();
    protected Context mContext;
    protected boolean mIsPrintLog;
    protected boolean mIsErrorUpNow;
    protected long mMaxLines;

    protected ILocalDataOperation mDataOperation;
    protected IUpDataOpration mUpDataOpration;

    public Collector(Context context, boolean isPrintLog, boolean isErrorUpNow, long maxLines, ILocalDataOperation localDataOperation, IUpDataOpration upDataOpration) {
        mContext = context;
        mIsPrintLog = isPrintLog;
        mIsErrorUpNow = isErrorUpNow;
        mMaxLines = maxLines;
        mDataOperation = localDataOperation;
        mUpDataOpration = upDataOpration;
    }

    @Override
    public void commitNormalData(List<IEntity> entitys) {
        for (IEntity entity : entitys) {
            commitNormalData(entity);
        }
    }

    @Override
    public void commitNormalData(IEntity entity) {
        if (mIsPrintLog) {
            Log.e(TAG, entity.toString());
        }

    }

    @Override
    public void commitErrorData(List<IEntity> entitys) {
        for (IEntity entity : entitys) {
            commitErrorData(entity);
        }
    }

    @Override
    public void commitErrorData(IEntity entity) {
        if (mIsPrintLog) {
            Log.e(TAG, entity.toString());
        }
    }
}
