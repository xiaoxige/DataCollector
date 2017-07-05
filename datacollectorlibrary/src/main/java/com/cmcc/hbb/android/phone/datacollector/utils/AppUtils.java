package com.cmcc.hbb.android.phone.datacollector.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


/**
 * Created by guowenhui
 * 2016/12/20
 * 获取清单文件meda-data
 */

public class AppUtils {


    /**
     * @param context 上下文
     * @param key
     * @return
     */
    public static String getMedaData(Context context, String key) {
        String result = null;

        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);

            result = appInfo.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        return result;
    }

    public static String getUMengChannelId(Context context) {
        return getMedaData(context, "UMENG_CHANNEL");
    }


    /**
     * 获取进程名称
     */
    public static String getProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Return version name of the application.
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageInfo info = getPackageInfo(context);
        return info.versionName;
    }

    /**
     * Return version code of the application.
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        PackageInfo info = getPackageInfo(context);
        return info.versionCode;
    }

    /**
     * Return the package name of the application.
     *
     * @param context
     * @return
     */
    public static String getPackageName(Context context) {
        PackageInfo info = getPackageInfo(context);
        return info.packageName;
    }

    /**
     * Return the package info.
     *
     * @param context
     * @return
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageManager manager;
        PackageInfo info = null;
        manager = context.getPackageManager();
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }
}
