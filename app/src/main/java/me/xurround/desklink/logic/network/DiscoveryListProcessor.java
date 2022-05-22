package me.xurround.desklink.logic.network;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import me.xurround.desklink.models.Device;
import me.xurround.desklink.models.KnownDevice;

public class DiscoveryListProcessor
{
    private final HashMap<Device, Long> devices;
    private final List<String> knownIds;

    private ScheduledExecutorService checkService;

    private UpdateDiscoveredDevicesListener uddListener;

    public DiscoveryListProcessor(UpdateDiscoveredDevicesListener uddListener)
    {
        this.uddListener = uddListener;
        devices = new HashMap<>();
        knownIds = new ArrayList<>();
    }

    public void loadKnownDevices(List<KnownDevice> knownDevices)
    {
        devices.clear();
        knownIds.clear();
        for (KnownDevice device : knownDevices)
            knownIds.add(device.getId());
    }

    public void handleNewDevice(Device device)
    {
        if (!knownIds.contains(device.getId()))
            devices.put(device, System.currentTimeMillis());
    }

    public interface UpdateDiscoveredDevicesListener
    {
        void update(Set<Device> devices);
    }

    public void start()
    {
        checkService = Executors.newSingleThreadScheduledExecutor();
        checkService.scheduleAtFixedRate(() ->
        {
            for (Device dev : devices.keySet())
            {
                if (System.currentTimeMillis() - devices.get(dev) > 5000)
                    devices.remove(dev);
                if (uddListener != null)
                    uddListener.update(devices.keySet());
            }

        }, 1, 1, TimeUnit.SECONDS);
    }

    public void stop()
    {
        checkService.shutdownNow();
    }
}
