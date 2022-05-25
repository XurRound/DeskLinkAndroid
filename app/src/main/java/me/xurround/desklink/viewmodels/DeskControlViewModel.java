package me.xurround.desklink.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import me.xurround.desklink.R;
import me.xurround.desklink.interfaces.ConnectCallback;
import me.xurround.desklink.interfaces.OnHardwareKeyEvent;
import me.xurround.desklink.logic.AppSettings;
import me.xurround.desklink.logic.network.NetworkClient;

public class DeskControlViewModel extends AndroidViewModel
{
    private NetworkClient networkClient;

    private MutableLiveData<Integer> currentToolMD;

    public DeskControlViewModel(Application application)
    {
        super(application);
        resetCurrentTool();
    }

    public void resetCurrentTool()
    {
        currentToolMD = new MutableLiveData<>(R.id.tool_touchpad);
    }

    private OnHardwareKeyEvent hardwareKeyEventListener;

    public void setupConnection(String ipAddress, int port, ConnectCallback connectCallback)
    {
        networkClient = new NetworkClient(ipAddress, port, AppSettings.getInstance(getApplication().getApplicationContext()).getIdentifier());
        networkClient.connect(connectCallback);
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

    public MutableLiveData<Integer> getCurrentToolMD()
    {
        return currentToolMD;
    }

    @Override
    protected void onCleared()
    {
        super.onCleared();
    }
}
