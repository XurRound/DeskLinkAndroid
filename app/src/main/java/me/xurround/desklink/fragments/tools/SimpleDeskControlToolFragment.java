package me.xurround.desklink.fragments.tools;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import me.xurround.desklink.R;
import me.xurround.desklink.logic.AppSettings;
import me.xurround.desklink.logic.ui.FileDialog;
import me.xurround.desklink.logic.ui.OnSwipeTouchListener;
import me.xurround.desklink.viewmodels.DeskControlViewModel;

public class SimpleDeskControlToolFragment extends Fragment
{
    private ActivityResultLauncher<String[]> launcher;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        launcher = registerForActivityResult(new ActivityResultContracts.OpenDocument(), result ->
        {
            Log.d("FILE_PICKER", result.getPath());
        });

        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_tool_simple_desk_control, container, false);

        Button loadPresentationHelperButton = view.findViewById(R.id.load_presentation_button);

        ConstraintLayout psController = view.findViewById(R.id.presentation_swipe_controller);

        DeskControlViewModel viewModel = new ViewModelProvider(requireActivity()).get(DeskControlViewModel.class);

        if (AppSettings.getInstance(requireContext()).getPRVolumeHook())
        {
            viewModel.setHardwareKeyEventListener(k ->
            {
                if (k == KeyEvent.KEYCODE_VOLUME_UP)
                {
                    viewModel.sendData(new byte[] { (byte)0xC0 });
                    return true;
                }
                if (k == KeyEvent.KEYCODE_VOLUME_DOWN)
                {
                    viewModel.sendData(new byte[] { (byte)0xC1 });
                    return true;
                }
                return false;
            });
        }
        else
            viewModel.setHardwareKeyEventListener(null);

        psController.setOnTouchListener(new OnSwipeTouchListener(requireContext(), direction ->
        {
            switch (direction)
            {
                case TOP:
                    viewModel.sendData(new byte[] { (byte)0xC0 });
                    break;
                case BOTTOM:
                    viewModel.sendData(new byte[] { (byte)0xC1 });
                    break;
            }
        }));


        loadPresentationHelperButton.setOnClickListener(v ->
        {
            /*FileDialog fileDialog = new FileDialog(requireActivity(), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), null);
            fileDialog.setFileListener(file ->
            {
                Log.d("FILE_PICKER", file.getAbsolutePath());
                Bundle bundle = new Bundle();
                bundle.putString("FILE_PATH", file.getAbsolutePath());
                Navigation.findNavController(view).navigate(R.id.action_desk_to_presentation, bundle);
            });
            fileDialog.showDialog();*/
            launcher.launch(new String[] { "application/vnd.openxmlformats-officedocument.presentationml.presentation" });
        });

        return view;
    }
}
