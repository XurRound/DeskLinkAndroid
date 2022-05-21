package me.xurround.desklink.fragments;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.zxing.BarcodeFormat;

import java.util.ArrayList;
import java.util.List;

import me.xurround.desklink.R;

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

        CodeScannerView scannerView = view.findViewById(R.id.scanner_view);
        scannerView.setFlashButtonVisible(false);
        codeScanner = new CodeScanner(requireContext(), scannerView);
        List<BarcodeFormat> formats = new ArrayList<>();
        formats.add(BarcodeFormat.QR_CODE);
        codeScanner.setFormats(formats);
        codeScanner.setDecodeCallback(result ->
        {
            handler.post(() ->
            {
                Toast.makeText(requireContext(), result.getText(), Toast.LENGTH_SHORT).show();
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
