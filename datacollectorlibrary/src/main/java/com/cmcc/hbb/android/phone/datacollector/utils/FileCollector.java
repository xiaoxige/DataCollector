package com.cmcc.hbb.android.phone.datacollector.utils;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.cmcc.hbb.android.phone.datacollector.config.CollectorConfig;
import com.cmcc.hbb.android.phone.datacollector.interfaces.ACollector;
import com.cmcc.hbb.android.phone.datacollector.interfaces.ALogEntity;
import com.cmcc.hbb.android.phone.datacollector.interfaces.IEntity;
import com.cmcc.hbb.android.phone.datacollector.interfaces.IResult;
import com.cmcc.hbb.android.phone.datacollector.interfaces.IUpServer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhuxiaoan on 2017/7/4.
 */

public class FileCollector extends ACollector {

    private static final String LOGROOTPATH = "hx";
    private static final String LOGSAVEFILENAME = "hbb_save_log.log";
    private static final String LOGFULLFILENAME = "hbb_full_log";

    private LogThread mLogThread;

    private AtomicInteger mPushNum;

    public FileCollector(Context context, @CollectorConfig.CollectorType int collectorType, long limitFileSize, boolean isShwoLog, IUpServer upServer) {
        super(context, collectorType, limitFileSize, isShwoLog, upServer);
        mPushNum = new AtomicInteger(0);
        mLogThread = new LogThread();
    }

    @Override
    public void start() {
        mLogThread.start();
    }

