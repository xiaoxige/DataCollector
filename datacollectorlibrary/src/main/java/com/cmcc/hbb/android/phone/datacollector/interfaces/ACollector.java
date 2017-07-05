package com.cmcc.hbb.android.phone.datacollector.interfaces;

import android.content.Context;
import android.util.Log;

import com.cmcc.hbb.android.phone.datacollector.config.CollectorConfig;
import com.cmcc.hbb.android.phone.datacollector.utils.DataCollector;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by zhuxiaoan on 2017/7/4.
 */

public abstract class ACollector implements IPush {

    public static final int TYPE_ERROR_LOG = 0x00;
    public static final int TYPE_NORMAL_LOG = 0x01;


    private static final String TAG = ACollector.class.getSimpleName();
    protected Context mContext;
    @CollectorConfig.CollectorType
    protected int mCollectorType;
    protected boolean mIsShowLog;
    protected long mLimitFileSize;
    protected IUpServer mUpServer;

    public ACollector(Context context, @CollectorConfig.CollectorType int collectorType, long limitFileSize, boolean isShowLog, IUpServer upServer) {
        mContext = context;
        mCollectorType = collectorType;
        mIsShowLog = isShowLog;
        mLimitFileSize = limitFileSize;
        mUpServer = upServer;
    }

    public void commit(IEntity entity, @CollectorConfig.CollectorType int collectorType) {
        if (mIsShowLog) {
            Log.e(TAG, entity.toContent());
        }
    }

    public void commitNormal(ALogEntity entity) {
        if (mIsShowLog) {
            Log.e(TAG, entity.toContent());
        }
    }

    public void commitError(ALogEntity entity, Throwable throwable) {

        String errorMsg;
        if (throwable != null) {
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            throwable.printStackTrace(printWriter);
            Throwable cause = throwable.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.close();
            //异常堆栈信息
            errorMsg = writer.toString();
        } else {
            errorMsg = "";
        }

        entity.desc += errorMsg + "\n" + DataCollector.collectDeviceInfo(mContext);

        if (mIsShowLog) {
            Log.e("TAG", entity.toContent());
        }

    }

    public void start() {
    }

}
