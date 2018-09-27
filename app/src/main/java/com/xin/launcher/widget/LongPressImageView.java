package com.xin.launcher.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

public class LongPressImageView extends android.support.v7.widget.AppCompatImageView {
    private int mLastMotionX, mLastMotionY;

    private boolean isMoved;

    private boolean isReleased;

    private Runnable mLongPressRunnable;

    private static final int TOUCH_SLOP = 20;

    public LongPressImageView(Context context) {
        super(context);
        mLongPressRunnable = new Runnable() {

            @Override
            public void run() {
                if (isReleased || isMoved) return;
                performLongClick();
            }
        };
    }

    public LongPressImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mLongPressRunnable = new Runnable() {

            @Override
            public void run() {
                if (isReleased || isMoved) return;
                performLongClick();
            }
        };
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = x;
                mLastMotionY = y;

                isReleased = false;
                isMoved = false;
                postDelayed(mLongPressRunnable, ViewConfiguration.getLongPressTimeout());
                break;
            case MotionEvent.ACTION_MOVE:
                if (isMoved) break;
                if (Math.abs(mLastMotionX - x) > TOUCH_SLOP
                        || Math.abs(mLastMotionY - y) > TOUCH_SLOP) {

                    isMoved = true;
                    removeCallbacks(mLongPressRunnable);
                }
                break;
            case MotionEvent.ACTION_UP:

                isReleased = true;
                removeCallbacks(mLongPressRunnable);
                break;
        }
        return true;
    }
}
