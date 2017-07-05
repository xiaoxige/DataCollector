package com.cmcc.hbb.android.phone.datacollector.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


public class ScreenUtils {
    /**
     * dp2px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px2dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * px2sp
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * sp2px
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

//    public static float getDividerHeight(Context context) {
//        return context.getResources().getDimension(R.dimen.divider_height);
//    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics metric = context.getApplicationContext()
                .getResources().getDisplayMetrics();
        return metric.heightPixels;
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics metric = context.getApplicationContext()
                .getResources().getDisplayMetrics();
        return metric.widthPixels;
    }

    /**
     * 屏幕密度DPI（120 / 160 / 240）
     */
    public static float getDensityDpi(Context context) {
        DisplayMetrics metric = context.getApplicationContext()
                .getResources().getDisplayMetrics();

        return metric.densityDpi;
    }

    /**
     * 屏幕密度（0.75 / 1.0 / 1.5）
     */
    public static float getDensity(Context context) {
        DisplayMetrics metric = context.getApplicationContext()
                .getResources().getDisplayMetrics();

        return metric.density;
    }

    public static float getscaledDensity(Context context) {
        DisplayMetrics metric = context.getApplicationContext()
                .getResources().getDisplayMetrics();

        return metric.scaledDensity;
    }

    public static boolean hideSoftInput(Activity activity, EditText editText) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.hideSoftInputFromWindow(editText.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void showSoftKeyboard(Context context, View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static boolean showSoftInput(Activity activity, EditText et) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }

    public static boolean hideSoftInput(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
}
