package me.xurround.desklink.fragments.tools;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.nio.charset.StandardCharsets;

import me.xurround.desklink.R;
import me.xurround.desklink.viewmodels.DeskControlViewModel;

public class KeyboardToolFragment extends Fragment
{
    private EditText keyboard;

    private InputMethodManager inputMethodManager;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_tool_keyboard, container, false);

        inputMethodManager = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        keyboard = view.findViewById(R.id.keyboard_input);

        DeskControlViewModel dcvm = new ViewModelProvider(requireActivity()).get(DeskControlViewModel.class);

        keyboard.setOnKeyListener((v, i, keyEvent) ->
        {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN)
            {
                if (i == KeyEvent.KEYCODE_DEL)
                    dcvm.sendData(new byte[]{(byte) 0xA0, 0x08});
            }
            return false;
        });

        keyboard.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                String data = charSequence.toString();
                if (!data.equals(""))
                {
                    if (data.contains("\n"))
                        dcvm.sendData(new byte[]{(byte) 0xA0, 0x0D});
                    else
                    {
                        byte[] strBytes = data.getBytes(StandardCharsets.UTF_8);
                        byte[] tData = new byte[strBytes.length + 1];
                        tData[0] = (byte)0xA0;
                        System.arraycopy(strBytes, 0, tData, 1, strBytes.length);
                        dcvm.sendData(tData);
                    }
                    keyboard.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        return view;
    }

    @Override
    public void onResume()
    {
        Handler handler = new Handler();

        handler.postDelayed(() ->
        {
            keyboard.requestFocus();
            if (inputMethodManager != null)
                inputMethodManager.showSoftInput(keyboard, InputMethodManager.SHOW_IMPLICIT);
        },100);

        super.onResume();
    }
}
