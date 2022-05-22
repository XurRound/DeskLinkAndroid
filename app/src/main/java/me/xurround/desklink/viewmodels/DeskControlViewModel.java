package me.xurround.desklink.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;

import me.xurround.desklink.interfaces.ConnectCallback;
import me.xurround.desklink.interfaces.OnHardwareKeyEvent;
import me.xurround.desklink.logic.AppSettings;
import me.xurround.desklink.logic.network.NetworkClient;

public class DeskControlViewModel extends AndroidViewModel
{
    private NetworkClient networkClient;

    public DeskControlViewModel(Application application)
    {
        super(application);
    }

    private OnHardwareKeyEvent hardwareKeyEventListener;

    public void setupConnection(String ipAddress, int port)
    {
        networkClient = new NetworkClient(ipAddress, port, AppSettings.getInstance(getApplication().getApplicationContext()).getIdentifier());
        networkClient.connect(new ConnectCallback()
        {
            @Override
            public void onSuccess()
            {
                Log.d("DCVM", "Connected!");
            }

            @Override
            public void onFailure()
            {
                Log.d("DCVM", "Failed!");
            }
        });
    }

    public OnHardwareKeyEvent getHardwareKeyEventListener()
    {
        return hardwareKeyEventListener;
    }

    public void setHardwareKeyEventListener(OnHardwareKeyEvent hardwareKeyEventListener)
    {
        this.hardwareKeyEventListener = hardwareKeyEventListener;
    }

    public void sendData(byte[] data)
    {
        networkClient.send(data);
    }

    public void closeConnection()
    {
        networkClient.disconnect();
    }

    @Override
    protected void onCleared()
    {
        super.onCleared();
    }
}
