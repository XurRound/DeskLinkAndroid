package me.xurround.desklink.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import me.xurround.desklink.R;
import me.xurround.desklink.fragments.tools.AirmouseToolFragment;
import me.xurround.desklink.fragments.tools.KeyboardToolFragment;
import me.xurround.desklink.fragments.tools.SimpleDeskControlToolFragment;
import me.xurround.desklink.fragments.tools.TouchpadToolFragment;

public class DeskControlFragment extends Fragment
{
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
        navigate(new TouchpadToolFragment());

        deskToolbar.setOnItemSelectedListener(item ->
        {
            if (item.getItemId() == R.id.tool_airmouse)
                navigate(new AirmouseToolFragment());
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

        return view;
    }

    private void navigate(Fragment fragment)
    {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.slide_in_left,
                R.anim.slide_out_right,
                R.anim.slide_in_right,
                R.anim.slide_out_left
        );
        transaction.replace(R.id.tools_container, fragment);
        transaction.commit();
    }
}