package cn.xiaoxige.datacollector;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by ChaiHongwei on 2017/6/26 13:55.
 */

public class DataCollector {

    public static String mSaveLogFileRootPathName = "hx";
    public static String mSaveLogFileName = "hbb_log";

    private static Application sApplication;
    private static LogThread sLogThread;


    public static void start(Application application) {
        sApplication = application;
        sLogThread = new LogThread();
        sLogThread.start();
    }


    private static class LogThread extends Thread {
        private Handler mHandler;
        private final Object mSync = new Object();

        public void run() {
            Looper.prepare();
            synchronized (mSync) {
                mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        BufferedWriter out = null;
                        try {
                            File logFile = createFile(sApplication, mSaveLogFileRootPathName, mSaveLogFileName, msg.arg1 == 1);

                            out = new BufferedWriter(new OutputStreamWriter(
                                    new FileOutputStream(logFile, true)));
                            out.write(msg.obj + "\n");
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                out.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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


    public static File createFile(Context context, String filePath, String fileName, boolean isAppend) throws IOException {
        String state = Environment.getExternalStorageState();
        File file;
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            file = context.getExternalCacheDir();
        } else {
            file = context.getCacheDir();
        }

        File folder = new File(file.getAbsolutePath() + File.separator + filePath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        int newFileCount = 0;
        File newFile;
        File existingFile = null;

        newFile = new File(folder, String.format("%s_%s.log", fileName, newFileCount));
        while (newFile.exists()) {
            existingFile = newFile;
            newFileCount++;
            newFile = new File(folder, String.format("%s_%s.log", fileName, newFileCount));
        }
        if (existingFile == null || !isAppend) {
            newFile.createNewFile();
            return newFile;
        } else {
            return existingFile;
        }
    }
}
