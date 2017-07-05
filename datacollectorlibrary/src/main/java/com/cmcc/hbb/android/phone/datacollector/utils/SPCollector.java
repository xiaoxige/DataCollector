package com.cmcc.hbb.android.phone.datacollector.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by ChaiHongwei on 2017/5/16 10:31.
 */

public class SPCollector {
    private Context mContext;
    private String[] mSharedPrefIds;

    public SPCollector(Context context, String[] sharedPrefIds) {
        this.mContext = context;
        this.mSharedPrefIds = sharedPrefIds;
    }

    @NonNull
    public JSONObject collect() {
        final JSONObject result = new JSONObject();

        //收集默认的SharedPreference信息
        final Map<String, SharedPreferences> sharedPrefs = new TreeMap<>();
        sharedPrefs.put("default", PreferenceManager.getDefaultSharedPreferences(mContext));

        //收集应用自定义的SharedPreference信息
        if (mSharedPrefIds != null) {
            for (final String sharedPrefId : mSharedPrefIds) {
                sharedPrefs.put(sharedPrefId,
                        mContext.getSharedPreferences(sharedPrefId, Context.MODE_PRIVATE));
            }
        }

        //遍历所有的SharedPreference文件
        for (Map.Entry<String, SharedPreferences> entry : sharedPrefs.entrySet()) {
            final String sharedPrefId = entry.getKey();

            final SharedPreferences prefs = entry.getValue();

            final Map<String, ?> preEntries = prefs.getAll();

            if (preEntries.isEmpty()) {
                continue;
            }

            //遍历添加某个SharedPreference文件中的内容
            for (final Map.Entry<String, ?> prefEntry : preEntries.entrySet()) {
                final Object prefValue = prefEntry.getValue();
                String key = sharedPrefId + "#" + prefEntry.getKey();
                try {
                    result.put(key, prefValue);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }
}
