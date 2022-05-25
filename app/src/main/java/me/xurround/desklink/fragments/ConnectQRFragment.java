package me.xurround.desklink.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.zxing.BarcodeFormat;

import java.util.ArrayList;
import java.util.List;

import me.xurround.desklink.R;
import me.xurround.desklink.interfaces.RegisterQRCallback;
import me.xurround.desklink.logic.AppSettings;
import me.xurround.desklink.models.Device;
import me.xurround.desklink.models.KnownDevice;
import me.xurround.desklink.viewmodels.ConnectViewModel;

public class ConnectQRFragment extends Fragment
{
    private CodeScanner codeScanner;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_connect_qr, container, false);

        Handler handler = new Handler(Looper.getMainLooper());

        ConnectViewModel cvm = new ViewModelProvider(requireActivity()).get(ConnectViewModel.class);

        CodeScannerView scannerView = view.findViewById(R.id.scanner_view);
        scannerView.setFlashButtonVisible(false);
        codeScanner = new CodeScanner(requireContext(), scannerView);
        List<BarcodeFormat> formats = new ArrayList<>();
        formats.add(BarcodeFormat.QR_CODE);
        codeScanner.setFormats(formats);
        codeScanner.setDecodeCallback(result ->
        {
            List<KnownDevice> knownDevices = AppSettings.getInstance(requireContext()).loadKnownDevices();
            cvm.beginRegisterByQR(result.getText(), new RegisterQRCallback()
            {
                @Override
                public void onSuccess(Device device)
                {
                    handler.post(() ->
                    {
                        if (knownDevices.contains(device))
                        {
                            new AlertDialog.Builder(requireContext())
                                    .setTitle("Error")
                                    .setMessage("This device is in list already!")
                                    .setCancelable(true)
                                    .setOnCancelListener(d ->
                                    {
                                        Navigation.findNavController(view).navigate(R.id.action_qr_to_browser);
                                    })
                                    .setPositiveButton("Ok", (d, w) ->
                                    {
                                        Navigation.findNavController(view).navigate(R.id.action_qr_to_browser);
                                    }).show();
                            return;
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                        EditText descText = new EditText(requireContext());
                        LinearLayout linearLayout = new LinearLayout(requireContext());
                        linearLayout.addView(descText);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(40, 20, 40, 20);
                        descText.setLayoutParams(layoutParams);
                        builder.setView(linearLayout);
                        builder.setCancelable(false);
                        builder.setTitle("New device");
                        builder.setMessage("Please, provide description for " + device.getName());
                        builder.setPositiveButton("Add", (d, w) ->
                        {
                            knownDevices.add(new KnownDevice(device, descText.getText().toString()));
                            AppSettings.getInstance(requireContext()).saveKnownDevices(knownDevices);
                            Toast.makeText(getContext(), "Successfully added " + device.getName(), Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(view).navigate(R.id.action_qr_to_browser);
                        });
                        builder.setNegativeButton("Cancel", (d, w) -> { });
                        builder.show();
                    });
                }

                @Override
                public void onFailure()
                {
                    handler.post(() ->
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                        builder.setCancelable(true);
                        builder.setTitle("New device");
                        builder.setMessage("Request rejected!");
                        builder.setPositiveButton("Ok", (d, w) ->
                        {
                            Navigation.findNavController(view).navigate(R.id.action_qr_to_browser);
                        });
                        builder.show();
                    });
                }
            });
        });

        registerForActivityResult(new ActivityResultContracts.RequestPermission(), result ->
        {
            if (result)
                codeScanner.startPreview();
        }).launch(Manifest.permission.CAMERA);

        return view;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        codeScanner.stopPreview();
        codeScanner.releaseResources();
    }
}
