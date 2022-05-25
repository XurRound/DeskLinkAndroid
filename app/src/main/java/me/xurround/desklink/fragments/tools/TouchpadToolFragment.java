package me.xurround.desklink.fragments.tools;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import me.xurround.desklink.R;
import me.xurround.desklink.logic.AppSettings;
import me.xurround.desklink.logic.ui.TouchpadGestureListener;
import me.xurround.desklink.viewmodels.DeskControlViewModel;

public class TouchpadToolFragment extends Fragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    private int axisX = 0;
    private int axisY = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_tool_touchpad, container, false);

        RelativeLayout touchpad = view.findViewById(R.id.touchpad);
        ConstraintLayout tpButtons = view.findViewById(R.id.touchpad_buttons);

        DeskControlViewModel viewModel = new ViewModelProvider(requireActivity()).get(DeskControlViewModel.class);

        float sensitivity = (3 + AppSettings.getInstance(requireContext()).getTouchpadSensitivity()) / 5f;

        TouchpadGestureListener gestureListener = new TouchpadGestureListener(event ->
        {
            switch (event)
            {
                case LEFT_CLICK:
                    viewModel.sendData(new byte[] { (byte)0xB1 });
                    break;
                case RIGHT_CLICK:
                    viewModel.sendData(new byte[] { (byte)0xB2 });
                    break;
                case LEFT_DOWN:
                    viewModel.sendData(new byte[] { (byte)0xB3 });
                    break;
                case LEFT_UP:
                    viewModel.sendData(new byte[] { (byte)0xB4 });
                    break;
            }
        });

        GestureDetectorCompat gestureDetector = new GestureDetectorCompat(requireContext(), gestureListener);
        gestureDetector.setOnDoubleTapListener(gestureListener);

        touchpad.setOnTouchListener((v, event) ->
        {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_POINTER_UP:
                    if (event.getPointerCount() == 2)
                    {
                        viewModel.sendData(new byte[] { (byte)0xB2 });
                        break;
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    axisX = Math.round(event.getX() * sensitivity);
                    axisY = Math.round(event.getY() * sensitivity);
                    break;
                case MotionEvent.ACTION_MOVE:
                    int x = Math.round(event.getX() * sensitivity);
                    int y = Math.round(event.getY() * sensitivity);
                    int dX = x - axisX;
                    int dY = y - axisY;
                    axisX = x;
                    axisY = y;
                    if (dX != 0 || dY != 0)
                    {
                        viewModel.sendData(new byte[]
                        {
                            (byte)0xF0,
                            (byte)(-dX >> 8),
                            (byte)(-dX & 0xFF),
                            (byte)(-dY >> 8),
                            (byte)(-dY & 0xFF)
                        });
                    }
                    break;
            }
            return gestureDetector.onTouchEvent(event);
        });

        tpButtons.setOnTouchListener((v, event) ->
        {
            if (event.getAction() == MotionEvent.ACTION_UP)
            {
                if (event.getX() < tpButtons.getWidth() / 2f)
                    viewModel.sendData(new byte[] { (byte)0xB1 });
                else
                    viewModel.sendData(new byte[] { (byte)0xB2 });
            }
            return true;
        });

        return view;
    }
}