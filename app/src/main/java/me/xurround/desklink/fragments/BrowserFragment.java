package me.xurround.desklink.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.xurround.desklink.R;
import me.xurround.desklink.adapters.KnownDeviceListAdapter;
import me.xurround.desklink.logic.AppSettings;
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
        TextView deviceName = view.findViewById(R.id.device_name);

        LinearLayout browserPlaceholder = view.findViewById(R.id.browser_placeholder);

        deviceName.setText(AppSettings.getInstance(requireContext()).getDeviceName());

        MainViewModel mvm = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        List<KnownDevice> kds = new ArrayList<>();

        kdView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration divider = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        kdView.addItemDecoration(divider);
        kdView.setItemAnimator(null);
        kdView.setAdapter(new KnownDeviceListAdapter(kds, kd ->
        {
            Bundle bundle = new Bundle();
            bundle.putString("ADDRESS", kd.getIpAddress());
            bundle.putString("NAME", kd.getName());
            Navigation.findNavController(view).navigate(R.id.action_browser_to_desk_control, bundle);
        }, (device) ->
        {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Remove device")
                    .setMessage("You will not be able to control " + device.getName() + " anymore")
                    .setCancelable(true)
                    .setPositiveButton("Remove", (v, d) ->
                    {
                        mvm.getKnownDevicesMD().getValue().remove(device);
                        AppSettings.getInstance(requireContext()).saveKnownDevices(mvm.getKnownDevicesMD().getValue());
                        mvm.getKnownDevicesMD().postValue(mvm.getKnownDevicesMD().getValue());
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }));
        mvm.getKnownDevicesMD().observe(getViewLifecycleOwner(), knownDevices ->
        {
            kds.clear();
            kds.addAll(knownDevices);
            Objects.requireNonNull(kdView.getAdapter()).notifyItemRangeChanged(0, knownDevices.size());
            if (knownDevices.isEmpty())
            {
                browserPlaceholder.setVisibility(View.VISIBLE);
                kdView.setVisibility(View.GONE);
            }
            else
            {
                browserPlaceholder.setVisibility(View.GONE);
                kdView.setVisibility(View.VISIBLE);
            }
        });
        mvm.setKnownDevices(AppSettings.getInstance(requireContext()).loadKnownDevices());

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