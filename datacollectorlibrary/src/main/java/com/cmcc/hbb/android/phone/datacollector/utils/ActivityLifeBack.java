package com.cmcc.hbb.android.phone.datacollector.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cmcc.hbb.android.phone.datacollector.ClickEntity;
import com.cmcc.hbb.android.phone.datacollector.LifeEntity;
import com.cmcc.hbb.android.phone.datacollector.interfaces.ITouchEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by 小稀革 on 2017/7/8.
 */

public class ActivityLifeBack implements Application.ActivityLifecycleCallbacks, ITouchEvent {

    private static ActivityLifeBack mLifeBack;
    private AtomicBoolean mIsInit;
    private Context mContext;
    private List<View> mViews;
    private Activity mActicity;

    private ActivityLifeBack() {
        mIsInit = new AtomicBoolean(false);
        mViews = new ArrayList<>();
    }

    public synchronized static ActivityLifeBack getInstance() {
        if (mLifeBack == null) {
            mLifeBack = new ActivityLifeBack();
        }
        return mLifeBack;
    }

    public synchronized void initLifeBack(Context context) {
        if (mIsInit.get()) {
            return;
        }
        mContext = context;
        ((Application) context).registerActivityLifecycleCallbacks(this);
        mIsInit.set(true);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        LifeEntity lifeEntity = new LifeEntity(activity);
        lifeEntity.desc = "onCreate";
        CollectorUtil.getInstance().commitNormal(lifeEntity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        LifeEntity lifeEntity = new LifeEntity(activity);
        lifeEntity.desc = "onStart";
        CollectorUtil.getInstance().commitNormal(lifeEntity);
    }

    @Override
    public void onActivityResumed(Activity activity) {

        LifeEntity lifeEntity = new LifeEntity(activity);
        lifeEntity.desc = "onResume";
        CollectorUtil.getInstance().commitNormal(lifeEntity);

        mActicity = activity;
        mViews.clear();
        mViews.addAll(getActivityAllViews(activity));

    }

    private List<View> getActivityAllViews(Activity activity) {
        List<View> views = new ArrayList<>();


        View rootView = activity.getWindow().getDecorView();
        initViews(views, rootView);

        new TouchListener(mContext).start(rootView, this);

        return views;
    }

    private void initViews(List<View> views, View view) {
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            int childCount = group.getChildCount();
            for (int i = 0; i < childCount; i++) {
                initViews(views, group.getChildAt(i));
            }
        } else {
            views.add(view);
        }
    }


    @Override
    public void onActivityPaused(Activity activity) {
        LifeEntity lifeEntity = new LifeEntity(activity);
        lifeEntity.desc = "onPause";
        CollectorUtil.getInstance().commitNormal(lifeEntity);

        mActicity = null;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        LifeEntity lifeEntity = new LifeEntity(activity);
        lifeEntity.desc = "onStop";
        CollectorUtil.getInstance().commitNormal(lifeEntity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        LifeEntity lifeEntity = new LifeEntity(activity);
        lifeEntity.desc = "onSaveInstance";
        CollectorUtil.getInstance().commitNormal(lifeEntity);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        LifeEntity lifeEntity = new LifeEntity(activity);
        lifeEntity.desc = "onDestroy";
        CollectorUtil.getInstance().commitNormal(lifeEntity);
    }

    @Override
    public void onTouchEvent(float x, float y) {

        View view = getOnTouchView(x, y);

        if (view == null) {
        } else {
            ClickEntity entity = new ClickEntity(mActicity);
            entity.desc = getTouchDesc(view);
            CollectorUtil.getInstance().commitNormal(entity);
        }
    }

    private View getOnTouchView(final float x, final float y) {
        for (View view : mViews) {
            int[] position = new int[2];
            view.getLocationOnScreen(position);
            boolean isInX = position[0] <= x && x <= position[0] + view.getWidth();
            boolean isInY = position[1] <= y && y <= position[1] + view.getHeight();
            if (isInX && isInY)
                return view;
        }
        return null;
    }

    private String getTouchDesc(View view) {
        String desc;
        if (view == null) {
            return "view is null";
        }
        desc = "view isenabled: " + view.isEnabled() + ", name: " + view.getClass().getSimpleName() + ", ";
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            desc += "text: " + textView.getText().toString();
        } else if (view instanceof Button) {
            Button button = (Button) view;
            desc += "text: " + button.getText().toString();
        } else if (view instanceof ImageView) {
        } else if (view instanceof RadioButton) {
            RadioButton radioButton = (RadioButton) view;
            desc += "text: " + radioButton.getText() + ", isSelect: " + radioButton.isSelected();
        } else if (view instanceof CheckBox) {
            CheckBox checkBox = (CheckBox) view;
            desc += "text: " + checkBox.getText() + ", isSelect: " + checkBox.isChecked();
        } else {

        }

        return desc;
    }
}
