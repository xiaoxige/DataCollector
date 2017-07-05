package com.cmcc.hbb.android.phone.datacollector.utils;

import android.os.Build;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

/**
 * Created by ChaiHongwei on 2017/5/16 10:31.
 */

public class BuildCollector {
    @NonNull
    public JSONObject collectBuild() {
        return collect(Build.class);
    }

    @NonNull
    public JSONObject collectBuildVersion() {
        return collect(Build.VERSION.class);
    }

    @NonNull
    private JSONObject collect(Class classInfo) {
        final JSONObject result = new JSONObject();

        final Field[] keys = classInfo.getFields();

        for (final Field key : keys) {
            //跳过deprecated的字段
            if (key.isAnnotationPresent(Deprecated.class)) {
                continue;
            }
            try {
                Class type = key.getType();
                if (type == String.class || type == int.class) {
                    final Object value = key.get(null);

                    if (value != null) {
                        result.put(key.getName(), value);
                    }
                } else if (type.isArray()) {
                    final Object value = key.get(null);

                    if (value != null) {
                        int length = Array.getLength(value);

                        JSONArray jsonArray = new JSONArray();
                        for (int i = 0; i < length; i++) {
                            jsonArray.put(Array.get(value, i));
                        }
                        result.put(key.getName(), jsonArray);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
