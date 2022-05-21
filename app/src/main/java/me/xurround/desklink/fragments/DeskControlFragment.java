package me.xurround.desklink.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import me.xurround.desklink.R;
import me.xurround.desklink.fragments.tools.AirmouseToolFragment;
import me.xurround.desklink.fragments.tools.KeyboardToolFragment;
import me.xurround.desklink.fragments.tools.SimpleDeskControlToolFragment;
import me.xurround.desklink.fragments.tools.TouchpadToolFragment;
import me.xurround.desklink.logic.network.TcpSender;
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

        Button disconnectButton = view.findViewById(R.id.disconnect_btn);
        BottomNavigationView deskToolbar = view.findViewById(R.id.desk_toolbar);

        deskToolbar.setSelectedItemId(R.id.tool_touchpad);
        navigate(new TouchpadToolFragment(), false);

        Bundle args = requireArguments();
        String deviceAddress = args.getString("ADDRESS", "192.168.0.7");
        String deviceName = args.getString("NAME", "Computer");

        TextView deviceNameTV = view.findViewById(R.id.dc_device_name);
        deviceNameTV.setText(deviceName);

        deskToolbar.setOnItemSelectedListener(item ->
        {
            if (item.getItemId() == R.id.tool_airmouse)
            {
                viewModel.sendData(new byte[] { 7 });
                navigate(new AirmouseToolFragment());
            }
            if (item.getItemId() == R.id.tool_touchpad)
                navigate(new TouchpadToolFragment());
            if (item.getItemId() == R.id.tool_powerpoint)
                navigate(new SimpleDeskControlToolFragment());
            if (item.getItemId() == R.id.tool_keyboard)
                navigate(new KeyboardToolFragment());
            return true;
        });

        disconnectButton.setOnClickListener(v ->
        {
            Navigation.findNavController(view).navigateUp();
        });

        viewModel = new ViewModelProvider(requireActivity()).get(DeskControlViewModel.class);
        viewModel.setupConnection(deviceAddress, 15500);

        return view;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        viewModel.closeConnection();
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