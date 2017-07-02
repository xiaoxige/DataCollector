package com.cmcc.hbb.android.phone.datacollector.utils;

import android.content.Context;

import com.cmcc.hbb.android.phone.datacollector.entity.IEntity;
import com.cmcc.hbb.android.phone.datacollector.interfaceview.Collector;
import com.cmcc.hbb.android.phone.datacollector.interfaceview.ILocalDataOperation;
import com.cmcc.hbb.android.phone.datacollector.interfaceview.IUpDataOpration;

import java.util.List;

/**
 * Created by 小稀革 on 2017/7/2.
 */

public class FileCollector extends Collector {


    public FileCollector(Context context, boolean isPrintLog, boolean isErrorUpNow, long maxLines, ILocalDataOperation localDataOperation, IUpDataOpration upDataOpration) {
        super(context, isPrintLog, isErrorUpNow, maxLines, localDataOperation, upDataOpration);
    }

    @Override
    public void start() {

    }

    @Override
    public void push() {
//        if (mDataOperation.getLines() >= mMaxLines || mIsErrorUpNow) {
//            mUpDataOpration.upCollectorDataToServer(mDataOperation.getAllEntity());
//        }
    }

    @Override
    public void commitErrorData(IEntity entity) {
        super.commitErrorData(entity);
//        mDataOperation.save(entity);
    }

    @Override
    public void commitErrorData(List<IEntity> entitys) {
        super.commitErrorData(entitys);
//        mDataOperation.save(entitys);
    }

    @Override
    public void commitNormalData(IEntity entity) {
        super.commitNormalData(entity);
//        mDataOperation.save(entity);
    }

    @Override
    public void commitNormalData(List<IEntity> entitys) {
        super.commitNormalData(entitys);
//        mDataOperation.save(entitys);
    }
}
