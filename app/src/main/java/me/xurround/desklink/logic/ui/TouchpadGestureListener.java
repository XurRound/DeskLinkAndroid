package me.xurround.desklink.logic.ui;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class TouchpadGestureListener extends GestureDetector.SimpleOnGestureListener implements GestureDetector.OnDoubleTapListener
{
    public enum TouchpadEvent
    {
        LEFT_CLICK,
        RIGHT_CLICK,
        LEFT_DOWN,
        LEFT_UP,
    }

    private final TouchpadEventListener touchpadEventListener;

    public TouchpadGestureListener(TouchpadEventListener touchpadEventListener)
    {
        this.touchpadEventListener = touchpadEventListener;
    }

    @Override
    public boolean onDown(MotionEvent event) { return true; }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) { return true; }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) { return true; }

    @Override
    public boolean onDoubleTap(MotionEvent event) { return true; }

    @Override
    public boolean onSingleTapUp(MotionEvent event) { return true; }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                sendEvent(TouchpadEvent.LEFT_DOWN);
                break;
            case MotionEvent.ACTION_UP:
                sendEvent(TouchpadEvent.LEFT_UP);
                break;
        }
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event)
    {
        sendEvent(TouchpadEvent.LEFT_CLICK);
        return true;
    }

    private void sendEvent(TouchpadEvent event)
    {
        if (touchpadEventListener != null)
            touchpadEventListener.onTouchpadEvent(event);
    }

    public interface TouchpadEventListener
    {
        void onTouchpadEvent(TouchpadEvent event);
    }
}
