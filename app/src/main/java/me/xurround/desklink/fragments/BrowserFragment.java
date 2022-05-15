package me.xurround.desklink.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.xurround.desklink.R;
import me.xurround.desklink.adapters.KnownDeviceListAdapter;
import me.xurround.desklink.models.KnownDevice;
import me.xurround.desklink.viewmodels.MainViewModel;

public class BrowserFragment extends Fragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_browser, container, false);

        ImageButton settingsButton = view.findViewById(R.id.settings_btn);
        RecyclerView kdView = view.findViewById(R.id.kd_devices_list);
        FloatingActionButton connectDeviceButton = view.findViewById(R.id.connect_device_btn);

        MainViewModel mvm = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        List<KnownDevice> kds = new ArrayList<>();

        kdView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration divider = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        kdView.addItemDecoration(divider);
        kdView.setAdapter(new KnownDeviceListAdapter(kds, kd ->
        {
            Navigation.findNavController(view).navigate(R.id.action_browser_to_desk_control);
        }));
        mvm.getKnownDevicesMD().observe(getViewLifecycleOwner(), knownDevices ->
        {
            kds.clear();
            kds.addAll(knownDevices);
            Objects.requireNonNull(kdView.getAdapter()).notifyItemRangeChanged(0, knownDevices.size());
        });

        connectDeviceButton.setOnClickListener(v ->
        {
            Navigation.findNavController(view).navigate(R.id.action_browser_to_connect);
        });
        settingsButton.setOnClickListener(v ->
        {
            Navigation.findNavController(view).navigate(R.id.action_browser_to_settings);
        });

        return view;
    }
}