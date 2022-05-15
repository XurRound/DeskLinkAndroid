package me.xurround.desklink.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import me.xurround.desklink.R;
import me.xurround.desklink.logic.ui.HeightAnimator;

public class ConnectFragment extends Fragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_connect, container, false);

        Button openSearchSettingsButton = view.findViewById(R.id.openSearchSettingsButton);
        LinearLayout ll = view.findViewById(R.id.connectSlideHeader);

        Spinner conModeSpinner = view.findViewById(R.id.connection_mode_spn);

        HeightAnimator settingsSlideAnimator = new HeightAnimator(ll, 120);
        settingsSlideAnimator.setDuration(300);

        settingsSlideAnimator.setOnToggleListener(conModeSpinner::setEnabled);

        openSearchSettingsButton.setOnClickListener(v -> settingsSlideAnimator.toggle());

        return view;
    }
}