package com.cmcc.hbb.android.phone.datacollector.utils;

import android.content.Context;
import android.os.Message;

import com.cmcc.hbb.android.phone.datacollector.config.CollectorConfig;
import com.cmcc.hbb.android.phone.datacollector.interfaces.ACollector;
import com.cmcc.hbb.android.phone.datacollector.interfaces.ALogEntity;
import com.cmcc.hbb.android.phone.datacollector.interfaces.IEntity;
import com.cmcc.hbb.android.phone.datacollector.interfaces.IPollingListener;


/**
 * Created by zhuxiaoan on 2017/7/4.
 * 收集日志的工具类
 */

public class CollectorUtil implements IPollingListener {

    private static CollectorUtil sCollectorUtil;

    /**
     * 用于把轮询回调到外部
     */
    private IPollingListener mListener;

    private Context mContext;
    private CollectorConfig mConfig;
    private PollingHandler mPollingHandler;

    private ACollector mFileCollector;

    private CollectorUtil() {
    }

    public static CollectorUtil getInstance() {
        if (sCollectorUtil == null) {
            sCollectorUtil = new CollectorUtil();
        }
        return sCollectorUtil;
    }


    public CollectorUtil initCollector(Context context, CollectorConfig collectorConfig) {
        mConfig = collectorConfig;
        mContext = context;

        mFileCollector = new FileCollector(mContext, mConfig.getCollectorType(), mConfig.getLimitFileSize(), mConfig.isShowLog(), mConfig.getUpServer());
        mPollingHandler = new PollingHandler(this, mConfig.getPollingTime());

        return sCollectorUtil;
    }

    /**
     * 设置监听(用于外部)
     *
     * @param listener
     * @return
     */
    public CollectorUtil setPollingTimeBack(IPollingListener listener) {
        mListener = listener;
        return sCollectorUtil;
    }

    /**
     * 开始收集
     */
    public void start() {

        mFileCollector.start();
        mPollingHandler.sendEmptyMessage(PollingHandler.EMPTY);

        if (mConfig.isCollectorLifeAndClick()) {
            ActivityLifeBack.getInstance().initLifeBack(mContext);
        }

    }

    /**
     * 自定义提交日志信息
     */
    public void commit(IEntity entity, @CollectorConfig.CollectorType int collectorType) {
        if (mFileCollector != null && entity != null) {
            mFileCollector.commit(entity, collectorType);
        }
    }

    /**
     * 保存正常日志信息
     */
    public void commitNormal(ALogEntity entity) {
        if (mFileCollector != null && entity != null) {
            mFileCollector.commitNormal(entity);
        }
    }

    /**
     * 保存错误日志信息
     */
    public void commitError(ALogEntity entity, Throwable throwable) {
        if (mFileCollector != null && entity != null) {
            mFileCollector.commitError(entity, throwable);
        }
    }

    @Override
    public boolean pollingTimeBack(Message message) {

        if (mListener != null) {
            mListener.pollingTimeBack(message);
        }
        if (mFileCollector != null)
            mFileCollector.push();

        return true;
    }
}
