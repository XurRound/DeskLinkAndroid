package me.xurround.desklink.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.xurround.desklink.R;
import me.xurround.desklink.adapters.DeviceListAdapter;
import me.xurround.desklink.logic.AppSettings;
import me.xurround.desklink.logic.ui.HeightAnimator;
import me.xurround.desklink.models.Device;
import me.xurround.desklink.models.KnownDevice;
import me.xurround.desklink.viewmodels.ConnectViewModel;

public class ConnectFragment extends Fragment
{
    private ConnectViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_connect, container, false);

        Button openSearchSettingsButton = view.findViewById(R.id.openSearchSettingsButton);
        Button scanQRButton = view.findViewById(R.id.connect_qr_button);
        LinearLayout ll = view.findViewById(R.id.connectSlideHeader);

        Spinner conModeSpinner = view.findViewById(R.id.connection_mode_spn);

        HeightAnimator settingsSlideAnimator = new HeightAnimator(ll, 120);
        settingsSlideAnimator.setDuration(300);
        settingsSlideAnimator.setOnToggleListener(conModeSpinner::setEnabled);

        openSearchSettingsButton.setOnClickListener(v -> settingsSlideAnimator.toggle());

        scanQRButton.setOnClickListener(v ->
        {
            Navigation.findNavController(view).navigate(R.id.action_connect_to_qr);
        });

        RecyclerView devicesList = view.findViewById(R.id.devices_list);
        devicesList.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Device> discoveredDevices = new ArrayList<>();
        DeviceListAdapter dlAdapter = new DeviceListAdapter(discoveredDevices, device ->
        {
            List<KnownDevice> knownDevices = AppSettings.getInstance(requireContext()).loadKnownDevices();
            knownDevices.add(new KnownDevice(device, "LOL"));
            AppSettings.getInstance(requireContext()).saveKnownDevices(knownDevices);
            Toast.makeText(getContext(), "Connecting to: " + device.getName(), Toast.LENGTH_SHORT).show();
        });
        devicesList.setAdapter(dlAdapter);

        viewModel = new ViewModelProvider(requireActivity()).get(ConnectViewModel.class);
        viewModel.getDiscoveredDevices().observe(getViewLifecycleOwner(), devices ->
        {
            discoveredDevices.clear();
            discoveredDevices.addAll(devices);
            dlAdapter.notifyDataSetChanged();
        });
        viewModel.startDiscovery();

        return view;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        viewModel.stopDiscovery();
    }
}