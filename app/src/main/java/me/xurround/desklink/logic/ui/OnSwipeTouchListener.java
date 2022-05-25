package me.xurround.desklink.logic.ui;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class OnSwipeTouchListener implements OnTouchListener
{
    private final GestureDetector gestureDetector;

    public OnSwipeTouchListener (Context context, OnSwipeListener onSwipeListener)
    {
        gestureDetector = new GestureDetector(context, new GestureListener(onSwipeListener));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        private final OnSwipeListener onSwipeListener;

        private GestureListener(OnSwipeListener onSwipeListener)
        {
            this.onSwipeListener = onSwipeListener;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            boolean result = false;
            try
            {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY))
                {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD)
                    {
                        if (diffX > 0)
                        {
                            if (onSwipeListener != null)
                                onSwipeListener.onSwipe(SwipeDirection.RIGHT);
                        }
                        else
                        {
                            if (onSwipeListener != null)
                                onSwipeListener.onSwipe(SwipeDirection.LEFT);
                        }
                        result = true;
                    }
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD)
                {
                    if (diffY > 0)
                    {
                        if (onSwipeListener != null)
                            onSwipeListener.onSwipe(SwipeDirection.BOTTOM);
                    }
                    else
                    {
                        if (onSwipeListener != null)
                            onSwipeListener.onSwipe(SwipeDirection.TOP);
                    }
                    result = true;
                }
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public interface OnSwipeListener
    {
        void onSwipe(SwipeDirection direction);
    }

    public enum SwipeDirection
    {
        TOP, BOTTOM, LEFT, RIGHT
    }
}