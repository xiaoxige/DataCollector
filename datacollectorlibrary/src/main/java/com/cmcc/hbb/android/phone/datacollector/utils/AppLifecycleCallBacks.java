package com.cmcc.hbb.android.phone.datacollector.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.cmcc.hbb.android.phone.datacollector.entity.CollectorEntity;
import com.cmcc.hbb.android.phone.datacollector.entity.IEntity;
import com.cmcc.hbb.android.phone.datacollector.interfaceview.ICollectorOpration;
import com.cmcc.hbb.android.phone.datacollector.interfaceview.ICollectorRelease;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ChaiHongwei on 2017/6/30 10:22.
 */

public class AppLifecycleCallBacks implements Application.ActivityLifecycleCallbacks {

    private static int mActivityN;
    private static AppLifecycleCallBacks mAppLifecycleCallBacks;
    private final String TAG = this.getClass().getSimpleName();

    private final List<View> mViews = new ArrayList<>();
    private final Map<View, String> mViewPaths = new HashMap<>();
    private WeakReference activityRefer = null;

    private Context mContext;
    private ICollectorOpration mCollectorOpration;
    private ICollectorRelease mCollectorRelease;


    public static AppLifecycleCallBacks getInstance() {
        if (mAppLifecycleCallBacks == null) {
            mAppLifecycleCallBacks = new AppLifecycleCallBacks();
        }
        return mAppLifecycleCallBacks;
    }

    public void initLifecycle(Context context,
                              ICollectorOpration collectorOpration,
                              ICollectorRelease collectorRelease) {
        this.mContext = context;
        this.mCollectorOpration = collectorOpration;
        this.mCollectorRelease = collectorRelease;
        mActivityN = 0;
        // 注册
        ((Application) mContext).registerActivityLifecycleCallbacks(this);
    }

    private AppLifecycleCallBacks() {
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        mActivityN++;
        IEntity entity = createCollectorEntity(activity.getClass().getSimpleName(), "create", "onActivityCreated");
        mCollectorOpration.commitNormalData(entity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        IEntity entity = createCollectorEntity(activity.getClass().getSimpleName(), "start", "onActivityStarted");
        mCollectorOpration.commitNormalData(entity);
    }

    @Override
    public void onActivityResumed(Activity activity) {

        IEntity entity = createCollectorEntity(activity.getClass().getSimpleName(), "resumed", "onActivityResumed");
        mCollectorOpration.commitNormalData(entity);


        this.activityRefer = new WeakReference(activity);
        View decorView = activity.getWindow().getDecorView();
        mViews.clear();
        mViewPaths.clear();
        //找出两个页面中的view
        iteratorView(decorView);

        //        if(decorView instanceof ViewGroup) {
        //            ViewGroup decorViewGroup = (ViewGroup)decorView;
        //            View fistView = decorViewGroup.getChildAt(0);
        //            if(!(fistView instanceof CoverFrameLayout)) {
        //                creatCoverFrameLayout(decorViewGroup);
        //            }
        //        }
    }

//    private CoverFrameLayout creatCoverFrameLayout(ViewGroup decorView) {
//        if (activityRefer.get() == null) return null;
//        //TODO 在这里可能需要不根据不同的情况返回不同的这个Layout.比如frgment ? popWIndows 等等
//        return new CoverFrameLayout((Activity) activityRefer.get(), decorView);
//    }

    private void iteratorView(View view) {
        String parentViewPath = mViewPaths.get(view);
        if (parentViewPath == null) {
            parentViewPath = "MainWindow";
        }
        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            int childCount = vg.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = vg.getChildAt(i);

                String childViewPath = parentViewPath + "/" + childView.getClass().getSimpleName()
                        + "[" + i + "]";
                mViewPaths.put(childView, childViewPath);

                iteratorView(childView);
            }
        } else {
            //TODO 在这里如果需要项目化功能,还需要结合自身项目的功能和统计规则对一些特殊的控件的保存方式。
            //TODO 比如:adapterView itemview上面的点击? 还得考虑weview的事件?对纯绘制View的事件?
            //TODO 自己项目统计规则 是怎么样的? 可能保存view的不只是Set ,甚至可能需要扩充至Map等
            mViews.add(view);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        IEntity entity = createCollectorEntity(activity.getClass().getSimpleName(), "pause", "onActivityPaused");
        mCollectorOpration.commitNormalData(entity);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        IEntity entity = createCollectorEntity(activity.getClass().getSimpleName(), "stop", "onActivityStopped");
        mCollectorOpration.commitNormalData(entity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        IEntity entity = createCollectorEntity(activity.getClass().getSimpleName(), "activitysaveinstancestate", "onActivitySaveInstanceState");
        mCollectorOpration.commitNormalData(entity);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mActivityN--;
        if (mActivityN <= 0) {
            mCollectorRelease.release();
        }
    }

    public View touchAnyView(MotionEvent motionEvent) {
        if (activityRefer.get() == null) return null;
        View touchView = findTouchView(motionEvent);
        if (touchView == null) {
        } else {
            IEntity entity = createCollectorEntity(TAG, "onclick", findTouchViewTag(touchView));
            mCollectorOpration.commitNormalData(entity);
        }

        return touchView;
    }

    private View findTouchView(MotionEvent motionEvent) {
        int[] location = new int[2];
        for (View v : mViews) {
            if (v.isShown()) {
                v.getLocationOnScreen(location);
                Rect r = new Rect();
                v.getGlobalVisibleRect(r);
                boolean contains = r.contains((int) motionEvent.getX(), (int) motionEvent.getY());
                if (contains) {
                    //TODO 在这里如果需要项目化功能,还需要对此view是否可见?是否可点进行一些判断,再确定用户点击的是哪个View
                    return v;
                }
            }
        }
        return null;
    }

    private String findTouchViewTag(View v) {
        if (v == null) return "View is null";
        if (v instanceof TextView) {
            if (v instanceof CheckBox) {
                CheckBox cb = (CheckBox) v;
                return "CheckBox, text is:" + cb.getText();
            } else if (v instanceof Button) {
                Button btn = (Button) v;
                return "Button, text is:" + btn.getText();
            } else if (v instanceof EditText) {
                EditText et = (EditText) v;
                return "EditText, text is:" + et.getText();
            } else {
                TextView tv = (TextView) v;
                return "TextView, text is:" + tv.getText().toString();
            }
        } else if (v instanceof ImageView) {
            ImageView im = (ImageView) v;
            return "ImageView, contentDrscription is:" + im.getContentDescription();
        } else if (v instanceof Spinner) {
            Spinner spinner = (Spinner) v;
            return "Spinner, item is:" + spinner.getCount();
        }
        return "No Identity";
    }

    private IEntity createCollectorEntity(String id, String type, String msg) {
        return new CollectorEntity(id, type, msg);
    }
}