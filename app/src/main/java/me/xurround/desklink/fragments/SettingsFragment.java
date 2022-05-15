package me.xurround.desklink.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.xurround.desklink.R;

public class SettingsFragment extends PreferenceFragmentCompat
{
    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey)
    {
        setPreferencesFromResource(R.xml.app_settings, rootKey);
    }
}