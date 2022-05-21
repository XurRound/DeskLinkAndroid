package me.xurround.desklink.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import me.xurround.desklink.logic.network.ServiceDiscovery;
import me.xurround.desklink.models.Device;

public class ConnectViewModel extends AndroidViewModel
{
    private MutableLiveData<List<Device>> discoveredDevices;

    private final ServiceDiscovery serviceDiscovery;

    public ConnectViewModel(Application application)
    {
        super(application);

        discoveredDevices = new MutableLiveData<>(new ArrayList<>());

        serviceDiscovery = new ServiceDiscovery(device ->
        {
            if (!discoveredDevices.getValue().contains(device))
            {
                discoveredDevices.getValue().add(device);
                discoveredDevices.postValue(discoveredDevices.getValue());
            }
        });
    }

    public MutableLiveData<List<Device>> getDiscoveredDevices()
    {
        return discoveredDevices;
    }

    public void startDiscovery()
    {
        discoveredDevices.setValue(new ArrayList<>());
        serviceDiscovery.start();
    }

    public void stopDiscovery()
    {
        serviceDiscovery.stop();
    }
}
