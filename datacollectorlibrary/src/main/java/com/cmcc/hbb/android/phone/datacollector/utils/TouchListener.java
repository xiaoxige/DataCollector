package com.cmcc.hbb.android.phone.datacollector.utils;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.cmcc.hbb.android.phone.datacollector.interfaces.ITouchEvent;

/**
 * Created by 小稀革 on 2017/7/8.
 */

public class TouchListener extends FrameLayout {

    private ITouchEvent iTouchEvent;

    public TouchListener(@NonNull Context context) {
        this(context, null);
    }

    public TouchListener(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchListener(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void start(View rootView, ITouchEvent touchEvent) {
        if (rootView instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) rootView;

            if (viewGroup.getChildCount() != 1) {
                return;
            }

            View firstView = viewGroup.getChildAt(0);

            viewGroup.removeAllViews();

            viewGroup.addView(this);

            addView(firstView);

            iTouchEvent = touchEvent;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return false;
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (iTouchEvent != null) {
                iTouchEvent.onTouchEvent(ev.getX(), ev.getY());
            }
        }
        return false;
    }
}
