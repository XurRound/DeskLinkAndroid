package me.xurround.desklink.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import me.xurround.desklink.R;
import me.xurround.desklink.fragments.tools.AirmouseToolFragment;
import me.xurround.desklink.fragments.tools.KeyboardToolFragment;
import me.xurround.desklink.fragments.tools.SimpleDeskControlToolFragment;
import me.xurround.desklink.fragments.tools.TouchpadToolFragment;
import me.xurround.desklink.interfaces.ConnectCallback;
import me.xurround.desklink.viewmodels.DeskControlViewModel;

public class DeskControlFragment extends Fragment
{
    private DeskControlViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_desk_control, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(DeskControlViewModel.class);

        Button disconnectButton = view.findViewById(R.id.disconnect_btn);
        BottomNavigationView deskToolbar = view.findViewById(R.id.desk_toolbar);

        Bundle args = requireArguments();
        String deviceAddress = args.getString("ADDRESS", "192.168.0.7");
        String deviceName = args.getString("NAME", "Computer");

        TextView deviceNameTV = view.findViewById(R.id.dc_device_name);
        deviceNameTV.setText(deviceName);

        ImageView connectionStatus = view.findViewById(R.id.connection_status);
        TextView connectionText = view.findViewById(R.id.connection_text);

        deskToolbar.setOnItemSelectedListener(item ->
        {
            viewModel.getCurrentToolMD().setValue(item.getItemId());
            return true;
        });

        disconnectButton.setOnClickListener(v ->
        {
            Navigation.findNavController(view).navigateUp();
        });

        ProgressDialog progressDialog = ProgressDialog.show(requireContext(),
                "Awaiting connection",
                "Just a few seconds...",
                true);
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(dialog ->
        {
            Navigation.findNavController(view).navigateUp();
        });

        Handler handler = new Handler(Looper.getMainLooper());

        viewModel.setupConnection(deviceAddress, 15500, new ConnectCallback()
        {
            @Override
            public void onSuccess()
            {
                handler.post(() ->
                {
                    progressDialog.dismiss();
                    connectionStatus.setColorFilter(Color.GREEN);
                    connectionText.setText("Connected");
                });
            }

            @Override
            public void onFailure()
            {
                handler.post(() ->
                {
                    progressDialog.dismiss();
                    new AlertDialog.Builder(requireContext())
                            .setTitle("Connection failure")
                            .setMessage("Device rejected your connection")
                            .setOnCancelListener(dialog ->
                            {
                                Navigation.findNavController(view).navigateUp();
                            })
                            .setPositiveButton("Ok", (v, e) ->
                            {
                                Navigation.findNavController(view).navigateUp();
                            })
                            .show();
                });
            }
        });

        deskToolbar.setSelectedItemId(viewModel.getCurrentToolMD().getValue());

        viewModel.getCurrentToolMD().observe(getViewLifecycleOwner(), id ->
        {
            if (id == R.id.tool_airmouse)
                navigate(new AirmouseToolFragment());
            if (id == R.id.tool_touchpad)
                navigate(new TouchpadToolFragment());
            if (id == R.id.tool_powerpoint)
                navigate(new SimpleDeskControlToolFragment());
            if (id == R.id.tool_keyboard)
                navigate(new KeyboardToolFragment());
        });

        return view;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        viewModel.closeConnection();
        viewModel.resetCurrentTool();
    }

    private void navigate(Fragment fragment)
    {
        navigate(fragment, true);
    }

    private void navigate(Fragment fragment, boolean animate)
    {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        if (animate)
        {
            transaction.setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right,
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
            );
        }
        transaction.replace(R.id.tools_container, fragment);
        transaction.commit();
    }
}