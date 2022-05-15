package me.xurround.desklink.logic.ui;

import android.view.View;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;

public class HeightAnimator
{
    private final ValueAnimator valueAnimator;
    private final View target;

    private final int range;
    private boolean state;

    private OnToggleListener onToggleListener;

    public HeightAnimator(View view, int range)
    {
        this.valueAnimator = new ValueAnimator();
        this.target = view;
        this.range = range;
        this.state = false;

        this.valueAnimator.addUpdateListener(value ->
        {
            this.target.getLayoutParams().height = (Integer)value.getAnimatedValue();
            this.target.requestLayout();
        });

        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    public void setDuration(int duration)
    {
        valueAnimator.setDuration(duration);
    }

    public void setOnToggleListener(OnToggleListener onToggleListener)
    {
        this.onToggleListener = onToggleListener;
    }

    public void open()
    {
        this.state = true;
        int initHeight = target.getHeight();
        valueAnimator.setIntValues(initHeight, initHeight + this.range);
        valueAnimator.start();
        if (onToggleListener != null)
            onToggleListener.onToggle(this.state);
    }

    public void close()
    {
        this.state = false;
        int initHeight = target.getHeight();
        valueAnimator.setIntValues(initHeight, initHeight - this.range);
        valueAnimator.start();
        if (onToggleListener != null)
            onToggleListener.onToggle(this.state);
    }

    public void toggle()
    {
        this.state = !this.state;
        if (this.state)
            open();
        else
            close();
    }

    public interface OnToggleListener
    {
        void onToggle(boolean state);
    }
}