    @Override
    public void push() {
        if (mUpServer == null || mPushNum.get() > 0) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                mPushNum.set(0);
                pushProc();
            }
        }).start();

    }

    private void pushProc() {
        String state = Environment.getExternalStorageState();
        String rootPath;
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            rootPath = mContext.getExternalCacheDir().getAbsolutePath();
        } else {
            rootPath = mContext.getCacheDir().getAbsolutePath();
        }
        File rootFile = new File(rootPath + File.separator + LOGROOTPATH);
        if (!rootFile.exists()) {
            return;
        }

        File[] files = rootFile.listFiles();

        if (files == null)
            return;

        mPushNum.set(files.length);
        Log.e("TAG", "*********************************" + files.length);

        for (final File file : files) {
            if (!file.getName().contains(LOGFULLFILENAME)) {
                mPushNum.decrementAndGet();
                continue;
            }
            Log.e("TAG", "+++++++++++++++++++++++++++++" + file.getName());
            try {
                mUpServer.upServer(getMessageByFile(file), new IResult() {
                    @Override
                    public void success() {
                        Log.e("TAG", file.getName() + "上传成功...");
                        file.delete();
                        mPushNum.decrementAndGet();
                    }

                    @Override
                    public void error() {
                        Log.e("TAG", file.getName() + "上传失败...");
                        mPushNum.decrementAndGet();
                    }
                });
            } catch (Exception e) {
                mPushNum.decrementAndGet();
                continue;
            }
        }
    }

    /**
     * 读取文件内容
     */
    private String getMessageByFile(File file) throws Exception {

        String msg;
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        int len;
        byte[] b = new byte[1024];
        StringBuffer buffer = new StringBuffer();
        while ((len = bis.read(b)) != -1) {
            buffer.append(new String(b, 0, len));
        }
        bis.close();

        msg = buffer.toString();
        return msg;
    }

    /****************************************************************************/


    @Override
    public void commit(IEntity entity, @CollectorConfig.CollectorType int collectorType) {

        super.commit(entity, collectorType);
        Message msg = Message.obtain();
        msg.what = collectorType;
        msg.obj = entity.toContent();
        mLogThread.getHandler().sendMessage(msg);

    }

    @Override
    public void commitNormal(ALogEntity entity) {
        super.commitNormal(entity);
        Message msg = Message.obtain();
        msg.what = TYPE_NORMAL_LOG;
        msg.obj = entity.toContent();
        mLogThread.getHandler().sendMessage(msg);
    }

    @Override
    public void commitError(ALogEntity entity, Throwable throwable) {
        super.commitError(entity, throwable);
        Message msg = Message.obtain();
        msg.what = TYPE_ERROR_LOG;
        msg.obj = entity.toContent();
        mLogThread.getHandler().sendMessage(msg);
    }

    private class LogThread extends Thread {
        private Handler mHandler;
        private final Object mSync = new Object();

        public void run() {
            Looper.prepare();
            synchronized (mSync) {
                mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        try {
                            saveLogProc(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                mSync.notifyAll();
            }
            Looper.loop();
        }

        public Handler getHandler() {
            synchronized (mSync) {
                if (mHandler == null) {
                    try {
                        mSync.wait();
                    } catch (InterruptedException e) {
                    }
                }
                return mHandler;
            }
        }

        public void exit() {
            getHandler().post(new Runnable() {
                public void run() {
                    Looper.myLooper().quit();
                }
            });
        }
    }

    private void saveLogProc(Message msg) throws Exception {
        // 检查主文件目录是否存在
        String state = Environment.getExternalStorageState();
        String rootPath;
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            rootPath = mContext.getExternalCacheDir().getAbsolutePath();
        } else {
            rootPath = mContext.getCacheDir().getAbsolutePath();
        }
        File rootFile = new File(rootPath + File.separator + LOGROOTPATH);
        if (!rootFile.exists()) {
            rootFile.mkdirs();
        }

        // 保存Log到文件中
        saveLog(rootFile.getAbsolutePath(), msg);

    }

    private void saveLog(String rootPath, Message msg) throws Exception {
        // 检查写的文件是否存在
        File saveFile = new File(rootPath + File.separator + LOGSAVEFILENAME);
        if (!saveFile.exists()) {
            saveFile.createNewFile();
        }

        // 错误信息及配置上传错误信息
        if (msg.what == TYPE_ERROR_LOG && mCollectorType == CollectorConfig.TYPE_COLLECTOR_ERROR) {
            // 把错误信息写入文件中

            writeToFile(saveFile, msg.obj.toString(), true);

            execfullFileProc(rootPath, saveFile, msg);
            return;
        }

        long saveFileLength = saveFile.length();
        if (saveFileLength >= mLimitFileSize) {

            // 文件转移, 换页
            execfullFileProc(rootPath, saveFile, msg);

            saveLog(rootPath, msg);
            return;
        }

        // saveFile文件大小一定是小于限制文件大小, 进行保存操作
        writeToFile(saveFile, msg.obj.toString(), true);
    }

    private void writeToFile(File file, String msg, boolean isApped) {
        FileOutputStream fos = null;
        BufferedWriter bw = null;
        try {
            fos = new FileOutputStream(file, isApped);
            bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write("\n" + msg + "\n");
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(null, fos, bw);
        }
    }

    private void closeStream(InputStream is, OutputStream os, Writer wr) {
        try {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
            if (wr != null) {
                wr.close();
            }
        } catch (Exception e) {

        }
    }

    /**
     * 转移saveFile内容到fullFile文件中, 并删除saveFile文件
     */
    private void execfullFileProc(String rootPath, File saveFile, Message msg) throws IOException {


        // 如果收集全部, 则直接换页, 如果是收集错误信息, 当错误信息到来时换页, 否则不换页进行覆盖以前么有问题的文件
        boolean isNewFile = (mCollectorType == CollectorConfig.TYPE_COLLECTOR_ALL) ? true :
                (msg.what == TYPE_ERROR_LOG && mCollectorType == CollectorConfig.TYPE_COLLECTOR_ERROR) ? true : false;

        if (isNewFile) {
            // 得到一个转移saveFile的目标File
            File fullFile = createFullFile(rootPath);

            FileInputStream fis = new FileInputStream(saveFile);
            BufferedInputStream bis = new BufferedInputStream(fis);

            FileOutputStream fos = new FileOutputStream(fullFile);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            int len;
            byte[] b = new byte[1024];

            while ((len = bis.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            bos.flush();

            bos.close();
            bis.close();
        }
        saveFile.delete();
    }

    private File createFullFile(String rootPath) throws IOException {
        File fileRoot = new File(rootPath);
        File[] listFile = fileRoot.listFiles();

        int max = -1;
        String fileName;
        for (int i = 0; i < listFile.length; i++) {
            fileName = listFile[i].getName();
            if (!fileName.contains(LOGFULLFILENAME)) {
                continue;
            }
            String fileNum = fileName.replace(LOGFULLFILENAME, "");
            fileNum = fileNum.replace(".log", "");
            try {
                int num = Integer.parseInt(fileNum);
                if (num > max) {
                    max = num;
                }
            } catch (Exception e) {
                // max 不变
            }
        }


//        int fileNo = (msg.what == TYPE_ERROR_LOG && mCollectorType == CollectorConfig.TYPE_COLLECTOR_ERROR) ? max + 1 : max;

//        int fileNo = (mCollectorType == CollectorConfig.TYPE_COLLECTOR_ALL) ? max + 1 :
//                (msg.what == TYPE_ERROR_LOG && mCollectorType == CollectorConfig.TYPE_COLLECTOR_ERROR) ? max + 1 : max;

        int fileNo = true ? max + 1 : max;
        File fullFile = new File(rootPath + File.separator + LOGFULLFILENAME + fileNo + ".log");

        fullFile.createNewFile();

        return fullFile;
    }

}
