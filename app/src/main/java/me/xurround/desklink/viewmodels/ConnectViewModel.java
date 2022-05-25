package me.xurround.desklink.viewmodels;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.xurround.desklink.interfaces.RegisterCallback;
import me.xurround.desklink.interfaces.RegisterQRCallback;
import me.xurround.desklink.logic.AppSettings;
import me.xurround.desklink.logic.network.DiscoveryListProcessor;
import me.xurround.desklink.logic.network.RegistrationService;
import me.xurround.desklink.logic.network.ServiceDiscovery;
import me.xurround.desklink.models.Device;
import me.xurround.desklink.models.KnownDevice;

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
        discoveredDevices.getValue().clear();
        getDiscoveredDevices().postValue(discoveredDevices.getValue());
        discoveryListProcessor.loadKnownDevices(AppSettings.getInstance(getApplication()).loadKnownDevices());
        discoveryListProcessor.start();
        serviceDiscovery.start();
    }

    public void stopDiscovery()
    {
        serviceDiscovery.stop();
        discoveryListProcessor.stop();
    }

    public void beginRegisterByDevice(Device device, RegisterCallback registerCallback)
    {
        RegistrationService registrationService = new RegistrationService(15500, registerCallback);
        registrationService.tryRegister(device, AppSettings.getInstance(getApplication()).getDeviceName(), AppSettings.getInstance(getApplication()).getIdentifier());
    }

    public void beginRegisterByQR(String data, RegisterQRCallback registerCallback)
    {
        String[] parts = data.split("\\|");
        if (parts.length != 4 || !Objects.equals(parts[0], "DLS"))
            return;
        String devId = parts[1];
        String[] ips = parts[2].split(",");
        String devName = parts[3];
        RegistrationService registrationService = new RegistrationService(15500, new RegisterCallback()
        {
            @Override
            public void onSuccess(String ip)
            {
                registerCallback.onSuccess(new Device(devId, devName, ip));
            }

            @Override
            public void onFailure()
            {
                registerCallback.onFailure();
            }
        });
        for (String ip : ips)
        {
            registrationService.tryRegister(
                    new Device(devId, devName, ip),
                    AppSettings.getInstance(getApplication()).getDeviceName(),
                    AppSettings.getInstance(getApplication()).getIdentifier());
        }
    }
}
