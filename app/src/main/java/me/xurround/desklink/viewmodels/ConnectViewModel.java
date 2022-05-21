package me.xurround.desklink.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import me.xurround.desklink.logic.network.DiscoveryListProcessor;
import me.xurround.desklink.logic.network.ServiceDiscovery;
import me.xurround.desklink.models.Device;

public class ConnectViewModel extends AndroidViewModel
{
    private final MutableLiveData<List<Device>> discoveredDevices;

    private final ServiceDiscovery serviceDiscovery;

    private final DiscoveryListProcessor discoveryListProcessor;

    public ConnectViewModel(Application application)
    {
        super(application);

        discoveredDevices = new MutableLiveData<>(new ArrayList<>());

        discoveryListProcessor = new DiscoveryListProcessor(devices ->
        {
            discoveredDevices.getValue().clear();
            discoveredDevices.getValue().addAll(devices);
            getDiscoveredDevices().postValue(discoveredDevices.getValue());
        });

        serviceDiscovery = new ServiceDiscovery(discoveryListProcessor::handleNewDevice);
    }

    public MutableLiveData<List<Device>> getDiscoveredDevices()
    {
        return discoveredDevices;
    }

    public void startDiscovery()
    {
        serviceDiscovery.start();
    }

    public void stopDiscovery()
    {
        serviceDiscovery.stop();
        discoveryListProcessor.dispose();
    }
}
