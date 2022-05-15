package me.xurround.desklink.fragments.tools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import me.xurround.desklink.R;

public class SimpleDeskControlToolFragment extends Fragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_tool_simple_desk_control, container, false);

        Button loadPresentationHelperButton = view.findViewById(R.id.load_presentation_button);

        loadPresentationHelperButton.setOnClickListener(v ->
        {
            Navigation.findNavController(view).navigate(R.id.action_desk_to_presentation);
        });

        return view;
    }
}
