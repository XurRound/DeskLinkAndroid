package me.xurround.desklink;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.KeyEvent;

import me.xurround.desklink.viewmodels.DeskControlViewModel;

public class MainActivity extends AppCompatActivity
{
    private DeskControlViewModel dcvm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dcvm = new ViewModelProvider(this).get(DeskControlViewModel.class);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (dcvm.getHardwareKeyEventListener() != null && dcvm.getHardwareKeyEventListener().onKeyDown(keyCode))
            return true;
        return super.onKeyDown(keyCode, event);
    }
}