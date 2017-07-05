package com.cmcc.hbb.android.phone.datacollector.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONObject;

import java.lang.reflect.Field;

/**
 * Created by ChaiHongwei on 2017/5/16 10:31.
 */

public class SettingsCollector {
    private Context mContext;

    public SettingsCollector(Context context) {
        this.mContext = context;
    }

    @NonNull
    public JSONObject collectSystemSettings() {
        final JSONObject result = new JSONObject();

        final Field[] keys = Settings.System.class.getFields();

        for (final Field key : keys) {
            //跳过deprecated的字段
            if (!key.isAnnotationPresent(Deprecated.class) && key.getType() == String.class) {
                try {
                    final Object value = Settings.System.getString(mContext.getContentResolver(),
                            (String) key.get(null));

                    if (value != null) {
                        result.put(key.getName(), value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    @NonNull
    public JSONObject collectSecureSettings() {
        final JSONObject result = new JSONObject();

        final Field[] keys = Settings.Secure.class.getFields();

        for (final Field key : keys) {
            //跳过deprecated的字段
            if (!key.isAnnotationPresent(Deprecated.class) && key.getType() == String.class
                    && isAuthorized(key)) {
                try {
                    final Object value = Settings.System.getString(mContext.getContentResolver(),
                            (String) key.get(null));

                    if (value != null) {
                        result.put(key.getName(), value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    @NonNull
    public JSONObject collectGlobalSettings() {
        final JSONObject result = new JSONObject();
        //api17才新增的类
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return result;
        }

        final Field[] keys = Settings.Global.class.getFields();

        for (final Field key : keys) {
            //跳过deprecated的字段
            if (!key.isAnnotationPresent(Deprecated.class) && key.getType() == String.class
                    && isAuthorized(key)) {
                try {
                    final Object value = Settings.System.getString(mContext.getContentResolver(),
                            (String) key.get(null));

                    if (value != null) {
                        result.put(key.getName(), value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    private boolean isAuthorized(@Nullable Field key) {
        if (key == null || key.getName().startsWith("WIFI_AP")) {
            return false;
        }

        return true;
    }
}
