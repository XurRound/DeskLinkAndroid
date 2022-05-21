package me.xurround.desklink.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import me.xurround.desklink.logic.network.TcpSender;

public class DeskControlViewModel extends AndroidViewModel
{
    private TcpSender tcpSender;

    public DeskControlViewModel(Application application)
    {
        super(application);
    }

    public void setupConnection(String ipAddress, int port)
    {
        tcpSender = new TcpSender(ipAddress, port);
        tcpSender.connect();
    }

    public void sendData(byte[] data)
    {
        tcpSender.send(data);
    }

    public void closeConnection()
    {
        tcpSender.disconnect();
    }

    @Override
    protected void onCleared()
    {
        super.onCleared();
        tcpSender = null;
    }
}
