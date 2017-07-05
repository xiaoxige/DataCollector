package com.cmcc.hbb.android.phone.datacollector.utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by ChaiHongwei on 2017/6/20 11:20.
 * 数据采集辅助类
 */

public class DataCollector {
    private static final String TAG = DataCollector.class.getSimpleName();

    /**
     * 收集信息
     */
    public static JSONObject collectDeviceInfo(Context ctx) {
        return collectDeviceInfo(ctx, new String[]{"sp_config"});
    }

    public static JSONObject collectDeviceInfo(Context ctx, String[] sharedPrefIds) {
        JSONObject result = new JSONObject();
        try {
            result.put("versionName", AppUtils.getVersionName(ctx));
            result.put("versionCode", AppUtils.getVersionCode(ctx));
            result.put("processName", AppUtils.getProcessName());
            result.put("packageName", AppUtils.getPackageName(ctx));
            result.put("umengChannelId", AppUtils.getUMengChannelId(ctx));

            result.put("widthPixels", ScreenUtils.getScreenWidth(ctx));
            result.put("heightPixels", ScreenUtils.getScreenHeight(ctx));
            result.put("statusHeight", ScreenUtils.getStatusHeight(ctx));
            result.put("density", ScreenUtils.getDensity(ctx));
            result.put("densityDpi", ScreenUtils.getDensityDpi(ctx));
            result.put("scaledDensity", ScreenUtils.getscaledDensity(ctx));

            result.put("language", Locale.getDefault().getLanguage());
            result.put("imei", DeviceUtils.getIMEI(ctx));
            result.put("clientCode", DeviceUtils.getClientCode(ctx));
            result.put("externalMemoryAvailable", DeviceUtils.externalMemoryAvailable() + "");
            result.put("availableExternalMemorySize",
                    DeviceUtils.formatFileSize(DeviceUtils.getAvailableExternalMemorySize(), false));
            result.put("totalExternalMemorySize",
                    DeviceUtils.formatFileSize(DeviceUtils.getTotalExternalMemorySize(), false));
            result.put("availableInternalMemorySize",
                    DeviceUtils.formatFileSize(DeviceUtils.getAvailableInternalMemorySize(), false));
            result.put("totalInternalMemorySize",
                    DeviceUtils.formatFileSize(DeviceUtils.getTotalInternalMemorySize(), false));
            result.put("totalMemorySize", DeviceUtils.getTotalMemorySize(ctx) + "");
            result.put("availableMemory", DeviceUtils.getAvailableMemory(ctx) + "");
            result.put("isScreenOn", DeviceUtils.isScreenOn(ctx));
            result.put("macAddress", DeviceUtils.getMacAddress(ctx) + "");
            result.put("ip", DeviceUtils.getIP(ctx));
            result.put("netWorkInfo", NetworkUtils.getNetworkInfo(ctx) + "");
            result.put("build", new BuildCollector().collectBuild());
            result.put("build$Version", new BuildCollector().collectBuildVersion());
            result.put("spdata", new SPCollector(ctx, sharedPrefIds).collect());
            result.put("settings$system", new SettingsCollector(ctx).collectSystemSettings());
            result.put("settings$global", new SettingsCollector(ctx).collectGlobalSettings());
            result.put("settings$secure", new SettingsCollector(ctx).collectSecureSettings());
        } catch (Exception e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        return result;
    }
}
