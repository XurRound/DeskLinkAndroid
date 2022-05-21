package me.xurround.desklink.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;

import me.xurround.desklink.interfaces.ConnectCallbackListener;
import me.xurround.desklink.logic.AppSettings;
import me.xurround.desklink.logic.network.NetworkClient;

public class DeskControlViewModel extends AndroidViewModel
{
    private NetworkClient networkClient;

    public DeskControlViewModel(Application application)
    {
        super(application);
    }

    public void setupConnection(String ipAddress, int port)
    {
        networkClient = new NetworkClient(ipAddress, port);
        Log.d("VM", AppSettings.getInstance(getApplication().getApplicationContext()).getIdentifier());
        networkClient.connect(AppSettings.getInstance(getApplication().getApplicationContext()).getIdentifier(), new ConnectCallbackListener()
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
